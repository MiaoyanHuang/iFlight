package com.example.flight.dao;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import androidx.core.content.res.ResourcesCompat;
import com.example.flight.R;
import com.example.flight.entity.User;
import com.example.flight.utils.ImageBase64Utils;
import com.example.flight.utils.JDBCUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class UserDao {

    private static final String TAG = "UserDao";

    /**
     * Function: Login
     * */
    public boolean login(String userAccount, String userPassword){
        Connection connection = JDBCUtils.getConn();
        try {
            String sql = "select * from user where userAccount = ? and userPassword = ?";
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, userAccount);
                    ps.setString(2, userPassword);
                    ResultSet rs = ps.executeQuery();
                    return rs.next();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Login: " + e.getMessage());
        }
        return false;
    }

    /**
     * Function: Register
     * */
    public boolean register(User user){
        Connection connection = JDBCUtils.getConn();
        try {
            String sql = "insert into user(userAccount,userPassword,userName,photo) values (?,?,?,?)";
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    Drawable Drawable = ResourcesCompat.getDrawable(Resources.getSystem(), R.drawable.user_icon, null);
                    Bitmap bitmap = ((BitmapDrawable) Objects.requireNonNull(Drawable)).getBitmap();
                    String imageEncoded = ImageBase64Utils.imageToBase64(bitmap);
                    ps.setString(1,user.getUserAccount());
                    ps.setString(2,user.getUserPassword());
                    ps.setString(3,user.getUserName());
                    ps.setString(4,imageEncoded);
                    ps.executeUpdate();
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Register: " + e.getMessage());
        }
        return false;
    }

    /**
     * Function: Query Exist User Account When User Register
     * */
    public Boolean queryAccount(String userAccount) {
        Connection connection = JDBCUtils.getConn();
        try {
            String sql = "select * from user where userAccount = ?";
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, userAccount);
                    ResultSet rs = ps.executeQuery();
                    return rs.next();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Find User：" + e.getMessage());
        }
        return false;
    }

    /**
     * Function: Get User's Data
     * */
    public User getUserData(String userAccount) {
        Connection connection = JDBCUtils.getConn();
        User user_account = null;
        try {
            String sql = "select * from user where userAccount = ?";
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, userAccount);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        String userAccount1 = rs.getString(2);
                        String userPassword = rs.getString(3);
                        String userName = rs.getString(4);
                        user_account = new User(id, userAccount1, userPassword, userName);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Find User：" + e.getMessage());
        }
        return user_account;
    }

    /**
     * Function: Upload / Update User's Photo
     * */
    public boolean uploadPhoto(String user_id, String imageEncoded) {
        Connection connection = JDBCUtils.getConn();
        try {
            String sql = "update user set photo = ? where id = ?";
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, imageEncoded);
                    ps.setString(2, user_id);
                    ps.executeUpdate();
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Upload Photo" + e.getMessage());
        }
        return false;
    }

    /**
     * Function: Get User's Photo
     * */
    public String getPhoto(String user_id) {
        Connection connection = JDBCUtils.getConn();
        String imageEncoded = null;
        try {
            String sql = "select photo from user where id = ?";
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, user_id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        imageEncoded = rs.getString(1);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Get Photo" + e.getMessage());
        }
        return imageEncoded;
    }
}
