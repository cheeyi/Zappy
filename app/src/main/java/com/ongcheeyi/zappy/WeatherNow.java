package com.ongcheeyi.zappy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by CheeYi on 6/30/15.
 */
public class WeatherNow {
    private String summary, timezone, icon;
    private long time;
    private double temp, humidity, precip;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIconId() {
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

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecip() {
        return precip;
    }

    public void setPrecip(double precip) {
        this.precip = precip;
    }
}
