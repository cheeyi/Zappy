package com.ongcheeyi.zappy.weather;

/**
 * Created by CheeYi on 7/4/15.
 * Data model for storing weather information for the upcoming hours
 */
public class WeatherHourly {
    private long time;
    private String summary;
    private double temp;
    private String icon;
    private String timezone;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
