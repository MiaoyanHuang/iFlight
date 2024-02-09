package hmy.fyp.flight.api;

import android.util.Log;

import hmy.fyp.flight.utils.MD5EncryptUtils;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Reference Function: Ok Http
 * Reference Method: Copy and Paste
 * API Name: AeroDataBox
 * API Website: <a href="https://rapidapi.com/aedbx-aedbx/api/aerodatabox/">...</a>
 * API Key: ********************************************************************
 */
public class API_Flight {
    private static final String TAG = "api_flight";
    private Response response = null;
    public String getFlightInfo(String FlightNo, String Date){ //for new API

        String AppID = "appid=11287";
        String Lang = "lang=en";
        String APIKey = "*";

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
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of getFlightInfo: " + e.getMessage());
            return "Connection error or time out";
        } finally {
            if (response != null) {
                response.close();
            }
        }

    }
}
