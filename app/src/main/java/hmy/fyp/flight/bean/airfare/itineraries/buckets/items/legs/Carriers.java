package hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.carriers.Marketing;
import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.legs.carriers.Operating;

public class Carriers {

    @SerializedName("marketing")
    private List<Marketing> marketing;

    @SerializedName("operating")
    private List<Operating> operating;

    @SerializedName("operationType")
    private String operationType;

    public List<Marketing> getMarketing() {
        return marketing;
    }

    public void setMarketing(List<Marketing> marketing) {
        this.marketing = marketing;
    }

    public List<Operating> getOperating() {
        return operating;
    }

    public void setOperating(List<Operating> operating) {
        this.operating = operating;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @NonNull
    @Override
    public String toString() {
        return "Carriers{" +
                "marketing=" + marketing +
                ", operating=" + operating +
                ", operationType='" + operationType + '\'' +
                '}';
    }
}
