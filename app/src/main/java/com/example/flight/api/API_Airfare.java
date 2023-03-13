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
 * API Name: Skyscanner
 * API Website: <a href="https://rapidapi.com/3b-data-3b-data-default/api/skyscanner44">...</a>
 * API Key: 1. H761157417@gmail.com: 225b54b282msh74ee543b8d95fc2p152f4fjsnbde570c0ea2c 每月26日刷新 100次查询
 *          2. I19017857@student.newinti.edu.my: 97d7fcce93msh2ceb3df9047f878p123262jsn8e7785cb6117  每月28号刷新 100次查询
 *          3. 1901856099@qq.com: f946e0f9b1msh260555160e012b3p172592jsn91956cd282e6 每月7号刷新 100次查询
 */
public class API_Airfare {
    private static final String TAG = "api_airfare";
    private Response response = null;
    public String getAirfareInfo (String DepartureAirportCode,String ArrivalAirportCode,String DepartureDate,String Currency) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://skyscanner44.p.rapidapi.com/search?adults=1&origin=" +
                            DepartureAirportCode +
                            "&destination=" + ArrivalAirportCode +
                            "&departureDate=" + DepartureDate +
                            "&currency=" + Currency)
                    .get()
                    .addHeader("X-RapidAPI-Key", "97d7fcce93msh2ceb3df9047f878p123262jsn8e7785cb6117")
                    .addHeader("X-RapidAPI-Host", "skyscanner44.p.rapidapi.com")
                    .build();
            response = client.newCall(request).execute();
            String airfareInfo = Objects.requireNonNull(response.body()).string(); // Get the response body
            return airfareInfo;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Exception OkHttp Message: " + e.getMessage());
            return "Connection Error or Time Out";
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
