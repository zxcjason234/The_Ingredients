package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AfterLogin extends AppCompatActivity {

    private static final String apiurl="http://192.168.0.195/Graduate/json_user_fetch.php";
    ListView lv;
    String url = "http://192.168.0.195:5000/rec";//替换成自己的服务器地址
    private static String FridgeID[];
    private static String FridgeName[];


    String dt;
    String[] fridgeName;

    String fid;
    Button btnprofile, btnfridge, logout;
    ProgressBar progressBar;
    TextView loginName;

    SharedPreferences shp;
    SharedPreferences.Editor shpEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        TextView txtloginName = (TextView)findViewById(R.id.txtloginName);//接收前一頁給的東西
        lv=(ListView)findViewById(R.id.lv);
        shp = getSharedPreferences("myPreferences", MODE_PRIVATE);


        String username = shp.getString("name", "");
        txtloginName.setText(username);//接收前一頁給的東西



        btnprofile = findViewById(R.id.btnprofile);

        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = txtloginName.getText().toString();//讓別人吃要打的
                Intent intent = new Intent(getApplicationContext(), set_member.class);
                intent.putExtra("userid",str);//讓別人吃要打的
                startActivity(intent);
                finish();
            }
        });
        loginName = (TextView) findViewById(R.id.txtloginName);
        progressBar = findViewById(R.id.progress);
        btnfridge = findViewById(R.id.btnfridge);


        btnfridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String u = loginName.getText().toString();

                if (!username.equals("")) {
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
                            PutData putData = new PutData("http://192.168.0.195/Graduate/afterlogin_fridge.php?member_id="+u, "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Login Success")){
                                        Intent intent = new Intent(AfterLogin.this, MainActivity.class);
                                        intent.putExtra("bbb",u);//讓別人吃要打的
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        String str = txtloginName.getText().toString();//讓別人吃要打的
                                        Intent intent = new Intent(getApplicationContext(), insertfridge.class);
                                        intent.putExtra("ccc",str);//讓別人吃要打的
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"All fields required2", Toast.LENGTH_SHORT).show();
                }

            }
        });//end of btnfridge

        fetch_data_into_array(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String s = lv.getItemAtPosition(position).toString();
                dt = fridgeName[position];
                String b = loginName.getText().toString();
                //表示目前在MainAtivty透過Intent開啟Main2Activity
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[1];
                        field[0] = "FridgeID";

                        String[] data = new String[1];
                        data[0]= s;
                        PutData putData = new PutData("http://192.168.0.195/Graduate/selected_dish_fetch.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Sign Up Success")){
                                    Intent intent = new Intent(AfterLogin.this, MainActivity.class);
                                    intent.putExtra("refridgeid", s);
                                    intent.putExtra("dt", dt);
                                    intent.putExtra("memberid", b);
                                    intent.putExtra("zero","0");
                                    intent.putExtra("expired","0");
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"All fields required2", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            }
        });//end of lv

        //登出帳號
        logout = findViewById(R.id.btn_logout2);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

    }//end of onCreate


    public void fetch_data_into_array(View view)
    {
        String b = loginName.getText().toString();//改成直接把txtloginName 這格先丟進去loginName(上面有做過)所以這邊直接把他灌進去b在餵b
        String finalurl=apiurl+"?member_id="+b;//這樣才可以在每一次登入都成功吃到xml裡面的名字

        class  dbManager extends AsyncTask<String,Void,String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    FridgeID = new String[ja.length()];
                    FridgeName = new String[ja.length()];

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        FridgeID[i] = jo.getString("FridgeID");
                        fid = FridgeID[i];
                        FridgeName[i] = jo.getString("FridgeName");

                    }
                    fridgeName=FridgeName;


                    myadapter adptr = new myadapter(getApplicationContext(), FridgeID, FridgeName);
                    lv.setAdapter(adptr);

                } catch (Exception ex) {
                    btnfridge.setVisibility(View.VISIBLE); //顯示
                }
            }

            @Override
            protected String doInBackground(String... strings)
            {
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer data = new StringBuffer();
                    String line;

                    while ((line = br.readLine()) != null) {
                        data.append(line + "\n");
                    }
                    br.close();

                    return data.toString();

                } catch (Exception ex) {
                    return ex.getMessage();
                }

            }

        }
        dbManager obj=new dbManager();
        obj.execute(finalurl);

    }//End of fetch_data_into_array

    class myadapter extends ArrayAdapter<String>
    {
        Context context;
        String ttl[];
        String dsc[];

        myadapter(Context c, String ttl[], String dsc[])
        {
            super(c,R.layout.rowfridge,R.id.tv1,ttl);
            context=c;
            this.ttl=ttl;
            this.dsc=dsc;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.rowfridge,parent,false);
            TextView tv1=row.findViewById(R.id.tv1);
            TextView tv2=row.findViewById(R.id.tv2);
            tv1.setText(ttl[position]);
            tv2.setText(dsc[position]);

            return row;
        }
    }//End of myadapter






    public void Logout() {
        try {
            if (shp == null)
                shp = getSharedPreferences("myPreferences", MODE_PRIVATE);

            shpEditor = shp.edit();
            shpEditor.putString("name", "");
            shpEditor.commit();

            Intent i = new Intent(AfterLogin.this, Login.class);
            startActivity(i);
            finish();

        } catch (Exception ex) {
            Toast.makeText(AfterLogin.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }//End of LogOut
}