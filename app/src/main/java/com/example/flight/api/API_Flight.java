package com.example.flight.api;

import android.util.Log;

import com.example.flight.utils.MD5EncryptUtils;

import java.io.IOException;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class API_Flight {
    private static final String TAG = "api_flight";
    private Response response = null;
    public String getFlightInfo (String FlightNo,String Date) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://aerodatabox.p.rapidapi.com/flights/number/" + FlightNo + "/" + Date)
                    .get()
                    .addHeader("X-RapidAPI-Key", "*****************************")
                    .addHeader("X-RapidAPI-Host", "aerodatabox.p.rapidapi.com")
                    .build();

            response = client.newCall(request).execute();
            String flightInfo = Objects.requireNonNull(response.body()).string(); // Get the response body
            return flightInfo;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of getFlightInfo: " + e.getMessage());
            return "Connection Error or Time Out";
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public String getFlightInfo_NewAPI(String FlightNo, String Date){ //for new API

        String AppID = "appid=11287";
        String Lang = "lang=en";
        String APIKey = "63bf62884539e";

        String Parameter = AppID + "&date=" + Date + "&fnum=" + FlightNo + "&" + Lang;
        String Token = MD5EncryptUtils.encrypt(MD5EncryptUtils.encrypt(Parameter + APIKey));
        Log.d(TAG, "getFlightInfo_NewAPI: " + Parameter + ", token=" + Token);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://open-al.variflight.com/api/flight?" + Parameter + "&token=" + Token)
                    .get()
                    .build();
            response = client.newCall(request).execute();
            String flightInfo = Objects.requireNonNull(response.body()).string(); // Get the response body
            return flightInfo;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of getFlightInfo: " + e.getMessage());
            return "Connection Error or Time Out";
        } finally {
            if (response != null) {
                response.close();
            }
        }

    }
}
