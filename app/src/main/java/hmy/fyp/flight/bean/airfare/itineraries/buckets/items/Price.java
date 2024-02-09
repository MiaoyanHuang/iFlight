package hmy.fyp.flight.bean.airfare.itineraries.buckets.items;

import com.google.gson.annotations.SerializedName;

public class Price {

    @SerializedName("raw")
    private double raw;

    @SerializedName("formatted")
    private String formatted;

    public double getRaw() {
        return raw;
    }

    public void setRaw(double raw) {
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
