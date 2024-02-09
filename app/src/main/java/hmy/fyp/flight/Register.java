package hmy.fyp.flight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hmy.fyp.flight.dao.UserDao;
import hmy.fyp.flight.entity.User;

public class Register extends AppCompatActivity {
    private EditText account_Input, password_Input, nickName_Input;
    private Button Back_button,Register_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Back_button = findViewById(R.id.register_back_button);
        Register_Button = findViewById(R.id.register_register_button);
        account_Input = findViewById(R.id.register_account_input);
        nickName_Input = findViewById(R.id.register_nickname_input);
        password_Input = findViewById(R.id.register_password_input);

        initView();
    }

    /**
     * Function: Handle for register
     */
    private final Handler hand = new Handler(Looper.myLooper())
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 0) {
                Toast.makeText(getApplicationContext(),"Register failed",Toast.LENGTH_SHORT).show();
            } else if(msg.what == 1) {
                Toast.makeText(getApplicationContext(),"Account already exist",Toast.LENGTH_SHORT).show();
            } else if(msg.what == 2) {
                Toast.makeText(getApplicationContext(), "Register successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        }
    };

    /**
     * Function: Initialize the view
     */
    private void initView(){
        //Register Button Listener
        Register_Button.setOnClickListener(v -> register());

        //Back Button Listener
        Back_button.setOnClickListener(v -> finish());
    }

    /**
     * Function: Register
     */
    private void register() {
        String Account = account_Input.getText().toString();
        String Password = password_Input.getText().toString();
        String NickName = nickName_Input.getText().toString();
        if (Account.equals("")){
            Toast.makeText(this, "Please input your account", Toast.LENGTH_SHORT).show();
        } else if(NickName.equals("")){
            Toast.makeText(this, "Please input your nickname", Toast.LENGTH_SHORT).show();
        } else if(Password.equals("")){
            Toast.makeText(this, "Please input your password", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(){
                @Override
                public void run() {
                    Message msg = new Message();
                    UserDao userDao = new UserDao();
                    Boolean result_query = userDao.queryAccount(Account);
                    if(result_query){
                        msg.what = 1; // Account Exist
                    } else {
                        User user = new User();
                        user.setUserAccount(Account);
                        user.setUserPassword(Password);
                        user.setUserName(NickName);
                        boolean result_register = userDao.register(user, getApplicationContext());
                        if(result_register){
                            msg.what = 2; // Register Successfully
                        } else {
                            msg.what = 0; // Register Failed
                        }
                    }
                    hand.sendEmptyMessage(msg.what);
                }
            }.start();
        }
    }
}