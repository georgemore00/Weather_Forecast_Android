package com.example.laboration1.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

/**
 * Representation of a weather object consisting of validTime, temperature and coverage.
 * Uses Room Database and annotations for mapping the entities.
 * Implements Serializable so it can be used with onSaveInstanceState method.
 */

@Entity(tableName = "t_weather", foreignKeys = @ForeignKey(
        entity = Forecast.class,
        parentColumns = "id",
        childColumns = "forecast_id",
        onDelete = CASCADE),
        indices = @Index("forecast_id"))

public class Weather implements Serializable
{

    @PrimaryKey(autoGenerate = true)
    private int id;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "validTime")
    private Date validTime;

    @ColumnInfo(name = "temperature")
    private Double temperature;

    @ColumnInfo(name = "coverage")
    private int coverage;

    @ColumnInfo(name = "forecast_id")
    private Long forecastId;


    /**
     * Constructor used by Room Database.
     * @param id
     * @param validTime
     * @param temperature
     * @param coverage
     * @param forecastId
     */
    public Weather(int id, Date validTime, double temperature, int coverage, Long forecastId)
    {
        this.id = id;
        this.validTime = validTime;
        this.temperature = temperature;
        this.coverage = coverage;
        this.forecastId = forecastId;
    }


    /**
     * The default constructor for this Weather class.
     * @param validTime
     * @param temperature
     * @param coverage
     */
    @Ignore
    public Weather(Date validTime, double temperature, int coverage)
    {
        this.validTime = validTime;
        this.temperature = temperature;
        this.coverage = coverage;
    }

    public Date getValidTime()
    {
        return validTime;
    }

    public Double getTemperature() {
        return temperature;
    }

    public int getCoverage()
    {
        return coverage;
    }

    public int getId() {
        return id;
    }

    public Long getForecastId() {
        return forecastId;
    }

    public void setForecastId(Long forecastId) {
        this.forecastId = forecastId;
    }

    @Override
    public String toString()
    {
        return "Weather{" +
                "validTime='" + validTime + '\'' +
                ", temperature='" + temperature + '\'' +
                ", coverage=" + coverage +
                '}';
    }
}
