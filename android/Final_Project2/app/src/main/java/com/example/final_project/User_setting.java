package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class User_setting extends AppCompatActivity {

    SharedPreferences shp;
    SharedPreferences.Editor shpEditor; //會記住帳號
    Button logout, quit, insert, editFridgeName;
    ProgressBar progressBar;
    TextView  fridgeid, loginName;
    EditText  memberid, editName;
    String mid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_user);
        shp = getSharedPreferences("myPreferences", MODE_PRIVATE);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_user);

        progressBar = findViewById(R.id.progressBar);
//        CardView btn_user_published = (CardView)findViewById(R.id.btn_user_published);
        CardView btn_user_hobby = (CardView)findViewById(R.id.btn_user_hobby);
//        CardView btn_collection = (CardView)findViewById(R.id.btn_collection);
        CardView btn_set_member = (CardView)findViewById(R.id.btn_set_member);


        CardView addperson = findViewById(R.id.iconadd);
        CardView edit_fridge = findViewById(R.id.btn_edit_fridge);



        // 接收登入者的id
        String username = getIntent().getStringExtra("memberid");//接收前一頁給的東西
        TextView txtloginName = (TextView) findViewById(R.id.txt_member_id_user);//接收前一頁給的東西
        txtloginName.setText(username);//接收前一頁給的東西
        mid=txtloginName.getText().toString();

        String refridgeid = getIntent().getStringExtra("refridgeid");//接收前一頁給的東西
        TextView txtrefridgeid = (TextView) findViewById(R.id.txt_fridge_id_user);//接收前一頁給的東西
        txtrefridgeid.setText(refridgeid);//接收前一頁給的東西

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
                        finish();
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
                        finish();

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
                        finish();

//                        startActivity(new Intent(getApplicationContext()
//                                ,shopping_list.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.User:
//                        String ustr = txtloginName.getText().toString();//讓別人吃要打的
//                        Intent uintent = new Intent(getApplicationContext(), User_setting.class);
//                        uintent.putExtra("memberid",ustr);//讓別人吃要打的
//
//                        String ufri = txtrefridgeid.getText().toString();//讓別人吃要打的
//                        uintent.putExtra("refridgeid",ufri);//讓別人吃要打的
//                        startActivity(uintent);
//                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });//End of onNavigationItemSelected



//        btn_user_published.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//                // TODO Auto-generated method stub
//
//                String ustr = txtloginName.getText().toString();//讓別人吃要打的
//                String ufri = txtrefridgeid.getText().toString();//讓別人吃要打的
//                Intent intent = new Intent(User_setting.this, user_published.class);
//                intent.putExtra("memberid",ustr);//讓別人吃要打的
//                intent.putExtra("refridgeid",ufri);//讓別人吃要打的
//                startActivity(intent);
//
//
//            }
//
//        });

//        btn_collection.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//                // TODO Auto-generated method stub
//
//                String ustr = txtloginName.getText().toString();//讓別人吃要打的
//                String ufri = txtrefridgeid.getText().toString();//讓別人吃要打的
//                Intent intent = new Intent(User_setting.this, dishes_collection.class);
//                intent.putExtra("memberid",mid);
//                intent.putExtra("memberid",ustr);//讓別人吃要打的
//                intent.putExtra("refridgeid",ufri);//讓別人吃要打的
//                startActivity(intent);
//
//
//            }
//
//        });

        btn_user_hobby.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub

                String ustr = txtloginName.getText().toString();//讓別人吃要打的
                String ufri = txtrefridgeid.getText().toString();//讓別人吃要打的
                Intent intent = new Intent(User_setting.this, set_user_hobby.class);
                intent.putExtra("memberid",ustr);//讓別人吃要打的
                intent.putExtra("refridgeid",ufri);//讓別人吃要打的
                startActivity(intent);


            }

        });


        //新增會員到冰箱
        memberid = (EditText)  findViewById(R.id.edt_member_id_user);
        fridgeid = (TextView) findViewById(R.id.txt_fridge_id_user);
        addperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String u = memberid.getText().toString();
                String i = fridgeid.getText().toString();

                if (!u.equals("") &&!i.equals("") ) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "member_id";
                            field[1] = "FridgeID";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = u;
                            data[1] = i;
                            PutData putData = new PutData("http://192.168.0.195/Graduate/fridge.php?member_id="+u+"&FridgeID="+i, "POST", field, data);
                            PutData putData_note = new PutData("http://192.168.0.195/Graduate/note_user.php?member_id="+u+"&FridgeID="+i, "POST", field, data);
                            if (putData.startPut() && putData_note.startPut()) {
                                if (putData.onComplete() && putData_note.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Sign Up Success")){
                                        Toast.makeText(getApplicationContext(),"新增成功",Toast.LENGTH_SHORT).show();
                                        String str = txtloginName.getText().toString();//讓別人吃要打的
                                        Intent intent = new Intent(getApplicationContext(), User_setting.class);
                                        intent.putExtra("Welcome",str);//讓別人吃要打的
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"資料有誤",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"資料尚未輸入", Toast.LENGTH_SHORT).show();
                }

            }
        });//End of insertmember

        //編輯冰箱名稱
        editName = (EditText)  findViewById(R.id.edt_fridge_id_user);

        edit_fridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String u = fridgeid.getText().toString();
                String n = editName.getText().toString();

                if (!u.equals("") && !n.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "FridgeID";
                            field[1] = "FridgeName";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = u;
                            data[1] = n;
                            PutData putData = new PutData("http://192.168.0.195/Graduate/Edit_FridgeName.php?FridgeID="+u+"&FridgeName="+n, "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Login Success")){
                                        Toast.makeText(getApplicationContext(),"改名成功",Toast.LENGTH_SHORT).show();
                                        String str = txtloginName.getText().toString();//讓別人吃要打的
                                        Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
                                        intent.putExtra("Welcome",str);//讓別人吃要打的
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"資料尚未輸入",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"資料尚未輸入", Toast.LENGTH_SHORT).show();
                }
            }
        });//End of editfridgename

        //設定個人資料
        btn_set_member.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                String ustr = txtloginName.getText().toString();//讓別人吃要打的
                String ufri = txtrefridgeid.getText().toString();//讓別人吃要打的
                String str = txtloginName.getText().toString();//讓別人吃要打的
                Intent intent = new Intent(getApplicationContext(), set_member.class);
                intent.putExtra("memberid",ustr);//讓別人吃要打的
                intent.putExtra("refridgeid",ufri);//讓別人吃要打的
                intent.putExtra("userid",str);//讓別人吃要打的
                startActivity(intent);
                finish();
            }

        });//End of setmember

        //登出帳號
        CardView btn_logout = (CardView)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });


        loginName = (TextView) findViewById(R.id.txt_member_id_user);//退出冰箱
        CardView btn_quit_fridge = (CardView)findViewById(R.id.btn_quit_fridge);
//        quit = findViewById(R.id.btn_quit_fridge);
        btn_quit_fridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String u = loginName.getText().toString();

                if (!u.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[1];
                            field[0] = "member_id";
                            //Creating array for data
                            String[] data = new String[1];
                            data[0] = u;
                            PutData putData = new PutData("http://192.168.0.195/Graduate/delete_fridge.php?member_id="+u, "POST", field, data);
                            PutData putData_del_note = new PutData("http://192.168.0.195/Graduate/delete_note.php?member_id="+u, "POST", field, data);
                            if (putData.startPut() && putData_del_note.startPut()) {
                                if (putData.onComplete() && putData_del_note.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    String result_note = putData.getResult();
                                    if(result.equals("Sign Up Success") && result_note.equals("Sign Up Success")){
                                        Toast.makeText(getApplicationContext(),"退出成功",Toast.LENGTH_SHORT).show();
                                        String str = txtloginName.getText().toString();//讓別人吃要打的
                                        Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
                                        intent.putExtra("Welcome",str);//讓別人吃要打的
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"資料尚未輸入", Toast.LENGTH_SHORT).show();
                }

            }
        });//End of quit fridge



    }//End of OnCreat

    //登出帳號
    public void Logout() {
        try {
            if (shp == null)
                shp = getSharedPreferences("myPreferences", MODE_PRIVATE);

            shpEditor = shp.edit();
            shpEditor.putString("name", "");
            shpEditor.commit();

            Intent i = new Intent(User_setting.this, Login.class);
            startActivity(i);
            finish();

        } catch (Exception ex) {
            Toast.makeText(User_setting.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }//End of LogOut
}