package com.example.laboration1.model;

import android.util.Log;

import com.example.laboration1.model.Forecast;
import com.example.laboration1.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class used for parsing JSONObjects
 */
public class Parser
{
    /**
     * This method parses a JSON Response into a Forecast object and returns it.
     * @param jsonObject the JSON Response
     * @return the parsed Forecast object.
     */
    public static Forecast getForecast(JSONObject jsonObject)
    {
        ArrayList<Weather> listItems = new ArrayList<>();
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String string_approvedTime = jsonObject.getString("approvedTime");
            Date approvedTime = format.parse(string_approvedTime);

            Double Longitude = jsonObject.getJSONObject("geometry").getJSONArray("coordinates")
                    .getJSONArray(0).getDouble(0);
            Double Latitude = jsonObject.getJSONObject("geometry").getJSONArray("coordinates")
                    .getJSONArray(0).getDouble(1);


            JSONArray timeSeries = jsonObject.getJSONArray("timeSeries");

            int coverage =0;
            Double temperature = 0.0;

            for(int i=0; i<timeSeries.length(); i++)
            {
                String string_validTime = timeSeries.getJSONObject(i).getString("validTime");
                Date validTime = format.parse(string_validTime);

                JSONArray params = timeSeries.getJSONObject(i).getJSONArray("parameters");

                for(int j=0; j<params.length(); j++)
                {
                    if(params.getJSONObject(j).getString("name").equalsIgnoreCase("tcc_mean"))
                    {
                        coverage = params.getJSONObject(j).getJSONArray("values").getInt(0);
                    }

                    if(params.getJSONObject(j).getString("name").equalsIgnoreCase("t"))
                    {
                        temperature = params.getJSONObject(j).getJSONArray("values").getDouble(0);
                    }
                }
                Weather w = new Weather(validTime,temperature,coverage);

                listItems.add(w);
            }

            Forecast forecast = new Forecast(approvedTime, listItems,Latitude, Longitude);
            return forecast;
        }

        catch (JSONException | ParseException e)
        {
            Log.i("Error", "Parse error encountered");
            e.printStackTrace();
        }
        return null;

    }
}
