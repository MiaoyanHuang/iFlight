package hmy.fyp.flight.utils;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtil {
    private static final String TAG = "JDBCUtil";
    private static final String driver = "com.mysql.jdbc.Driver"; // MySql Driver
    private static final String ip = "10.0.2.2:3306/"; // VM ip
    private static final String dbName = "flight"; // Database Name
    private static final String user = "root"; // Username
    private static final String password = "admin"; // Password

    /**
     * Function： Connect to the MySQL Database
     */
    public static Connection getConn() {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + dbName, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of JDBC: " + e.getMessage());
        }
        return connection;
    }
}

