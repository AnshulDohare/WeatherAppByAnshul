package com.example.weatherappbyanshul;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText et_city,et_country;
    Button getWeather;
    TextView weather_detail;
    DecimalFormat df = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_city = findViewById(R.id.et_city);
        et_country = findViewById(R.id.et_country);
        getWeather = findViewById(R.id.btn_get_weather);
        weather_detail = findViewById(R.id.weather_detail);
        GetWeatherDetail();

        getWeather.setOnClickListener(v -> GetWeatherDetail());


    }
    @SuppressLint("SetTextI18n")
    private void GetWeatherDetail(){
        String tempUrl;
        String city = et_city.getText().toString().trim();
        String country = et_country.getText().toString().trim();
        if(city.equals("")){
            weather_detail.setText("Please Enter City Name");
        }
        else{
            String url = "https://api.openweathermap.org/data/2.5/weather";
            String apiKey = "57368c62b5343bf18269e552a5230237";
            if(country.equals("")){
                tempUrl = url +"?q="+city+"&appid="+ apiKey;
            }
            else{
                tempUrl = url +"?q="+city+","+country+"&appid="+ apiKey;
            }
            @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, response -> {
                String output = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    JSONObject jsonObjectCoord = jsonResponse.getJSONObject("coord");
                    String longitude = jsonObjectCoord.getString("lon");
                    String latitude = jsonObjectCoord.getString("lat");

                    JSONArray jsonWeatherArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonWeatherArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");

                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temperature = jsonObjectMain.getDouble("temp")-273.15;
                    double feels_like = jsonObjectMain.getDouble("feels_like")-273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");

                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String windSpeed = jsonObjectWind.getString("speed");

                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");

                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String country1 = jsonObjectSys.getString("country");

                    int visibility = jsonResponse.getInt("visibility")/1000;

                    String city1 = jsonResponse.getString("name");

                    output+="Current weather of "+ city1 +" {"+ country1 + "}"
                            +"\nLongitude : "+longitude
                            +"\nLatitude : "+latitude
                            +"\nWeather : "+description
                            +"\nTemperature : "+df.format(temperature)+" C"
                            +"\nFeels Like : "+df.format(feels_like)+" C"
                            +"\nPressure : "+pressure + " hPa"
                            +"\nHumidity : "+humidity + " %"
                            +"\nWind Speed: "+windSpeed +" m/s"
                            +"\nCloud : "+clouds + " %"
                            +"\nVisibility : "+visibility+" Km";
                    weather_detail.setText(output);

                } catch (JSONException e) {
                    weather_detail.setText("There is some problem to get weather details of "+city);
                    //throw new RuntimeException(e);
                }

            }, error -> weather_detail.setText("Unable to Get Weather Details of "+city));
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}