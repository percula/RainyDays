package dev.percula.rainydays;

import dev.percula.rainydays.model.Location;
import dev.percula.rainydays.model.RainData;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestData {

    public static List<Location> makeLocationTestData() {
        // Create some dummy data for testing
        ArrayList<Location> dummyLocations = new ArrayList<Location>();
        dummyLocations.add(new Location(
                "USW00014740",
                "HARTFORD BRADLEY INTERNATIONAL AIRPORT, CT US",
                41.9375,
                -72.6819

        ));
        dummyLocations.add(new Location(
                "USW00014752",
                "HARTFORD BRAINARD FIELD, CT US",
                41.73611,
                -72.65056

        ));
        dummyLocations.add(new Location(
                "USC00068138",
                "STORRS, CT US",
                41.7951,
                -72.2285
        ));
        dummyLocations.add(new Location(
                "USW00054767",
                "WILLIMANTIC WINDHAM AIRPORT, CT US",
                41.74194,
                -72.18361
        ));
        dummyLocations.add(new Location(
                "USW00054788",
                "MERIDEN MARKHAM MUNICIPAL AIRPORT, CT US",
                41.50972,
                -72.82778
        ));
        return dummyLocations;
    }

    public static List<RainData> makeRainDataTestData() {
        // Create some dummy data for testing
        ArrayList<RainData> list = new ArrayList<RainData>();
        for (int i = 0; i < 100; i++) {
            list.add(new RainData(
                    LocalDate.now().minusDays(i),
                    "USC00068138",
                    new Random().nextDouble())
            );
        }
        return list;
    }
}
