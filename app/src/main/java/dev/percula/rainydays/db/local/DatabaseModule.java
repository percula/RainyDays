package dev.percula.rainydays.db.local;

import android.content.Context;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    @Inject
    public AppDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                "local-database")
                .build();
    }

}
