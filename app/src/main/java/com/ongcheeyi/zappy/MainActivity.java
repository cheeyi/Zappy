package com.ongcheeyi.zappy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.ongcheeyi.zappy.weather.Forecast;
import com.ongcheeyi.zappy.weather.LocationNow;
import com.ongcheeyi.zappy.weather.WeatherDaily;
import com.ongcheeyi.zappy.weather.WeatherHourly;
import com.ongcheeyi.zappy.weather.WeatherNow;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY = "DAILY_FORECAST";

    @Bind(R.id.timeLabel) TextView timeLabel; // annotation by ButterKnife
    @Bind(R.id.temperatureLabel) TextView tempLabel;
    @Bind(R.id.humidityValue) TextView humidityValue;
    @Bind(R.id.precipValue) TextView precipValue;
    @Bind(R.id.summaryLabel) TextView summaryLabel;
    @Bind(R.id.iconImageView) ImageView iconImageView;
    @Bind(R.id.refreshImageView) ImageView refreshImageView;
    @Bind(R.id.locationLabel) TextView locationLabel;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    //lines above replace boilerplate: tempLabel = (TextView)findViewById(R.id.temperatureLabel);

    private LocationRequest locationRequest;
    private Forecast forecast;
    private LocationNow currentLocation;
    private Location gpsLocation; // used in case Google Play Services not installed
    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    private boolean metric;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this); // achieve all binding using a single line
        progressBar.setVisibility(View.INVISIBLE); // invisible if user not refreshing

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false); // load default prefs

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        // default to Minneapolis
        latitude = 44.970591;
        longitude = -93.223;

        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLocation();
            }
        });
        buildGoogleApiClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        metric = sharedPref.getBoolean("metric", false);
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            // Release resources
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Connected to Google Play services!
        // The good stuff goes here.
        Log.v(TAG, "Connected to Google Play Services!");
        updateLocation();
    }

    private void updateLocation() {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (lastLocation != null) {
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
        } else {
            // Invoke a location update request, if this succeeds we get a new location and the
            // UI will be updated by means of the callback method onLocationChanged()
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

            if (gpsLocation != null) {
                Log.v(TAG,"Cannot request location update! Trying GPS as last resort.");
                latitude = gpsLocation.getLatitude();
                longitude = gpsLocation.getLongitude();
            }
        }
        getWeather(latitude, longitude); // also updates UI elements
        getAddress(latitude, longitude);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
        Log.v(TAG, "Connection to Google Play Services was interrupted.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (result.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                result.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error, then attempt GPS
             */
            Log.i(TAG, "Location services connection failed with code " + result.getErrorCode());
            // Get the location manager
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Define the criteria how to select the location provider -> use
            // default
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            gpsLocation = locationManager.getLastKnownLocation(provider);
            updateLocation();
        }
    }

    public void getAddress(double latitude, double longitude) {
        String googleApiKey = getString(R.string.google_api_key);
        String locationUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude +
                "," + longitude + "&key=" + googleApiKey;

        if (internetAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(locationUrl).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.v(TAG, getString(R.string.response_failure));
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String rawJSON = response.body().string();
                        if (response.isSuccessful()) { // location found
                            currentLocation = getLocationDetails(rawJSON);

                            while(forecast == null); // TODO: bad code
                            forecast.setLocation(currentLocation.getCity());
                            forecast.insertLocation();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() { // this runs on the main UI thread
                                    locationLabel.setText(currentLocation.getCity());
                                }
                            });
                        } else {
                            alertError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Exception caught ", e);
                    }
                }
            });
        } else { // Network unavailable
            Toast.makeText(this, getString(R.string.network_unavailable),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void refreshAnimation() {
        // simulate a refresh animation using the circular progress bar (spinner)
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
            refreshImageView.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            refreshImageView.setVisibility(View.INVISIBLE);
        }
    }

    private void getWeather(double latitude, double longitude) {

        String forecastURL = getString(R.string.forecast_api_url_prefix) + latitude + "," + longitude;

        if (internetAvailable()) {
            refreshAnimation(); // provide visual feedback of refresh
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastURL).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.v(TAG, getString(R.string.response_failure));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshAnimation();
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String rawJSON = response.body().string();
                        if (response.isSuccessful()) {
                            forecast = getForecastDetails(rawJSON);

                            // the following is necessary  because this method is running
                            // on a background (async) thread, and a merge to the main flow is needed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() { // this runs on the main UI thread
                                    updateDisplay();
                                    if (forecast.getCurrentWeather().getTemp() > 70) {
                                        getWindow().getDecorView().setBackgroundColor
                                                (Color.parseColor(getString(R.string.pastel_red)));
                                    } else if (forecast.getCurrentWeather().getTemp() > 50) {
                                        getWindow().getDecorView().setBackgroundColor
                                                (Color.parseColor(getString(R.string.pastel_orange)));
                                    } else {
                                        getWindow().getDecorView().setBackgroundColor
                                                (Color.parseColor(getString(R.string.pastel_blue)));
                                    }
                                    refreshAnimation();
                                }
                            });
                        } else {
                            alertError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Exception caught ", e);
                    }
                }
            });
        } else { // Network unavailable
            Toast.makeText(this, getString(R.string.network_unavailable),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void updateDisplay() { // refreshes all UI elements
        WeatherNow currentWeather = forecast.getCurrentWeather();
        Drawable drawable = getResources().getDrawable(currentWeather.getIconId());

        YoYo.with(Techniques.Tada) // provides user with visual feedback of temp being refreshed
                .duration(700)
                .playOn(tempLabel);

        if(metric) {
            tempLabel.setText(Math.round(convertFahrenheitToCelcius(currentWeather.getTemp())) + "");
        } else { // Fahrenheit
            tempLabel.setText(currentWeather.getTemp() + "");
        }
        timeLabel.setText(currentWeather.formatTime() + "");
        humidityValue.setText((int)currentWeather.getHumidity() + "%"); // get rid of .0s
        precipValue.setText((int)currentWeather.getPrecip() + "%");
        iconImageView.setImageDrawable(drawable);
        summaryLabel.setText(currentWeather.getSummary());
    }

    private Forecast getForecastDetails(String rawJSON) throws JSONException {
        Forecast forecast = new Forecast();
        forecast.setCurrentWeather(getWeatherDetails(rawJSON));
        forecast.setWeatherHourly(getWeatherHourly(rawJSON));
        forecast.setWeatherDaily(getWeatherDaily(rawJSON));

        return forecast;
    }

    private WeatherDaily[] getWeatherDaily(String rawJSON) throws JSONException {
        String timezone = new JSONObject(rawJSON).getString("timezone");
        JSONObject daily = new JSONObject(rawJSON).getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        WeatherDaily[] dailyWeather = new WeatherDaily[data.length()];
        for (int i = 0; i < data.length(); i++) {
            JSONObject day = data.getJSONObject(i);

            WeatherDaily dayInfo = new WeatherDaily();
            dayInfo.setIcon(day.getString("icon"));
            dayInfo.setSummary(day.getString("summary"));
            dayInfo.setMaxTemp(day.getDouble("temperatureMax"));
            dayInfo.setTime(day.getLong("time"));
            dayInfo.setTimezone(timezone);
            if (currentLocation != null) {
                dayInfo.setLocation(currentLocation.getCity());
            }
            dailyWeather[i] = dayInfo;
        }

        return dailyWeather;
    }

    private WeatherHourly[] getWeatherHourly(String rawJSON) throws JSONException {
        String timezone = new JSONObject(rawJSON).getString("timezone");
        JSONObject hourly = new JSONObject(rawJSON).getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        WeatherHourly[] hourlyWeather = new WeatherHourly[data.length()];
        for (int i = 0; i < data.length(); i++) {
            JSONObject hour = data.getJSONObject(i);

            WeatherHourly hourInfo = new WeatherHourly();
            hourInfo.setIcon(hour.getString("icon"));
            hourInfo.setSummary(hour.getString("summary"));
            hourInfo.setTemp(hour.getDouble("temperature"));
            hourInfo.setTime(hour.getLong("time"));
            hourInfo.setTimezone(timezone);

            hourlyWeather[i] = hourInfo;
        }

        return hourlyWeather;
    }

    private WeatherNow getWeatherDetails(String rawJSON) throws JSONException {
        // Passes responsibility to handle exception to its caller
        String timezone = new JSONObject(rawJSON).getString("timezone");
        JSONObject forecast = new JSONObject(rawJSON).getJSONObject("currently");
        WeatherNow weather = new WeatherNow();
        weather.setHumidity(forecast.getDouble("humidity"));
        weather.setTime(forecast.getLong("time"));
        weather.setIcon(forecast.getString("icon"));
        weather.setPrecip(forecast.getDouble("precipProbability"));
        weather.setSummary(forecast.getString("summary"));
        weather.setTemp(forecast.getDouble("temperature"));
        weather.setTimezone(timezone);

        return weather;
    }

    private LocationNow getLocationDetails(String rawJSON) throws JSONException {
        JSONArray resultsArray = new JSONObject(rawJSON).getJSONArray("results");
        JSONArray results = resultsArray.getJSONObject(0).getJSONArray("address_components");
        LocationNow location = new LocationNow();

        if (results.length() > 0 && resultsArray.length() > 0) {
            location.setCity(results.getJSONObject(3).getString("short_name"));
            location.setCountry(results.getJSONObject(6).getString("short_name"));
        } else {
            location.setCity(getString(R.string.location_undefined));
            location.setCountry(getString(R.string.location_undefined));
        }
        return location;
    }

    private boolean internetAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        boolean networkOk = false;
        if (info != null && info.isConnected()) {
            networkOk = true;
        }
        return networkOk;
    }

    private void alertError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    // Converts to celcius
    private double convertFahrenheitToCelcius(double fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "Location changed, updating accordingly.");
        updateLocation();
    }

    public void startSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.dailyButton) // Butterknife magic
    public void startDailyActivity(View view) {
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY, forecast.getWeatherDaily());
        startActivity(intent);
    }

}
