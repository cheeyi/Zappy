#Zappy
An Android weather app powered by the Forecast API (http://forecast.io/). 

###Notes
Requires Google Play Services for location to work properly for now, but I'm currently trying to fetch location by means of a device's built-in GPS.

###Third party libraries used
1. OkHttp (https://github.com/square/okhttp): a HTTP client by Square, used to send and receive requests to/from Google's geocoding API and Forecast.io's weather API
2. ButterKnife (https://github.com/JakeWharton/butterknife): simplifies code by making it easy to write boilerplate code to bind resources to variables

###Resoures used to implement location updates
1. Google's "Making Your App Location-Aware": http://developer.android.com/training/location/index.html
2. Treehouse's "The Beginner’s Guide to Location in Android": http://blog.teamtreehouse.com/beginners-guide-location-android
