package hmy.fyp.flight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hmy.fyp.flight.R;
import hmy.fyp.flight.entity.Flight;
import java.util.List;

public class Adapter_FavoriteList extends RecyclerView.Adapter<Adapter_FavoriteList.FavoriteListViewHolder> {
    private final Context mContext;
    private final List<Flight> mFlightList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Adapter_FavoriteList(Context context, List<Flight> mFlightList) {
        this.mContext = context;
        this.mFlightList = mFlightList;
    }

    @NonNull
    @Override
    public FavoriteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_favorite_list, parent, false);
        FavoriteListViewHolder favoriteListViewHolder = new FavoriteListViewHolder(view);
        return favoriteListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteListViewHolder holder, int position) {

            Flight flight = mFlightList.get(position);
            holder.favoriteList_flight_number.setText(flight.getFlight_No());
            holder.favoriteList_departureAirport.setText(flight.getFlight_DepartureAirport());
            holder.favoriteList_departureTime.setText(flight.getFlight_ScheduleDepartureTime());
            holder.favoriteList_arrivalAirport.setText(flight.getFlight_ArrivalAirport());
            holder.favoriteList_arrivalTime.setText(flight.getFlight_ScheduleArrivalTime());
            holder.itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            });
    }

    @Override
    public int getItemCount() {
        if (mFlightList == null) {
            return 0;
        }
        return mFlightList.size();
    }

    public static class FavoriteListViewHolder extends RecyclerView.ViewHolder {

        TextView favoriteList_flight_number,favoriteList_departureAirport,favoriteList_departureTime,favoriteList_arrivalAirport,favoriteList_arrivalTime;

        public FavoriteListViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteList_flight_number = itemView.findViewById(R.id.favoriteList_flight_number);
            favoriteList_departureAirport = itemView.findViewById(R.id.favoriteList_departureAirport);
            favoriteList_departureTime = itemView.findViewById(R.id.favoriteList_departureTime);
            favoriteList_arrivalAirport = itemView.findViewById(R.id.favoriteList_arrivalAirport);
            favoriteList_arrivalTime = itemView.findViewById(R.id.favoriteList_arrivalTime);
        }
    }
}

