package com.example.laboration1.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

/**
 * Representation of a database class which extends RoomDatabase.
 * Maps the Forecast and Weather entities.
 */
@androidx.room.Database(entities = {Forecast.class, Weather.class}, exportSchema = false, version = 10)
public abstract class Database extends RoomDatabase
{
    private static final String DB_NAME = "db";
    private static Database instance;

    public static synchronized Database getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),Database.class,DB_NAME)
            .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    public abstract ForecastDao forecastDao();

}
