package hmy.fyp.flight.bean.airfare.itineraries.buckets.items;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.Carriers;
import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.Destination;
import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.Origin;
import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.Segments;

public class Legs {

    @SerializedName("id")
    private String id;

    @SerializedName("origin")
    private Origin origin;

    @SerializedName("destination")
    private Destination destination;

    @SerializedName("durationInMinutes")
    private int durationInMinutes;

    @SerializedName("stopCount")
    private int stopCount;

    @SerializedName("isSmallestStops")
    private String isSmallestStops;

    @SerializedName("departure")
    private String departure;

    @SerializedName("arrival")
    private String arrival;

    @SerializedName("timeDeltaInDays")
    private String timeDeltaInDays;

    //carriers   //可能要做list
    @SerializedName("carriers")
    private Carriers carriers;

    //segments
    @SerializedName("segments")
    private List<Segments> segments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public int getStopCount() {
        return stopCount;
    }

    public void setStopCount(int stopCount) {
        this.stopCount = stopCount;
    }

    public String getIsSmallestStops() {
        return isSmallestStops;
    }

    public void setIsSmallestStops(String isSmallestStops) {
        this.isSmallestStops = isSmallestStops;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getTimeDeltaInDays() {
        return timeDeltaInDays;
    }

    public void setTimeDeltaInDays(String timeDeltaInDays) {
        this.timeDeltaInDays = timeDeltaInDays;
    }

    public Carriers getCarriers() {
        return carriers;
    }

    public void setCarriers(Carriers carriers) {
        this.carriers = carriers;
    }

    public List<Segments> getSegments() {
        return segments;
    }

    public void setSegments(List<Segments> segments) {
        this.segments = segments;
    }

    @NonNull
    @Override
    public String toString() {
        return "Legs{" +
                "id='" + id + '\'' +
                ", origin=" + origin +
                ", destination=" + destination +
                ", durationInMinutes=" + durationInMinutes +
                ", stopCount=" + stopCount +
                ", isSmallestStops='" + isSmallestStops + '\'' +
                ", departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
                ", timeDeltaInDays=" + timeDeltaInDays +
                ", carriers=" + carriers +
                ", segments=" + segments +
                '}';
    }
}
