package hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Origin {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("displayCode")
    private String displayCode;

    @SerializedName("city")
    private String city;

    @SerializedName("isHighlighted")
    private String isHighlighted;

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

    public String getDisplayCode() {
        return displayCode;
    }

    public void setDisplayCode(String displayCode) {
        this.displayCode = displayCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIsHighlighted() {
        return isHighlighted;
    }

    public void setIsHighlighted(String isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    @NonNull
    @Override
    public String toString() {
        return "Origin{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", displayCode='" + displayCode + '\'' +
                ", city='" + city + '\'' +
                ", isHighlighted='" + isHighlighted + '\'' +
                '}';
    }
}
