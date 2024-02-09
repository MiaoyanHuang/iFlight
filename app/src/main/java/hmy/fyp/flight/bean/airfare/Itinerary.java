package hmy.fyp.flight.bean.airfare;

import hmy.fyp.flight.bean.airfare.itineraries.Buckets;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Itinerary {

    @SerializedName("buckets")
    private List<Buckets> buckets;

    public List<Buckets> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Buckets> buckets) {
        this.buckets = buckets;
    }

    @Override
    public String toString() {
        return "Itinerary{" +
                "buckets=" + buckets +
                '}';
    }
}
