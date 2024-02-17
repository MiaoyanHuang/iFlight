package hmy.fyp.flight;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hmy.fyp.flight.adapter.Adapter_Airfare;
import hmy.fyp.flight.bean.airfare.Airfare_Bean;
import hmy.fyp.flight.bean.airfare.itineraries.Buckets;
import hmy.fyp.flight.bean.airfare.itineraries.buckets.Items;

public class Airfare_Result extends AppCompatActivity {
    private final String TAG = "airfare_result";
    private Button airfare_result_back_button, airfare_result_best, airfare_result_cheap, airfare_result_fast, airfare_result_direct;
    private RecyclerView airfare_result_airfare_list;
    private TextView airfare_result_depAirport, airfare_result_arrAirport, airfare_result_date, airfare_result_totalResult;
    private ImageView airfare_result_noDataIcon;
    private List<Items> allFlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airfare_result);

        airfare_result_noDataIcon = findViewById(R.id.airfare_result_noDataIcon);
        airfare_result_back_button = findViewById(R.id.airfare_result_back_button);
        airfare_result_best = findViewById(R.id.airfare_result_best);
        airfare_result_cheap = findViewById(R.id.airfare_result_cheap);
        airfare_result_fast = findViewById(R.id.airfare_result_fast);
        airfare_result_direct = findViewById(R.id.airfare_result_direct);
        airfare_result_airfare_list = findViewById(R.id.airfare_result_airfare_list);
        airfare_result_depAirport = findViewById(R.id.airfare_result_depAirport);
        airfare_result_arrAirport = findViewById(R.id.airfare_result_arrAirport);
        airfare_result_date = findViewById(R.id.airfare_result_date);
        airfare_result_totalResult = findViewById(R.id.airfare_result_totalResult);

        initView();
    }

    /**
     * Function: Initialize View
     */
    private void initView() {  // 后续考虑开发按钮被按下的反馈
        Gson_Airfare();

        //Back Button Listener
        airfare_result_back_button.setOnClickListener(v -> finish());

        //Best Button Listener
        airfare_result_best.setOnClickListener(v -> displayAirfare(airfare_result_best, "All"));

        //Cheap Button Listener
        airfare_result_cheap.setOnClickListener(v -> displayAirfare(airfare_result_cheap, "Cheap"));

        //Fast Button Listener
        airfare_result_fast.setOnClickListener(v -> displayAirfare(airfare_result_fast, "Fast"));

        //Direct Button Listener
        airfare_result_direct.setOnClickListener(v -> displayAirfare(airfare_result_direct, "Direct"));

        displayAirfare(airfare_result_best, "All"); // Display default Information when redirect to result
    }

    /**
     * Function: Resolve the Airfare Info by Gson
     * Reference Function: Gson
     * Reference From: <a href="https://www.youtube.com/watch?v=f-kcvxYZrB4">...</a>
     */
    private void Gson_Airfare() {
        // Get the airfare_Info from the intent
        Intent intent = getIntent();
        String airfare_Info = intent.getStringExtra("airfare_info");
        String date = intent.getStringExtra("airfare_date");
        String dep_AirportName = intent.getStringExtra("Dep_AirportName");
        String arr_AirportName = intent.getStringExtra("Arr_AirportName");

        Gson gson_airfare = new Gson();
        Airfare_Bean airfare_bean = gson_airfare.fromJson(airfare_Info, Airfare_Bean.class);
        Log.d("TAG", "displayAirfare: " + airfare_bean);
        List<Buckets> buckets = airfare_bean.getItineraries().getBuckets();

        //combine all items
        allFlight = new ArrayList<>();
        for (int i = 0; i < buckets.size(); i++) {
            allFlight.addAll(buckets.get(i).getItems());
        }
        Log.d(TAG, "items size: " + allFlight.size());

        //remove duplicate items
        for (int i = 0; i < allFlight.size(); i++) {
            for (int j = i + 1; j < allFlight.size(); j++) {
                if (allFlight.get(i).getId().equals(allFlight.get(j).getId())) {
                    allFlight.remove(j);
                    j--;
                }
            }
        }
        displayJourneyDetails(dep_AirportName, arr_AirportName, date);
    }

    /**
     * Function: Display Flight Details
     */
    @SuppressLint("SetTextI18n")
    private void displayJourneyDetails(String dep_AirportName, String arr_AirportName, String date) { //Display flight details
        //Display Airport Name
        airfare_result_depAirport.setText(dep_AirportName);
        airfare_result_arrAirport.setText(arr_AirportName);

        //Display Date
        airfare_result_date.setText(date);

        //Display total result
        int ResultCount = allFlight.size();
        airfare_result_totalResult.setText("Total Tickets: " + ResultCount);
    }

    /**
     * Function: Display Airfare Information
     */
    private void displayAirfare(Button buttonName, String sortBy) {
        setButtonProperty(buttonName);
        airfare_result_noDataIcon.setVisibility(View.INVISIBLE);
        List<Items> sortedItems = new ArrayList<>(allFlight);
        switch (sortBy) {
            case "All":
                break;
            case "Cheap":
                Log.d(TAG, "2");
                for (int i = 0; i < sortedItems.size(); i++) {
                    for (int j = i + 1; j < sortedItems.size(); j++) {
                        if (sortedItems.get(i).getPrice().getRaw() > sortedItems.get(j).getPrice().getRaw()) {
                            Items temp = sortedItems.get(i);
                            sortedItems.set(i, sortedItems.get(j));
                            sortedItems.set(j, temp);
                        }
                    }
                }
                break;
            case "Fast":
                for (int i = 0; i < sortedItems.size(); i++) {
                    for (int j = i + 1; j < sortedItems.size(); j++) {
                        if (sortedItems.get(i).getLegs().get(0).getDurationInMinutes() > sortedItems.get(j).getLegs().get(0).getDurationInMinutes()) {
                            Items temp = sortedItems.get(i);
                            sortedItems.set(i, sortedItems.get(j));
                            sortedItems.set(j, temp);
                        }
                    }
                }
                break;
            case "Direct":
                for (int i = 0; i < sortedItems.size(); i++) {
                    if (sortedItems.get(i).getLegs().get(0).getStopCount() != 0) {
                        sortedItems.remove(i);
                        i--; //if remove an item, the index should be minus 1
                    }
                }
                break;
        }
        if (sortedItems.size() == 0) {
            airfare_result_airfare_list.setAdapter(null);
            airfare_result_noDataIcon.setVisibility(View.VISIBLE);
        } else {
            Adapter_Airfare adapter_airfare = new Adapter_Airfare(this, sortedItems);
            airfare_result_airfare_list.setAdapter(adapter_airfare);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            airfare_result_airfare_list.setLayoutManager(layoutManager);
            adapter_airfare.setOnItemClickListener(position -> redirectToDetail(sortedItems.get(position)));
        }
    }


    /**
     * Function: Redirect to Airfare Detail
     */
    private void redirectToDetail(Items item) {
        Gson gson = new Gson();
        String itemInfo = gson.toJson(item);

        Intent intent = getIntent();
        String DepartureAirport = intent.getStringExtra("Dep_AirportName");
        String ArrivalAirport = intent.getStringExtra("Arr_AirportName");

        Intent toDetail = new Intent(this, Airfare_Detail.class);
        toDetail.putExtra("Airfare_Result_DepartureAirport", DepartureAirport);
        toDetail.putExtra("Airfare_Result_ArrivalAirport", ArrivalAirport);
        toDetail.putExtra("Airfare_Result_itemInfo", itemInfo);

        startActivity(toDetail);
    }

    /**
     * Function: Set Button Property
     * Note: 设置按钮颜色 按钮字体颜色 按钮是否可点击       后续考虑是否增加按钮被按下的动态效果
     */
    private void setButtonProperty(Button buttonName) {
        List<Button> ButtonList = new ArrayList<>(Arrays.asList(airfare_result_best, airfare_result_cheap, airfare_result_fast, airfare_result_direct));
        ButtonList.remove(buttonName);
        for (Button view : ButtonList) {
            view.setBackgroundColor(getResources().getColor(R.color.TopicColor));
            view.setTextColor(getResources().getColor(R.color.white));
            view.setClickable(true);
        }
        buttonName.setBackgroundColor(getResources().getColor(R.color.white));
        buttonName.setTextColor(getResources().getColor(R.color.TopicColor));
        buttonName.setClickable(false);
    }
}