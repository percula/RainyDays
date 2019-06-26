Rainy Days
=====================
[![License][license-svg]][license-link]

A simple app that shows you how much it has rained, using data from NOAA weather stations.

[Download from the Play Store.](https://play.google.com/store/apps/details?id=dev.percula.rainydays)

## Features

- Choose the weather station closest to you or pick on a map
- View the amount it has rained as far back as you care to scroll
- Metric or Imperial units

## Notes

- NOAA's rain data is not always up-to-date. The latest data may be from a week ago.
- Since publishing this app, the NOAA weather station API to find new weather stations stopped working consistently. To test the app, use the sample locations. Hopefully NOAA fixes the issue soon.

## Manual Installation

1. Clone this repository
2. Open with Android Studio version 3.4 or higher (a lower version will work too, but you'll have to modify the gradle plugin versions)
3. Click Run

The map place picker requires two Google API keys to work. The app will work without them, but you will only be able to use your current location.

### Steps to add API keys:
1. Navigate here: https://console.developers.google.com/apis
2. Create a new project in the upper left or select an existing project
3. In APIs and Services, click "Enable APIs and Services"
4. Enable the Places API, Maps SDK for Android, and Geocoding API
5. Go to "Credentials", "Create credentials", "API key"
6. Click "RESTRICT KEY" and under "API Restrictions" select Places and Maps
7. Create another API key and this time select "Geocoding" as the restriction
8. In app/src/main/res/values/keys.xml, copy/paste your API keys

*Note: You will need to enable billing for geocoding (autocomplete) to work more than once.*

[license-link]: https://github.com/percula/RainyDays/blob/master/LICENSE.
[license-svg]: https://img.shields.io/:license-mit-blue.svg?style=flat

## Future plans

Since this app was created as a demo app, it is simple and relatively bare-bones. In the future, I plan to integrate the functionality from this app into [Planter](https://play.google.com/store/apps/details?id=com.perculacreative.peter.gardenplanner), so that gardeners will know when to water their plants, or to wait if it's just rained a lot.