package hmy.fyp.flight.dao;

import android.util.Log;

import hmy.fyp.flight.entity.Flight;
import hmy.fyp.flight.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FlightDao {

    private static final String TAG = "FlightDao";

    /**
     * Function: Favorite Flight
     */
    public void favoriteFlight(Flight flight, String id) {
        String sql = "insert into favorite(flight_no,flight_date,flight_airline," +
                "flight_depAirport,flight_depTime,flight_arrAirport,flight_arrTime,user_id) " +
                "values(?,?,?,?,?,?,?,?)";
        try (Connection connection = JDBCUtil.getConn()) {
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, flight.getFlight_No());
                    ps.setString(2, flight.getFlight_Date());
                    ps.setString(3, flight.getFlight_AirlineName());
                    ps.setString(4, flight.getFlight_DepartureAirport());
                    ps.setString(5, flight.getFlight_ScheduleDepartureTime());
                    ps.setString(6, flight.getFlight_ArrivalAirport());
                    ps.setString(7, flight.getFlight_ScheduleArrivalTime());
                    ps.setString(8, id);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Favorite Flight: " + e.getMessage());
        }
    }

    /**
     * Function: Delete Favorite Flight
     */
    public void deleteFavoriteFlight(String flightNo, String flightDate, String id) {
        String sql = "delete from favorite where flight_no = ? and flight_date = ? and user_id = ?";
        try (Connection connection = JDBCUtil.getConn()) {
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, flightNo);
                    ps.setString(2, flightDate);
                    ps.setString(3, id);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Delete Favorite Flight: " + e.getMessage());
        }
    }

    /**
     * Function: Check Favorite Flight
     */
    public boolean checkFavoriteFlight(String flight_No, String flight_Date, String id) {
        String sql = "select * from favorite where flight_no = ? and flight_date = ? and user_id = ?";
        try (Connection connection = JDBCUtil.getConn()) {
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, flight_No);
                    ps.setString(2, flight_Date);
                    ps.setString(3, id);
                    ResultSet rs = ps.executeQuery();
                    return rs.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Check Favorite Flight: " + e.getMessage());
        }
        return false;
    }

    /**
     * Function: Search and Get Favorite Flight
     */
    public List<Flight> searchFavoriteFlight(int user_id) {
        List<Flight> flightList = new ArrayList<>();
        String sql = "select * from favorite where user_id = ?";
        try (Connection connection = JDBCUtil.getConn()) {
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, user_id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Flight flight = new Flight();
                        flight.setFlight_No(rs.getString("flight_no"));
                        flight.setFlight_Date(rs.getString("flight_date"));
                        flight.setFlight_AirlineName(rs.getString("flight_airline"));
                        flight.setFlight_DepartureAirport(rs.getString("flight_depAirport"));
                        flight.setFlight_ScheduleDepartureTime(rs.getString("flight_depTime"));
                        flight.setFlight_ArrivalAirport(rs.getString("flight_arrAirport"));
                        flight.setFlight_ScheduleArrivalTime(rs.getString("flight_arrTime"));
                        flightList.add(flight);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of List Favorite Flight: " + e.getMessage());
        }
        return flightList;
    }
}


