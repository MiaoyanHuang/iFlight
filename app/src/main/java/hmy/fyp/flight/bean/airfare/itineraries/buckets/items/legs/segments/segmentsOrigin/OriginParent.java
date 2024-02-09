package hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.segments.segmentsOrigin;

import com.google.gson.annotations.SerializedName;

public class OriginParent {

    @SerializedName("flightPlaceId")
    private String flightPlaceId;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    public String getFlightPlaceId() {
        return flightPlaceId;
    }

    public void setFlightPlaceId(String flightPlaceId) {
        this.flightPlaceId = flightPlaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OriginParent{" +
                "flightPlaceId='" + flightPlaceId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
