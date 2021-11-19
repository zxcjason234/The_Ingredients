package com.example.final_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login extends AppCompatActivity {

    SharedPreferences shp;
    SharedPreferences.Editor shpEditor;
    Button buttonLogin;
    ProgressBar progressBar;
    TextView Member_ID, Member_pass, txtres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        shp = getSharedPreferences("myPreferences", MODE_PRIVATE); //SharedPreferencesd 開啟設定模式
        CheckLogin();                                                    //先執行這個fun


        Member_ID = findViewById(R.id.edt_login_memberID);
        Member_pass = findViewById(R.id.edt_login_memberpass);

        progressBar = findViewById(R.id.progress);
        buttonLogin = findViewById(R.id.buttonLogin);

        txtres = findViewById(R.id.txtres);

        txtres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username, password;
                username = String.valueOf(Member_ID.getText());
                password = String.valueOf(Member_pass.getText());

                if (!username.equals("") && !password.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "member_id";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;
                            Log.i("pw",password);
                            PutData putData = new PutData("http://192.168.0.195/Graduate/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Login Success")){
                                        //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        DoLogin(Member_ID.getText().toString(), Member_pass.getText().toString()); //SharedPreferences
//                                        String str = Member_ID.getText().toString();
//                                        Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
//                                        intent.putExtra("Welcome",str);
//                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"帳號或密碼錯誤",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"缺少帳號或密碼", Toast.LENGTH_SHORT).show();
                }
            }
        });//end of btnlogin


    }//end of oncreate

    public void CheckLogin() {
        if (shp == null)
            shp = getSharedPreferences("myPreferences", MODE_PRIVATE);

        String userName = shp.getString("name", "");


        if (userName != null && !userName.equals("")) {
            Intent i = new Intent(Login.this, AfterLogin.class);
            startActivity(i);
            finish();
        }
    }//End of CheckLogin

    public void DoLogin(String userid, String password) {
        try {
//            if (password.equals("Android3am")) {
                if (shp == null)
                    shp = getSharedPreferences("myPreferences", MODE_PRIVATE);

                shpEditor = shp.edit();
                shpEditor.putString("name", userid);
                shpEditor.commit();

//                                        Intent intent = new Intent(getApplicationContext(), AfterLogin.class);

//                                        startActivity(intent);
                String str = Member_ID.getText().toString();
                Intent i = new Intent(Login.this, AfterLogin.class);
                i.putExtra("Welcome",str);
                startActivity(i);
                finish();
//            } else{}
                //txtInfo.setText("Invalid Credentails");
        } catch (Exception ex) {
            //txtInfo.setText(ex.getMessage().toString());
        }
    }//End of DoLogin

}//end of class