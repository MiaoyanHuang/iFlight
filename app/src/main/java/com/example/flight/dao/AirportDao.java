package com.example.flight.dao;

import android.util.Log;
import com.example.flight.entity.Airport;
import com.example.flight.utils.JDBCUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AirportDao {

    private static final String TAG = "AirportDao";

    /**
     * Function: Get All Airports From  MySQL Database
     */
    public List<Airport> getAirport(){
        List<Airport> airportList = new ArrayList<>();
        Connection connection = JDBCUtils.getConn();
        try{
            String sql = "select * from airport order by Country, Airport_Name";
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Airport airport = new Airport();
                        airport.setAirportIATACode(rs.getString("IATA_Code"));
                        airport.setAirportName(rs.getString("Airport_Name"));
                        airport.setCountry(rs.getString("Country"));
                        airportList.add(airport);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of List Airport: " + e.getMessage());
        }
        return airportList;
    }

    /**
     * Function: Search Airport By User Inputted Keyword
     */
    public List<Airport> searchAirport(String searchInput){
        List<Airport> airportList = new ArrayList<>();
        Connection connection = JDBCUtils.getConn();
        try{
            String sql = "select * from airport where Airport_Name LIKE ? or IATA_Code LIKE ? or Country LIKE ?";
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, "%"+searchInput+"%");
                    ps.setString(2, "%"+searchInput+"%");
                    ps.setString(3, "%"+searchInput+"%");
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Airport airport = new Airport();
                        airport.setAirportName(rs.getString("Airport_Name"));
                        airport.setAirportIATACode(rs.getString("IATA_Code"));
                        airport.setCountry(rs.getString("Country"));
                        airportList.add(airport);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Search Airport: " + e.getMessage());
            return null;
        }
        return airportList;
    }
}
