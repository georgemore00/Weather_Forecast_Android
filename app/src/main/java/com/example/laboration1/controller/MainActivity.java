package com.example.laboration1.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.laboration1.R;
import com.example.laboration1.model.Database;
import com.example.laboration1.model.Forecast;
import com.example.laboration1.model.NetworkState;
import com.example.laboration1.model.Parser;
import com.example.laboration1.model.Weather;
import com.example.laboration1.view.ForecastAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Represents the MainActivity and the controller for the MVC pattern.
 */
public class MainActivity extends AppCompatActivity {

    private TextView TextViewApprovedTime;
    private TextView TextViewLatitude;
    private TextView TextViewLongitude;
    private EditText EditTextLatitude;
    private EditText EditTextLongitude;
    private RecyclerView recyclerView;
    private Forecast forecast;
    private ForecastAdapter adapter;
    private Button ButtonSearch;
    private Database con;
    private static final String URL = "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/";

    /**
     * Instantiates the views and objects. Uses savedInstanceState to set the view and Forecast Object if it isn't null.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextViewApprovedTime = (TextView) findViewById(R.id.textViewApprovedTime);
        TextViewLatitude = (TextView) findViewById(R.id.textViewLatitude);
        TextViewLongitude = (TextView) findViewById(R.id.textViewLongitude);

        EditTextLongitude = (EditText) findViewById(R.id.editText_Longitude);
        EditTextLatitude = (EditText) findViewById(R.id.editText_Latitude);

        forecast = new Forecast();
        adapter = new ForecastAdapter(forecast,this);
        recyclerView.setAdapter(adapter);

        con = Database.getInstance(this);

        ButtonSearch = findViewById(R.id.Button_Search);
        ButtonSearch.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                //Check if input is valid
                if(validInput())
                {
                    Double lon = Double.parseDouble(EditTextLongitude.getText().toString());
                    Double lat = Double.parseDouble(EditTextLatitude.getText().toString());

                    if(NetworkState.isConnected(getApplicationContext()))
                    {
                        DownloadData(lon,lat);
                    }
                    else
                    {
                        getStoredData(lat,lon);
                    }
                }
            }
        });
        if(savedInstanceState!=null)
        {
            forecast = (Forecast) savedInstanceState.getSerializable("Saved Forecast");
            //Forecast wont be null since it is instantiated to an empty object, the variables can be null.
            if(forecast.getApprovedTime()!=null)
            {
                updateView(forecast);
            }
        }
    }

    /**
     * Method used to display messages as a Toast to the user.
     * @param message the message to be displayed.
     */
    public void showToast(String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Validates the user Input in the Longitude and Latitude EditTexts.
     * Uses EditText.setError if input is invalid.
     * @return true if they're not empty or a single comma.
     */
    public boolean validInput()
    {
        String latitude = EditTextLatitude.getText().toString();
        String longitude = EditTextLongitude.getText().toString();

        if(latitude.isEmpty() || latitude.equals("."))
        {
            EditTextLatitude.setError("Invalid input");
        }if(longitude.isEmpty() || longitude.equals("."))
        {
            EditTextLongitude.setError("Invalid input");
        }
        return ((!latitude.isEmpty() && !latitude.equals("."))
                && (!longitude.isEmpty() && !longitude.equals("."))) ;
    }

    /**
     * Queries the database for a Forecast Object by using Longitude and Latitude.
     * Accesses the database in an AsyncTask and updates the view on the UiThread.
     * @param Longitude
     * @param Latitude
     */
    //Accesses RoomDatabase on an async thread, then updates UI inside runOnUiThread
    public void getStoredData(Double Longitude, Double Latitude)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run()
            {
                Forecast f = con.forecastDao().findForecastByCoordinates(Latitude,Longitude);
                if(f!=null)
                {
                        forecast = f;
                        List<Weather> timeSeries = con.forecastDao().findWeathersByForecastId(forecast.getId());
                        forecast.setTimeSeries(timeSeries);

                        showToast("Warning! currently using stored data which might not be up to date.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateView(forecast);
                            }
                        });
                }
                else
                {
                    showToast("No previously stored data was found");
                }
            }
        });
    }

    /**
     * Takes Longitude and Latitude and creates a HTTP request to SMHI's server.
     * Uses the Volley Library to create the HTTP request.
     * Uses the Parser class to parse the HTTP Response, stores the parsed data to the database and updates the view.
     * @param lon
     * @param lat
     */
    public void DownloadData(Double lon, Double lat)
    {
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley error", error.toString());
                if (error instanceof NetworkError) {
                    showToast("Volley network error, check network connection.");
                }
                if (error instanceof ServerError) {
                    showToast("Server error, retry with new coordinates or wait for SMHI to publish data.");
                }
            }
        };

    StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + lon + "/lat/" + lat +"/data.json" ,
            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    JSONObject jsonObject = null;
                    try
                    {
                        jsonObject = new JSONObject(response);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    forecast = Parser.getForecast(jsonObject);

                        showToast("Download is finished.");
                        updateView(forecast);
                        storeData(forecast);
                }
            }, errorListener
       );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        showToast("Attempting to download data..");
    }

    /**
     * Takes a Forecast Object and stores it the database.
     * Accesses database in an AsyncTask.
     * @param forecast
     */
    public void storeData(Forecast forecast)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Insert Data to room db
                con.forecastDao().insertBoth(forecast);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        showToast("Downloaded data was stored to Room Database.");
                    }
                });
            }
        });
    }


    /**
     * Updates adapter and sets TextViews.
     * Takes a Forecast Object.
     * @param forecast
     */
    public void updateView(Forecast forecast)
    {
        adapter.updateForecast(forecast);

        TextViewLatitude.setText(String.valueOf("Latitude: " + forecast.getLatitude()));
        TextViewLongitude.setText(String.valueOf("Longitude: " + forecast.getLongitude()));
        TextViewApprovedTime.setText("Approved at:" + forecast.getApprovedTime());

        TextViewLatitude.setVisibility(View.VISIBLE);
        TextViewLongitude.setVisibility(View.VISIBLE);
        TextViewApprovedTime.setVisibility(View.VISIBLE);
    }


    /**
     * Compares a given date to the current date.
     * @param date
     * @return true if given date is less than 10 minutes old.
     */
    public boolean approvedTimeStillValid(Date date)
    {
        Long currentTimeInMillis = System.currentTimeMillis();
        Long approvedTime = date.getTime();

        int tenMinutes = 1000 * 10 * 60;

        return (currentTimeInMillis - approvedTime < tenMinutes);

    }

    /**
     * Validates the current Forecast data, makes a new download if the data is invalid.
     */
    @Override
    protected void onStart()
    {
        if(forecast.getApprovedTime()!=null)
        {
            if (!approvedTimeStillValid(forecast.getApprovedTime()))
            {
                if(NetworkState.isConnected(getApplicationContext()))
                {
                    DownloadData(forecast.getLongitude(),forecast.getLatitude());
                }
            }
        }
        super.onStart();
    }

    /**
     * Saves the activity state's Forecast object by serializing it.
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Saved Forecast", forecast);
    }
}