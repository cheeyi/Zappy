package com.ongcheeyi.zappy.weather;

import com.ongcheeyi.zappy.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by CheeYi on 6/30/15.
 */
public class WeatherNow {
    private String summary;
    private String timezone;
    private String icon;
    private String location;
    private long time;
    private double temp, humidity, precip;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIconId() {
        return Forecast.getIconId(icon);
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getTime() {
        return time;
    }

    public String formatTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a"); // a is am/pm
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        Date dateObject = new Date(time*1000);
        String time = formatter.format(dateObject);

        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTemp() {
        return (int)Math.round(temp);
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumidity() {
        return (int)Math.round(humidity*100);
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecip() { // get a rounded percentage of precipitation
        return (int)Math.round(precip*100);
    }

    public void setPrecip(double precip) {
        this.precip = precip;
    }
}
