package hmy.fyp.flight;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import hmy.fyp.flight.api.API_Flight;
import hmy.fyp.flight.api.API_Weather;
import hmy.fyp.flight.bean.flight.Flight_Bean;
import hmy.fyp.flight.bean.flight.Flight_Bean_Error;
import hmy.fyp.flight.entity.Flight;

public class Flight_Tracking extends AppCompatActivity {
    private static final String TAG = "flight_tracking";
    private EditText flight_tracking_date, flight_tracking_no;
    private Button back_button, search_button;
    private ProgressDialog progressDialog;
    private String flight_Info, departureWeather_Info, arrivalWeather_Info, departureCity, arrivalCity;
    private final Flight flight = new Flight();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_tracking);

        flight_tracking_date = findViewById(R.id.flight_tracking_date_input);
        flight_tracking_no = findViewById(R.id.flight_tracking_flight_no_input);
        back_button = findViewById(R.id.flight_tracking_back_button);
        search_button = findViewById(R.id.flight_tracking_search_button);

        intiView();
    }

    /**
     * Function: Handle for flight tracking
     */
    private final Handler handler_tracking = new Handler(Objects.requireNonNull(Looper.myLooper())) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                progressDialogBox(false);
                Toast.makeText(getApplicationContext(), "Connection error or time out.", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                progressDialogBox(false);
                String err = (String) msg.obj;
                String errorMessage = getErrorMessage(err);
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                String result = (String) msg.obj;
                Gson(result);
            } else if (msg.what == 3) {
                searchDepartureWeatherInfo();
            } else if (msg.what == 4) {
                searchArrivalWeatherInfo();
            } else if (msg.what == 5) {
                progressDialogBox(false);
                redirectToResult();
            }
        }
    };

    /**
     * Function: Initialize the view
     */
    @SuppressLint("ClickableViewAccessibility")
    private void intiView() {
        //Back Button Listener
        back_button.setOnClickListener(v -> finish());

        //当用户在FavoriteFragment中点击favoriteList中的item时，parameter会传递到这里
        Intent intent = getIntent();
        boolean isSearchFavoriteFlight = intent.getBooleanExtra("Favorite_SearchFavoriteFlight", false);
        if (isSearchFavoriteFlight) {
            String FlightNumber = intent.getStringExtra("Favorite_FlightNumber");
            String Date = intent.getStringExtra("Favorite_Date");
            if (FlightNumber != null && Date != null) {
                searchFlightInfo(FlightNumber, Date);
            }
        }

        //Date Picker Listener
        flight_tracking_date.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    String pickDate = format.format(calendar.getTime());
                    flight_tracking_date.setText(pickDate);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                return true;
            }
            return false;
        });

        //Search Flight Button Listener
        search_button.setOnClickListener(v -> {
            String FlightNumber = flight_tracking_no.getText().toString();
            String Date = flight_tracking_date.getText().toString();
            searchFlightInfo(FlightNumber, Date);
        });
    }

    /**
     * Function: Search Flight Information
     */
    private void searchFlightInfo(String FlightNumber, String Date) {
        if (FlightNumber.equals("")) { // if flight number is empty
            Toast.makeText(this, "Please input flight number", Toast.LENGTH_SHORT).show();
        } else if (Date.equals("")) { // if date is empty
            Toast.makeText(this, "Please pick the date", Toast.LENGTH_SHORT).show();
        } else if (!FlightNumber.matches("[a-zA-Z0-9]+")) { // if flight number is not number or letter
            Toast.makeText(this, "Please input flight number in the right format", Toast.LENGTH_SHORT).show();
        } else if (!Date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) { //if the date format is not yyyy-mm-dd
            Toast.makeText(this, "Please input Date in the right format", Toast.LENGTH_SHORT).show();
        } else {
            progressDialogBox(true);
            new Thread(() -> {
                Message msg = new Message();
                API_Flight ft = new API_Flight();
                String result = ft.getFlightInfo(FlightNumber, Date);
                //String result = testNewAPIJsonData(); // for test
                Log.d(TAG, "FlightInfo_Json: " + result);
                if (result == null || result.equals("Connection error or time out")) {
                    msg.what = 0; // handle connection error
                } else if (result.contains("error")) {
                    msg.obj = result;
                    msg.what = 1; // handle error message
                } else {
                    flight.setFlight_Date(Date);
                    msg.obj = result;
                    msg.what = 2; // handle flight information
                }
                handler_tracking.sendMessage(msg);
            }).start();
        }
    }

    /**
     * Function: Resolve Flight Information Json to get departure and arrival city name
     * Reference Function: Gson
     * Reference From: <a href="https://www.youtube.com/watch?v=f-kcvxYZrB4">...</a>
     */
    private void Gson(String result) {
        Gson gson = new Gson();
        List<Flight_Bean> flightData = gson.fromJson(result, new TypeToken<List<Flight_Bean>>() {
        }.getType());
        departureCity = flightData.get(0).getFlightDep();
        arrivalCity = flightData.get(0).getFlightArr();
        flight_Info = gson.toJson(flightData.get(0));
        handler_tracking.sendEmptyMessage(3);
    }

    /**
     * Function: Get Error Message
     */
    private String getErrorMessage(String err) {
        Gson gson = new Gson();
        Flight_Bean_Error errorMessage = gson.fromJson(err, Flight_Bean_Error.class);
        return errorMessage.getError();
    }

    /**
     * Function: Search Departure and Arrival Weather Information
     */
    private void searchDepartureWeatherInfo() {
        new Thread(() -> {
            API_Weather API_Weather_Dep = new API_Weather();
            departureWeather_Info = API_Weather_Dep.getWeatherInfo(departureCity);
            Log.d(TAG, "departureWeather_Info: " + departureWeather_Info);
            handler_tracking.sendEmptyMessage(4);
        }).start();
    }

    private void searchArrivalWeatherInfo() {
        new Thread(() -> {
            API_Weather API_Weather_Arr = new API_Weather();
            arrivalWeather_Info = API_Weather_Arr.getWeatherInfo(arrivalCity);
            Log.d(TAG, "arrivalWeather_Info: " + arrivalWeather_Info);
            handler_tracking.sendEmptyMessage(5);
        }).start();
    }

    /**
     * Function: Redirect to Flight Tracking Result Interface
     */
    private void redirectToResult() {
        Intent intent = new Intent(this, Flight_Tracking_Result.class);
        String pickDate = flight.getFlight_Date();
        intent.putExtra("flight_info", flight_Info);
        intent.putExtra("departureWeather_info", departureWeather_Info);
        intent.putExtra("arrivalWeather_info", arrivalWeather_Info);
        intent.putExtra("flight_date", pickDate);
        startActivity(intent);
    }

    /**
     * Function: Show Progress Dialog
     */
    private void progressDialogBox(boolean isEnable) {
        if (isEnable) {  // true = show progress dialog
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Searching Flight Information");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else { // false = dismiss progress dialog
            progressDialog.dismiss();
        }
    }
/*    private String testNewAPIJsonData_error(){
        return "{\"error_code\":2,\"error\":\"This is an error message for debug\"}";
    }
    private String testNewAPIJsonData(){
       return "[\n" +
               "    {\n" +
               "        \"fcategory\":\"0\",\n" +
               "        \"fservice\":\"J\",\n" +
               "        \"FirstClassCheckinTable\":\"F\",\n" +
               "        \"BusinessCheckinTable\":\"F\",\n" +
               "        \"FlightNo\":\"SC4660\",\n" +
               "        \"FlightCompany\":\"Shandong Airlines\",\n" +
               "        \"FlightDepcode\":\"SHA\",\n" +
               "        \"FlightArrcode\":\"TAO\",\n" +
               "        \"FlightDeptimePlanDate\":\"2023-03-23 19:20:00\",\n" +
               "        \"FlightArrtimePlanDate\":\"2023-03-23 21:05:00\",\n" +
               "        \"FlightDeptimeReadyDate\":\"\",\n" +
               "        \"FlightArrtimeReadyDate\":\"\",\n" +
               "        \"FlightDeptimeDate\":\"\",\n" +
               "        \"FlightArrtimeDate\":\"\",\n" +
               "        \"FlightIngateTime\":\"\",\n" +
               "        \"FlightOutgateTime\":\"\",\n" +
               "        \"CheckinTable\":\"B03-C18\",\n" +
               "        \"CheckDoor\":\"gate 4\\u30016\",\n" +
               "        \"BoardGate\":\"27\",\n" +
               "        \"ReachExit\":\"\",\n" +
               "        \"BaggageID\":\"\",\n" +
               "        \"BoardState\":\"\",\n" +
               "        \"FlightState\":\"schedule\",\n" +
               "        \"FlightHTerminal\":\"T2\",\n" +
               "        \"FlightTerminal\":\"T1\",\n" +
               "        \"org_timezone\":\"28800\",\n" +
               "        \"dst_timezone\":\"28800\",\n" +
               "        \"ShareFlightNo\":\"\",\n" +
               "        \"StopFlag\":\"0\",\n" +
               "        \"ShareFlag\":\"0\",\n" +
               "        \"VirtualFlag\":\"\",\n" +
               "        \"LegFlag\":\"0\",\n" +
               "        \"LastCheckinTime\":\"2023-03-23 17:35:00\",\n" +
               "        \"EstimateBoardingStartTime\":\"2023-03-23 17:35:00\",\n" +
               "        \"EstimateBoardingEndTime\":\"2023-03-23 18:05:00\",\n" +
               "        \"FlightDep\":\"Shanghai\",\n" +
               "        \"FlightArr\":\"Qingdao\",\n" +
               "        \"bridge\":\"utilize aerobridge\",\n" +
               "        \"arr_bridge\":\"\",\n" +
               "        \"FlightDepAirport\":\"Shanghai Hongqiao\",\n" +
               "        \"FlightArrAirport\":\"Qingdao Jiaodong\",\n" +
               "        \"VeryZhunReadyDeptimeDate\":\"2023-03-23 19:20:00\",\n" +
               "        \"VeryZhunReadyArrtimeDate\":\"2023-03-23 20:35:00\"\n" +
               "    }\n" +
               "]";
    }*/
}