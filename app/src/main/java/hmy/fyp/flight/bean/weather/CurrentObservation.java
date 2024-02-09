package hmy.fyp.flight.bean.weather;

import com.google.gson.annotations.SerializedName;

public class CurrentObservation {
    @SerializedName("pubDate")
    private int pubDate;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("atmosphere")
    private Atmosphere atmosphere;
    @SerializedName("astronomy")
    private Astronomy astronomy;
    @SerializedName("condition")
    private Condition condition;

    public int getPubDate() {
        return pubDate;
    }

    public void setPubDate(int pubDate) {
        this.pubDate = pubDate;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(Atmosphere atmosphere) {
        this.atmosphere = atmosphere;
    }

    public Astronomy getAstronomy() {
        return astronomy;
    }

    public void setAstronomy(Astronomy astronomy) {
        this.astronomy = astronomy;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "CurrentObservation{" +
                "pubDate=" + pubDate +
                ", wind=" + wind +
                ", atmosphere=" + atmosphere +
                ", astronomy=" + astronomy +
                ", condition=" + condition +
                '}';
    }
}
