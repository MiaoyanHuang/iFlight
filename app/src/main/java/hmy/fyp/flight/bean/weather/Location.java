package hmy.fyp.flight.bean.weather;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("city")
    private String city;
    @SerializedName("region")
    private String region;
    @SerializedName("woeid")
    private String woeid;
    @SerializedName("country")
    private String country;
    @SerializedName("lat")
    private String lat;
    @SerializedName("long")
    private String lon;
    @SerializedName("timezone_id")
    private String timezoneId;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTimezoneId() {
        return timezoneId;
    }

    public void setTimezoneId(String timezoneId) {
        this.timezoneId = timezoneId;
    }

    @Override
    public String toString() {
        return "Location{" +
                "city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", woeid='" + woeid + '\'' +
                ", country='" + country + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", timezoneId='" + timezoneId + '\'' +
                '}';
    }
}
