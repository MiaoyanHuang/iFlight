package com.example.flight;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Spinner;
import android.widget.Toast;
import com.example.flight.api.API_Airfare;
import com.example.flight.picker.Picker_Airport;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Airfare extends AppCompatActivity {
    private static final String TAG = "airfare";
    private static final int Dep_REQUEST_CODE = 1; // Departure Request Code
    private static final int Arr_REQUEST_CODE = 2; // Arrival Request Code
    private String dep_AirportName, arr_airportName;
    private EditText airfare_departure_input, airfare_arrival_input, airfare_date_input;
    private Spinner airfare_currency;
    private Button airfare_back_button,airfare_search_button;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airfare);

        airfare_departure_input = findViewById(R.id.airfare_departure_input);
        airfare_arrival_input = findViewById(R.id.airfare_arrival_input);
        airfare_date_input = findViewById(R.id.airfare_date_input);
        airfare_currency = findViewById(R.id.airfare_currency);
        airfare_back_button = findViewById(R.id.airfare_back_button);
        airfare_search_button = findViewById(R.id.airfare_search_button);

        intiView();
    }
    /**
     * Function: Handle for Airfare
     */
    private final Handler handler_airfare = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                progressDialogBox(0);
                Toast.makeText(Airfare.this, "No Airfare Information Found", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                progressDialogBox(0);
                Toast.makeText(Airfare.this, "API Usage Exceeded or API Internal Error", Toast.LENGTH_SHORT).show();
            } else if (msg.what ==3) {
                progressDialogBox(0);
                Toast.makeText(getApplicationContext(), "Connection Error or Time Out.", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 4) {
                progressDialogBox(0);
                String airfare_Info = (String) msg.obj;
                redirectToResult(airfare_Info);
            }
        }
    };

    /**
     * Function: ActivityResultLauncher for Picker_City result -> (to replace startActivityForResult(@Deprecated) and onActivityResult function)
     * Reference From: <a href="https://www.cnblogs.com/lihuawei/p/16574688.html">...</a>
     */
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
        result -> {
        if (result.getData()!=null) {
            int Request_Code = result.getData().getIntExtra("Request_Code", 0);

            if (result.getResultCode() == RESULT_OK && Request_Code == Dep_REQUEST_CODE) {
                String IATA_Code = result.getData().getStringExtra("IATA_Code");
                dep_AirportName = result.getData().getStringExtra("AirportName");
                airfare_departure_input.setText(IATA_Code);
            } else if (result.getResultCode() == RESULT_OK && Request_Code == Arr_REQUEST_CODE) {
                String IATA_Code = result.getData().getStringExtra("IATA_Code");
                arr_airportName = result.getData().getStringExtra("AirportName");
                airfare_arrival_input.setText(IATA_Code);
            }
        }
    });

    /**
     * Function: Initialize the view
     */
    @SuppressLint("ClickableViewAccessibility")
    private void intiView() {

        //Back Button Listener
        airfare_back_button.setOnClickListener(v -> finish());

        //Search Button Listener
        airfare_departure_input.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Intent intent = new Intent(this, Picker_Airport.class);
                intent.putExtra("Request_Code", Dep_REQUEST_CODE);
                activityResultLauncher.launch(intent);
                return true;
            }
            return false;
        });

        //Arrival City Picker Listener
        airfare_arrival_input.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Intent intent = new Intent(this, Picker_Airport.class);
                intent.putExtra("Request_Code", Arr_REQUEST_CODE);
                activityResultLauncher.launch(intent);
                return true;
            }
            return false;
        });

        //Date Picker Listener
        airfare_date_input.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    String pickDate = format.format(calendar.getTime());
                    airfare_date_input.setText(pickDate);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis()); // Set the minimum date to today
                datePickerDialog.show();
                return true;
            }
            return false;
        });

        //Search Airfare Listener
        airfare_search_button.setOnClickListener(v -> {
            String departure = airfare_departure_input.getText().toString();
            String arrival = airfare_arrival_input.getText().toString();
            String date = airfare_date_input.getText().toString();
            String currency = airfare_currency.getSelectedItem().toString().substring(0, 3);
            searchAirfare(departure, arrival, date, currency);
        });
    }

    /**
     * Function: Search Airfare
     */
    private void searchAirfare(String departure, String arrival, String date, String currency) {
        //Validate the input
        if (departure.equals("")) {
            Toast.makeText(this, "Please Pick The Departure", Toast.LENGTH_SHORT).show();
        } else if (arrival.equals("")){
            Toast.makeText(this, "Please Pick The Arrival", Toast.LENGTH_SHORT).show();
        } else if (date.equals("")) {
            Toast.makeText(this, "Please Pick The Date", Toast.LENGTH_SHORT).show();
        } else if (departure.equals(arrival)) {
            Toast.makeText(this, "Departure And Arrival Cannot Be The Same", Toast.LENGTH_SHORT).show();
        } else {
            progressDialogBox(1);
            new Thread(() -> {
                Message msg = new Message();
                API_Airfare api_airfare = new API_Airfare();
                String airfare_Info = api_airfare.getAirfareInfo(departure, arrival, date, currency);
                if (airfare_Info == null || airfare_Info.contains("\"totalResults\":0")) {
                    msg.what = 1;
                } else {
                    if (airfare_Info.contains("message")) {
                        msg.what = 2;
                    } else if(airfare_Info.equals("Connection Error or Time Out")) {
                        msg.what = 3;
                    } else {
                        msg.what = 4;
                        msg.obj = airfare_Info;
                    }
                }
                handler_airfare.sendMessage(msg);
            }).start();
        }
    }

    /**
     * Function: Redirect to Airfare Result
     */
    private void redirectToResult(String airfare_Info){
        Intent intent = new Intent(this, Airfare_Result.class);
        String date = airfare_date_input.getText().toString();
        intent.putExtra("airfare_date", date);
        Log.d(TAG, "airfare_Info: " + airfare_Info);
        intent.putExtra("airfare_info", airfare_Info);
        intent.putExtra("Dep_AirportName",dep_AirportName);
        intent.putExtra("Arr_AirportName",arr_airportName);
        startActivity(intent);
    }

    /**
     * Function: Show Progress Dialog
     */
    private void progressDialogBox(int msg){
        if (msg == 1) {  // 1 = show progress dialog
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Searching Flight Ticket");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else if(msg == 0){ // 0 = dismiss progress dialog
            progressDialog.dismiss();
        }
    }

    private String testJson(){
        return "{\n" +
                "    \"context\":{\n" +
                "        \"status\":\"incomplete\",\n" +
                "        \"sessionId\":\"6d221e0b-b16e-499a-9cf4-183cb44b0104\",\n" +
                "        \"totalResults\":10\n" +
                "    },\n" +
                "    \"itineraries\":{\n" +
                "        \"buckets\":[\n" +
                "{"+
                "}," +
                "            {\n" +
                "                \"id\":\"Cheapest\",\n" +
                "                \"name\":\"Cheapest\",\n" +
                "                \"items\":[\n" +
                "                    {\n" +
                "                        \"id\":\"10342-2301282110--31883-0-17944-2301282230\",\n" +
                "                        \"price\":{\n" +
                "                            \"raw\":111.87,\n" +
                "                            \"formatted\":\"112 €\"\n" +
                "                        },\n" +
                "                        \"legs\":[\n" +
                "                            {\n" +
                "                                \"id\":\"10342-2301282110--31883-0-17944-2301282230\",\n" +
                "                                \"origin\":{\n" +
                "                                    \"id\":\"CAN\",\n" +
                "                                    \"name\":\"Guangzhou\",\n" +
                "                                    \"displayCode\":\"CAN\",\n" +
                "                                    \"city\":\"Guangzhou\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"destination\":{\n" +
                "                                    \"id\":\"XMN\",\n" +
                "                                    \"name\":\"Xiamen\",\n" +
                "                                    \"displayCode\":\"XMN\",\n" +
                "                                    \"city\":\"Xiamen\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"durationInMinutes\":80,\n" +
                "                                \"stopCount\":0,\n" +
                "                                \"isSmallestStops\":false,\n" +
                "                                \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                \"arrival\":\"2023-01-28T22:30:00\",\n" +
                "                                \"timeDeltaInDays\":0,\n" +
                "                                \"carriers\":{\n" +
                "                                    \"marketing\":[\n" +
                "                                        {\n" +
                "                                            \"id\":-31883,\n" +
                "                                            \"logoUrl\":\"https://logos.skyscnr.com/images/airlines/favicon/SC.png\",\n" +
                "                                            \"name\":\"Shandong Airlines\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"operationType\":\"fully_operated\"\n" +
                "                                },\n" +
                "                                \"segments\":[\n" +
                "                                    {\n" +
                "                                        \"id\":\"10342-17944-2301282110-2301282230--31883\",\n" +
                "                                        \"origin\":{\n" +
                "                                            \"flightPlaceId\":\"CAN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CCAN\",\n" +
                "                                                \"name\":\"Guangzhou\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Guangzhou\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"destination\":{\n" +
                "                                            \"flightPlaceId\":\"XMN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CXMN\",\n" +
                "                                                \"name\":\"Xiamen\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Xiamen\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                        \"arrival\":\"2023-01-28T22:30:00\",\n" +
                "                                        \"durationInMinutes\":80,\n" +
                "                                        \"flightNumber\":\"2148\",\n" +
                "                                        \"marketingCarrier\":{\n" +
                "                                            \"id\":-31883,\n" +
                "                                            \"name\":\"Shandong Airlines\",\n" +
                "                                            \"alternateId\":\"SC\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        },\n" +
                "                                        \"operatingCarrier\":{\n" +
                "                                            \"id\":-31883,\n" +
                "                                            \"name\":\"Shandong Airlines\",\n" +
                "                                            \"alternateId\":\"SC\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        }\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"isSelfTransfer\":false,\n" +
                "                        \"isProtectedSelfTransfer\":false,\n" +
                "                        \"farePolicy\":{\n" +
                "                            \"isChangeAllowed\":false,\n" +
                "                            \"isPartiallyChangeable\":false,\n" +
                "                            \"isCancellationAllowed\":false,\n" +
                "                            \"isPartiallyRefundable\":false\n" +
                "                        },\n" +
                "                        \"tags\":[\n" +
                "                            \"cheapest\",\n" +
                "                            \"shortest\"\n" +
                "                        ],\n" +
                "                        \"isMashUp\":false,\n" +
                "                        \"hasFlexibleOptions\":false,\n" +
                "                        \"score\":1,\n" +
                "                        \"pricingOptions\":[\n" +
                "                            {\n" +
                "                                \"agentIds\":[\n" +
                "                                    \"ctuk\"\n" +
                "                                ],\n" +
                "                                \"amount\":111.87,\n" +
                "                                \"bookingProposition\":\"PBOOK\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"deeplink\":\"https://www.skyscanner.net/transport/flights/can/xmn/230128/config/10342-2301282110--31883-0-17944-2301282230?adults=1&amp;adultsv2=1&amp;cabinclass=economy&amp;children=0&amp;childrenv2=&amp;destinationentityid=27536311&amp;originentityid=27539684&amp;inboundaltsenabled=false&amp;infants=0&amp;outboundaltsenabled=false&amp;preferdirects=false&amp;ref=home&amp;rtn=0\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\":\"10342-2301282110--31664-0-17944-2301282235\",\n" +
                "                        \"price\":{\n" +
                "                            \"raw\":113.38,\n" +
                "                            \"formatted\":\"114 €\"\n" +
                "                        },\n" +
                "                        \"legs\":[\n" +
                "                            {\n" +
                "                                \"id\":\"10342-2301282110--31664-0-17944-2301282235\",\n" +
                "                                \"origin\":{\n" +
                "                                    \"id\":\"CAN\",\n" +
                "                                    \"name\":\"Guangzhou\",\n" +
                "                                    \"displayCode\":\"CAN\",\n" +
                "                                    \"city\":\"Guangzhou\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"destination\":{\n" +
                "                                    \"id\":\"XMN\",\n" +
                "                                    \"name\":\"Xiamen\",\n" +
                "                                    \"displayCode\":\"XMN\",\n" +
                "                                    \"city\":\"Xiamen\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"durationInMinutes\":85,\n" +
                "                                \"stopCount\":0,\n" +
                "                                \"isSmallestStops\":false,\n" +
                "                                \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                \"arrival\":\"2023-01-28T22:35:00\",\n" +
                "                                \"timeDeltaInDays\":0,\n" +
                "                                \"carriers\":{\n" +
                "                                    \"marketing\":[\n" +
                "                                        {\n" +
                "                                            \"id\":-31664,\n" +
                "                                            \"logoUrl\":\"https://logos.skyscnr.com/images/airlines/favicon/MF.png\",\n" +
                "                                            \"name\":\"Xiamen Airlines\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"operationType\":\"fully_operated\"\n" +
                "                                },\n" +
                "                                \"segments\":[\n" +
                "                                    {\n" +
                "                                        \"id\":\"10342-17944-2301282110-2301282235--31664\",\n" +
                "                                        \"origin\":{\n" +
                "                                            \"flightPlaceId\":\"CAN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CCAN\",\n" +
                "                                                \"name\":\"Guangzhou\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Guangzhou\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"destination\":{\n" +
                "                                            \"flightPlaceId\":\"XMN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CXMN\",\n" +
                "                                                \"name\":\"Xiamen\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Xiamen\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                        \"arrival\":\"2023-01-28T22:35:00\",\n" +
                "                                        \"durationInMinutes\":85,\n" +
                "                                        \"flightNumber\":\"8302\",\n" +
                "                                        \"marketingCarrier\":{\n" +
                "                                            \"id\":-31664,\n" +
                "                                            \"name\":\"Xiamen Airlines\",\n" +
                "                                            \"alternateId\":\"MF\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        },\n" +
                "                                        \"operatingCarrier\":{\n" +
                "                                            \"id\":-31664,\n" +
                "                                            \"name\":\"Xiamen Airlines\",\n" +
                "                                            \"alternateId\":\"MF\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        }\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"isSelfTransfer\":false,\n" +
                "                        \"isProtectedSelfTransfer\":false,\n" +
                "                        \"farePolicy\":{\n" +
                "                            \"isChangeAllowed\":false,\n" +
                "                            \"isPartiallyChangeable\":false,\n" +
                "                            \"isCancellationAllowed\":false,\n" +
                "                            \"isPartiallyRefundable\":false\n" +
                "                        },\n" +
                "                        \"tags\":[\n" +
                "                            \"second_cheapest\",\n" +
                "                            \"second_shortest\"\n" +
                "                        ],\n" +
                "                        \"isMashUp\":false,\n" +
                "                        \"hasFlexibleOptions\":false,\n" +
                "                        \"score\":0.9531638,\n" +
                "                        \"pricingOptions\":[\n" +
                "                            {\n" +
                "                                \"agentIds\":[\n" +
                "                                    \"ctuk\"\n" +
                "                                ],\n" +
                "                                \"amount\":113.38,\n" +
                "                                \"bookingProposition\":\"PBOOK\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"deeplink\":\"https://www.skyscanner.net/transport/flights/can/xmn/230128/config/10342-2301282110--31664-0-17944-2301282235?adults=1&amp;adultsv2=1&amp;cabinclass=economy&amp;children=0&amp;childrenv2=&amp;destinationentityid=27536311&amp;originentityid=27539684&amp;inboundaltsenabled=false&amp;infants=0&amp;outboundaltsenabled=false&amp;preferdirects=false&amp;ref=home&amp;rtn=0\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\":\"10342-2301282110--32247-0-17944-2301282235\",\n" +
                "                        \"price\":{\n" +
                "                            \"raw\":117.49,\n" +
                "                            \"formatted\":\"118 €\"\n" +
                "                        },\n" +
                "                        \"legs\":[\n" +
                "                            {\n" +
                "                                \"id\":\"10342-2301282110--32247-0-17944-2301282235\",\n" +
                "                                \"origin\":{\n" +
                "                                    \"id\":\"CAN\",\n" +
                "                                    \"name\":\"Guangzhou\",\n" +
                "                                    \"displayCode\":\"CAN\",\n" +
                "                                    \"city\":\"Guangzhou\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"destination\":{\n" +
                "                                    \"id\":\"XMN\",\n" +
                "                                    \"name\":\"Xiamen\",\n" +
                "                                    \"displayCode\":\"XMN\",\n" +
                "                                    \"city\":\"Xiamen\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"durationInMinutes\":85,\n" +
                "                                \"stopCount\":0,\n" +
                "                                \"isSmallestStops\":false,\n" +
                "                                \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                \"arrival\":\"2023-01-28T22:35:00\",\n" +
                "                                \"timeDeltaInDays\":0,\n" +
                "                                \"carriers\":{\n" +
                "                                    \"marketing\":[\n" +
                "                                        {\n" +
                "                                            \"id\":-32247,\n" +
                "                                            \"logoUrl\":\"https://logos.skyscnr.com/images/airlines/favicon/NS.png\",\n" +
                "                                            \"name\":\"Hebei Airlines\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"operating\":[\n" +
                "                                        {\n" +
                "                                            \"id\":-31664,\n" +
                "                                            \"logoUrl\":\"https://logos.skyscnr.com/images/airlines/favicon/MF.png\",\n" +
                "                                            \"name\":\"Xiamen Airlines\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"operationType\":\"not_operated\"\n" +
                "                                },\n" +
                "                                \"segments\":[\n" +
                "                                    {\n" +
                "                                        \"id\":\"10342-17944-2301282110-2301282235--32247\",\n" +
                "                                        \"origin\":{\n" +
                "                                            \"flightPlaceId\":\"CAN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CCAN\",\n" +
                "                                                \"name\":\"Guangzhou\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Guangzhou\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"destination\":{\n" +
                "                                            \"flightPlaceId\":\"XMN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CXMN\",\n" +
                "                                                \"name\":\"Xiamen\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Xiamen\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                        \"arrival\":\"2023-01-28T22:35:00\",\n" +
                "                                        \"durationInMinutes\":85,\n" +
                "                                        \"flightNumber\":\"8260\",\n" +
                "                                        \"marketingCarrier\":{\n" +
                "                                            \"id\":-32247,\n" +
                "                                            \"name\":\"Hebei Airlines\",\n" +
                "                                            \"alternateId\":\"NS\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        },\n" +
                "                                        \"operatingCarrier\":{\n" +
                "                                            \"id\":-31664,\n" +
                "                                            \"name\":\"Xiamen Airlines\",\n" +
                "                                            \"alternateId\":\"MF\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        }\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"isSelfTransfer\":false,\n" +
                "                        \"isProtectedSelfTransfer\":false,\n" +
                "                        \"farePolicy\":{\n" +
                "                            \"isChangeAllowed\":false,\n" +
                "                            \"isPartiallyChangeable\":false,\n" +
                "                            \"isCancellationAllowed\":false,\n" +
                "                            \"isPartiallyRefundable\":false\n" +
                "                        },\n" +
                "                        \"tags\":[\n" +
                "                            \"third_cheapest\",\n" +
                "                            \"second_shortest\"\n" +
                "                        ],\n" +
                "                        \"isMashUp\":false,\n" +
                "                        \"hasFlexibleOptions\":false,\n" +
                "                        \"score\":0.8256824,\n" +
                "                        \"pricingOptions\":[\n" +
                "                            {\n" +
                "                                \"agentIds\":[\n" +
                "                                    \"ctuk\"\n" +
                "                                ],\n" +
                "                                \"amount\":117.49,\n" +
                "                                \"bookingProposition\":\"PBOOK\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"deeplink\":\"https://www.skyscanner.net/transport/flights/can/xmn/230128/config/10342-2301282110--32247-0-17944-2301282235?adults=1&amp;adultsv2=1&amp;cabinclass=economy&amp;children=0&amp;childrenv2=&amp;destinationentityid=27536311&amp;originentityid=27539684&amp;inboundaltsenabled=false&amp;infants=0&amp;outboundaltsenabled=false&amp;preferdirects=false&amp;ref=home&amp;rtn=0\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\":\"Fastest\",\n" +
                "                \"name\":\"Fastest\",\n" +
                "                \"items\":[\n" +
                "                    {\n" +
                "                        \"id\":\"10342-2301282110--32690-0-17944-2301282230\",\n" +
                "                        \"price\":{\n" +
                "                            \"raw\":140.64,\n" +
                "                            \"formatted\":\"141 €\"\n" +
                "                        },\n" +
                "                        \"legs\":[\n" +
                "                            {\n" +
                "                                \"id\":\"10342-2301282110--32690-0-17944-2301282230\",\n" +
                "                                \"origin\":{\n" +
                "                                    \"id\":\"CAN\",\n" +
                "                                    \"name\":\"Guangzhou\",\n" +
                "                                    \"displayCode\":\"CAN\",\n" +
                "                                    \"city\":\"Guangzhou\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"destination\":{\n" +
                "                                    \"id\":\"XMN\",\n" +
                "                                    \"name\":\"Xiamen\",\n" +
                "                                    \"displayCode\":\"XMN\",\n" +
                "                                    \"city\":\"Xiamen\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"durationInMinutes\":80,\n" +
                "                                \"stopCount\":0,\n" +
                "                                \"isSmallestStops\":false,\n" +
                "                                \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                \"arrival\":\"2023-01-28T22:30:00\",\n" +
                "                                \"timeDeltaInDays\":0,\n" +
                "                                \"carriers\":{\n" +
                "                                    \"marketing\":[\n" +
                "                                        {\n" +
                "                                            \"id\":-32690,\n" +
                "                                            \"logoUrl\":\"https://logos.skyscnr.com/images/airlines/favicon/CA.png\",\n" +
                "                                            \"name\":\"Air China\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"operating\":[\n" +
                "                                        {\n" +
                "                                            \"id\":-31883,\n" +
                "                                            \"logoUrl\":\"https://logos.skyscnr.com/images/airlines/favicon/SC.png\",\n" +
                "                                            \"name\":\"Shandong Airlines\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"operationType\":\"not_operated\"\n" +
                "                                },\n" +
                "                                \"segments\":[\n" +
                "                                    {\n" +
                "                                        \"id\":\"10342-17944-2301282110-2301282230--32690\",\n" +
                "                                        \"origin\":{\n" +
                "                                            \"flightPlaceId\":\"CAN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CCAN\",\n" +
                "                                                \"name\":\"Guangzhou\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Guangzhou\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"destination\":{\n" +
                "                                            \"flightPlaceId\":\"XMN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CXMN\",\n" +
                "                                                \"name\":\"Xiamen\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Xiamen\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                        \"arrival\":\"2023-01-28T22:30:00\",\n" +
                "                                        \"durationInMinutes\":80,\n" +
                "                                        \"flightNumber\":\"4654\",\n" +
                "                                        \"marketingCarrier\":{\n" +
                "                                            \"id\":-32690,\n" +
                "                                            \"name\":\"Air China\",\n" +
                "                                            \"alternateId\":\"CA\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        },\n" +
                "                                        \"operatingCarrier\":{\n" +
                "                                            \"id\":-31883,\n" +
                "                                            \"name\":\"Shandong Airlines\",\n" +
                "                                            \"alternateId\":\"SC\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        }\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"isSelfTransfer\":false,\n" +
                "                        \"isProtectedSelfTransfer\":false,\n" +
                "                        \"farePolicy\":{\n" +
                "                            \"isChangeAllowed\":false,\n" +
                "                            \"isPartiallyChangeable\":false,\n" +
                "                            \"isCancellationAllowed\":false,\n" +
                "                            \"isPartiallyRefundable\":false\n" +
                "                        },\n" +
                "                        \"tags\":[\n" +
                "                            \"shortest\"\n" +
                "                        ],\n" +
                "                        \"isMashUp\":false,\n" +
                "                        \"hasFlexibleOptions\":false,\n" +
                "                        \"score\":1,\n" +
                "                        \"pricingOptions\":[\n" +
                "                            {\n" +
                "                                \"agentIds\":[\n" +
                "                                    \"ctuk\"\n" +
                "                                ],\n" +
                "                                \"amount\":140.64,\n" +
                "                                \"bookingProposition\":\"PBOOK\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"deeplink\":\"https://www.skyscanner.net/transport/flights/can/xmn/230128/config/10342-2301282110--32690-0-17944-2301282230?adults=1&amp;adultsv2=1&amp;cabinclass=economy&amp;children=0&amp;childrenv2=&amp;destinationentityid=27536311&amp;originentityid=27539684&amp;inboundaltsenabled=false&amp;infants=0&amp;outboundaltsenabled=false&amp;preferdirects=false&amp;ref=home&amp;rtn=0\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\":\"10342-2301282110--31880-0-17944-2301282230\",\n" +
                "                        \"price\":{\n" +
                "                            \"raw\":140.64,\n" +
                "                            \"formatted\":\"141 €\"\n" +
                "                        },\n" +
                "                        \"legs\":[\n" +
                "                            {\n" +
                "                                \"id\":\"10342-2301282110--31880-0-17944-2301282230\",\n" +
                "                                \"origin\":{\n" +
                "                                    \"id\":\"CAN\",\n" +
                "                                    \"name\":\"Guangzhou\",\n" +
                "                                    \"displayCode\":\"CAN\",\n" +
                "                                    \"city\":\"Guangzhou\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"destination\":{\n" +
                "                                    \"id\":\"XMN\",\n" +
                "                                    \"name\":\"Xiamen\",\n" +
                "                                    \"displayCode\":\"XMN\",\n" +
                "                                    \"city\":\"Xiamen\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"durationInMinutes\":80,\n" +
                "                                \"stopCount\":0,\n" +
                "                                \"isSmallestStops\":false,\n" +
                "                                \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                \"arrival\":\"2023-01-28T22:30:00\",\n" +
                "                                \"timeDeltaInDays\":0,\n" +
                "                                \"carriers\":{\n" +
                "                                    \"marketing\":[\n" +
                "                                        {\n" +
                "                                            \"id\":-31880,\n" +
                "                                            \"logoUrl\":\"https://logos.skyscnr.com/images/airlines/favicon/ZH.png\",\n" +
                "                                            \"name\":\"Shenzhen Airlines\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"operating\":[\n" +
                "                                        {\n" +
                "                                            \"id\":-31883,\n" +
                "                                            \"logoUrl\":\"https://logos.skyscnr.com/images/airlines/favicon/SC.png\",\n" +
                "                                            \"name\":\"Shandong Airlines\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"operationType\":\"not_operated\"\n" +
                "                                },\n" +
                "                                \"segments\":[\n" +
                "                                    {\n" +
                "                                        \"id\":\"10342-17944-2301282110-2301282230--31880\",\n" +
                "                                        \"origin\":{\n" +
                "                                            \"flightPlaceId\":\"CAN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CCAN\",\n" +
                "                                                \"name\":\"Guangzhou\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Guangzhou\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"destination\":{\n" +
                "                                            \"flightPlaceId\":\"XMN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CXMN\",\n" +
                "                                                \"name\":\"Xiamen\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Xiamen\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                        \"arrival\":\"2023-01-28T22:30:00\",\n" +
                "                                        \"durationInMinutes\":80,\n" +
                "                                        \"flightNumber\":\"2552\",\n" +
                "                                        \"marketingCarrier\":{\n" +
                "                                            \"id\":-31880,\n" +
                "                                            \"name\":\"Shenzhen Airlines\",\n" +
                "                                            \"alternateId\":\"ZH\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        },\n" +
                "                                        \"operatingCarrier\":{\n" +
                "                                            \"id\":-31883,\n" +
                "                                            \"name\":\"Shandong Airlines\",\n" +
                "                                            \"alternateId\":\"SC\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        }\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"isSelfTransfer\":false,\n" +
                "                        \"isProtectedSelfTransfer\":false,\n" +
                "                        \"farePolicy\":{\n" +
                "                            \"isChangeAllowed\":false,\n" +
                "                            \"isPartiallyChangeable\":false,\n" +
                "                            \"isCancellationAllowed\":false,\n" +
                "                            \"isPartiallyRefundable\":false\n" +
                "                        },\n" +
                "                        \"tags\":[\n" +
                "                            \"shortest\"\n" +
                "                        ],\n" +
                "                        \"isMashUp\":false,\n" +
                "                        \"hasFlexibleOptions\":false,\n" +
                "                        \"score\":1,\n" +
                "                        \"pricingOptions\":[\n" +
                "                            {\n" +
                "                                \"agentIds\":[\n" +
                "                                    \"ctuk\"\n" +
                "                                ],\n" +
                "                                \"amount\":140.64,\n" +
                "                                \"bookingProposition\":\"PBOOK\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"deeplink\":\"https://www.skyscanner.net/transport/flights/can/xmn/230128/config/10342-2301282110--31880-0-17944-2301282230?adults=1&amp;adultsv2=1&amp;cabinclass=economy&amp;children=0&amp;childrenv2=&amp;destinationentityid=27536311&amp;originentityid=27539684&amp;inboundaltsenabled=false&amp;infants=0&amp;outboundaltsenabled=false&amp;preferdirects=false&amp;ref=home&amp;rtn=0\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\":\"10342-2301282110--31883-0-17944-2301282230\",\n" +
                "                        \"price\":{\n" +
                "                            \"raw\":111.87,\n" +
                "                            \"formatted\":\"112 €\"\n" +
                "                        },\n" +
                "                        \"legs\":[\n" +
                "                            {\n" +
                "                                \"id\":\"10342-2301282110--31883-0-17944-2301282230\",\n" +
                "                                \"origin\":{\n" +
                "                                    \"id\":\"CAN\",\n" +
                "                                    \"name\":\"Guangzhou\",\n" +
                "                                    \"displayCode\":\"CAN\",\n" +
                "                                    \"city\":\"Guangzhou\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"destination\":{\n" +
                "                                    \"id\":\"XMN\",\n" +
                "                                    \"name\":\"Xiamen\",\n" +
                "                                    \"displayCode\":\"XMN\",\n" +
                "                                    \"city\":\"Xiamen\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"durationInMinutes\":80,\n" +
                "                                \"stopCount\":0,\n" +
                "                                \"isSmallestStops\":false,\n" +
                "                                \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                \"arrival\":\"2023-01-28T22:30:00\",\n" +
                "                                \"timeDeltaInDays\":0,\n" +
                "                                \"carriers\":{\n" +
                "                                    \"marketing\":[\n" +
                "                                        {\n" +
                "                                            \"id\":-31883,\n" +
                "                                            \"logoUrl\":\"https://logos.skyscnr.com/images/airlines/favicon/SC.png\",\n" +
                "                                            \"name\":\"Shandong Airlines\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"operationType\":\"fully_operated\"\n" +
                "                                },\n" +
                "                                \"segments\":[\n" +
                "                                    {\n" +
                "                                        \"id\":\"10342-17944-2301282110-2301282230--31883\",\n" +
                "                                        \"origin\":{\n" +
                "                                            \"flightPlaceId\":\"CAN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CCAN\",\n" +
                "                                                \"name\":\"Guangzhou\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Guangzhou\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"destination\":{\n" +
                "                                            \"flightPlaceId\":\"XMN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CXMN\",\n" +
                "                                                \"name\":\"Xiamen\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Xiamen\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                        \"arrival\":\"2023-01-28T22:30:00\",\n" +
                "                                        \"durationInMinutes\":80,\n" +
                "                                        \"flightNumber\":\"2148\",\n" +
                "                                        \"marketingCarrier\":{\n" +
                "                                            \"id\":-31883,\n" +
                "                                            \"name\":\"Shandong Airlines\",\n" +
                "                                            \"alternateId\":\"SC\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        },\n" +
                "                                        \"operatingCarrier\":{\n" +
                "                                            \"id\":-31883,\n" +
                "                                            \"name\":\"Shandong Airlines\",\n" +
                "                                            \"alternateId\":\"SC\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        }\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"isSelfTransfer\":false,\n" +
                "                        \"isProtectedSelfTransfer\":false,\n" +
                "                        \"farePolicy\":{\n" +
                "                            \"isChangeAllowed\":false,\n" +
                "                            \"isPartiallyChangeable\":false,\n" +
                "                            \"isCancellationAllowed\":false,\n" +
                "                            \"isPartiallyRefundable\":false\n" +
                "                        },\n" +
                "                        \"tags\":[\n" +
                "                            \"cheapest\",\n" +
                "                            \"shortest\"\n" +
                "                        ],\n" +
                "                        \"isMashUp\":false,\n" +
                "                        \"hasFlexibleOptions\":false,\n" +
                "                        \"score\":1,\n" +
                "                        \"pricingOptions\":[\n" +
                "                            {\n" +
                "                                \"agentIds\":[\n" +
                "                                    \"ctuk\"\n" +
                "                                ],\n" +
                "                                \"amount\":111.87,\n" +
                "                                \"bookingProposition\":\"PBOOK\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"deeplink\":\"https://www.skyscanner.net/transport/flights/can/xmn/230128/config/10342-2301282110--31883-0-17944-2301282230?adults=1&amp;adultsv2=1&amp;cabinclass=economy&amp;children=0&amp;childrenv2=&amp;destinationentityid=27536311&amp;originentityid=27539684&amp;inboundaltsenabled=false&amp;infants=0&amp;outboundaltsenabled=false&amp;preferdirects=false&amp;ref=home&amp;rtn=0\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\":\"Direct\",\n" +
                "                \"name\":\"Direct\",\n" +
                "                \"items\":[\n" +
                "                    {\n" +
                "                        \"id\":\"10342-2301282110--31883-0-17944-2301282230\",\n" +
                "                        \"price\":{\n" +
                "                            \"raw\":111.87,\n" +
                "                            \"formatted\":\"112 €\"\n" +
                "                        },\n" +
                "                        \"legs\":[\n" +
                "                            {\n" +
                "                                \"id\":\"10342-2301282110--31883-0-17944-2301282230\",\n" +
                "                                \"origin\":{\n" +
                "                                    \"id\":\"CAN\",\n" +
                "                                    \"name\":\"Guangzhou\",\n" +
                "                                    \"displayCode\":\"CAN\",\n" +
                "                                    \"city\":\"Guangzhou\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"destination\":{\n" +
                "                                    \"id\":\"XMN\",\n" +
                "                                    \"name\":\"Xiamen\",\n" +
                "                                    \"displayCode\":\"XMN\",\n" +
                "                                    \"city\":\"Xiamen\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"durationInMinutes\":80,\n" +
                "                                \"stopCount\":0,\n" +
                "                                \"isSmallestStops\":false,\n" +
                "                                \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                \"arrival\":\"2023-01-28T22:30:00\",\n" +
                "                                \"timeDeltaInDays\":0,\n" +
                "                                \"carriers\":{\n" +
                "                                    \"marketing\":[\n" +
                "                                        {\n" +
                "                                            \"id\":-31883,\n" +
                "                                            \"logoUrl\":\"https://logos.skyscnr.com/images/airlines/favicon/SC.png\",\n" +
                "                                            \"name\":\"Shandong Airlines\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"operationType\":\"fully_operated\"\n" +
                "                                },\n" +
                "                                \"segments\":[\n" +
                "                                    {\n" +
                "                                        \"id\":\"10342-17944-2301282110-2301282230--31883\",\n" +
                "                                        \"origin\":{\n" +
                "                                            \"flightPlaceId\":\"CAN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CCAN\",\n" +
                "                                                \"name\":\"Guangzhou\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Guangzhou\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"destination\":{\n" +
                "                                            \"flightPlaceId\":\"XMN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CXMN\",\n" +
                "                                                \"name\":\"Xiamen\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Xiamen\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                        \"arrival\":\"2023-01-28T22:30:00\",\n" +
                "                                        \"durationInMinutes\":80,\n" +
                "                                        \"flightNumber\":\"2148\",\n" +
                "                                        \"marketingCarrier\":{\n" +
                "                                            \"id\":-31883,\n" +
                "                                            \"name\":\"Shandong Airlines\",\n" +
                "                                            \"alternateId\":\"SC\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        },\n" +
                "                                        \"operatingCarrier\":{\n" +
                "                                            \"id\":-31883,\n" +
                "                                            \"name\":\"Shandong Airlines\",\n" +
                "                                            \"alternateId\":\"SC\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        }\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"isSelfTransfer\":false,\n" +
                "                        \"isProtectedSelfTransfer\":false,\n" +
                "                        \"farePolicy\":{\n" +
                "                            \"isChangeAllowed\":false,\n" +
                "                            \"isPartiallyChangeable\":false,\n" +
                "                            \"isCancellationAllowed\":false,\n" +
                "                            \"isPartiallyRefundable\":false\n" +
                "                        },\n" +
                "                        \"tags\":[\n" +
                "                            \"cheapest\",\n" +
                "                            \"shortest\"\n" +
                "                        ],\n" +
                "                        \"isMashUp\":false,\n" +
                "                        \"hasFlexibleOptions\":false,\n" +
                "                        \"score\":0.9375,\n" +
                "                        \"pricingOptions\":[\n" +
                "                            {\n" +
                "                                \"agentIds\":[\n" +
                "                                    \"ctuk\"\n" +
                "                                ],\n" +
                "                                \"amount\":111.87,\n" +
                "                                \"bookingProposition\":\"PBOOK\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"deeplink\":\"https://www.skyscanner.net/transport/flights/can/xmn/230128/config/10342-2301282110--31883-0-17944-2301282230?adults=1&amp;adultsv2=1&amp;cabinclass=economy&amp;children=0&amp;childrenv2=&amp;destinationentityid=27536311&amp;originentityid=27539684&amp;inboundaltsenabled=false&amp;infants=0&amp;outboundaltsenabled=false&amp;preferdirects=false&amp;ref=home&amp;rtn=0\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\":\"10342-2301282110--31664-0-17944-2301282235\",\n" +
                "                        \"price\":{\n" +
                "                            \"raw\":113.38,\n" +
                "                            \"formatted\":\"114 €\"\n" +
                "                        },\n" +
                "                        \"legs\":[\n" +
                "                            {\n" +
                "                                \"id\":\"10342-2301282110--31664-0-17944-2301282235\",\n" +
                "                                \"origin\":{\n" +
                "                                    \"id\":\"CAN\",\n" +
                "                                    \"name\":\"Guangzhou\",\n" +
                "                                    \"displayCode\":\"CAN\",\n" +
                "                                    \"city\":\"Guangzhou\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"destination\":{\n" +
                "                                    \"id\":\"XMN\",\n" +
                "                                    \"name\":\"Xiamen\",\n" +
                "                                    \"displayCode\":\"XMN\",\n" +
                "                                    \"city\":\"Xiamen\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"durationInMinutes\":85,\n" +
                "                                \"stopCount\":0,\n" +
                "                                \"isSmallestStops\":false,\n" +
                "                                \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                \"arrival\":\"2023-01-28T22:35:00\",\n" +
                "                                \"timeDeltaInDays\":0,\n" +
                "                                \"carriers\":{\n" +
                "                                    \"marketing\":[\n" +
                "                                        {\n" +
                "                                            \"id\":-31664,\n" +
                "                                            \"logoUrl\":\"https://logos.skyscnr.com/images/airlines/favicon/MF.png\",\n" +
                "                                            \"name\":\"Xiamen Airlines\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"operationType\":\"fully_operated\"\n" +
                "                                },\n" +
                "                                \"segments\":[\n" +
                "                                    {\n" +
                "                                        \"id\":\"10342-17944-2301282110-2301282235--31664\",\n" +
                "                                        \"origin\":{\n" +
                "                                            \"flightPlaceId\":\"CAN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CCAN\",\n" +
                "                                                \"name\":\"Guangzhou\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Guangzhou\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"destination\":{\n" +
                "                                            \"flightPlaceId\":\"XMN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CXMN\",\n" +
                "                                                \"name\":\"Xiamen\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Xiamen\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                        \"arrival\":\"2023-01-28T22:35:00\",\n" +
                "                                        \"durationInMinutes\":85,\n" +
                "                                        \"flightNumber\":\"8302\",\n" +
                "                                        \"marketingCarrier\":{\n" +
                "                                            \"id\":-31664,\n" +
                "                                            \"name\":\"Xiamen Airlines\",\n" +
                "                                            \"alternateId\":\"MF\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        },\n" +
                "                                        \"operatingCarrier\":{\n" +
                "                                            \"id\":-31664,\n" +
                "                                            \"name\":\"Xiamen Airlines\",\n" +
                "                                            \"alternateId\":\"MF\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        }\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"isSelfTransfer\":false,\n" +
                "                        \"isProtectedSelfTransfer\":false,\n" +
                "                        \"farePolicy\":{\n" +
                "                            \"isChangeAllowed\":false,\n" +
                "                            \"isPartiallyChangeable\":false,\n" +
                "                            \"isCancellationAllowed\":false,\n" +
                "                            \"isPartiallyRefundable\":false\n" +
                "                        },\n" +
                "                        \"tags\":[\n" +
                "                            \"second_cheapest\",\n" +
                "                            \"second_shortest\"\n" +
                "                        ],\n" +
                "                        \"isMashUp\":false,\n" +
                "                        \"hasFlexibleOptions\":false,\n" +
                "                        \"score\":0.870602,\n" +
                "                        \"pricingOptions\":[\n" +
                "                            {\n" +
                "                                \"agentIds\":[\n" +
                "                                    \"ctuk\"\n" +
                "                                ],\n" +
                "                                \"amount\":113.38,\n" +
                "                                \"bookingProposition\":\"PBOOK\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"deeplink\":\"https://www.skyscanner.net/transport/flights/can/xmn/230128/config/10342-2301282110--31664-0-17944-2301282235?adults=1&amp;adultsv2=1&amp;cabinclass=economy&amp;children=0&amp;childrenv2=&amp;destinationentityid=27536311&amp;originentityid=27539684&amp;inboundaltsenabled=false&amp;infants=0&amp;outboundaltsenabled=false&amp;preferdirects=false&amp;ref=home&amp;rtn=0\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\":\"10342-2301282110--32247-0-17944-2301282235\",\n" +
                "                        \"price\":{\n" +
                "                            \"raw\":117.49,\n" +
                "                            \"formatted\":\"118 €\"\n" +
                "                        },\n" +
                "                        \"legs\":[\n" +
                "                            {\n" +
                "                                \"id\":\"10342-2301282110--32247-0-17944-2301282235\",\n" +
                "                                \"origin\":{\n" +
                "                                    \"id\":\"CAN\",\n" +
                "                                    \"name\":\"Guangzhou\",\n" +
                "                                    \"displayCode\":\"CAN\",\n" +
                "                                    \"city\":\"Guangzhou\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"destination\":{\n" +
                "                                    \"id\":\"XMN\",\n" +
                "                                    \"name\":\"Xiamen\",\n" +
                "                                    \"displayCode\":\"XMN\",\n" +
                "                                    \"city\":\"Xiamen\",\n" +
                "                                    \"isHighlighted\":false\n" +
                "                                },\n" +
                "                                \"durationInMinutes\":85,\n" +
                "                                \"stopCount\":0,\n" +
                "                                \"isSmallestStops\":false,\n" +
                "                                \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                \"arrival\":\"2023-01-28T22:35:00\",\n" +
                "                                \"timeDeltaInDays\":0,\n" +
                "                                \"carriers\":{\n" +
                "                                    \"marketing\":[\n" +
                "                                        {\n" +
                "                                            \"id\":-32247,\n" +
                "                                            \"logoUrl\":\"https://logos.skyscnr.com/images/airlines/favicon/NS.png\",\n" +
                "                                            \"name\":\"Hebei Airlines\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"operating\":[\n" +
                "                                        {\n" +
                "                                            \"id\":-31664,\n" +
                "                                            \"logoUrl\":\"https://logos.skyscnr.com/images/airlines/favicon/MF.png\",\n" +
                "                                            \"name\":\"Xiamen Airlines\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"operationType\":\"not_operated\"\n" +
                "                                },\n" +
                "                                \"segments\":[\n" +
                "                                    {\n" +
                "                                        \"id\":\"10342-17944-2301282110-2301282235--32247\",\n" +
                "                                        \"origin\":{\n" +
                "                                            \"flightPlaceId\":\"CAN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CCAN\",\n" +
                "                                                \"name\":\"Guangzhou\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Guangzhou\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"destination\":{\n" +
                "                                            \"flightPlaceId\":\"XMN\",\n" +
                "                                            \"parent\":{\n" +
                "                                                \"flightPlaceId\":\"CXMN\",\n" +
                "                                                \"name\":\"Xiamen\",\n" +
                "                                                \"type\":\"City\"\n" +
                "                                            },\n" +
                "                                            \"name\":\"Xiamen\",\n" +
                "                                            \"type\":\"Airport\"\n" +
                "                                        },\n" +
                "                                        \"departure\":\"2023-01-28T21:10:00\",\n" +
                "                                        \"arrival\":\"2023-01-28T22:35:00\",\n" +
                "                                        \"durationInMinutes\":85,\n" +
                "                                        \"flightNumber\":\"8260\",\n" +
                "                                        \"marketingCarrier\":{\n" +
                "                                            \"id\":-32247,\n" +
                "                                            \"name\":\"Hebei Airlines\",\n" +
                "                                            \"alternateId\":\"NS\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        },\n" +
                "                                        \"operatingCarrier\":{\n" +
                "                                            \"id\":-31664,\n" +
                "                                            \"name\":\"Xiamen Airlines\",\n" +
                "                                            \"alternateId\":\"MF\",\n" +
                "                                            \"allianceId\":0\n" +
                "                                        }\n" +
                "                                    }\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"isSelfTransfer\":false,\n" +
                "                        \"isProtectedSelfTransfer\":false,\n" +
                "                        \"farePolicy\":{\n" +
                "                            \"isChangeAllowed\":false,\n" +
                "                            \"isPartiallyChangeable\":false,\n" +
                "                            \"isCancellationAllowed\":false,\n" +
                "                            \"isPartiallyRefundable\":false\n" +
                "                        },\n" +
                "                        \"tags\":[\n" +
                "                            \"third_cheapest\",\n" +
                "                            \"second_shortest\"\n" +
                "                        ],\n" +
                "                        \"isMashUp\":false,\n" +
                "                        \"hasFlexibleOptions\":false,\n" +
                "                        \"score\":0.840063,\n" +
                "                        \"pricingOptions\":[\n" +
                "                            {\n" +
                "                                \"agentIds\":[\n" +
                "                                    \"ctuk\"\n" +
                "                                ],\n" +
                "                                \"amount\":117.49,\n" +
                "                                \"bookingProposition\":\"PBOOK\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"deeplink\":\"https://www.skyscanner.net/transport/flights/can/xmn/230128/config/10342-2301282110--32247-0-17944-2301282235?adults=1&amp;adultsv2=1&amp;cabinclass=economy&amp;children=0&amp;childrenv2=&amp;destinationentityid=27536311&amp;originentityid=27539684&amp;inboundaltsenabled=false&amp;infants=0&amp;outboundaltsenabled=false&amp;preferdirects=false&amp;ref=home&amp;rtn=0\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
    }
}