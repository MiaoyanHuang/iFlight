package hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs;

import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.segments.MarketingCarrier;
import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.segments.OperatingCarrier;
import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.segments.SegmentsDestination;
import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.segments.SegmentsOrigin;
import com.google.gson.annotations.SerializedName;

public class Segments {

    @SerializedName("id")
    private String id;

    @SerializedName("origin")
    private SegmentsOrigin origin;

    @SerializedName("destination")
    private SegmentsDestination destination;

    @SerializedName("departure")
    private String departure;

    @SerializedName("arrival")
    private String arrival;

    @SerializedName("durationInMinutes")
    private int durationInMinutes;

    @SerializedName("flightNumber")
    private String flightNumber;

    @SerializedName("marketingCarrier")
    private MarketingCarrier marketingCarrier;

    @SerializedName("operatingCarrier")
    private OperatingCarrier operatingCarrier;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SegmentsOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(SegmentsOrigin origin) {
        this.origin = origin;
    }

    public SegmentsDestination getDestination() {
        return destination;
    }

    public void setDestination(SegmentsDestination destination) {
        this.destination = destination;
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

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public MarketingCarrier getMarketingCarrier() {
        return marketingCarrier;
    }

    public void setMarketingCarrier(MarketingCarrier marketingCarrier) {
        this.marketingCarrier = marketingCarrier;
    }

    public OperatingCarrier getOperatingCarrier() {
        return operatingCarrier;
    }

    public void setOperatingCarrier(OperatingCarrier operatingCarrier) {
        this.operatingCarrier = operatingCarrier;
    }

    @Override
    public String toString() {
        return "Segments{" +
                "id='" + id + '\'' +
                ", origin=" + origin +
                ", destination=" + destination +
                ", departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
                ", durationInMinutes=" + durationInMinutes +
                ", flightNumber='" + flightNumber + '\'' +
                ", marketingCarrier=" + marketingCarrier +
                ", operatingCarrier=" + operatingCarrier +
                '}';
    }
}
