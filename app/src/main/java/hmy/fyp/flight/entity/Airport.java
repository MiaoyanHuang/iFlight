package hmy.fyp.flight.entity;

public class Airport {

    private String airportIATACode;
    private String airportName;
    private String country;

    public String getAirportName() {
        return airportName;
    }

    public String getAirportIATACode() {
        return airportIATACode;
    }

    public void setAirportIATACode(String airportIATACode) {
        this.airportIATACode = airportIATACode;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
