package hmy.fyp.flight.bean.airfare;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import hmy.fyp.flight.bean.airfare.itineraries.Buckets;

public class Itinerary {

    @SerializedName("buckets")
    private List<Buckets> buckets;

    public List<Buckets> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Buckets> buckets) {
        this.buckets = buckets;
    }

    @NonNull
    @Override
    public String toString() {
        return "Itinerary{" +
                "buckets=" + buckets +
                '}';
    }
}
