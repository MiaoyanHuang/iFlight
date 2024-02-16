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
 * API Name: Skyscanner
 * API Website: <a href="https://rapidapi.com/3b-data-3b-data-default/api/skyscanner44">...</a>
 * API Key: ********************************************************************
 */
public class API_Airfare {
    private static final String TAG = "api_airfare";

    public String getAirfareInfo(String DepartureAirportCode, String ArrivalAirportCode, String DepartureDate, String Currency) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://skyscanner44.p.rapidapi.com/search?adults=1&origin=" +
                        DepartureAirportCode +
                        "&destination=" + ArrivalAirportCode +
                        "&departureDate=" + DepartureDate +
                        "&currency=" + Currency)
                .get()
                .addHeader("X-RapidAPI-Key", "*")
                .addHeader("X-RapidAPI-Host", "skyscanner44.p.rapidapi.com")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Exception OkHttp Message: " + e.getMessage());
            return "Connection error or time out";
        }
    }
}
