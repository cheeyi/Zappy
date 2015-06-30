package com.ongcheeyi.zappy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String forecastAPIKey = "716daa82001a49e90ba44b206f6e3486";
        double latitude = 44.970591;
        double longitude = -93.223;
        String forecastURL = "https://api.forecast.io/forecast/716daa82001a49e90ba44b206f6e3486/"
                + latitude + "," + longitude;

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
                    Log.v(TAG, response.body().string());
                    if (response.isSuccessful()) {
                        Log.v(TAG, response.body().string());
                    } else {
                        alertError();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Exception caught ", e);
                }
            }
        });

    }

    private void alertError() {

    }


}
