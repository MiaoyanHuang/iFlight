package com.example.flight.bean.airfare.itineraries.buckets.items;

import com.google.gson.annotations.SerializedName;

public class Price {

    @SerializedName("raw")
    private String raw;

    @SerializedName("formatted")
    private String formatted;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    @Override
    public String toString() {
        return "Price{" +
                "raw='" + raw + '\'' +
                ", formatted='" + formatted + '\'' +
                '}';
    }
}
