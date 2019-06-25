package dev.percula.rainydays.db.dependencyinjection;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

@Module
public class AppModule {

    @NonNull
    private Application application;

    public AppModule(@NotNull Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application.getApplicationContext();
    }

}
