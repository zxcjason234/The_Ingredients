package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class set_member extends AppCompatActivity {

    SharedPreferences shp;
    SharedPreferences.Editor shpEditor; //會記住帳號
    EditText Name, Password;
    ProgressBar progressBar;
    Button btnupgrade, btnbackprofile;

    TextView User, tran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_set_member);
        shp = getSharedPreferences("myPreferences", MODE_PRIVATE);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        TextView tbtv=(TextView)findViewById(R.id.textView2); //toolbar上面的textview
        ImageView btn_back = (ImageView)findViewById(R.id.imageView); //圖片的按鈕
        tbtv.setText("個人資料");

        // 上面接收登入者的id
        String username = getIntent().getStringExtra("memberid");//接收前一頁給的東西
        TextView txtloginName = (TextView) findViewById(R.id.txt_member_id_member);//接收前一頁給的東西
        txtloginName.setText(username);//接收前一頁給的東西

        // 上面接收冰箱的id
        String refridgeid = getIntent().getStringExtra("refridgeid");//接收前一頁給的東西
        TextView txtrefridgeid = (TextView) findViewById(R.id.txt_fridge_id_member);//接收前一頁給的東西
        txtrefridgeid.setText(refridgeid);//接收前一頁給的東西

        // 下面使用者名字
        String user = getIntent().getStringExtra("userid");
        TextView txtUser = (TextView)findViewById(R.id.txtUser);
        txtUser.setText(user);



        bottomNavigationView.setSelectedItemId(R.id.User);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.Fridge:
                        String str = txtloginName.getText().toString();//讓別人吃要打的
                        Intent intent = new Intent(getApplicationContext(), Fridge.class);
                        intent.putExtra("memberid",str);//讓別人吃要打的

                        String fri = txtrefridgeid.getText().toString();//讓別人吃要打的
                        intent.putExtra("refridgeid",fri);//讓別人吃要打的
                        startActivity(intent);
//                        startActivity(new Intent(getApplicationContext()
//                                ,Fridge.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.home:
                        String hstr = txtloginName.getText().toString();//讓別人吃要打的
                        Intent hintent = new Intent(getApplicationContext(), MainActivity.class);
                        hintent.putExtra("memberid",hstr);//讓別人吃要打的

                        String hfri = txtrefridgeid.getText().toString();//讓別人吃要打的
                        hintent.putExtra("refridgeid",hfri);//讓別人吃要打的
                        startActivity(hintent);
//                        startActivity(new Intent(getApplicationContext()
//                                ,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.shopping_list:
                        String sstr = txtloginName.getText().toString();//讓別人吃要打的
                        Intent sintent = new Intent(getApplicationContext(), shopping_list.class);
                        sintent.putExtra("memberid",sstr);//讓別人吃要打的

                        String sfri = txtrefridgeid.getText().toString();//讓別人吃要打的
                        sintent.putExtra("refridgeid",sfri);//讓別人吃要打的
                        startActivity(sintent);
//                        startActivity(new Intent(getApplicationContext()
//                                ,shopping_list.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.User:
                        String ustr = txtloginName.getText().toString();//讓別人吃要打的
                        Intent uintent = new Intent(getApplicationContext(), User_setting.class);
                        uintent.putExtra("memberid",ustr);//讓別人吃要打的

                        String ufri = txtrefridgeid.getText().toString();//讓別人吃要打的
                        uintent.putExtra("refridgeid",ufri);//讓別人吃要打的
                        startActivity(uintent);
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });//End of onNavigationItemSelected

        User = (TextView) findViewById(R.id.txtUser);
        Name = (EditText)  findViewById(R.id.txtname);
        Password = (EditText)  findViewById(R.id.txtPass);
        StrictMode.enableDefaults();
        progressBar = findViewById(R.id.progress2);


        btnupgrade = findViewById(R.id.btnupgrade);
        btnupgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String u = User.getText().toString();
                String n = Name.getText().toString();
                String p = Password.getText().toString();

                if (!n.equals("") && !p.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[3];
                            field[0] = "member_id";
                            field[1] = "member_name";
                            field[2] = "password";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = u;
                            data[1] = n;
                            data[2] = p;
                            PutData putData = new PutData("http://192.168.0.195/Graduate/Profile.php?member_id="+u+"&member_name="+n+"&password="+p, "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Login Success")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        String str = txtUser.getText().toString();//讓別人吃要打的
                                        Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
                                        intent.putExtra("Welcome",str);//讓別人吃要打的
                                        startActivity(intent);
                                        finish();
                                        Logout();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }else if (n.equals("") && !p.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "member_id";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = u;
                            data[1] = p;
                            PutData putData = new PutData("http://192.168.0.195/Graduate/Profile_pass.php?member_id="+u+"&password="+p, "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Login Success")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        String str = txtUser.getText().toString();//讓別人吃要打的
                                        Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
                                        intent.putExtra("Welcome",str);//讓別人吃要打的
                                        startActivity(intent);
                                        finish();
                                        Logout();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }else if (!n.equals("") && p.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "member_id";
                            field[1] = "member_name";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = u;
                            data[1] = n;
                            PutData putData = new PutData("http://192.168.0.195/Graduate/Profile_name.php?member_id="+u+"&member_name="+n, "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Login Success")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        String str = txtUser.getText().toString();//讓別人吃要打的
                                        Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
                                        intent.putExtra("Welcome",str);//讓別人吃要打的
                                        startActivity(intent);
                                        finish();
                                        Logout();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"資料未輸入", Toast.LENGTH_SHORT).show();
                }
            }
        });//End of update

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id2 = txtrefridgeid.getText().toString();
                String id3 = txtloginName.getText().toString();
                Intent intent3 = new Intent(getApplicationContext(),User_setting.class);
                intent3.putExtra("refridgeid",id2);
                intent3.putExtra("memberid",id3); // 我這邊把memberid傳回去了，雖然我這個功能用不到
                startActivity(intent3);
                finish();
            }
        });//End of back

    }//End of OnCreate

    //登出帳號
    public void Logout() {
        try {
            if (shp == null)
                shp = getSharedPreferences("myPreferences", MODE_PRIVATE);

            shpEditor = shp.edit();
            shpEditor.putString("name", "");
            shpEditor.commit();

            Intent i = new Intent(set_member.this, Login.class);
            startActivity(i);
            finish();

        } catch (Exception ex) {
            Toast.makeText(set_member.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }//End of LogOut
}