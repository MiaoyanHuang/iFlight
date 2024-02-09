package com.example.flight.bean.weather;


import com.google.gson.annotations.SerializedName;

public class Condition {
    @SerializedName("temperature")
    private String temperature;
    @SerializedName("text")
    private String text;
    @SerializedName("code")
    private String code;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
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

    @Override
    public String toString() {
        return "Condition{" +
                "temperature='" + temperature + '\'' +
                ", text='" + text + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
