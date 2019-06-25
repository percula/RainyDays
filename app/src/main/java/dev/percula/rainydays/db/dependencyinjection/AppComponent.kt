package dev.percula.rainydays.db.dependencyinjection

import dagger.Component
import dev.percula.rainydays.db.local.DatabaseModule
import dev.percula.rainydays.db.network.NetworkAsyncs
import dev.percula.rainydays.db.network.NetworkService
import dev.percula.rainydays.ui.LocationPickerFragment
import dev.percula.rainydays.ui.MainActivity
import dev.percula.rainydays.viewmodel.LocationListViewModel
import dev.percula.rainydays.viewmodel.RainDataViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkService::class, DatabaseModule::class])
interface AppComponent {

    fun providesNetworkService(): NetworkService

    fun inject(fetchstations: NetworkAsyncs)
    fun inject(activity: MainActivity)
    fun inject(fragment: LocationPickerFragment)
    fun inject(viewModel: LocationListViewModel)
    fun inject(viewModel: RainDataViewModel)

}