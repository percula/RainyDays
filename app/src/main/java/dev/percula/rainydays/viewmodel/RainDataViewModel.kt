package dev.percula.rainydays.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagedList
import dev.percula.rainydays.db.local.AppDatabase
import dev.percula.rainydays.db.network.NetworkService
import dev.percula.rainydays.db.paging.RainDataBoundaryCallback
import dev.percula.rainydays.db.paging.toLiveDataNoPlaceholders
import dev.percula.rainydays.model.Location
import dev.percula.rainydays.model.RainData
import dev.percula.rainydays.ui.App
import java.util.concurrent.Executors
import javax.inject.Inject

class RainDataViewModel(application: Application, val location: Location): AndroidViewModel(application) {

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var networkService: NetworkService

    // thread pool used for disk access
    @Suppress("PrivatePropertyName")
    private val DISK_IO = Executors.newSingleThreadExecutor()

    init {
        (application as? App)?.appComponent?.inject(this)
    }

    val rainList: LiveData<PagedList<RainData>> =
        database.rainDataDao().loadAllFromLocation(location.id).toLiveDataNoPlaceholders(
            pageSize = 50,
            boundaryCallback = RainDataBoundaryCallback(
                ioExecutor = DISK_IO,
                networkPageSize = 365,
                database = database,
                location = location,
                coroutineScope = viewModelScope,
                networkService = networkService
            )
        )

}


@Suppress("UNCHECKED_CAST")
class RainDataViewModelFactory(val application: Application, val location: Location) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RainDataViewModel(application, location) as T
    }
}