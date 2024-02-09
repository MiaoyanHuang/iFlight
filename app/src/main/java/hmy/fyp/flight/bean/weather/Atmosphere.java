package hmy.fyp.flight.bean.weather;

import com.google.gson.annotations.SerializedName;

public class Atmosphere {
    @SerializedName("humidity")
    private String humidity;
    @SerializedName("visibility")
    private String visibility;
    @SerializedName("pressure")
    private String pressure;

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "Atmosphere{" +
                "humidity='" + humidity + '\'' +
                ", visibility='" + visibility + '\'' +
                ", pressure='" + pressure + '\'' +
                '}';
    }
}
