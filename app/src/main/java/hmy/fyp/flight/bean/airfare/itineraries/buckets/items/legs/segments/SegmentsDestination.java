package hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.segments;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.segments.segmentsDestination.DestinationParent;

public class SegmentsDestination {

    @SerializedName("flightPlaceId")
    private String flightPlaceId;

    @SerializedName("parent")
    private DestinationParent destinationParent;


    public String getFlightPlaceId() {
        return flightPlaceId;
    }

    public void setFlightPlaceId(String flightPlaceId) {
        this.flightPlaceId = flightPlaceId;
    }

    public DestinationParent getDestinationParent() {
        return destinationParent;
    }

    public void setDestinationParent(DestinationParent destinationParent) {
        this.destinationParent = destinationParent;
    }

    @NonNull
    @Override
    public String toString() {
        return "SegmentsDestination{" +
                "flightPlaceId='" + flightPlaceId + '\'' +
                ", destinationParent=" + destinationParent +
                '}';
    }
}
