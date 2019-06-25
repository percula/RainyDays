Rainy Days
=====================
[![License][license-svg]][license-link]

A simple app that shows you how much it has rained.

## Features

- Choose the weather station closest to you or pick on a map
- View the amount it has rained as far back as you care to scroll
- Metric or Imperial units

## Installation

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
