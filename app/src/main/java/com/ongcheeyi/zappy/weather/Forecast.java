package com.ongcheeyi.zappy.weather;

import com.ongcheeyi.zappy.R;

/**
 * Created by CheeYi on 7/4/15.
 * Provides encapsulation for current, hourly and daily weather forecasts
 */
public class Forecast {

    private WeatherNow currentWeather;
    private WeatherHourly[] weatherHourly;
    private WeatherDaily[] weatherDaily;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void insertLocation() {
        if (weatherDaily.length > 0) {
            for (int i = 0; i < weatherDaily.length; i++) {
                weatherDaily[i].setLocation(location);
            }
        }
    }

    public WeatherNow getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(WeatherNow currentWeather) {
        this.currentWeather = currentWeather;
    }

    public WeatherHourly[] getWeatherHourly() {
        return weatherHourly;
    }

    public void setWeatherHourly(WeatherHourly[] weatherHourly) {
        this.weatherHourly = weatherHourly;
    }

    public WeatherDaily[] getWeatherDaily() {
        return weatherDaily;
    }

    public void setWeatherDaily(WeatherDaily[] weatherDaily) {
        this.weatherDaily = weatherDaily;
    }

    public static int getIconId(String icon) {

        int iconId = R.drawable.clear_day; // default

        if (icon.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (icon.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (icon.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (icon.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (icon.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (icon.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (icon.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (icon.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (icon.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (icon.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }

    // Converts to celcius
    public static double convertFahrenheitToCelcius(double fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }
}
