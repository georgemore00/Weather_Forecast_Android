package com.example.laboration1.model;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

/**
 * Representation of a Data Access Object for the Forecast Entity.
 * Provides methods to select and add forecast objects.
 */
@Dao
public abstract class ForecastDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertForecast(Forecast forecast);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertWeather(Weather weather);


    @Transaction
    public void insertBoth(Forecast forecast)
    {

        // Save rowId of inserted Forecast which will be the primary key.
        final long id = insert(forecast);

        // Set the primary key as foreign key to all related Weather Entities.
        for(int i=0; i<forecast.getTimeSeries().size(); i++)
        {
            Weather w = forecast.getTimeSeries().get(i);
            w.setForecastId(id);
            insertWeather(w);
        }
    }

    @Delete
    public abstract void delete(Forecast forecast);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long insert(Forecast forecast);

    @Query("SELECT * FROM t_weather WHERE forecast_id LIKE :id")
    public abstract List<Weather> findWeathersByForecastId(int id);


    @Query("SELECT * FROM t_forecast WHERE Latitude LIKE :latitude AND Longitude LIKE :longitude")
    public abstract Forecast findForecastByCoordinates(Double longitude, Double latitude);

}
