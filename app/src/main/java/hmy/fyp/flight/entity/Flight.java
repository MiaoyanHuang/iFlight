package com.example.flight.entity;

/**
 * Created by: Huang Miaoyan
 * Create Date: 2022-12-25
 * Modified Date:
 */

public class Flight {
    private String flight_No;
    private String flight_AirlineName;
    private String flight_Date;
    private String flight_ScheduleDepartureTime;
    private String flight_ScheduleArrivalTime;
    private String flight_DepartureAirport;
    private String flight_ArrivalAirport;

    public Flight() {}

    public Flight(String flight_No, String flight_AirlineName, String flight_Date, String flight_ScheduleDepartureTime, String flight_ScheduleArrivalTime, String flight_DepartureAirport, String flight_ArrivalAirport) {
        this.flight_No = flight_No;
        this.flight_AirlineName = flight_AirlineName;
        this.flight_Date = flight_Date;
        this.flight_ScheduleDepartureTime = flight_ScheduleDepartureTime;
        this.flight_ScheduleArrivalTime = flight_ScheduleArrivalTime;
        this.flight_DepartureAirport = flight_DepartureAirport;
        this.flight_ArrivalAirport = flight_ArrivalAirport;
    }

    public String getFlight_No() {
        return flight_No;
    }

    public void setFlight_No(String flight_No) {
        this.flight_No = flight_No;
    }

    public String getFlight_AirlineName() {
        return flight_AirlineName;
    }

    public void setFlight_AirlineName(String flight_AirlineName) {
        this.flight_AirlineName = flight_AirlineName;
    }

    public String getFlight_Date() {
        return flight_Date;
    }

    public void setFlight_Date(String flight_Date) {
        this.flight_Date = flight_Date;
    }

    public String getFlight_ScheduleDepartureTime() {
        return flight_ScheduleDepartureTime;
    }

    public void setFlight_ScheduleDepartureTime(String flight_ScheduleDepartureTime) {
        this.flight_ScheduleDepartureTime = flight_ScheduleDepartureTime;
    }

    public String getFlight_ScheduleArrivalTime() {
        return flight_ScheduleArrivalTime;
    }

    public void setFlight_ScheduleArrivalTime(String flight_ScheduleArrivalTime) {
        this.flight_ScheduleArrivalTime = flight_ScheduleArrivalTime;
    }

    public String getFlight_DepartureAirport() {
        return flight_DepartureAirport;
    }

    public void setFlight_DepartureAirport(String flight_DepartureAirport) {
        this.flight_DepartureAirport = flight_DepartureAirport;
    }

    public String getFlight_ArrivalAirport() {
        return flight_ArrivalAirport;
    }

    public void setFlight_ArrivalAirport(String flight_ArrivalAirport) {
        this.flight_ArrivalAirport = flight_ArrivalAirport;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flight_No='" + flight_No + '\'' +
                ", flight_AirlineName='" + flight_AirlineName + '\'' +
                ", flight_Date='" + flight_Date + '\'' +
                ", flight_ScheduleDepartureTime='" + flight_ScheduleDepartureTime + '\'' +
                ", flight_ScheduleArrivalTime='" + flight_ScheduleArrivalTime + '\'' +
                ", flight_DepartureAirport='" + flight_DepartureAirport + '\'' +
                ", flight_ArrivalAirport='" + flight_ArrivalAirport + '\'' +
                '}';
    }
}

