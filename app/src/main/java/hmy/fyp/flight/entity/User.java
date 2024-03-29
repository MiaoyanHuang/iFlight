package hmy.fyp.flight.entity;

/**
 * Created by: Huang Miaoyan
 * Create Date: 2022-12-21
 */
public class User {

    private int id;
    private String userAccount;
    private String userPassword;
    private String userName;

    public User() {
    }

    public User(int id, String userAccount, String userPassword, String userName) {
        this.id = id;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userName = userName;

    }

    public int getId() {
        return id;
    }

// --Commented out by Inspection START (2/17/2024 10:27 PM):
//    public void setId(int id) {
//        this.id = id;
//    }
// --Commented out by Inspection STOP (2/17/2024 10:27 PM)

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}