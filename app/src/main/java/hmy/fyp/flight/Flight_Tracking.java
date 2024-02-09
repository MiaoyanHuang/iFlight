package com.example.flight;

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
import androidx.appcompat.app.AppCompatActivity;
import com.example.flight.api.API_Flight;
import com.example.flight.api.API_Weather;
import com.example.flight.bean.flight.Flight_Bean;
import com.example.flight.bean.flight.Flight_Bean_BeiYong;
import com.example.flight.entity.Flight;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Flight_Tracking extends AppCompatActivity {
    private static final String TAG = "flight_tracking";
    private EditText flight_tracking_date, flight_tracking_no;
    private Button back_button, search_button;
    private ProgressDialog progressDialog;
    private String flight_Info, departureWeather_Info, arrivalWeather_Info, departureIATA, arrivalIATA;
    Flight flight = new Flight();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_tracking);

        flight_tracking_date = findViewById(R.id.flight_tracking_date_input);
        flight_tracking_no = findViewById(R.id.flight_tracking_flight_no_input);
        back_button = findViewById(R.id.flight_tracking_back_button);
        search_button = findViewById(R.id.flight_tracking_search_button);

        intiView();
        //testNewAPI();
    }

    private void testNewAPI() { //测试新的API
        String testJsonData = testNewAPIJsonData();

        Gson gson = new Gson();
        TypeToken<List<Flight_Bean_BeiYong>> typeToken = new TypeToken<List<Flight_Bean_BeiYong>>() {};
        List<Flight_Bean_BeiYong> flightDataList = gson.fromJson(testJsonData, typeToken.getType());
        Log.d(TAG, "testNewAPI: " + flightDataList);
    }

    /**
     * Function: Handle for flight tracking
     */
    private final Handler handler_tracking = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0) {
                Gson();
            } else if (msg.what == 1) {
                progressDialogBox(0);
                Toast.makeText(getApplicationContext(), "Out of API Usage or API Internal Error.", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2){
                progressDialogBox(0);
                Toast.makeText(getApplicationContext(), "Connection Error or Time Out.", Toast.LENGTH_SHORT).show();
            } else if (msg.what ==3){
                progressDialogBox(0);
                Toast.makeText(getApplicationContext(), "No Information was Found For This Flight.", Toast.LENGTH_SHORT).show();
            } else if (msg.what ==4){
                searchArrivalWeatherInfo();
            } else if (msg.what == 5) {
                progressDialogBox(0);
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
        String SearchFavoriteFlight = intent.getStringExtra("Favorite_SearchFavoriteFlight");
        if(SearchFavoriteFlight != null && SearchFavoriteFlight.equals("Yes")){
            String FlightNumber = intent.getStringExtra("Favorite_FlightNumber");
            String Date = intent.getStringExtra("Favorite_Date");
            searchFlightInfo(FlightNumber, Date);
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
            Toast.makeText(this, "Please Input The Flight Number", Toast.LENGTH_SHORT).show();
        } else if (Date.equals("")) { // if date is empty
            Toast.makeText(this, "Please Pick The Date", Toast.LENGTH_SHORT).show();
        } else if (!FlightNumber.matches("[a-zA-Z0-9]+")){ // if flight number is not number or letter
            Toast.makeText(this, "Please Input The Flight Number In The Right Format", Toast.LENGTH_SHORT).show();
        } else if (!Date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) { //if the date format is not yyyy-mm-dd
            Toast.makeText(this, "Please Input Your Date In The Right Format", Toast.LENGTH_SHORT).show();
        } else{
            progressDialogBox(1);
            new Thread(){
                @Override
                public void run() {
                    Message msg = new Message();
                    API_Flight ft = new API_Flight();
                    flight_Info = ft.getFlightInfo(FlightNumber, Date);
                    Log.d(TAG, "FlightInfo_Json: " + flight_Info);
                    if (flight_Info!=null) {
                         if (flight_Info.contains("message")) { // if flight info contain (error) message
                            msg.what = 1;
                        } else if (flight_Info.equals("Connection Error or Time Out")) {
                            msg.what = 2;
                        } else  { // if flight info is not empty and not contain message
                            flight.setFlight_Date(Date);
                            msg.what = 0;
                         }
                    } else {
                        msg.what = 3;
                    }
                    handler_tracking.sendEmptyMessage(msg.what);
                }
            }.start();
        }
    }

    /**
     * Function: Resolve Flight Information Json to get departure and arrival airport IATA code
     * Reference Function: Gson
     * Reference From: <a href="https://www.youtube.com/watch?v=f-kcvxYZrB4">...</a>
     */
    private void Gson() {
        Gson gson_flight = new Gson();
        TypeToken<List<Flight_Bean>> token = new TypeToken<List<Flight_Bean>>() {};
        List<Flight_Bean> flights = gson_flight.fromJson(flight_Info, token.getType());

        for (Flight_Bean flight_bean : flights) {
            departureIATA = flight_bean.departure.airport.iata; // get departure airport IATA
            arrivalIATA =flight_bean.arrival.airport.iata; // get arrival airport IATA
        }
        searchDepartureWeatherInfo();
    }

    /**
     * Function: Search Departure and Arrival Weather Information
     */
    private void searchDepartureWeatherInfo() {
        new Thread() {
            @Override
            public void run() {
                API_Weather API_Weather_Dep = new API_Weather();
                departureWeather_Info = API_Weather_Dep.getWeatherInfo(departureIATA);
                Log.d(TAG, "departureWeather_Info: " + departureWeather_Info);
                handler_tracking.sendEmptyMessage(4);
            }
        }.start();
    }

    private void searchArrivalWeatherInfo() {
        new Thread() {
            @Override
            public void run() {
                API_Weather API_Weather_Arr = new API_Weather();
                arrivalWeather_Info = API_Weather_Arr.getWeatherInfo(arrivalIATA);
                Log.d(TAG, "arrivalWeather_Info: " + arrivalWeather_Info);
                handler_tracking.sendEmptyMessage(5);
            }
        }.start();
    }

    /**
     * Function: Redirect to Flight Tracking Result Interface
     */
    private void redirectToResult(){
        Intent intent = new Intent(this, Flight_Tracking_Result.class);
        String pickDate = flight.getFlight_Date();

        intent.putExtra("flight_info", flight_Info);
        intent.putExtra("departureWeather_info", departureWeather_Info);
        intent.putExtra("arrivalWeather_info", arrivalWeather_Info);
        intent.putExtra("flight_date", pickDate);

        // Clean the global variable
        flight_Info = null;
        departureWeather_Info = null;
        arrivalWeather_Info = null;
        departureIATA = null;
        arrivalIATA = null;

        startActivity(intent);
    }

    /**
     * Function: Show Progress Dialog
     */
    private void progressDialogBox(int msg) {
        if (msg == 1){  // 1 = show progress dialog
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Searching Flight Information");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else if(msg == 0){ // 0 = dismiss progress dialog
            progressDialog.dismiss();
        }
    }

    private String testJsonData() {
        return "[\n" +
                "    {\n" +
                "        \"departure\":{\n" +
                "            \"airport\":{\n" +
                "                \"icao\":\"VTBS\",\n" +
                "                \"iata\":\"BKK\",\n" +
                "                \"name\":\"Bangkok, Suvarnabhumi\",\n" +
                "                \"shortName\":\"Suvarnabhumi\",\n" +
                "                \"municipalityName\":\"Bangkok\",\n" +
                "                \"location\":{\n" +
                "                    \"lat\":13.681099,\n" +
                "                    \"lon\":100.747\n" +
                "                },\n" +
                "                \"countryCode\":\"TH\"\n" +
                "            },\n" +
                "            \"scheduledTimeLocal\":\"2023-01-19 17:40+07:00\",\n" +
                "            \"actualTimeLocal\":\"2023-01-19 18:00+07:00\",\n" +
                "            \"runwayTimeLocal\":\"2023-01-19 18:00+07:00\",\n" +
                "            \"scheduledTimeUtc\":\"2023-01-19 10:40Z\",\n" +
                "            \"actualTimeUtc\":\"2023-01-19 11:00Z\",\n" +
                "            \"runwayTimeUtc\":\"2023-01-19 11:00Z\",\n" +
                "            \"quality\":[\n" +
                "                \"Basic\",\n" +
                "                \"Live\"\n" +
                "            ]\n" +
                "        },\n" +
                "        \"arrival\":{\n" +
                "            \"airport\":{\n" +
                "                \"icao\":\"VTSP\",\n" +
                "                \"name\":\"Phuket\"\n" +
                "            },\n" +
                "            \"quality\":[\n" +
                "\n" +
                "            ]\n" +
                "        },\n" +
                "        \"lastUpdatedUtc\":\"2023-01-19 14:11Z\",\n" +
                "        \"number\":\"QR 843\",\n" +
                "        \"callSign\":\"QR843\",\n" +
                "        \"status\":\"Departed\",\n" +
                "        \"codeshareStatus\":\"IsOperator\",\n" +
                "        \"isCargo\":false,\n" +
                "        \"aircraft\":{\n" +
                "            \"reg\":\"A7-AEG\",\n" +
                "            \"modeS\":\"06A041\",\n" +
                "            \"model\":\"Airbus A330-300\"\n" +
                "        },\n" +
                "        \"airline\":{\n" +
                "            \"name\":\"Qatar Airways\",\n" +
                "            \"iata\":\"QR\",\n" +
                "            \"icao\":\"QTR\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"greatCircleDistance\":{\n" +
                "            \"meter\":5300360.23,\n" +
                "            \"km\":5300.36,\n" +
                "            \"mile\":3293.491,\n" +
                "            \"nm\":2861.966,\n" +
                "            \"feet\":17389633.3\n" +
                "        },\n" +
                "        \"departure\":{\n" +
                "            \"airport\":{\n" +
                "                \"icao\":\"VTSP\",\n" +
                "                \"iata\":\"HKT\",\n" +
                "                \"name\":\"Phuket\",\n" +
                "                \"shortName\":\"Phuket\",\n" +
                "                \"municipalityName\":\"Phuket\",\n" +
                "                \"location\":{\n" +
                "                    \"lat\":8.1132,\n" +
                "                    \"lon\":98.3169\n" +
                "                },\n" +
                "                \"countryCode\":\"TH\"\n" +
                "            },\n" +
                "            \"scheduledTimeLocal\":\"2023-01-19 20:20+07:00\",\n" +
                "            \"scheduledTimeUtc\":\"2023-01-19 13:20Z\",\n" +
                "            \"terminal\":\"I\",\n" +
                "            \"quality\":[\n" +
                "                \"Basic\"\n" +
                "            ]\n" +
                "        },\n" +
                "        \"arrival\":{\n" +
                "            \"airport\":{\n" +
                "                \"icao\":\"OTHH\",\n" +
                "                \"iata\":\"DOH\",\n" +
                "                \"name\":\"Doha, Hamad\",\n" +
                "                \"shortName\":\"Hamad\",\n" +
                "                \"municipalityName\":\"Doha\",\n" +
                "                \"location\":{\n" +
                "                    \"lat\":25.273056,\n" +
                "                    \"lon\":51.608055\n" +
                "                },\n" +
                "                \"countryCode\":\"QA\"\n" +
                "            },\n" +
                "            \"scheduledTimeLocal\":\"2023-01-19 23:25+03:00\",\n" +
                "            \"scheduledTimeUtc\":\"2023-01-19 20:25Z\",\n" +
                "            \"quality\":[\n" +
                "                \"Basic\"\n" +
                "            ]\n" +
                "        },\n" +
                "        \"lastUpdatedUtc\":\"2023-01-12 01:35Z\",\n" +
                "        \"number\":\"QR 843\",\n" +
                "        \"status\":\"Unknown\",\n" +
                "        \"codeshareStatus\":\"Unknown\",\n" +
                "        \"isCargo\":false,\n" +
                "        \"aircraft\":{\n" +
                "            \"model\":\"Airbus A330-300\"\n" +
                "        },\n" +
                "        \"airline\":{\n" +
                "            \"name\":\"Qatar Airways\",\n" +
                "            \"iata\":\"QR\",\n" +
                "            \"icao\":\"QTR\"\n" +
                "        }\n" +
                "    }\n" +
                "]";
    }

    private String testNewAPIJsonData(){
        return "[\n" +
                "                           {\n" +
                "                               \"fcategory\": \"4\",\n" +
                "                               \"ftype\": \"B737\",\n" +
                "                               \"fservice\": \"J\",\n" +
                "                               \"FirstClassCheckinTable\": \"\",\n" +
                "                               \"BusinessCheckinTable\": \"\",\n" +
                "                               \"FlightNo\": \"TB6487\",\n" +
                "                               \"FlightCompany\": \"Jetairfly\",\n" +
                "                               \"FlightDepcode\": \"LIL\",\n" +
                "                               \"FlightArrcode\": \"FUE\",\n" +
                "                               \"FlightDeptimePlanDate\": \"2022-09-03 13:30:00\",\n" +
                "                               \"FlightArrtimePlanDate\": \"2022-09-03 18:50:00\",\n" +
                "                               \"FFlightDeptimePlanDate\": \"2022-09-03 13:30:00\",\n" +
                "                               \"FFlightArrtimePlanDate\": \"2022-09-03 18:50:00\",\n" +
                "                               \"FlightDeptimeReadyDate\": \"2022-09-03 16:50:00\",\n" +
                "                               \"FlightArrtimeReadyDate\": \"2022-09-03 21:30:00\",\n" +
                "                               \"FlightDeptimeDate\": \"2022-09-03 17:04:00\",\n" +
                "                               \"FlightArrtimeDate\": \"2022-09-03 21:42:00\",\n" +
                "                               \"FlightIngateTime\": \"2022-09-03 21:42:00\",\n" +
                "                               \"FlightOutgateTime\": \"2022-09-03 17:04:00\",\n" +
                "                               \"CheckinTable\": \"\",\n" +
                "                               \"CheckDoor\": \"\",\n" +
                "                               \"BoardGate\": \"\",\n" +
                "                               \"ReachExit\": \"\",\n" +
                "                               \"BaggageID\": \"11\",\n" +
                "                               \"BoardState\": \"\",\n" +
                "                               \"FlightState\": \"arrival\",\n" +
                "                               \"FlightHTerminal\": \"MAIN\",\n" +
                "                               \"FlightTerminal\": \"T1\",\n" +
                "                               \"org_timezone\": \"7200\",\n" +
                "                               \"dst_timezone\": \"3600\",\n" +
                "                               \"ShareFlightNo\": \"\",\n" +
                "                               \"StopFlag\": \"1\",\n" +
                "                               \"ShareFlag\": \"0\",\n" +
                "                               \"VirtualFlag\": \"\",\n" +
                "                               \"BoardGateTime\": \"\",\n" +
                "                               \"ArrStandGate\": \"\",\n" +
                "                               \"DepStandGate\": \"\",\n" +
                "                               \"DelayReason\": \"\",\n" +
                "                               \"LegFlag\": \"0\",\n" +
                "                               \"Food\": \"\",\n" +
                "                               \"LastCheckinTime\": \"\",\n" +
                "                               \"EstimateBoardingStartTime\": \"\",\n" +
                "                               \"EstimateBoardingEndTime\": \"\",\n" +
                "                               \"FlightDep\": \"Lille\",\n" +
                "                               \"FlightArr\": \"Puerto del Rosario\",\n" +
                "                               \"deptel\": \"+33 320496747\",\n" +
                "                               \"arrtel\": \"\",\n" +
                "                               \"airlinetel\": \"\",\n" +
                "                               \"FlightWaitData\": 0,\n" +
                "                               \"bridge\": \"\",\n" +
                "                               \"arr_bridge\": \"\",\n" +
                "                               \"FlightDepAirport\": \"Lille\",\n" +
                "                               \"FlightArrAirport\": \"PUERTO DEL ROSARIO\",\n" +
                "                               \"OntimeRate\": \"50.00%\",\n" +
                "                               \"generic\": \"Boeing 737-700 Winglets\",\n" +
                "                               \"FlightYear\": 14.6,\n" +
                "                               \"ArrOntimeRate\": \"50.00%\",\n" +
                "                               \"DepWeather\": \"||||\",\n" +
                "                               \"ArrWeather\": \"||||\",\n" +
                "                               \"FlightDuration\": 241,\n" +
                "                               \"distance\": \"2837\",\n" +
                "                               \"FastestExitDuration\": \"\",\n" +
                "                               \"SlowestExitDuration\": \"\",\n" +
                "                               \"FastestExitTime\": \"\",\n" +
                "                               \"SlowestExitTime\": \"\",\n" +
                "                               \"ChangePlane\": \"-1\",\n" +
                "                               \"VeryZhunReadyDeptimeDate\": \"2022-09-03 17:03:00\",\n" +
                "                               \"VeryZhunReadyArrtimeDate\": \"2022-09-03 21:53:00\",\n" +
                "                               \"AssistFlightState\": \"arrival\",\n" +
                "                               \"DepAirportLat\": \"50.57205\",\n" +
                "                               \"DepAirportLon\": \"3.106067\",\n" +
                "                               \"DepTerminalLat\": \"50.57205\",\n" +
                "                               \"DepTerminalLon\": \"3.106067\",\n" +
                "                               \"ArrAirportLat\": \"28.450605\",\n" +
                "                               \"ArrAirportLon\": \"-13.869893\",\n" +
                "                               \"ArrTerminalLat\": \"28.450605\",\n" +
                "                               \"ArrTerminalLon\": \"-13.869893\",\n" +
                "                               \"FlightStateNum\": 2,\n" +
                "                               \"StopAirportCode\": \"LPA\",\n" +
                "                               \"StopCity\": \"Las Palmas\"\n" +
                "                           }\n" +
                "                       ] ";
    }
}