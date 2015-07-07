package com.ongcheeyi.zappy.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CheeYi on 7/4/15.
 * Data model for storing weather information for the upcoming hours
 */
public class WeatherHourly implements Parcelable {
    private long time;
    private String summary;
    private double temp;
    private String icon;
    private String timezone;
    private String location;

    public WeatherHourly() {}

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

    @Override
    public int describeContents() { // not used
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(summary);
        dest.writeDouble(temp);
        dest.writeString(icon);
        dest.writeString(timezone);
        dest.writeString(location);
    }

    private WeatherHourly(Parcel input) {
        // Order matters!
        time = input.readLong();
        summary = input.readString();
        temp = input.readDouble();
        icon = input.readString();
        timezone = input.readString();
        location = input.readString();
    }

    public static final Creator<WeatherHourly> CREATOR = new Creator<WeatherHourly>() {
        @Override
        public WeatherHourly createFromParcel(Parcel source) {
            return new WeatherHourly(source);
        }

        @Override
        public WeatherHourly[] newArray(int size) {
            return new WeatherHourly[size];
        }
    };
}
