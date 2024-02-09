package com.example.flight.api;

import android.util.Log;
import java.io.IOException;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
                    .addHeader("X-RapidAPI-Key", "*****************************")
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
