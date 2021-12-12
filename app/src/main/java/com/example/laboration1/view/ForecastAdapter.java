package com.example.laboration1.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laboration1.R;
import com.example.laboration1.model.Forecast;

/**
 * Representation of an Custom Adapter which acts as a bridge between the ViewHolder and the Forecast object.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder>
{

    private Forecast forecast;
    private Context context;

    public ForecastAdapter(Forecast forecast, Context context)
    {
        this.forecast= forecast;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_item,parent,false);
        return new ViewHolder(v);
    }

    /**
     * Binds the underlying data to the view. Uses pictures to represent the Coverage.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.TextViewTemperature.setText(String.valueOf(forecast.getTimeSeries().get(position).getTemperature()) + "°C");
        holder.TextViewValidTime.setText(forecast.getTimeSeries().get(position).getValidTime().toString());

        if(forecast.getTimeSeries().get(position).getCoverage()==0)
        {
            holder.imageView.setImageResource(context.getResources().getIdentifier("pic1"
                    , "drawable", this.context.getPackageName()));
        }
        else
        {
            holder.imageView.setImageResource(context.getResources().getIdentifier("pic" +
                    forecast.getTimeSeries().get(position).getCoverage(), "drawable", this.context.getPackageName()));
        }
    }

    /**
     *
     * @returns nr of items in the Forecast object's TimeSeries list.
     */
    @Override
    public int getItemCount()
    {
        return forecast.getTimeSeries().size();
    }

    /**
     * Takes a Forecast object and updates the underlying data.
     * Calls notifyDataSetChanged.
     * @param forecast
     */
    public void updateForecast(Forecast forecast)
    {
        this.forecast = forecast;
        this.notifyDataSetChanged();
    }

    public double getLatitude()
    {
        return this.forecast.getLatitude();
    }
    public double getLongitude()
    {
        return this.forecast.getLongitude();
    }

    /**
     * ViewHolder consisting of two TextViews and an ImageView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView TextViewTemperature;
        public TextView TextViewValidTime;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            TextViewTemperature = (TextView) itemView.findViewById(R.id.TextViewTemperature);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            TextViewValidTime = (TextView) itemView.findViewById(R.id.TextViewValidTime);
        }
    }
}
