package com.example.flight.api;

import android.util.Log;
import java.io.IOException;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Reference Function: Ok Http
 * Reference Method: Copy and Paste
 * API Name: Yahoo Weather
 * API Website: <a href="https://rapidapi.com/apishub/api/yahoo-weather5">...</a>
 * API Key: 1. H761157417@gmail.com: 225b54b282msh74ee543b8d95fc2p152f4fjsnbde570c0ea2c 每月26日刷新 100次查询
 *          2. I19017857@student.newinti.edu.my: 97d7fcce93msh2ceb3df9047f878p123262jsn8e7785cb6117  每月19号刷新 100次查询
 *          3. 1901856099@qq.com: f946e0f9b1msh260555160e012b3p172592jsn91956cd282e6 每月7号刷新 100次查询
 */
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
