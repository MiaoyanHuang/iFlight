package hmy.fyp.flight.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import hmy.fyp.flight.R;
import hmy.fyp.flight.bean.airfare.itineraries.buckets.Items;

import java.util.List;

public class Adapter_Airfare extends RecyclerView.Adapter<Adapter_Airfare.AirfareViewHolder> {
    private final Context mContext;
    private final List<Items> mItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Adapter_Airfare(Context mContext, List<Items> mItems) {
        this.mContext = mContext;
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public AirfareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_airfare_list, parent, false);
        return new AirfareViewHolder(view);
    }

    /**
     * Referenced Function Name: 1.Glide - A Function to Load Image from URL into ImageView
     * Referenced from: <a href="https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android">...</a>
     * Answered by: @chiragkyada
     * Answered on: 2014-03-26
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AirfareViewHolder holder, int position) {
        Items items = mItems.get(position);
        holder.airfareList_departureTime.setText(items.getLegs().get(0).getDeparture().substring(11, 16));
        holder.airfareList_departureIATA.setText(items.getLegs().get(0).getOrigin().getId());

        int duration = items.getLegs().get(0).getDurationInMinutes();
        holder.airfareList_duration.setText(transformDuration(String.valueOf(duration)));

        int stopCount = items.getLegs().get(0).getStopCount();
        if (stopCount != 0) {
            holder.airfareList_stops.setText(stopCount + " Stop");
        }

        holder.airfareList_arrivalTime.setText(items.getLegs().get(0).getArrival().substring(11, 16));
        holder.airfareList_arrivalIATA.setText(items.getLegs().get(0).getDestination().getId());

        String timeDeltaInDays = items.getLegs().get(0).getTimeDeltaInDays();
        if (!timeDeltaInDays.equals("0")) {
            holder.airfareList_price_delayDay.setText("+" + timeDeltaInDays);
        }

        String marketingCarrier = items.getLegs().get(0).getCarriers().getMarketing().get(0).getName();

        if (items.getLegs().get(0).getCarriers().getOperating() != null) {
            String operatingCarrier = items.getLegs().get(0).getCarriers().getOperating().get(0).getName();
            if (!marketingCarrier.equals(operatingCarrier)) {
                String operatedBy = " Operated By " + operatingCarrier;
                holder.airfareList_airlineName.setText(marketingCarrier + operatedBy);
            } else {
                holder.airfareList_airlineName.setText(marketingCarrier);
            }
        } else {
            holder.airfareList_airlineName.setText(marketingCarrier);
        }
        holder.airfareList_price.setText(items.getPrice().getFormatted());

        //Download Airlines Logo
        String imageUrl = items.getLegs().get(0).getCarriers().getMarketing().get(0).getLogoUrl();
        Glide.with(mContext).load(imageUrl).into(holder.airfareList_airlinesLogo);

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public static class AirfareViewHolder extends RecyclerView.ViewHolder {
        private final TextView airfareList_departureTime, airfareList_departureIATA;
        private final TextView airfareList_duration, airfareList_stops, airfareList_price_delayDay;
        private final TextView airfareList_arrivalTime, airfareList_arrivalIATA;
        private final TextView airfareList_airlineName, airfareList_price;
        private final ImageView airfareList_airlinesLogo;

        public AirfareViewHolder(@NonNull View itemView) {
            super(itemView);
            airfareList_departureTime = itemView.findViewById(R.id.airfareList_departureTime);
            airfareList_departureIATA = itemView.findViewById(R.id.airfareList_departureIATA);
            airfareList_duration = itemView.findViewById(R.id.airfareList_duration);
            airfareList_stops = itemView.findViewById(R.id.airfareList_stops);
            airfareList_arrivalTime = itemView.findViewById(R.id.airfareList_arrivalTime);
            airfareList_price_delayDay = itemView.findViewById(R.id.airfareList_price_delayDay);
            airfareList_arrivalIATA = itemView.findViewById(R.id.airfareList_arrivalIATA);
            airfareList_airlineName = itemView.findViewById(R.id.airfareList_airlineName);
            airfareList_price = itemView.findViewById(R.id.airfareList_price);
            airfareList_airlinesLogo = itemView.findViewById(R.id.airfareList_airlinesLogo);
        }
    }

    /**
     * Function: Transform Duration from Minutes to Hours and Minutes
     */
    private String transformDuration(String duration) {
        int durationInMinutes = Integer.parseInt(duration);
        int hours = durationInMinutes / 60;
        int minutes = durationInMinutes % 60;
        return hours + "h " + minutes + "m";
    }
}
