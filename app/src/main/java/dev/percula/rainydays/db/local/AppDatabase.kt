package dev.percula.rainydays.db.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.percula.rainydays.model.Location
import dev.percula.rainydays.model.RainData

@Database(entities = [Location::class, RainData::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun rainDataDao(): RainDataDao
}