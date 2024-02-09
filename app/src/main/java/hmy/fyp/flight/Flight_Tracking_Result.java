package com.example.flight;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.flight.adapter.Adapter_WeatherForecast;
import com.example.flight.bean.flight.Flight_Bean;
import com.example.flight.bean.weather.Forecast;
import com.example.flight.bean.weather.Weather_Bean;
import com.example.flight.dao.FlightDao;
import com.example.flight.entity.Flight;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class Flight_Tracking_Result extends AppCompatActivity {

    private TextView tracking_result_flight_no;
    private TextView tracking_result_status;
    private TextView tracking_result_airlineName;
    private TextView tracking_result_departure;
    private TextView tracking_result_departureTerminal;
    private TextView tracking_result_departure_scheduleTime;
    private TextView tracking_result_departure_actualTime;
    private TextView tracking_result_arrival;
    private TextView tracking_result_arrivalTerminal;
    private TextView tracking_result_arrival_scheduleTime;
    private TextView tracking_result_arrival_actualTime;
    private TextView tracking_result_departure_name;
    private TextView tracking_result_arrival_name;
    private TextView tracking_result_favorite_text;
    private Button tracking_result_back_button, tracking_result_favorite_button;
    private RecyclerView tracking_result_departure_weather, tracking_result_arrival_weather;
    private Adapter_WeatherForecast adapter_WeatherForecast;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_tracking_result);

        //Bind the view
        tracking_result_back_button = findViewById(R.id.tracking_result_back_button);
        //Flight Information Section
        tracking_result_flight_no = findViewById(R.id.tracking_result_flight_no);
        tracking_result_status = findViewById(R.id.tracking_result_status);
        tracking_result_airlineName = findViewById(R.id.tracking_result_airlineName);
        tracking_result_departure = findViewById(R.id.tracking_result_departure);
        tracking_result_departureTerminal = findViewById(R.id.tracking_result_departure_terminal);
        tracking_result_departure_scheduleTime = findViewById(R.id.tracking_result_departure_scheduleTime);
        tracking_result_departure_actualTime = findViewById(R.id.tracking_result_departure_actualTime);
        tracking_result_arrival = findViewById(R.id.tracking_result_arrival);
        tracking_result_arrivalTerminal = findViewById(R.id.tracking_result_arrival_terminal);
        tracking_result_arrival_scheduleTime = findViewById(R.id.tracking_result_arrival_scheduleTime);
        tracking_result_arrival_actualTime = findViewById(R.id.tracking_result_arrival_actualTime);
        tracking_result_departure_weather = findViewById(R.id.tracking_result_departure_weather);
        //Departure Weather Information Section
        tracking_result_departure_name = findViewById(R.id.tracking_result_departure_name);
        tracking_result_departure_weather = findViewById(R.id.tracking_result_departure_weather);
        //Arrival Weather Information Section
        tracking_result_arrival_weather = findViewById(R.id.tracking_result_arrival_weather);
        tracking_result_arrival_name = findViewById(R.id.tracking_result_arrival_name);

        //Favorite Button
        tracking_result_favorite_button = findViewById(R.id.tracking_result_favorite_button);
        tracking_result_favorite_text = findViewById(R.id.tracking_result_favorite_text);

        initView();
    }

    /**
     * Function: Handler for tracking result
     */
    private final Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(Flight_Tracking_Result.this, "Add to Favorite Successfully.", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                Toast.makeText(Flight_Tracking_Result.this, "Delete from Favorite Successfully.", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                tracking_result_favorite_button.setBackgroundResource(R.drawable.favorite_button_icon_unfavorite);
                tracking_result_favorite_text.setText(R.string.tracking_result_Cancel);
            } else if (msg.what == 4) {
                tracking_result_favorite_button.setBackgroundResource(R.drawable.favorite_button_icon_favorite);
                tracking_result_favorite_text.setText(R.string.tracking_result_favorite);
            }
        }
    };

    /**
     * Function: Initialize the view
     */
    private void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");

        //Get the flight and weather information from the previous activity
        Intent intent = getIntent();
        String flight_info = intent.getStringExtra("flight_info");
        String DepartureWeather_info = intent.getStringExtra("departureWeather_info");
        String ArrivalWeather_info = intent.getStringExtra("arrivalWeather_info");

        displayFlightInfo(flight_info);

        if(DepartureWeather_info.contains("message") || DepartureWeather_info.equals("Connection Error or Time Out")) {
            tracking_result_departure_name.setText("No Weather Information");
            Toast.makeText(this, "Didn't Find Weather Information or Internet Error", Toast.LENGTH_SHORT).show();
        } else {
            displayDepartureWeatherInfo(DepartureWeather_info);
        }

        if (ArrivalWeather_info.contains("message") || ArrivalWeather_info.equals("Connection Error or Time Out")){
            tracking_result_arrival_name.setText("No Weather Information");
            Toast.makeText(this, "Didn't Find Weather Information or Internet Error", Toast.LENGTH_SHORT).show();
        } else {
            displayArrivalWeatherInfo(ArrivalWeather_info);
        }

        checkFavorite(); // When the interface is loaded, check whether the flight is in the favorite list
        // Favorite Button Listener
        tracking_result_favorite_button.setOnClickListener(v -> FavoriteOrUnFavorite());

        //Back Button Listener
        tracking_result_back_button.setOnClickListener(v -> finish());
    }

    /**
     * Function: Display flight information in the tracking result page
     * Reference Function: Gson
     * Reference From: <a href="https://www.youtube.com/watch?v=f-kcvxYZrB4">...</a>
     */
    private void displayFlightInfo(String flight_info) {

        String flightNo = null;
        String status = null;
        String airlineName = null;

        String departureAirport = null;
        String departureTerminal = null;
        String scheduledDepartureTime = null;
        String actualDepartureTime = null;

        String arrivalAirport = null;
        String arrivalTerminal = null;
        String scheduledArrivalTime = null;
        String actualArrivalTime = null;

        Gson gson_flight = new Gson();
        TypeToken<List<Flight_Bean>> token = new TypeToken<List<Flight_Bean>>() {};
        List<Flight_Bean> flights = gson_flight.fromJson(flight_info, token.getType());

        if(flights.size() > 1){
            for(Flight_Bean flight_bean : flights) {

                //Flight Information
                flightNo = flight_bean.number;
                status = flight_bean.status;
                airlineName = flight_bean.airline.name + " " + "Airline";

                //Departure Information
                departureAirport = flight_bean.departure.airport.iata + " " + flight_bean.departure.airport.shortName + " " + "Airport";

                if (flight_bean.departure.terminal == null) {
                    departureTerminal = "N/A";
                } else {
                    departureTerminal = flight_bean.departure.terminal;
                }
                scheduledDepartureTime = flight_bean.departure.scheduledTimeLocal.substring(0, 16);

                //这个如果搜的是未来航班 那么会为空时会报错 所以要判断一下
                if (flight_bean.departure.runwayTimeLocal == null) {
                    //actualDepartureTime = flight_bean.departure.scheduledTimeLocal.substring(0,16);
                    actualDepartureTime = "N/A";
                } else {
                    actualDepartureTime = flight_bean.departure.runwayTimeLocal.substring(0, 16);
                }
            }

            flights.remove(0);

            for(Flight_Bean flight_bean : flights){

                //Arrival Information
                arrivalAirport = flight_bean.arrival.airport.iata + " " + flight_bean.arrival.airport.shortName + " " + "Airport";

                if (flight_bean.arrival.terminal == null) {
                    arrivalTerminal = "N/A";
                } else {
                    arrivalTerminal = flight_bean.arrival.terminal;
                }

                scheduledArrivalTime = flight_bean.arrival.scheduledTimeLocal.substring(0, 16);

                //这个如果搜的是未来航班 那么会为空时会报错 所以要判断一下
                if (flight_bean.arrival.actualTimeLocal == null || flight_bean.departure.runwayTimeLocal == null) {
                    //actualArrivalTime = flight_bean.arrival.scheduledTimeLocal.substring(0,16);
                    actualArrivalTime = "N/A";
                } else {
                    actualArrivalTime = flight_bean.arrival.actualTimeLocal.substring(0, 16);
                }
            }
        } else {
            for (Flight_Bean flight_bean : flights) {

                //Flight Information
                flightNo = flight_bean.number;
                status = flight_bean.status;
                airlineName = flight_bean.airline.name + " " + "Airlines";

                //Departure Information
                departureAirport = flight_bean.departure.airport.iata + " " + flight_bean.departure.airport.shortName + " " + "Airport";

                if (flight_bean.departure.terminal == null) {
                    departureTerminal = "N/A";
                } else {
                    departureTerminal = flight_bean.departure.terminal;
                }
                scheduledDepartureTime = flight_bean.departure.scheduledTimeLocal.substring(0, 16);

                //这个如果搜的是未来航班 那么会为空时会报错 所以要判断一下
                if (flight_bean.departure.runwayTimeLocal == null) {
                    //actualDepartureTime = flight_bean.departure.scheduledTimeLocal.substring(0,16);
                    actualDepartureTime = "N/A";
                } else {
                    actualDepartureTime = flight_bean.departure.runwayTimeLocal.substring(0, 16);
                }

                //Arrival Information
                arrivalAirport = flight_bean.arrival.airport.iata + " " + flight_bean.arrival.airport.shortName + " " + "Airport";

                if (flight_bean.arrival.terminal == null ) {
                    arrivalTerminal = "N/A";
                } else {
                    arrivalTerminal = flight_bean.arrival.terminal;
                }

                scheduledArrivalTime = flight_bean.arrival.scheduledTimeLocal.substring(0, 16);

                //这个如果搜的是未来航班 那么会为空时会报错 所以要判断一下
                if (flight_bean.arrival.actualTimeLocal == null || flight_bean.departure.runwayTimeLocal == null) {
                    //actualArrivalTime = flight_bean.arrival.scheduledTimeLocal.substring(0,16);
                    actualArrivalTime = "N/A";
                } else {
                    actualArrivalTime = flight_bean.arrival.actualTimeLocal.substring(0, 16);
                }
            }
        }
            //Set the flight information to the tracking result page
            tracking_result_flight_no.setText(flightNo);
            tracking_result_status.setText(status);
            tracking_result_airlineName.setText(airlineName);

            tracking_result_departure.setText(departureAirport);
            tracking_result_departureTerminal.setText(departureTerminal);
            tracking_result_departure_scheduleTime.setText(scheduledDepartureTime);
            tracking_result_departure_actualTime.setText(actualDepartureTime);

            tracking_result_arrival.setText(arrivalAirport);
            tracking_result_arrivalTerminal.setText(arrivalTerminal);
            tracking_result_arrival_scheduleTime.setText(scheduledArrivalTime);
            tracking_result_arrival_actualTime.setText(actualArrivalTime);

            //Set the flight status color
            if(status !=null && status.equals("Arrived")){
                tracking_result_status.setTextColor(getResources().getColor(R.color.flight_status_arrived));
            } else if(status !=null && status.equals("Cancelled")){
                tracking_result_status.setTextColor(getResources().getColor(R.color.flight_status_cancelled));
            } else if(status !=null && status.equals("Delayed")){
                tracking_result_status.setTextColor(getResources().getColor(R.color.flight_status_delayed));
            } else if(status !=null && status.equals("Unknown")){
                tracking_result_status.setTextColor(getResources().getColor(R.color.flight_status_unknown));
            }
    }

    /**
     * Function: Display Departure Weather
     */
    private void displayDepartureWeatherInfo(String departureWeather_info) {
        Gson gson_departureWeather = new Gson();
        Weather_Bean departureWeather = gson_departureWeather.fromJson(departureWeather_info, Weather_Bean.class);

        //Set the departure weather information to the tracking result page
        tracking_result_departure_name.setText(departureWeather.getLocation().getCity());

        List<Forecast> forecasts = departureWeather.getForecasts();
        //Log.d(TAG, "display_DepartureWeather_Info: " + forecasts);
        adapter_WeatherForecast = new Adapter_WeatherForecast(this, forecasts);
        tracking_result_departure_weather.setAdapter(adapter_WeatherForecast);

        LinearLayoutManager layoutManager_departure = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tracking_result_departure_weather.setLayoutManager(layoutManager_departure);
    }

    /**
     * Function: Display Arrival Weather
     */
    private void displayArrivalWeatherInfo(String arrivalWeather_info) {
        Gson gson_arrivalWeather = new Gson();
        Weather_Bean arrivalWeather = gson_arrivalWeather.fromJson(arrivalWeather_info, Weather_Bean.class);
        Log.d(TAG, "解析后的到达天气: " + arrivalWeather); //test resolve Json

        //Set the arrival weather information to the tracking result page
        tracking_result_arrival_name.setText(arrivalWeather.getLocation().getCity());

        List<Forecast> forecasts = arrivalWeather.getForecasts();
        //Log.d(TAG, "display_ArrivalWeather_Info: " + forecasts);
        adapter_WeatherForecast = new Adapter_WeatherForecast(this, forecasts);
        tracking_result_arrival_weather.setAdapter(adapter_WeatherForecast);

        LinearLayoutManager layoutManager_arrival = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tracking_result_arrival_weather.setLayoutManager(layoutManager_arrival);
    }

    /**
     * Function: Favorite or UnFavorite the flight to favorite list
     */
    private void FavoriteOrUnFavorite() {
        Intent intent = getIntent();
        FlightDao flightDao = new FlightDao();
        Message message = new Message();
        //Get the flight information from the view
        String flight_No = tracking_result_flight_no.getText().toString();
        String flight_Date = intent.getStringExtra("flight_date");
        String text = tracking_result_favorite_text.getText().toString();
        if (text.equals("Cancel")) {
            new Thread() {
                @Override
                public void run() {
                    flightDao.deleteFavoriteFlight(flight_No,flight_Date,user_id); // Delete
                    message.what = 2;
                    checkFavorite();
                }
            }.start();
        } else {
            new Thread() {
                @Override
                public void run() {
                    String flight_AirlineName = tracking_result_airlineName.getText().toString();
                    String flight_ScheduleDepartureTime = tracking_result_departure_scheduleTime.getText().toString();
                    String flight_ScheduleArrivalTime = tracking_result_arrival_scheduleTime.getText().toString();
                    String flight_DepartureAirportName = tracking_result_departure.getText().toString();
                    String flight_ArrivalAirportName = tracking_result_arrival.getText().toString();
                    //set data
                    Flight flight = new Flight();
                    flight.setFlight_No(flight_No);
                    flight.setFlight_Date(flight_Date);
                    flight.setFlight_AirlineName(flight_AirlineName);
                    flight.setFlight_ScheduleDepartureTime(flight_ScheduleDepartureTime);
                    flight.setFlight_ScheduleArrivalTime(flight_ScheduleArrivalTime);
                    flight.setFlight_DepartureAirport(flight_DepartureAirportName);
                    flight.setFlight_ArrivalAirport(flight_ArrivalAirportName);
                    flightDao.favoriteFlight(flight, user_id); // Favorite
                    message.what = 1;
                    checkFavorite();
                }
            }.start();
        }
        handler.sendMessage(message);
    }

    /**
     * Function: Check whether the flight is favorite
     */
    private void checkFavorite() {

        Intent intent = getIntent();
        String flight_No = tracking_result_flight_no.getText().toString();
        String flight_Date = intent.getStringExtra("flight_date");

        new Thread() {
            @Override
            public void run() {
                //检查航班是否已经被收藏
                FlightDao flightDao = new FlightDao();
                Message message = new Message();
                boolean result = flightDao.checkFavoriteFlight(flight_No, flight_Date, user_id);
                Log.d(TAG, "is Favorite?: " + result);
                if (result) {
                    message.what = 3;
                } else {
                    message.what = 4;
                }
                handler.sendEmptyMessage(message.what);
            }
        }.start();
    }
}