package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class insertfridge extends AppCompatActivity {

    EditText  fridgeid, fridgename;
    ProgressBar progressBar;
    Button btnbacksign, buttoninsert;
    TextView Userid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertfridge);

        String username = getIntent().getStringExtra("ccc");
        TextView txtUserid = (TextView)findViewById(R.id.txtUserid);
        txtUserid.setText(username);


        Userid = (TextView) findViewById(R.id.txtUserid);
        fridgeid = (EditText)  findViewById(R.id.txtfridgeid);
        fridgename = (EditText)  findViewById(R.id.txtfridgename);
        StrictMode.enableDefaults();

        TextView tbtv=(TextView)findViewById(R.id.textView2); //toolbar上面的textview
        ImageView btn_back = (ImageView)findViewById(R.id.imageView); //圖片的按鈕
        tbtv.setText("新增冰箱");

//        btnbacksign = findViewById(R.id.btnbacksign);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = txtUserid.getText().toString();//讓別人吃要打的
                Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
                intent.putExtra("bbb",str);//讓別人吃要打的
                startActivity(intent);
                finish();
            }
        });



        progressBar = findViewById(R.id.progress);

        buttoninsert = findViewById(R.id.buttoninsert);
        buttoninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String u = Userid.getText().toString();
                String i = fridgeid.getText().toString();
                String n = fridgename.getText().toString();


                if (!u.equals("") &&!i.equals("") && !n.equals("") ) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "FridgeID";
                            field[1] = "FridgeName";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = i;
                            data[1] = n;
                            PutData putData = new PutData("http://192.168.0.195/Graduate/onlyfridge.php?FridgeID="+i+"&FridgeName="+n, "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Sign Up Success")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        if (!u.equals("") &&!i.equals("") && !n.equals("") ) {
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
                                                            String result_note = putData_note.getResult();
                                                            if(result.equals("Sign Up Success") && result_note.equals("Sign Up Success")){
                                                                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                                                String str = txtUserid.getText().toString();//讓別人吃要打的
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
                                            Toast.makeText(getApplicationContext(),"All fields required", Toast.LENGTH_SHORT).show();
                                        }
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
                    Toast.makeText(getApplicationContext(),"All fields required", Toast.LENGTH_SHORT).show();
                }//新增到onlyfridge

//                if (!u.equals("") &&!i.equals("") && !n.equals("") ) {
//                    progressBar.setVisibility(View.VISIBLE);
//                    Handler handler = new Handler(Looper.getMainLooper());
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            //Starting Write and Read data with URL
//                            //Creating array for parameters
//                            String[] field = new String[2];
//                            field[0] = "member_id";
//                            field[1] = "FridgeID";
//                            //Creating array for data
//                            String[] data = new String[2];
//                            data[0] = u;
//                            data[1] = i;
//                            PutData putData = new PutData("http://192.168.0.195/Graduate/fridge.php?member_id="+u+"&FridgeID="+i, "POST", field, data);
//                            if (putData.startPut()) {
//                                if (putData.onComplete()) {
//                                    progressBar.setVisibility(View.GONE);
//                                    String result = putData.getResult();
//                                    if(result.equals("Sign Up Success")){
//                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
//                                        String str = txtUserid.getText().toString();//讓別人吃要打的
//                                        Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
//                                        intent.putExtra("Welcome",str);//讓別人吃要打的
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                    else {
//                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                            //End Write and Read data with URL
//                        }
//                    });
//                }
//                else{
//                    Toast.makeText(getApplicationContext(),"All fields required", Toast.LENGTH_SHORT).show();
//                }

            }//End of onClick
        });

    }
}