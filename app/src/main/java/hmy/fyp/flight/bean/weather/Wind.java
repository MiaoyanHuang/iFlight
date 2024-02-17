package hmy.fyp.flight.bean.weather;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("chill")
    private String chill;
    @SerializedName("direction")
    private String direction;
    @SerializedName("speed")
    private String speed;

    public String getChill() {
        return chill;
    }

    public void setChill(String chill) {
        this.chill = chill;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    @NonNull
    @Override
    public String toString() {
        return "Wind{" +
                "chill='" + chill + '\'' +
                ", direction='" + direction + '\'' +
                ", speed='" + speed + '\'' +
                '}';
    }
}
