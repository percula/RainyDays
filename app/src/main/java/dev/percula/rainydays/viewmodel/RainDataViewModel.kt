package dev.percula.rainydays.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.paging.toLiveData
import dev.percula.rainydays.db.local.AppDatabase
import dev.percula.rainydays.model.Location
import dev.percula.rainydays.model.RainData
import dev.percula.rainydays.ui.App
import javax.inject.Inject

class RainDataViewModel(application: Application, val location: Location): AndroidViewModel(application) {

    @Inject
    lateinit var database: AppDatabase

    init {
        (application as? App)?.appComponent?.inject(this)
    }

    val rainList: LiveData<PagedList<RainData>> =
        database.rainDataDao().loadAllFromLocation(location.id).toLiveData(pageSize = 50)

}


class RainDataViewModelFactory(val application: Application, val location: Location) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RainDataViewModel(application, location) as T
    }
}