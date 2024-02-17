package hmy.fyp.flight.bean.airfare;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Airfare_Bean {

    @SerializedName("context")
    private Context context;

    @SerializedName("itineraries")
    private Itinerary itineraries;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Itinerary getItineraries() {
        return itineraries;
    }

    public void setItineraries(Itinerary itineraries) {
        this.itineraries = itineraries;
    }

    @NonNull
    @Override
    public String toString() {
        return "Airfare_Bean{" +
                "context=" + context +
                ", itineraries=" + itineraries +
                '}';
    }
}
