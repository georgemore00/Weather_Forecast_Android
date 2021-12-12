package com.example.laboration1.model;

import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Representation of a Forecast object consisting of an approvedTime, Latitude, Longitude and a list of Weather objects.
 * Uses Room Database and annotations for mapping the entities.
 * Implements Serializable so it can be used with onSaveInstanceState method.
 */
@Entity(tableName = "t_forecast",
        indices = {@Index(value = {"Latitude", "Longitude"},
        unique = true)})

public class Forecast implements Serializable
{

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "approvedTime")
    @TypeConverters(DateConverter.class)
    private Date approvedTime;

    @Ignore
    private List<Weather> timeSeries;

    @ColumnInfo(name = "Latitude")
    private Double Latitude;

    @ColumnInfo(name = "Longitude")
    private Double Longitude;

    /**
     * Constructor used for Room Database mapping.
     * @param id
     * @param approvedTime
     * @param Longitude
     * @param Latitude
     */
    public Forecast(int id,Date approvedTime,  Double Longitude, Double Latitude)
    {
        this.id = id;
        this.approvedTime = approvedTime;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    /**
     * Constructor used by Room Database.
     * @param approvedTime
     * @param timeSeries
     * @param Latitude
     * @param Longitude
     */
    @Ignore()
    public Forecast(Date approvedTime, List<Weather> timeSeries, Double Latitude, Double Longitude)
    {
        this.approvedTime = approvedTime;
        this.timeSeries = timeSeries;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    public Forecast()
    {
        timeSeries = new ArrayList<Weather>();
    }

    public Date getApprovedTime() {
        return approvedTime;
    }

    public List<Weather> getTimeSeries()
    {
        return timeSeries;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Boolean addWeather(Weather w)
    {
        return timeSeries.add(w);
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "id=" + id +
                ", approvedTime=" + approvedTime +
                ", timeSeries=" + timeSeries +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public void setTimeSeries(List<Weather> timeSeries) {
        this.timeSeries = timeSeries;
    }

    public void setApprovedTime(Date approvedTime) {
        this.approvedTime = approvedTime;
    }
}
