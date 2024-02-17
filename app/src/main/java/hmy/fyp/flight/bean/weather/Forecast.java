package hmy.fyp.flight.bean.weather;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    @SerializedName("day")
    private String day;

    @SerializedName("date")
    private String date;

    @SerializedName("high")
    private String high;

    @SerializedName("low")
    private String low;

    @SerializedName("text")
    private String text;

    @SerializedName("code")
    private String code;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @NonNull
    @Override
    public String toString() {
        return "Forecast{" +
                "day='" + day + '\'' +
                ", date='" + date + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", text='" + text + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
