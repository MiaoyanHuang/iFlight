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
 * API Name: VisualCrossing Weather API  如果旧的Weather API closed 就使用这个API接口
 * API Website:<a href="https://www.visualcrossing.com/weather-api">...</a>
 * API Key: YXG8QECC3LJPESCQAXLKG27EE
 * Account: 1.I19017857@student.newinti.edu.my
 *          2.1901856099@qq.com
 *          3.h761157417@gmail.com
 * Password: Hmy976231579
 * Note: 1000 times / per day!!!
 */
public class API_Weather_BeiYong {
    private static final String TAG = "api_weather_beiyong";
    private Response response = null;

    public String getWeatherInfo(String City){ //city 就是location 暂用location 测试数据

        String Url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
        String APi_Key = "&key=YXG8QECC3LJPESCQAXLKG27EE";
        String ContentType = "&contentType=json";

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url( Url + City + "?unitGroup=metric&include=days%2Ccurrent" + APi_Key + ContentType)
                    .get()
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
