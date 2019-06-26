package dev.percula.rainydays.db.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.percula.ktx.onDefault
import dev.percula.ktx.onMain
import dev.percula.rainydays.R
import dev.percula.rainydays.core.LCE
import dev.percula.rainydays.core.asList
import dev.percula.rainydays.core.asMap
import dev.percula.rainydays.core.mutableLiveDataOf
import dev.percula.rainydays.model.Location
import dev.percula.rainydays.model.RainData
import dev.percula.rainydays.ui.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Mapper
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

object NetworkAsyncs {

    object QueryParams {
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"
        const val BBOX = "bbox"
        const val LIMIT = "limit"
        const val DATASET = "dataset"
        const val DATA_TYPES = "dataTypes"
        const val STATIONS = "stations"
        const val FORMAT = "format"
        const val UNITS = "units"
    }

    object QueryValues {
        const val DATASET_DAILY_SUMMARIES = "daily-summaries"
        const val DATA_TYPE_PRECIPITATION = "PRCP"
        const val FORMAT_JSON = "json"
        const val UNIT_IMPERIAL = "standard"
        const val UNIT_METRIC = "metric"
    }

    fun fetchStationsAsync(
        scope: CoroutineScope,
        networkService: NetworkService,
        startDate: LocalDate,
        endDate: LocalDate,
        latitude: Double,
        longitude: Double
    ): LiveData<LCE<List<Location>>> {
        val lce: MutableLiveData<LCE<List<Location>>> = mutableLiveDataOf(LCE.Loading)

        scope.launch(context = Dispatchers.IO) {
            try {
                // Create the network query parameters and call
                val dateFormatter = DateTimeFormatter.ISO_DATE
                val searchRadius = 0.250
                val left = longitude - searchRadius
                val right = longitude + searchRadius
                val top = latitude + searchRadius
                val bottom = latitude - searchRadius
                val call = networkService.api.getDataSources(
                    mutableMapOf(
                        NetworkAsyncs.QueryParams.START_DATE to startDate.format(dateFormatter),
                        NetworkAsyncs.QueryParams.END_DATE to endDate.format(dateFormatter),
                        NetworkAsyncs.QueryParams.BBOX to arrayOf(top, left, bottom, right).joinToString(","),
                        NetworkAsyncs.QueryParams.LIMIT to 5,
                        NetworkAsyncs.QueryParams.DATASET to NetworkAsyncs.QueryValues.DATASET_DAILY_SUMMARIES,
                        NetworkAsyncs.QueryParams.DATA_TYPES to NetworkAsyncs.QueryValues.DATA_TYPE_PRECIPITATION
                    )
                )
                val response = call.execute()

                // Handle errors
                if (response.code() != 200) {
                    onMain {
                        lce.value = LCE.Error(App.app.getString(R.string.warning_network_code, response.code()))
                    }
                    return@launch
                }

                val body = response.body() ?: run {
                    lce.value = LCE.Error("No stations found")
                    return@launch
                }

                // Find stations within JSON and return them as Location objects
                onDefault {
                    val results = body["results"]?.asList<Map<String, Any?>>()

                    results?.mapNotNull { map ->
                        val coordinates = map["location"]
                            ?.asMap<String, Any?>()
                            ?.get("coordinates")?.asList<Double>()
                            ?: return@mapNotNull null
                        val lat = coordinates.lastOrNull() ?: return@mapNotNull null
                        val lon = coordinates.firstOrNull() ?: return@mapNotNull null
                        map["stations"]
                            ?.asList<Map<String, Any?>>()
                            ?.firstOrNull()
                            ?.let {
                                val id = it["id"] as? String ?: return@mapNotNull null
                                val name = it["name"] as? String ?: return@mapNotNull null
                                Timber.d("Found a station: $name")
                                Location(
                                    id = id,
                                    name = name,
                                    latitude = lat,
                                    longitude = lon
                                )
                            }
                    }
                }
                    ?.takeIf { it.isNotEmpty() }
                    ?.onMain { lce.value = LCE.Success(it) }
                    ?: onMain {
                        // As I found out during development, the server is finicky and may not function at all times:
                        lce.value = if (body[QueryParams.DATA_TYPES]?.asMap<String, Any?>()?.get("sumOfOtherDocCounts") == 0.0) {
                            LCE.Error(App.app.getString(R.string.warning_server))
                        } else {
                            LCE.Error(App.app.getString(R.string.warning_no_location_found))
                        }
                    }
            } catch (e: Exception) {
                Timber.e(e)
                onMain { lce.value = LCE.Error(e) }
            }
        }

        return lce
    }


    suspend fun fetchPrecipitationAsync(
        networkService: NetworkService,
        startDate: LocalDate,
        endDate: LocalDate,
        location: Location
    ): LiveData<LCE<List<RainData>>> {
        return coroutineScope {

            val lce: MutableLiveData<LCE<List<RainData>>> = onMain { mutableLiveDataOf(LCE.Loading) }

            launch(context = Dispatchers.IO) {
                try {
                    // Create the network query parameters and call
                    val dateFormatter = DateTimeFormatter.ISO_DATE
                    val call = networkService.api.getData(
                        mutableMapOf(
                            NetworkAsyncs.QueryParams.DATASET to NetworkAsyncs.QueryValues.DATASET_DAILY_SUMMARIES,
                            NetworkAsyncs.QueryParams.START_DATE to startDate.format(dateFormatter),
                            NetworkAsyncs.QueryParams.END_DATE to endDate.format(dateFormatter),
                            NetworkAsyncs.QueryParams.DATA_TYPES to NetworkAsyncs.QueryValues.DATA_TYPE_PRECIPITATION,
                            NetworkAsyncs.QueryParams.UNITS to NetworkAsyncs.QueryValues.UNIT_IMPERIAL,
                            NetworkAsyncs.QueryParams.FORMAT to NetworkAsyncs.QueryValues.FORMAT_JSON,
                            NetworkAsyncs.QueryParams.STATIONS to location.id
                        )
                    )
                    val response = call.execute()

                    // Handle errors
                    if (response.code() != 200) {
                        onMain {
                            lce.value = LCE.Error(App.app.getString(R.string.warning_network_code, response.code()))
                        }
                        return@launch
                    }


                    val body = response.body() ?: run {
                        lce.value = LCE.Error("No data found")
                        return@launch
                    }

                    // Convert JSON to list of RainData and return it
                    onDefault {
                        val results = body.asList<Map<String, Any?>>()

                        results?.map { map ->
                            map.toMutableMap().let { mutableMap ->
                                // Workaround for missing PRCP values in JSON
                                if (mutableMap[RainData.Keys.PRCP] == null) {
                                    mutableMap[RainData.Keys.PRCP] = null
                                }
                                Mapper.unmapNullable<RainData>(mutableMap)
                            }
                        }
                    }
                        ?.onMain { lce.value = LCE.Success(it) }
                        ?: onMain { lce.value = LCE.Error("No data found") }
                } catch (e: Exception) {
                    Timber.e(e)
                    onMain { lce.value = LCE.Error(e) }
                }
            }

            return@coroutineScope lce
        }

    }

}