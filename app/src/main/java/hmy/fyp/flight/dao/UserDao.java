package hmy.fyp.flight.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import hmy.fyp.flight.R;
import hmy.fyp.flight.entity.User;
import hmy.fyp.flight.utils.ImageBase64Util;
import hmy.fyp.flight.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class UserDao {

    private static final String TAG = "UserDao";

    /**
     * Function: Login
     */
    public boolean login(String userAccount, String userPassword) {
        String sql = "select * from user where userAccount = ? and userPassword = ?";
        try (Connection connection = JDBCUtil.getConn()) {
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, userAccount);
                    ps.setString(2, userPassword);
                    ResultSet rs = ps.executeQuery();
                    return rs.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Login: " + e.getMessage());
        }
        return false;
    }

    /**
     * Function: Register
     */
    public boolean register(User user, Context context) {
        String sql = "insert into user(userAccount,userPassword,userName,photo) values (?,?,?,?)";
        try (Connection connection = JDBCUtil.getConn()) {
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.user_icon);
                    Bitmap bitmap = ((BitmapDrawable) Objects.requireNonNull(drawable)).getBitmap();
                    String imageEncoded = ImageBase64Util.imageToBase64(bitmap);
                    ps.setString(1, user.getUserAccount());
                    ps.setString(2, user.getUserPassword());
                    ps.setString(3, user.getUserName());
                    ps.setString(4, imageEncoded);
                    ps.executeUpdate();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Register: " + e.getMessage());
        }
        return false;
    }

    /**
     * Function: Query Exist User Account When User Register
     */
    public Boolean queryAccount(String userAccount) {
        try (Connection connection = JDBCUtil.getConn()) {
            String sql = "select * from user where userAccount = ?";
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, userAccount);
                    ResultSet rs = ps.executeQuery();
                    return rs.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Query Account：" + e.getMessage());
        }
        return false;
    }

    /**
     * Function: Get User's Data
     */
    public User getUserData(String userAccount) {
        User user_account = null;
        try (Connection connection = JDBCUtil.getConn()) {
            String sql = "select * from user where userAccount = ?";
            if (connection != null) {
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
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of get User Data：" + e.getMessage());
        }
        return user_account;
    }

    /**
     * Function: Upload / Update User's Photo
     */
    public boolean uploadPhoto(String user_id, String imageEncoded) {
        try (Connection connection = JDBCUtil.getConn()) {
            String sql = "update user set photo = ? where id = ?";
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, imageEncoded);
                    ps.setString(2, user_id);
                    ps.executeUpdate();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Upload Photo" + e.getMessage());
        }
        return false;
    }

    /**
     * Function: Get User's Photo
     */
    public String getPhoto(String user_id) {
        String imageEncoded = null;
        try (Connection connection = JDBCUtil.getConn()) {
            String sql = "select photo from user where id = ?";
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, user_id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        imageEncoded = rs.getString(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception Message of Get Photo" + e.getMessage());
        }
        return imageEncoded;
    }
}
