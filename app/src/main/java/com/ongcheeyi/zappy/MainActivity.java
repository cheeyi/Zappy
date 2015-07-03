package com.ongcheeyi.zappy;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private WeatherNow currentWeather;
    private LocationNow currentLocation;
    GoogleApiClient mGoogleApiClient;
    double latitude, longitude;

    @Bind(R.id.timeLabel) TextView timeLabel; // annotation by ButterKnife is preferred
    @Bind(R.id.temperatureLabel) TextView tempLabel;
    @Bind(R.id.humidityValue) TextView humidityValue;
    @Bind(R.id.precipValue) TextView precipValue;
    @Bind(R.id.summaryLabel) TextView summaryLabel;
    @Bind(R.id.iconImageView) ImageView iconImageView;
    @Bind(R.id.refreshImageView) ImageView refreshImageView;
    @Bind(R.id.locationLabel) TextView locationLabel;
    //old school method: tempLabel = (TextView)findViewById(R.id.temperatureLabel);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this); // achieve all binding using a single line

        // default
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
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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
        Log.v(TAG, "Connected to GPlay!");
        updateLocation();
    }

    private void updateLocation() {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (lastLocation != null) {
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            getWeather(latitude,longitude);
            getAddress(latitude,longitude);
        } else {
            Log.v(TAG,"Location null!");
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
        Log.v(TAG, "Connected to GPlay is interrupted!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        //
        // More about this in the 'Handle Connection Failures' section.
        Log.v(TAG, "Connected to GPlay has failed!");

    }

    public void getAddress(double latitude, double longitude) {
        String googleApiKey = "AIzaSyAzSVjefE6f_87YsU5KK8UbNPTATE6q6Rc";
        String locationUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude +
                "," + longitude + "&key=" + googleApiKey;
        Log.v(TAG,locationUrl);

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
                        if (response.isSuccessful()) {
                            currentLocation = getLocationDetails(rawJSON);
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

    private void getWeather(double latitude, double longitude) {

        String forecastURL = getString(R.string.forecast_api_url_prefix) + latitude + "," + longitude;

        if (internetAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastURL).build();
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
                        Log.v(TAG, rawJSON);
                        if (response.isSuccessful()) {
                            currentWeather = getWeatherDetails(rawJSON);
                            // the following is necessary  because onResponse is running
                            // on a different thread, and a merge to the main flow is needed
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() { // this runs on the main UI thread
                                    updateDisplay();
                                    if (currentWeather.getTemp() > 70) {
                                        getWindow().getDecorView().setBackgroundColor
                                                (Color.parseColor(getString(R.string.pastel_red)));
                                    } else if (currentWeather.getTemp() > 50) {
                                        getWindow().getDecorView().setBackgroundColor
                                                (Color.parseColor(getString(R.string.pastel_orange)));
                                    } else {
                                        getWindow().getDecorView().setBackgroundColor
                                                (Color.parseColor(getString(R.string.pastel_blue)));
                                    }
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
        Drawable drawable = getResources().getDrawable(currentWeather.getIconId());

        tempLabel.setText(currentWeather.getTemp() + ""); // hack to pass in double as 'text'
        timeLabel.setText(currentWeather.formatTime() + "");
        humidityValue.setText(currentWeather.getHumidity() + "");
        precipValue.setText(currentWeather.getPrecip() + "%");
        iconImageView.setImageDrawable(drawable);
        summaryLabel.setText(currentWeather.getSummary());

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

        Log.d(TAG, weather.formatTime());
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


}
