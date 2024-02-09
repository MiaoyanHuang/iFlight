package com.example.flight.bean.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather_Bean {

    @SerializedName("location")
    private Location location;
    @SerializedName("current_observation")
    private CurrentObservation currentObservation;
    @SerializedName("forecasts")
    private List<Forecast> forecasts;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public CurrentObservation getCurrentObservation() {
        return currentObservation;
    }

    public void setCurrentObservation(CurrentObservation currentObservation) {
        this.currentObservation = currentObservation;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    @Override
    public String toString() {
        return "bean_weather{" +
                "location=" + location +
                ", currentObservation=" + currentObservation +
                ", forecasts=" + forecasts +
                '}';
    }
}
