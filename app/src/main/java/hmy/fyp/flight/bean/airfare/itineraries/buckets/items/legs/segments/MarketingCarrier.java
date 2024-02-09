package com.example.flight.bean.airfare.itineraries.buckets.items.legs.segments;

import com.google.gson.annotations.SerializedName;

public class MarketingCarrier {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("alternateId")
    private String alternateId;

    @SerializedName("allianceId")
    private String allianceId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlternateId() {
        return alternateId;
    }

    public void setAlternateId(String alternateId) {
        this.alternateId = alternateId;
    }

    public String getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(String allianceId) {
        this.allianceId = allianceId;
    }

    @Override
    public String toString() {
        return "MarketingCarrier{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", alternateId='" + alternateId + '\'' +
                ", allianceId='" + allianceId + '\'' +
                '}';
    }
}
