package hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.segments;

import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.segments.segmentsOrigin.OriginParent;
import com.google.gson.annotations.SerializedName;

public class SegmentsOrigin {

    @SerializedName("flightPlaceId")
    private String flightPlaceId;

    @SerializedName("parent")
    private OriginParent originParent;

    public String getFlightPlaceId() {
        return flightPlaceId;
    }

    public void setFlightPlaceId(String flightPlaceId) {
        this.flightPlaceId = flightPlaceId;
    }

    public OriginParent getOriginParent() {
        return originParent;
    }

    public void setOriginParent(OriginParent originParent) {
        this.originParent = originParent;
    }


    @Override
    public String toString() {
        return "SegmentsOrigin{" +
                "flightPlaceId='" + flightPlaceId + '\'' +
                ", originParent=" + originParent +
                '}';
    }
}
