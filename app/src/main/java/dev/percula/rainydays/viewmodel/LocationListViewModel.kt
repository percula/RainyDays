package dev.percula.rainydays.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import dev.percula.rainydays.R
import dev.percula.rainydays.core.mutableLiveDataOf
import dev.percula.rainydays.db.local.AppDatabase
import dev.percula.rainydays.db.network.NetworkAsyncs
import dev.percula.rainydays.db.network.NetworkService
import dev.percula.rainydays.model.Location
import dev.percula.rainydays.ui.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

class LocationListViewModel(application: Application): AndroidViewModel(application) {

    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var database: AppDatabase

    init {
        (application as? App)?.appComponent?.inject(this)
    }

    fun findWeatherStation(latlng: LatLng) {
        findWeatherStationLocation.value = latlng
    }

    private val findWeatherStationLocation = mutableLiveDataOf<LatLng>()

    val findWeatherStationLD by lazy {
        findWeatherStationLocation.switchMap { latLng ->
            NetworkAsyncs.fetchStationsAsync(
                scope = viewModelScope,
                networkService = networkService,
                startDate = LocalDate.now().minusDays(7),
                endDate = LocalDate.now().minusDays(1),
                latitude = latLng.latitude,
                longitude = latLng.longitude
            ).map { lce ->
                lce.also {
                    it.doOnData {
                        // Save first (closest) location to local db
                        viewModelScope.launch(context = Dispatchers.IO) {
                            try {
                                it.firstOrNull()?.let {
                                    database.locationDao().insertAll(it)

                                    // Preload some data from the station
                                    NetworkAsyncs.fetchPrecipitationAsync(
                                        networkService = networkService,
                                        startDate = LocalDate.now().minusDays(365),
                                        endDate = LocalDate.now(),
                                        location = it
                                    ).observeForever {
                                        it.doOnData {
                                            viewModelScope.launch(context = Dispatchers.IO) {
                                                try {
                                                    database.rainDataDao().insertAll(*it.toTypedArray())
                                                } catch (e: Exception) {
                                                    Timber.e(e)
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                dev.percula.ktx.onMain {
                                    // Most likely failed due to location already being in db:
                                    if (e.message?.contains("UNIQUE") == true) {
                                        Toast.makeText(
                                            getApplication(),
                                            R.string.warning_location_already_added,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                Timber.e(e)
                            }
                        }
                    }
                }
            }
        }
    }

    val locations: LiveData<MutableList<Location>> by lazy { database.locationDao().allSync }

}