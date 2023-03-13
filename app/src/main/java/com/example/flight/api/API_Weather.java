package com.example.flight.api;

import android.util.Log;
import java.io.IOException;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class API_Weather {
    private static final String TAG = "api_weather";
    private Response response = null;
    public String getWeatherInfo (String city_IATA) {

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://yahoo-weather5.p.rapidapi.com/weather?location=" + city_IATA + "&format=json&u=c")
                    .get()
                    .addHeader("X-RapidAPI-Key", "f946e0f9b1msh260555160e012b3p172592jsn91956cd282e6")
                    .addHeader("X-RapidAPI-Host", "yahoo-weather5.p.rapidapi.com")
                    .build();
            response = client.newCall(request).execute();
            String weatherInfo = Objects.requireNonNull(response.body()).string();
            return weatherInfo;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of getWeatherInfo: " + e.getMessage());
            return "Connection Error or Time Out";
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
