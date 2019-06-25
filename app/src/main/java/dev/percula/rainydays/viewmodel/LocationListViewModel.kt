package dev.percula.rainydays.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dev.percula.rainydays.core.mutableLiveDataOf
import dev.percula.rainydays.db.local.AppDatabase
import dev.percula.rainydays.db.network.NetworkAsyncs
import dev.percula.rainydays.db.network.NetworkService
import dev.percula.rainydays.ui.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
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
                            it.firstOrNull()?.let { database.locationDao().insertAll(it) }
                        }
                    }
//                it.firstOrNull()?.let {
//                    launch {
//                        NetworkAsyncs.fetchPrecipitationAsync(
//                            networkService = networkService,
//                            startDate = LocalDate.of(2019, 1, 1),
//                            endDate = LocalDate.now().minusDays(1),
//                            location = it
//                        ).onMain {
//                            it.observe(this@LocationPickerFragment, Observer {
//                                it
//                            })
//                        }
//                    }
//                }
                }
            }
        }
    }

    val locations by lazy { database.locationDao().allSync }

}