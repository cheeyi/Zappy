#Zappy
An Android weather app powered by the Forecast API (http://forecast.io/). Full summary and changes can be seen here: http://ongcheeyi.com/portfolio_page/android-weather-app/

###Notes
Requires Google Play Services for location to work properly for now, but I'm currently trying to fetch location by means of a device's built-in GPS.

###Automated UI testing
Automated UI testing is something that I’ve never done before. I learned about JUnit testing in school, and it turns out that UI testing is much like JUnit testing; in fact, the Espresso test library from the Android support repository is based on JUnit and it uses the AndroidJUnitRunner as its default test runner. To be honest, there isn’t a lot to test for in this project because the app basically detects the user’s location and displays the weather data “as is” from the Forecast server. For learning purposes, I wrote a simple test case that basically presses the refresh button (hence this is called ‘automated’ UI testing: you simulate user interaction of tapping on UI elements and perform assertion on the state or content of those elements). The test case then checks to see if the location matches “Minneapolis”. Obviously this test case will only pass if you’re running the app from Minneapolis, but like I said this is my first time performing automated UI testing. I hope to be able to perform more complex instrumentation and simulation of how the users interact with the app in the future.

###Third party libraries used
1. OkHttp (https://github.com/square/okhttp): a HTTP client by Square, used to send and receive requests to/from Google's geocoding API and Forecast.io's weather API
2. ButterKnife (https://github.com/JakeWharton/butterknife): simplifies code by making it easy to write boilerplate code to bind resources to variables
3. Android View Animations (https://github.com/daimajia/AndroidViewAnimations): easy-to-use Android animations

###Resoures used to implement location updates
1. Google's "Making Your App Location-Aware": http://developer.android.com/training/location/index.html
2. Treehouse's "The Beginner’s Guide to Location in Android": http://blog.teamtreehouse.com/beginners-guide-location-android
