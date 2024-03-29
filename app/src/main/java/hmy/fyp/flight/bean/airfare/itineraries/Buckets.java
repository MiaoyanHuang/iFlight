package hmy.fyp.flight.bean.airfare.itineraries;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import hmy.fyp.flight.bean.airfare.itineraries.buckets.Items;

public class Buckets {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("items")
    private List<Items> items;

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

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public String toString() {
        return "Buckets{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", items=" + items +
                '}';
    }
}
