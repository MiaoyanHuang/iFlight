package hmy.fyp.flight;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

import hmy.fyp.flight.adapter.Adapter_WeatherForecast;
import hmy.fyp.flight.bean.flight.Flight_Bean;
import hmy.fyp.flight.bean.weather.Forecast;
import hmy.fyp.flight.bean.weather.Weather_Bean;
import hmy.fyp.flight.dao.FlightDao;
import hmy.fyp.flight.entity.Flight;
import com.google.gson.Gson;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Flight_Tracking_Result extends AppCompatActivity {

    private TextView tracking_result_flight_no;
    private TextView tracking_result_status,tracking_result_checkIn, tracking_result_gate, tracking_result_baggage;
    private TextView tracking_result_airlineName;
    private TextView tracking_result_departure;
    private TextView tracking_result_departure_time_type, tracking_result_departure_time;
    private TextView tracking_result_arrival;
    private TextView tracking_result_arrival_time_type, tracking_result_arrival_time, tracking_result_departure_delayDay;
    private TextView tracking_result_departure_planTime, tracking_result_arrival_planTime, tracking_result_arrival_delayDay;
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
        tracking_result_airlineName = findViewById(R.id.tracking_result_airlineName);
        tracking_result_status = findViewById(R.id.tracking_result_status);
        tracking_result_checkIn = findViewById(R.id.tracking_result_checkIn);
        tracking_result_gate = findViewById(R.id.tracking_result_gate);
        tracking_result_baggage = findViewById(R.id.tracking_result_baggage);

        tracking_result_departure = findViewById(R.id.tracking_result_departure);
        tracking_result_departure_time_type = findViewById(R.id.tracking_result_departure_time_type);
        tracking_result_departure_time = findViewById(R.id.tracking_result_departure_time);
        tracking_result_departure_delayDay = findViewById(R.id.tracking_result_departure_delayDay);

        tracking_result_arrival = findViewById(R.id.tracking_result_arrival);
        tracking_result_arrival_time_type = findViewById(R.id.tracking_result_arrival_time_type);
        tracking_result_arrival_time = findViewById(R.id.tracking_result_arrival_time);
        tracking_result_arrival_delayDay = findViewById(R.id.tracking_result_arrival_delayDay);

        tracking_result_departure_planTime = findViewById(R.id.tracking_result_departure_planTime);
        tracking_result_arrival_planTime = findViewById(R.id.tracking_result_arrival_planTime);

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
    private final Handler handler = new Handler(Objects.requireNonNull(Looper.myLooper())){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(Flight_Tracking_Result.this, "Add to favorite successfully.", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                Toast.makeText(Flight_Tracking_Result.this, "Delete from favorite successfully.", Toast.LENGTH_SHORT).show();
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
    @SuppressLint("SetTextI18n")
    private void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");

        //Get the flight and weather information from the previous activity
        Intent intent = getIntent();
        String flight_info = intent.getStringExtra("flight_info");
        String DepartureWeather_info = intent.getStringExtra("departureWeather_info");
        String ArrivalWeather_info = intent.getStringExtra("arrivalWeather_info");

        displayFlightInfo(flight_info);

        if(DepartureWeather_info.contains("message") || DepartureWeather_info.equals("Connection error or time out")) {
            tracking_result_departure_name.setText("No Weather Information");
            Toast.makeText(this, "Didn't find weather information or Internet error", Toast.LENGTH_SHORT).show();
        } else {
            displayDepartureWeatherInfo(DepartureWeather_info);
        }

        if (ArrivalWeather_info.contains("message") || ArrivalWeather_info.equals("Connection error or time out")){
            tracking_result_arrival_name.setText("No Weather Information");
            Toast.makeText(this, "Didn't find weather information or Internet error", Toast.LENGTH_SHORT).show();
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
     */
    private void displayFlightInfo(String flight_info) {

        Gson gson_flight = new Gson();
        Flight_Bean flightData = gson_flight.fromJson(flight_info, Flight_Bean.class);

        String flightNo = flightData.getFlightNo();
        String airlineName = flightData.getFlightCompany();
        String status = flightData.getFlightState().substring(0, 1).toUpperCase() + flightData.getFlightState().substring(1);

        //Departure and Arrival Airport
        String departureAirport = flightData.getFlightDepAirport() + " " + flightData.getFlightHTerminal();
        String arrivalAirport = flightData.getFlightArrAirport() + " " + flightData.getFlightTerminal();

        //Date and Time of departure and arrival
        String scheduledDepartureDateTime = flightData.getFlightDeptimePlanDate();
        String scheduledArrivalDateTime = flightData.getFlightArrtimePlanDate();

        String departureTime;
        if(!flightData.getFlightDeptimeDate().equals("")){
            String departureTimeDate = flightData.getFlightDeptimeDate();
            departureTime = departureTimeDate.substring(11, 16);
            tracking_result_departure_time_type.setText(R.string.actual);
            tracking_result_departure_delayDay.setText(compareDate(scheduledDepartureDateTime,departureTimeDate));
        } else {
            if (!flightData.getVeryZhunReadyDeptimeDate().equals("") && !flightData.getVeryZhunReadyArrtimeDate().equals("")) {
                String departureTimeDate = flightData.getVeryZhunReadyDeptimeDate();
                departureTime = departureTimeDate.substring(11, 16);
                tracking_result_departure_time_type.setText(R.string.estimate);
                tracking_result_departure_delayDay.setText(compareDate(scheduledDepartureDateTime,departureTimeDate));
            } else {
                departureTime = "--:--";
                tracking_result_departure_time_type.setText(R.string.estimate);
            }
        }

        String arrivalTime;
        if (!flightData.getFlightArrtimeDate().equals("")) {
            String arrivalTimeDate = flightData.getFlightArrtimeDate();
            arrivalTime = arrivalTimeDate.substring(11, 16);
            tracking_result_arrival_time_type.setText(R.string.actual);
            tracking_result_arrival_delayDay.setText(compareDate(scheduledArrivalDateTime,arrivalTimeDate));
        } else {
            if (!flightData.getVeryZhunReadyDeptimeDate().equals("") && !flightData.getVeryZhunReadyArrtimeDate().equals("")) {
                String arrivalTimeDate = flightData.getVeryZhunReadyArrtimeDate();
                arrivalTime = arrivalTimeDate.substring(11, 16);
                tracking_result_arrival_time_type.setText(R.string.estimate);
                tracking_result_arrival_delayDay.setText(compareDate(scheduledArrivalDateTime,arrivalTimeDate));
            } else {
                arrivalTime = "--:--";
                tracking_result_arrival_time_type.setText(R.string.estimate);
            }
        }

        //Schedule Time
        String scheduledDepartureTime = scheduledDepartureDateTime.substring(5, 16).replace("-", "/");
        String scheduledArrivalTime = scheduledArrivalDateTime.substring(5, 16).replace("-", "/");

        //Check in table, Board Gate, Baggage
        String checkIn;
        if(!flightData.getCheckinTable().equals("")){
            checkIn = flightData.getCheckinTable();
        } else {
            checkIn = "--";
        }

        String boardGate;
        if(!flightData.getBoardGate().equals("")){
            boardGate = flightData.getBoardGate();
        } else {
            boardGate = "--";
        }

        String baggage;
        if(!flightData.getBaggageID().equals("")){
            baggage = flightData.getBaggageID();
        } else {
            baggage = "--";
        }

        //Set the flight information to the tracking result page
        tracking_result_flight_no.setText(flightNo);
        tracking_result_airlineName.setText(airlineName);
        tracking_result_status.setText(status);
        tracking_result_checkIn.setText(checkIn);
        tracking_result_gate.setText(boardGate);
        tracking_result_baggage.setText(baggage);

        tracking_result_departure.setText(departureAirport);
        tracking_result_departure_time.setText(departureTime);

        tracking_result_arrival.setText(arrivalAirport);
        tracking_result_arrival_time.setText(arrivalTime);

        tracking_result_departure_planTime.setText(scheduledDepartureTime);
        tracking_result_arrival_planTime.setText(scheduledArrivalTime);

        //Set the flight status color
        setStatusColor(status);
    }

    /**
     * Function: Set the Flight Status Color
     */
    private void setStatusColor(String status) {
        switch (status) {
            case "Schedule":
            case "Arrival":
            case "Departure":
            case "Diversion":
            case "Climbing":
            case "Cruising":
            case "Descending":
            case "Landing":
            case "Diverted flight departure":
            case "Diverted flight arrival":
                tracking_result_status.setTextColor(getResources().getColor(R.color.flight_status_normal));
                break;
            case "Cancel":
            case "Cancel in advance":
            case "Diverted flight cancel":
                tracking_result_status.setTextColor(getResources().getColor(R.color.flight_status_error));
                break;
            case "Delay":
            case "Return":
            case "Returned flight departure":
            case "Returned flight arrival":
            case "Returned flight cancel":
            case "Circling":
            case "Circled":
                tracking_result_status.setTextColor(getResources().getColor(R.color.flight_status_delay));
                break;
        }
    }

    /**
     * Function: Compare the Actual Time and the Estimate Time
     */
    public String compareDate(String estimateDate,String actualDate){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date d1;
        Date d2;
        try {
            d1 = df.parse(estimateDate);
            d2 = df.parse(actualDate);
            if(d1!=null && d2!=null) {
                long diff = d2.getTime() - d1.getTime();
                if (diff == 0) {
                    return "";
                } else {
                    return "+" + diff / (1000 * 60 * 60 * 24);
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return "";
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
                    handler.sendMessage(message);
                }
            }.start();
        } else {
            new Thread() {
                @Override
                public void run() {
                    String flight_AirlineName = tracking_result_airlineName.getText().toString();
                    String flight_ScheduleDepartureTime = tracking_result_departure_planTime.getText().toString();
                    String flight_ScheduleArrivalTime = tracking_result_arrival_planTime.getText().toString();
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
                    handler.sendMessage(message);
                }
            }.start();
        }
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