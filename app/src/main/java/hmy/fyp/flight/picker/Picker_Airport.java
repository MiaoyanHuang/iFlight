package hmy.fyp.flight.picker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.ListView;

import hmy.fyp.flight.R;
import hmy.fyp.flight.adapter.Adapter_PickerList;
import hmy.fyp.flight.dao.AirportDao;
import hmy.fyp.flight.entity.Airport;
import java.util.List;

public class Picker_Airport extends AppCompatActivity {
    private ListView picker_list;
    private SearchView picker_searchBar;
    private Button picker_back_button;
    private int RequestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_city);
        picker_list = findViewById(R.id.picker_list);
        picker_back_button = findViewById(R.id.picker_back_button);
        picker_searchBar = findViewById(R.id.picker_searchBar);

        intiView();
    }

    /**
     * Function: Initialize View
     */
    private void intiView() {
        // Back Button Listener
        picker_back_button.setOnClickListener(v -> {
            Intent Intent = new Intent();
            Intent.putExtra("Request_Code", RequestCode);
            setResult(RESULT_CANCELED, Intent);
            finish();
        });

        // Get Request Code from Last Activity
        Intent requestCodeIntent = getIntent();
        RequestCode = requestCodeIntent.getIntExtra("Request_Code", 0);

        // Search Airport By User Inputted Keyword
        picker_searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String userInput) {
                searchAirport(userInput);
                return true;
            }
        });

        getAirports();
    }

    /**
     * Function: Handle for Picker List
     */
    private final Handler handler_picker = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                List<Airport> airportInfo = (List<Airport>) msg.obj;
                displayAirport(airportInfo);
            }
            if (msg.what == 1) {
                picker_list.setAdapter(null);
            }
        }
    };

    /**
     * Function: Display Airport List
     */
    private void displayAirport(List<Airport> airportInfo){
        Adapter_PickerList adapter = new Adapter_PickerList(Picker_Airport.this, airportInfo);
        picker_list.setAdapter(adapter);

        //Picker List Item Clicked Listener
        picker_list.setOnItemClickListener((parent, view, position, id) -> {
            Airport airport = airportInfo.get(position);
            String IATA_Code = airport.getAirportIATACode();
            String AirportName = airport.getAirportName();

            Intent Intent = new Intent();
            Intent.putExtra("IATA_Code", IATA_Code);
            Intent.putExtra("AirportName",AirportName);
            Intent.putExtra("Request_Code", RequestCode);
            setResult(RESULT_OK, Intent);
            finish();
        });
    }

    /**
     * Function: Get All Airports from MySQL Database
     */
    private void getAirports() {
        new Thread(){
            public void run(){
                AirportDao airportDao = new AirportDao();
                Message msg = new Message();
                List<Airport> airportInfo = airportDao.getAirport();
                if (airportInfo != null && airportInfo.size() != 0) {
                    msg.what = 0;
                    msg.obj = airportInfo;
                } else {
                    msg.what = 1;
                }
                handler_picker.sendMessage(msg);
            }
        }.start();
    }

    /**
     * Function: Search Airport By User Inputted Keyword in Search Bar
     */
    private void searchAirport(String userInput){
        new Thread(){
            public void run(){
                AirportDao airportDao = new AirportDao();
                Message msg = new Message();
                List<Airport> airportInfo = airportDao.searchAirport(userInput);
                if (airportInfo != null && airportInfo.size() != 0) {
                    msg.what = 0;
                    msg.obj = airportInfo;
                } else {
                    msg.what = 1;
                }
                handler_picker.sendMessage(msg);
            }
        }.start();
    }
}