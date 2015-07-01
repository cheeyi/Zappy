package com.ongcheeyi.zappy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private WeatherNow currentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String forecastAPIKey = "716daa82001a49e90ba44b206f6e3486";
        double latitude = 44.970591;
        double longitude = -93.223;
        String forecastURL = "https://api.forecast.io/forecast/716daa82001a49e90ba44b206f6e3486/"
                + latitude + "," + longitude;

        if (internetAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastURL).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String rawJSON = response.body().string();
                        Log.v(TAG, rawJSON);
                        if (response.isSuccessful()) {
                            currentWeather = getWeatherDetails(rawJSON);
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
