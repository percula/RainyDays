package dev.percula.rainydays.ui

import android.app.Application
import android.util.Log
import com.jakewharton.threetenabp.AndroidThreeTen
import dev.percula.rainydays.BuildConfig
import dev.percula.rainydays.db.dependencyinjection.AppComponent
import dev.percula.rainydays.db.dependencyinjection.AppModule
import dev.percula.rainydays.db.dependencyinjection.DaggerAppComponent
import timber.log.Timber

class App : Application() {

    lateinit var appComponent: AppComponent

    val crashReportingEnabled: Boolean get() = !BuildConfig.DEBUG

    override fun onCreate() {
        super.onCreate()
        app = this

        // Enable logging and crash reporting
        if (crashReportingEnabled) {
//            Fabric.with(applicationContext, Crashlytics()) TODO: In real app, use a crash reporting service
            Timber.plant(CrashReportingTree())
        } else {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return "${super.createStackElementTag(element)}.${element.methodName}:${element.lineNumber}"
                }
            })
        }

        // Instantiate Dagger (Dagger%COMPONENT_NAME%)
        appComponent = DaggerAppComponent.builder()
            // Instantiate any modules that require constructor arguments
            // If a Dagger 2 component does not have any constructor arguments for any of its modules,
            // then we can use .create() as a shortcut instead: appComponent = DaggerAppComponent.create();
            .appModule(AppModule(this))
            .build()

        AndroidThreeTen.init(this)
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

            // TODO: In real app, use a crash reporting service:
//            Crashlytics.log(priority, tag, message)
//
//            if (t != null && (priority == Log.ERROR || priority == Log.WARN)) {
//                Crashlytics.logException(t)
//            }
        }
    }


    companion object {
        @JvmStatic
        lateinit var app: App
    }
}