package hmy.fyp.flight;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import hmy.fyp.flight.bean.airfare.itineraries.buckets.Items;
import com.google.gson.Gson;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Airfare_Detail extends AppCompatActivity {

    private final String TAG = "airfare_detail";
    private TextView airfare_detail_departure_airport, airfare_detail_departure_date,airfare_detail_departure_weekday;
    private TextView airfare_detail_arrival_airport, airfare_detail_departure_time1, airfare_detail_departure_city1, airfare_detail_arrival_time1, airfare_detail_arrival_city1;
    private TextView airfare_detail_wait_time;
    private TextView airfare_detail_DelayInDay1, airfare_detail_DelayInDay2, airfare_detail_DelayInDay3;
    private TextView airfare_detail_transfer_flightNo, airfare_detail_transfer_airlines;
    private TextView airfare_detail_departure_time2, airfare_detail_departure_city2, airfare_detail_arrival_time2, airfare_detail_arrival_city2;
    private TextView airfare_detail_flightNo, airfare_detail_airlines, airfare_detail_price, airfare_detail_totalDuration;
    private TextView airfare_detail_changeAllowed, airfare_detail_cancellationAllowed;
    private Button airfare_detail_purchase_button;
    private ImageView airfare_detail_logo, airfare_detail_transfer_logo, airfare_detail_transfer_icon;
    private LinearLayout airfare_detail_transfer_flightInfo_block, airfare_detail_transfer_schedule_block;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airfare_detail);

        Button airfare_result_back_button = findViewById(R.id.airfare_detail_back_button);
        airfare_result_back_button.setOnClickListener(v -> finish());

        airfare_detail_departure_airport = findViewById(R.id.airfare_detail_departure_airport);

        airfare_detail_departure_date = findViewById(R.id.airfare_detail_departure_date);
        airfare_detail_departure_weekday = findViewById(R.id.airfare_detail_departure_weekday);

        airfare_detail_departure_time1 = findViewById(R.id.airfare_detail_departure_time1);
        airfare_detail_departure_city1 = findViewById(R.id.airfare_detail_departure_city1);
        airfare_detail_arrival_time1 = findViewById(R.id.airfare_detail_arrival_time1);
        airfare_detail_arrival_city1 = findViewById(R.id.airfare_detail_arrival_city1);

        airfare_detail_DelayInDay1 = findViewById(R.id.airfare_detail_DelayInDay1);
        airfare_detail_DelayInDay2 = findViewById(R.id.airfare_detail_DelayInDay2);
        airfare_detail_DelayInDay3 = findViewById(R.id.airfare_detail_DelayInDay3);

        airfare_detail_transfer_flightInfo_block = findViewById(R.id.airfare_detail_transfer_flightInfo_block);
        airfare_detail_transfer_icon = findViewById(R.id.airfare_detail_transfer_icon);
        airfare_detail_transfer_flightNo = findViewById(R.id.airfare_detail_transfer_flightNo);
        airfare_detail_transfer_airlines = findViewById(R.id.airfare_detail_transfer_airlines);
        airfare_detail_transfer_logo = findViewById(R.id.airfare_detail_transfer_logo);

        airfare_detail_transfer_schedule_block = findViewById(R.id.airfare_detail_transfer_schedule_block);
        airfare_detail_wait_time = findViewById(R.id.airfare_detail_wait_time);
        airfare_detail_departure_time2 = findViewById(R.id.airfare_detail_departure_time2);
        airfare_detail_departure_city2 = findViewById(R.id.airfare_detail_departure_city2);
        airfare_detail_arrival_time2 = findViewById(R.id.airfare_detail_arrival_time2);
        airfare_detail_arrival_city2 = findViewById(R.id.airfare_detail_arrival_city2);

        airfare_detail_flightNo = findViewById(R.id.airfare_detail_flightNo);
        airfare_detail_logo = findViewById(R.id.airfare_detail_logo);
        airfare_detail_airlines = findViewById(R.id.airfare_detail_airlines);

        airfare_detail_changeAllowed = findViewById(R.id.airfare_detail_changeAllowed);
        airfare_detail_cancellationAllowed = findViewById(R.id.airfare_detail_cancellationAllowed);

        airfare_detail_arrival_airport = findViewById(R.id.airfare_detail_arrival_airport);

        airfare_detail_totalDuration = findViewById(R.id.airfare_detail_totalDuration);

        airfare_detail_price = findViewById(R.id.airfare_detail_price);
        airfare_detail_purchase_button = findViewById(R.id.airfare_detail_purchase_button);

        displayDetail();
    }

    private void displayDetail() {

        Intent intent = getIntent();
        String DepartureAirport = intent.getStringExtra("Airfare_Result_DepartureAirport");
        String ArrivalAirport = intent.getStringExtra("Airfare_Result_ArrivalAirport");
        String itemInfo = intent.getStringExtra("Airfare_Result_itemInfo");

        //解析itemInfo
        Gson gson_airfare = new Gson();
        Items items = gson_airfare.fromJson(itemInfo, Items.class);

        // Flight Info Block
        String FlightIATA =  items.getLegs().get(0).getSegments().get(0).getMarketingCarrier().getAlternateId();
        String FlightNumber = items.getLegs().get(0).getSegments().get(0).getFlightNumber();
        String FlightNo = FlightIATA + FlightNumber;
        String LogoUrl = items.getLegs().get(0).getCarriers().getMarketing().get(0).getLogoUrl();
        String Airlines = items.getLegs().get(0).getCarriers().getMarketing().get(0).getName();

        // Schedule Block
        String DepartureDate = items.getLegs().get(0).getDeparture();
        String DepartureTime1 = items.getLegs().get(0).getSegments().get(0).getDeparture().substring(11,16);
        String DepartureCity1 = items.getLegs().get(0).getSegments().get(0).getOrigin().getOriginParent().getName();
        String ArrivalDate1 = items.getLegs().get(0).getSegments().get(0).getArrival();
        String ArrivalTime1 = ArrivalDate1.substring(11,16);
        String ArrivalCity1 = items.getLegs().get(0).getSegments().get(0).getDestination().getDestinationParent().getName();

        // Total Duration Block
        int TotalDuration = items.getLegs().get(0).getDurationInMinutes();

        // Price, Fare Policy and Purchase Button Block
        String Price =  items.getPrice().getFormatted();
        String ChangeAllowed = items.getFarePolicy().getIsChangeAllowed();
        String CancellationAllowed = items.getFarePolicy().getIsCancellationAllowed();
        String DeepLink = items.getDeeplink();

        airfare_detail_departure_airport.setText(DepartureAirport);

        airfare_detail_flightNo.setText(FlightNo);
        Glide.with(this).load(LogoUrl).into(airfare_detail_logo);
        airfare_detail_airlines.setText(Airlines);

        airfare_detail_departure_date.setText(transformDate(DepartureDate));
        airfare_detail_departure_weekday.setText(transformWeekday(DepartureDate));

        airfare_detail_departure_time1.setText(DepartureTime1);
        airfare_detail_departure_city1.setText(DepartureCity1);
        airfare_detail_arrival_time1.setText(ArrivalTime1);
        airfare_detail_DelayInDay1.setText(compareDate(DepartureDate, ArrivalDate1));
        airfare_detail_arrival_city1.setText(ArrivalCity1);

        airfare_detail_arrival_airport.setText(ArrivalAirport);

        airfare_detail_totalDuration.setText(transformDuration(String.valueOf(TotalDuration)));

        // For Transfer Flight
        if(items.getLegs().get(0).getSegments().size()>1 && items.getLegs().get(0).getStopCount() !=0) {
            airfare_detail_transfer_icon.setVisibility(View.VISIBLE);
            airfare_detail_transfer_flightInfo_block.setVisibility(View.VISIBLE);
            airfare_detail_transfer_schedule_block.setVisibility(View.VISIBLE);

            // Transfer Flight Info
            String FlightIATA_Transfer =  items.getLegs().get(0).getSegments().get(1).getMarketingCarrier().getAlternateId();
            String FlightNumber_Transfer = items.getLegs().get(0).getSegments().get(1).getFlightNumber();
            String FlightNo_Transfer = FlightIATA_Transfer + FlightNumber_Transfer;
            String LogoUrl_Transfer = items.getLegs().get(0).getCarriers().getMarketing().get(0).getLogoUrl();
            String Airlines_Transfer = items.getLegs().get(0).getCarriers().getMarketing().get(0).getName();

            // If 2nd flight is operated by different airlines, get the logo and name of 2nd flight
            if (items.getLegs().get(0).getCarriers().getMarketing().size()!=1) {
                LogoUrl_Transfer = items.getLegs().get(0).getCarriers().getMarketing().get(1).getLogoUrl();
                Airlines_Transfer = items.getLegs().get(0).getCarriers().getMarketing().get(1).getName();
            }

            // Departure Info for Transfer Station
            String DepartureDate2 = items.getLegs().get(0).getSegments().get(1).getDeparture();
            String DepartureCity2 = items.getLegs().get(0).getSegments().get(1).getOrigin().getOriginParent().getName();
            String DepartureTime2 = DepartureDate2.substring(11, 16);

            // Calculate Wait Time
            int duration_1st = items.getLegs().get(0).getSegments().get(0).getDurationInMinutes();
            int duration_2nd = items.getLegs().get(0).getSegments().get(1).getDurationInMinutes();
            String WaitTime = String.valueOf(TotalDuration - duration_1st - duration_2nd);

            // Arrival Info
            String ArrivalDate2 = items.getLegs().get(0).getSegments().get(1).getArrival();
            String ArrivalTime2 = ArrivalDate2.substring(11, 16);
            String ArrivalCity2 = items.getLegs().get(0).getSegments().get(1).getDestination().getDestinationParent().getName();

            airfare_detail_transfer_flightNo.setText(FlightNo_Transfer);
            Glide.with(this).load(LogoUrl_Transfer).into(airfare_detail_transfer_logo);
            airfare_detail_transfer_airlines.setText(Airlines_Transfer);

            airfare_detail_departure_time2.setText(DepartureTime2);
            airfare_detail_DelayInDay2.setText(compareDate(DepartureDate, DepartureDate2));
            airfare_detail_departure_city2.setText(DepartureCity2);

            airfare_detail_wait_time.setText(transformDuration(WaitTime));

            airfare_detail_arrival_time2.setText(ArrivalTime2);
            airfare_detail_DelayInDay3.setText(compareDate(DepartureDate, ArrivalDate2));
            airfare_detail_arrival_city2.setText(ArrivalCity2);
        }

        airfare_detail_price.setText(Price);
        airfare_detail_changeAllowed.setText(ChangeAllowed);
        airfare_detail_cancellationAllowed.setText(CancellationAllowed);
        airfare_detail_purchase_button.setOnClickListener(v -> {
            Intent openLink = new Intent(Intent.ACTION_VIEW, Uri.parse(DeepLink));
            startActivity(openLink);
        });
    }

    /**
     * Function: Transform Duration from Minutes to Hour and Minute
     */
    private String transformDuration(String duration) {
        int durationInMinutes = Integer.parseInt(duration);
        int hours = durationInMinutes / 60;
        int minutes = durationInMinutes % 60;
        return hours + " Hour " + minutes + " Minute";
    }

    /**
     * Function: Transform Date from yyyy-MM-dd to MMM. dd, yyyy
     */
    private String transformDate(String originalDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date;
        try {
            date = dateFormat.parse(originalDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM. dd, yyyy", Locale.US);
        return newDateFormat.format(date);
    }

    /**
     * Function: Get Weekday by Date
     */
    private String transformWeekday(String departureDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date;
        try {
            date = dateFormat.parse(departureDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat newDateFormat = new SimpleDateFormat("EEEE", Locale.US);
        return newDateFormat.format(date);
    }

    /**
     * Function: Compare Departure Date and Arrival Date
     */
    public String compareDate(String departureDate,String arrivalDate){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date d1;
        Date d2;
        try {
            d1 = df.parse(departureDate);
            d2 = df.parse(arrivalDate);
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
}