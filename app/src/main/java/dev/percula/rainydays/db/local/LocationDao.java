package dev.percula.rainydays.db.local;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import dev.percula.rainydays.model.Location;

import java.util.List;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM location")
    List<Location> getAll();

    @Query("SELECT * FROM location")
    LiveData<List<Location>> getAllSync();

    @Insert
    void insertAll(@NonNull Location... locations);

    @Delete
    void delete(Location location);

}
