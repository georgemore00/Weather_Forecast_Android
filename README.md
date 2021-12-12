# Weather_Forecast_Android
The Swedish Meteorological and Hydrological Institute, SMHI, publishes hourly updated weather 
forecast data in JSON-format at https://opendata-download-
metfcst.smhi.se/. 

I've created an Android app that uses this data to present weather forecast data for a 10 day period. 
The app lets the user input their longitude and latitude of choice, which will fetch data from SMHI by creating a HTTP request using Android Volley library.
The HTTP Response is parsed into model classes and stored in a local SQLite database which will be used in case of no network connection. 
Android Room ORM is used for the database. The solutions follows an MVC pattern. 
