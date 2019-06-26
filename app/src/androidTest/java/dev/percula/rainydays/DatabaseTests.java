package dev.percula.rainydays;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import dev.percula.rainydays.db.local.AppDatabase;
import dev.percula.rainydays.db.local.LocationDao;
import dev.percula.rainydays.db.local.RainDataDao;
import dev.percula.rainydays.model.Location;
import dev.percula.rainydays.model.RainData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Tests to validate the local database IO
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTests {
    private LocationDao locationDao;
    private RainDataDao rainDataDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        locationDao = db.locationDao();
        rainDataDao = db.rainDataDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeLocationsAndReadList() throws Exception {
        List<Location> locations = TestData.makeLocationTestData();
        locationDao.insertAll(locations.toArray(new Location[0]));
        List<Location> resultLocations = locationDao.getAll();
        assertThat(resultLocations, equalTo(locations));
    }

    @Test
    public void writeRainDataAndReadList() throws Exception {
        List<RainData> rainData = TestData.makeRainDataTestData();
        rainDataDao.insertAll(rainData.toArray(new RainData[0]));
        List<RainData> resultRainData = rainDataDao.getAll();
        assertThat(resultRainData, equalTo(rainData));
    }

    @Test
    public void writeRainDataAndDeleteFirst() throws Exception {
        List<RainData> rainData = TestData.makeRainDataTestData();
        rainDataDao.insertAll(rainData.toArray(new RainData[0]));
        rainDataDao.delete(rainData.get(0));
        List<RainData> resultRainData = rainDataDao.getAll();
        rainData.remove(0);
        assertThat(resultRainData, equalTo(rainData));
    }

}