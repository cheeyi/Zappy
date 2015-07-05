package com.ongcheeyi.zappy.weather;

/**
 * Created by CheeYi on 7/4/15.
 * Provides encapsulation for current, hourly and daily weather forecasts
 */
public class Forecast {

    private WeatherNow currentWeather;
    private WeatherHourly[] weatherHourly;
    private WeatherDaily[] weatherDaily;

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
}
