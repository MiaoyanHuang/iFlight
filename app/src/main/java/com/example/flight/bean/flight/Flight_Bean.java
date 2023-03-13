package com.example.flight.bean.flight;

import java.util.List;

public class Flight_Bean {

    public GreatCircleDistance greatCircleDistance;
    public Departure departure;
    public Arrival arrival;
    public String lastUpdatedUtc;
    public String number;
    public String callSign;
    public String status;
    public String codeshareStatus;
    public boolean isCargo;
    public Aircraft aircraft;
    public Airline airline;


    public static class GreatCircleDistance {

        public double meter;
        public double km;
        public double mile;
        public double nm;
        public double feet;

    }

    public static class Departure {
        public Airport airport;
        public String scheduledTimeLocal;
        public String actualTimeLocal;
        public String runwayTimeLocal;
        public String scheduledTimeUtc;
        public String actualTimeUtc;
        public String runwayTimeUtc;
        public String terminal;
        public List<String> quality;
    }

    public static class Arrival {
        public Airport airport;
        public String scheduledTimeLocal;
        public String actualTimeLocal;
        public String scheduledTimeUtc;
        public String actualTimeUtc;
        public String terminal;
        public String baggageBelt;
        public List<String> quality;
    }

    public static class Airport {
        public String icao;
        public String iata;
        public String name;
        public String shortName;
        public String municipalityName;
        public Location location;
        public String countryCode;
    }

    public static class Location {
        public double lat;
        public double lon;
    }

    public static class Aircraft {
        public String reg;
        public String modeS;
        public String model;
    }

    public static class Airline {
        public String name;
    }

}
