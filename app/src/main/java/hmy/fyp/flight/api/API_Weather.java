package hmy.fyp.flight.api;

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
 * API Key: ********************************************************************
 */
public class API_Weather {
    private static final String TAG = "api_weather";

    public String getWeatherInfo(String city) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://yahoo-weather5.p.rapidapi.com/weather?location=" + city + "&format=json&u=c")
                .get()
                .addHeader("X-RapidAPI-Key", "*")
                .addHeader("X-RapidAPI-Host", "yahoo-weather5.p.rapidapi.com")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of getWeatherInfo: " + e.getMessage());
            return "Connection error or time out";
        }
    }
}
