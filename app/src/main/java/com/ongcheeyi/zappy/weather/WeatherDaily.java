package com.ongcheeyi.zappy.weather;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by CheeYi on 7/4/15.
 * Data model for storing weather information for the upcoming week
 */
public class WeatherDaily implements Parcelable {

    private long time;
    private String summary, timezone, icon, location;
    private double maxTemp;

    public WeatherDaily(){}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getIcon() {
        return icon;
    }

    public int getIconId() {return Forecast.getIconId(icon);}

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getMaxTemp() {
        return (int)Math.round(maxTemp);
    }

    public int getCelsius() {
        return (int)Math.round(Forecast.convertFahrenheitToCelcius(maxTemp));
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE"); // weekday format
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        Date date = new Date(time*1000);
        return formatter.format(date);
    }

    @Override
    public int describeContents() { // not used
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(summary);
        dest.writeDouble(maxTemp);
        dest.writeString(icon);
        dest.writeString(timezone);
        dest.writeString(location);
    }

    private WeatherDaily(Parcel input) {
        // Order matters!
        time = input.readLong();
        summary = input.readString();
        maxTemp = input.readDouble();
        icon = input.readString();
        timezone = input.readString();
        location = input.readString();
    }

    public static final Creator<WeatherDaily> CREATOR = new Creator<WeatherDaily>() {
        @Override
        public WeatherDaily createFromParcel(Parcel source) {
            return new WeatherDaily(source);
        }

        @Override
        public WeatherDaily[] newArray(int size) {
            return new WeatherDaily[size];
        }
    };
}
