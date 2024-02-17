package hmy.fyp.flight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import hmy.fyp.flight.dao.UserDao;
import hmy.fyp.flight.entity.User;
import hmy.fyp.flight.utils.EncryptUtil;

public class Login extends AppCompatActivity {

    private EditText Account_Input, Password_Input;
    private Button Login_Button, Register_Button;
    private String Account, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login_Button = findViewById(R.id.login_login_button);
        Register_Button = findViewById(R.id.login_register_button);
        Account_Input = findViewById(R.id.login_account_input);
        Password_Input = findViewById(R.id.login_password_input);

        initView();
    }

    /**
     * Function: Handle for login
     */
    private final Handler handle_login = new Handler(Objects.requireNonNull(Looper.myLooper())) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                getID_nickName();
                Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_SHORT).show();
                toHomepage();
            } else if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "Wrong password or account does not exist", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * function: Initialize the view
     */
    private void initView() {
        //Login Button Listener
        Login_Button.setOnClickListener(v -> login());

        //Register Button Listener
        Register_Button.setOnClickListener(v -> toRegister());
    }

    /**
     * Function: Login
     */

    private void login() {
        Account = Account_Input.getText().toString();
        Password = Password_Input.getText().toString();
        if (Account.isEmpty()) {
            Toast.makeText(this, "Please input your account", Toast.LENGTH_SHORT).show();
        } else if (Password.isEmpty()) {
            Toast.makeText(this, "Please input your password", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(() -> {
                UserDao userDao = new UserDao();
                Message msg = new Message();
                boolean result = userDao.login(Account, EncryptUtil.encrypt(Password, EncryptUtil.SHA_256));
                if (result) {
                    msg.what = 0; // login successfully
                } else {
                    msg.what = 1; // login failed
                }
                handle_login.sendEmptyMessage(msg.what);
            }).start();
        }
    }

    /**
     * Function: Redirect to Register Page
     */
    private void toRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    /**
     * Function: Redirect to Homepage after user login successfully
     */
    public void toHomepage() {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
        finish();
    }

    /**
     * Function: Get user ID and NickName
     */
    private void getID_nickName() {
        new Thread(() -> {
            UserDao userDao = new UserDao();
            User user = userDao.getUserData(Account);
            String user_id = String.valueOf(user.getId());
            String user_name = user.getUserName();
            SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_id", user_id);
            editor.putString("user_name", user_name);
            editor.apply();
        }
        ).start();
    }
}