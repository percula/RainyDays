package dev.percula.rainydays.db.local;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import dev.percula.rainydays.model.RainData;

import java.util.List;

@Dao
public interface RainDataDao {

    @Query("SELECT * FROM raindata")
    List<RainData> getAll();

    @Query("SELECT * FROM raindata WHERE station IS :location ORDER BY DATE DESC")
    DataSource.Factory<Integer, RainData> loadAllFromLocation(String location);

    @Insert
    void insertAll(RainData... rainData);

    @Delete
    void delete(RainData rainData);

}
