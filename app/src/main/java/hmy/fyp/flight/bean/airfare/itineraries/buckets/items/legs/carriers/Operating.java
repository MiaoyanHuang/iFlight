package hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.carriers;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Operating {

    @SerializedName("id")
    private String id;

    @SerializedName("logoUrl")
    private String logoUrl;

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Operating{" +
                "id='" + id + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
