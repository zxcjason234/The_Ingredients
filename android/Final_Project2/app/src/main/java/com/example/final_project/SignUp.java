package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {


    ProgressBar progressBar;
    Button buttonSignUp;
    TextView Member_ID, Member_name, Member_pass, txtloginback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txtloginback = findViewById(R.id.txtloginback);

        txtloginback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        Member_ID = findViewById(R.id.edt_SignUp_memberID);
        Member_name = findViewById(R.id.edt_SignUp_memberName);
        Member_pass = findViewById(R.id.edt_SignUp_memberpass);
        progressBar = findViewById(R.id.progress);

        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String UserID, Username, password;

                UserID = String.valueOf(Member_ID.getText());
                Username = String.valueOf(Member_name.getText());
                password = String.valueOf(Member_pass.getText());

                if (!UserID.equals("") &&!Username.equals("") && !password.equals("") ) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[3];
                            field[0] = "member_id";
                            field[1] = "member_name";
                            field[2] = "password";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = UserID;
                            data[1] = Username;
                            data[2] = password;
                            PutData putData = new PutData("http://192.168.0.195/Graduate/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Sign Up Success")){
                                        Toast.makeText(getApplicationContext(),"註冊成功",Toast.LENGTH_SHORT).show();
                                        String ustr = Member_ID.getText().toString();//讓別人吃要打的
                                        Intent intent = new Intent(getApplicationContext(), set_user_hobby_register.class);
                                        intent.putExtra("memberid",ustr);//讓別人吃要打的
                                        startActivity(intent);
//                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"帳號已被使用",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"缺少帳號資料", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}