package com.example.final_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public  class MainActivity extends AppCompatActivity {

    TextView txt_fridge_id_main, txt_member_id_main;
    ListView FamilyNote_lv;
    String zz;
    HashSet<Integer> hs;
    private static final String notification_url="http://192.168.0.195/mine/expiry_reminder.php"; // 這就是連結資料庫的程式
    private static final String notification_url_expired="http://192.168.0.195/mine/expiry_reminder_expired.php"; // 這就是連結資料庫的程式
    private static String expiried_ingredient[];

    //    private static final String apiurl2 ="http://192.168.0.195/mine/familynote.php"; // 這就是連結資料庫的程式
    private static final String apiurl2 ="http://192.168.0.195/mine/note_fetch.php?Fridge_ID="; // 這就是連結資料庫的程式


    String url = "http://192.168.0.195:5000/rec";//替换成自己的服务器地址

    private static String Note[];

    private static final String apiurl="http://192.168.0.195/yu/fetch_dishes_from_db_randomly.php";
    private static String title[];
    private static String dishes_type[];
    private static String food_main[];
    private static String country_cusine[];
    private static String m_or_v[];
    private static String description[];
    private static String img[];
    private static String author[];
    private static String Provenance[];
    private static String step[];
    private static String lacked_ing[];
    private static String Dishes_id[];

    public static String t,dt,fm,cc,mv,dc,pro,st,i_path,mid,LIT,did,fid,zero,expired2,aut;
    String[] DID,IMG_PAT,DISHES_TYPE,
            FOOD_MAIN,
            COUNTRY_CUSINE,
            M_OR_V,
            DESCRIPTION,
            AUTHOR,
            PROVENANCE,
            STEP   ,
            LACKED_ING,TITLE;

    @Override
    protected void onStart() {
        super.onStart();

        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetch_data_into_array2(fid,recyclerView);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FamilyNote_lv = (ListView)findViewById(R.id.FamilyNote_lv);
        txt_fridge_id_main = (TextView)findViewById(R.id.txt_fridge_id_main);
        txt_member_id_main = (TextView)findViewById(R.id.txt_member_id_main);

        Intent intent4 = getIntent();
        zz = intent4.getStringExtra("refridgeid");

        // 接收登入者的id
        String username = getIntent().getStringExtra("memberid");//接收前一頁給的東西
//        Log.v("username",username);
        TextView txtloginName = (TextView) findViewById(R.id.txt_member_id_main);//接收前一頁給的東西
        txtloginName.setText(username);//接收前一頁給的東西
        mid=(String) txtloginName.getText();

        String refridgeid = getIntent().getStringExtra("refridgeid");//接收前一頁給的東西
        TextView txtrefridgeid = (TextView) findViewById(R.id.txt_fridge_id_main);//接收前一頁給的東西
        txtrefridgeid.setText(refridgeid);//接收前一頁給的東西
        fid= (String) txtrefridgeid.getText();

        // 接收登入者的id
        String aadasdaa  = getIntent().getStringExtra("zero");//接收前一頁給的東西
        TextView txtzero = (TextView) findViewById(R.id.txtzero);//接收前一頁給的東西
        txtzero.setText(aadasdaa);//接收前一頁給的東西
        zero= (String) txtrefridgeid.getText();


        // 接收登入者的id
        String expired  = getIntent().getStringExtra("expired");//接收前一頁給的東西
        TextView txtzero2 = (TextView) findViewById(R.id.txtzero2);//接收前一頁給的東西
        txtzero2.setText(expired);//接收前一頁給的東西
        expired2= (String) txtrefridgeid.getText();

        CardView dishes_recommeder = findViewById(R.id.iconadd);
        CardView btn_shopping_recommed = findViewById(R.id.btn_edit_fridge);
        CardView btn_recipe_publish = findViewById(R.id.btn_recipe_publish);
        CardView dishes_search = findViewById(R.id.dishes_search);


        ImageSlider imageSlider=findViewById(R.id.banner);


        SendMessage(url, username);




        fetch_data_into_array(username,imageSlider);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_main);

        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
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
//                        String hstr = txtloginName.getText().toString();//讓別人吃要打的
//                        Intent hintent = new Intent(getApplicationContext(), MainActivity.class);
//                        hintent.putExtra("memberid",hstr);//讓別人吃要打的
//
//                        String hfri = txtrefridgeid.getText().toString();//讓別人吃要打的
//                        hintent.putExtra("refridgeid",hfri);//讓別人吃要打的
//                        startActivity(hintent);
////                        startActivity(new Intent(getApplicationContext()
////                                ,MainActivity.class));
//                        overridePendingTransition(0,0);
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
                        String ustr = txtloginName.getText().toString();//讓別人吃要打的
                        Intent uintent = new Intent(getApplicationContext(), User_setting.class);
                        uintent.putExtra("memberid",ustr);//讓別人吃要打的

                        String ufri = txtrefridgeid.getText().toString();//讓別人吃要打的
                        uintent.putExtra("refridgeid",ufri);//讓別人吃要打的
                        startActivity(uintent);
                        finish();

                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });

        dishes_recommeder.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                String fridge_idcon = txt_fridge_id_main.getText().toString();
                String txt_member_idcon = txt_member_id_main.getText().toString();
                Intent intent = new Intent(MainActivity.this, dishes_recommend.class);
                intent.putExtra("refridgeid",fridge_idcon);
                intent.putExtra("memberid",txt_member_idcon);
                intent.putExtra("mid", mid);
                intent.putExtra("fid", fid);
                startActivity(intent);


            }

        });

        btn_shopping_recommed.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub

                String fridge_idcon = txt_fridge_id_main.getText().toString();
                String txt_member_idcon = txt_member_id_main.getText().toString();
                Intent intent = new Intent(MainActivity.this, shopping_recommed.class);
                intent.putExtra("refridgeid",fridge_idcon);
                intent.putExtra("memberid",txt_member_idcon);
                intent.putExtra("mid", mid);
                intent.putExtra("fid", fid);
                startActivity(intent);


            }

        });

        btn_recipe_publish.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub

                String ustr = txtloginName.getText().toString();//讓別人吃要打的
                String ufri = txtrefridgeid.getText().toString();//讓別人吃要打的
                Intent intent = new Intent(MainActivity.this, dishes_collection.class);
                intent.putExtra("memberid",mid);
                intent.putExtra("memberid",ustr);//讓別人吃要打的
                intent.putExtra("refridgeid",ufri);//讓別人吃要打的
                startActivity(intent);


            }

        });
        dishes_search.setOnClickListener(v -> {

            // TODO Auto-generated method stub

            String fridge_idcon = txt_fridge_id_main.getText().toString();
            String txt_member_idcon = txt_member_id_main.getText().toString();
            Intent intent = new Intent(MainActivity.this, dishes_search_activity.class);
            intent.putExtra("refridgeid",fridge_idcon);
            intent.putExtra("memberid",txt_member_idcon);
            intent.putExtra("mid", mid);
            intent.putExtra("fid", fid);
            startActivity(intent);


        });


        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetch_data_into_array2(fid,recyclerView);

//        fetch_data_into_array2(FamilyNote_lv);

//        FamilyNote_lv.setOnItemClickListener((parent, view, position, id) -> {
//            final String s = FamilyNote_lv.getItemAtPosition(position).toString();
//            String id2 = txt_fridge_id_main.getText().toString();
//            String id3 = txt_member_id_main.getText().toString();
//            Intent intent2 = new Intent(getApplicationContext(),EditNote.class);
//            intent2.putExtra("edit",id2);
//            intent2.putExtra("thenote",s);
//            intent2.putExtra("member",id3);
//            startActivity(intent2);
//            finish();
//        });

        String expired2 = txtzero2.getText().toString();

        if (expired2.equals("0"))
        {
            fetch_data_into_array_notification_Expired();
        }else {

        }

        String zero = txtzero.getText().toString();

        if (zero.equals("0"))
        {
            fetch_data_into_array_notification();
        }else {

        }





    } // End of onCreate


    //讀取已登入的會員id再放到演算法中
    private void SendMessage(String url, final String userName) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", userName);
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(AfterLogin.this, "服务器错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (res.equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(AfterLogin.this, res, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(AfterLogin.this, "成功", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                    }
                });
            }
        });

    }


    public void fetch_data_into_array_notification()
    {
        String finalurl3=notification_url+"?FridgeID="+zz; // zz更動後在這邊整合後顯示
        Log.v("finalurl3", finalurl3);

        class  dbManager extends AsyncTask<String,Void,String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    expiried_ingredient = new String[ja.length()]; // 這裡有幾個下方就會有幾個

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        if(jo == null || jo.length() == 0){
                            final String[] EMPTY_ARRAY1 = new String[0];
                        }

                        expiried_ingredient[i] = jo.getString("ingredient_title"); // 綠色的字是php那兒抓到的內容，大小寫要注意
                        Log.v("ingredient_title", expiried_ingredient[i]);
                    }

                    String str="";
                    for (int i=0; i<expiried_ingredient.length;i++) {

                        if(i==expiried_ingredient.length-1)
                        {
                            str+=expiried_ingredient[i];
                        }
                        else
                        {
                            str+=expiried_ingredient[i]+"、";
                        }

                    }
                    System.out.println(str);

                    AlertDialog.Builder alertDialog =
                            new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("過期通知");
                    alertDialog.setMessage( str + " 快過期了歐!" );
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                } catch (Exception ex) {
                    //Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show(); // 上方迴圈例外的時候，顯示錯誤訊息
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

            } // 這個function不知道是做什麼的，暫時不去研究它

        }
        dbManager obj = new dbManager();
        //obj.execute(apiurl);
        obj.execute(finalurl3); // 整合後顯示
    }//End of 快過期


    public void fetch_data_into_array_notification_Expired()
    {
        String finalurl4=notification_url_expired+"?FridgeID="+zz; // zz更動後在這邊整合後顯示
        Log.v("finalurl", finalurl4);

        class  dbManager extends AsyncTask<String,Void,String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    expiried_ingredient = new String[ja.length()]; // 這裡有幾個下方就會有幾個

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        if(jo == null || jo.length() == 0){
                            final String[] EMPTY_ARRAY1 = new String[0];
                        }

                        expiried_ingredient[i] = jo.getString("ingredient_title"); // 綠色的字是php那兒抓到的內容，大小寫要注意
                        Log.v("ingredient_title", expiried_ingredient[i]);
                    }

                    String str="";
                    for (int i=0; i<expiried_ingredient.length;i++) {

                        if(i==expiried_ingredient.length-1)
                        {
                            str+=expiried_ingredient[i];
                        }
                        else
                        {
                            str+=expiried_ingredient[i]+"、";
                        }

                    }
                    System.out.println(str);

                    AlertDialog.Builder alertDialog =
                            new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("已過期通知");
                    alertDialog.setMessage( str + " 過期了歐!" );
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                } catch (Exception ex) {
                    //Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show(); // 上方迴圈例外的時候，顯示錯誤訊息
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

            } // 這個function不知道是做什麼的，暫時不去研究它

        }
        dbManager obj = new dbManager();
        //obj.execute(apiurl);
        obj.execute(finalurl4); // 整合後顯示
    }//End of 已過期



//    public void fetch_data_into_array2(View view)
//    {
//        String finalurl2=apiurl2+"?Fridge_ID="+zz; // zz更動後在這邊整合後顯示
//
//        class  dbManager extends AsyncTask<String,Void,String>
//        {
//            protected void onPostExecute(String data)
//            {
//                try {
//                    JSONArray ja = new JSONArray(data);
//                    JSONObject jo = null;
//
//                    Note = new String[ja.length()]; // 這裡有幾個下方就會有幾個
//
//                    for (int i = 0; i < ja.length(); i++) {
//                        jo = ja.getJSONObject(i);
//                        if(jo == null || jo.length() == 0){
//                            final String[] EMPTY_ARRAY1 = new String[0];
//                            myadapter adptr = new myadapter(getApplicationContext(), Note); // 上方有幾個下方就有幾個
//                            FamilyNote_lv.setAdapter(adptr);
////                            setListViewHeight(purchasinglist_lv);
////                            adptr.notifyDataSetChanged();
//                        }
//
//                        Note[i] = jo.getString("note"); // 綠色的字是php那兒抓到的內容，大小寫要注意
//
//                    }
//
//                    myadapter adptr = new myadapter(getApplicationContext(), Note); // 上方有幾個下方就有幾個
//                    FamilyNote_lv.setAdapter(adptr);
////                    setListViewHeight(FamilyNote_lv);
//                    adptr.notifyDataSetChanged();
//
//                } catch (Exception ex) {
//                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show(); // 上方迴圈例外的時候，顯示錯誤訊息
//                }
//            }
//
//            @Override
//            protected String doInBackground(String... strings)
//            {
//                try {
//                    URL url = new URL(strings[0]);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//                    StringBuffer data = new StringBuffer();
//                    String line;
//
//                    while ((line = br.readLine()) != null) {
//                        data.append(line + "\n");
//                    }
//                    br.close();
//
//                    return data.toString();
//
//                } catch (Exception ex) {
//                    return ex.getMessage();
//                }
//
//            } // 這個function不知道是做什麼的，暫時不去研究它
//
//        }
//        dbManager obj = new dbManager();
//        //obj.execute(apiurl);
//        obj.execute(finalurl2); // 整合後顯示
//    }


    public void fetch_data_into_array2(String fid,RecyclerView rv)
    {
        String finalurl2=apiurl2+fid+"&member_id="+mid; // zz更動後在這邊整合後顯示
        Log.i("finalurl2",finalurl2);
        Log.i("fid",fid);
        Log.i("mid",mid);


        class  dbManager extends AsyncTask<String,Void,String>
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            protected void onPostExecute(String data)
            {
                try {

                    Log.i("data",data);
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    String member_name,note,upload_date,id;
                    com.example.final_project.poolData[] poolData=new com.example.final_project.poolData[ja.length()];
//                    LocalDateTime localDateTime = LocalDateTime.now();
//                    LocalDate date2 = localDateTime.toLocalDate();

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
//                        if(jo == null || jo.length() == 0){
//                            poolData[0]=new com.example.final_project.poolData(mid,"開始使用備忘錄",String.valueOf(date2));
//                        }
//                        else {
//
//
//                        }
                        member_name = jo.getString("member_name"); // 綠色的字是php那兒抓到的內容，大小寫要注意
                        Log.i("member_name",member_name);

                        note = jo.getString("note"); // 綠色的字是php那兒抓到的內容，大小寫要注意
                        Log.i("note",note);

                        upload_date = jo.getString("upload_date"); // 綠色的字是php那兒抓到的內容，大小寫要注意
                        Log.i("upload_date",upload_date);

                        id = jo.getString("member_id"); // 綠色的字是php那兒抓到的內容，大小寫要注意
                        Log.i("member_id",id);

                        poolData[i]=new com.example.final_project.poolData(member_name,note,upload_date,id);
                        Log.i("pooltitle",poolData[i].getPooltitle());
                        Log.i("poolmembername",poolData[i].getPoolmembername());
                        Log.i("pooltime",poolData[i].getPooltime());
                        Log.i("id",poolData[i].getPooltime());


                    }
                    poolAdapter1 poolAdapter=new poolAdapter1(poolData, MainActivity.this);
                    rv.setAdapter(poolAdapter);







                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show(); // 上方迴圈例外的時候，顯示錯誤訊息
                    Log.e("fetch error",ex.getMessage());
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

            } // 這個function不知道是做什麼的，暫時不去研究它

        }
        dbManager obj = new dbManager();
        //obj.execute(apiurl);
        obj.execute(finalurl2); // 整合後顯示
    }

    public void fetch_data_into_array(String mid,ImageSlider imageSlider)
    {

        String finalurl=apiurl+"?memID="+mid;//這樣才可以在每一次登入都成功吃到xml裡面的名字
        Log.i("home_url",finalurl);


        class  dbManager extends AsyncTask<String,Void,String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    String[] split_str;
                    String temp_str;


                    title = new String[ja.length()];
                    img = new String[ja.length()];
                    Dishes_id= new String[ja.length()];
                    List<SlideModel> slideModels=new ArrayList<>();


                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        Dishes_id[i]=jo.getString("Dishes_id");
                        title[i] = jo.getString("title");
                        Log.v("title",title[i]);
                        img[i] ="http://192.168.0.195/img/" + jo.getString("img");
                        Log.v("img",img[i]);
                        slideModels.add(new SlideModel(img[i],title[i]));

                    }
                    TITLE=title;
                    DID=Dishes_id;
                    IMG_PAT = img;








//                    Random ran1 = new Random();
////                    Random ran2 = new Random();
////                    Random ran3 = new Random();
////                    Random ran4 = new Random();
////
////                    int ran_num1=ran1.nextInt(title.length);
////                    int ran_num2=ran2.nextInt(title.length);
////                    int ran_num3=ran3.nextInt(title.length);
////                    int ran_num4=ran4.nextInt(title.length);
////
////
//
//                    for(int k =0;k<4;k++)
//                    {
//                        slideModels.add(new SlideModel(img[k],title[k]));
//                    }



                    imageSlider.setImageList(slideModels,true);

                    imageSlider.setItemClickListener(i -> {
//                        Toast.makeText(MainActivity.this,  slideModels.get(i).getTitle() +"clicked", Toast.LENGTH_SHORT).show();
                        Log.v("banner", String.valueOf(i));
                        Log.v("slider ", String.valueOf(slideModels.get(i).getTitle()));

                        int index=ArrayUtils.indexOf(title, slideModels.get(i).getTitle());
                        int id= Integer.parseInt(Dishes_id[index]);
//                        Log.v("slider", String.valueOf(ArrayUtils.indexOf(title, String.valueOf(slideModels.get(i).getTitle()))));
                        Log.v("index ", String.valueOf(index));
                        Log.v("id ", String.valueOf(id));


                        dt = title[index];
                        did = Dishes_id[index];


                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[2];
                                field[0] = "member_id";
                                field[1] = "Dishes_ID";
                                //Creating array for data
                                String[] data = new String[2];
                                data[0] = mid;
                                data[1] = did;
                                Log.v("mid",mid);
                                Log.v("did",did);
                                PutData putData = new PutData("http://192.168.0.195/yu/insert_user_behavior_click.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        Log.i("message: ", result);
                                        if(result.equals("UPDAYTE sucess") || result.equals("INSERT sucess")){
//                                    Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                            Intent intent1 = new Intent(MainActivity.this, dishes_info_rec_random.class);

                                            intent1.putExtra("mid", mid);
                                            intent1.putExtra("did", did);
                                            intent1.putExtra("fid", fid);
//                                            intent1.putExtra("aut", aut);
                                            startActivity(intent1);
                                        }
                                        else {
                                            Log.i("error: ", result);
//                                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                //End Write and Read data with URL
                            }
                        });





                    });



                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Exception",ex.getMessage());
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


    public static String getfid()
    {
        return fid;
    }

    public static String getmid()
    {
        return  mid;
    }





    class myadapter extends ArrayAdapter<String> // 這個class和row.xml有直接的關係
    {
        Context context;
        String ttl[];

        myadapter(Context c, String ttl[])
        {
            super(c, R.layout.family_note_row, R.id.family_note_contnent,ttl);
            context=c;
            this.ttl=ttl;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.family_note_row,parent,false);

            TextView tv1=row.findViewById(R.id.family_note_contnent);
            tv1.setText(ttl[position]); // 把文字放到陣列裡，已嘗試過，沒有position是不能跑的

            return row;
        }
    }

    public class poolAdapter1 extends  RecyclerView.Adapter<com.example.final_project.poolAdapter.ViewHolder> {


        poolData[] poolData;
        Context context;

        public poolAdapter1(poolData[] poolData, MainActivity activity) {

            this.poolData=poolData;
            this.context=activity;
        }

        @NonNull
        @Override
        public com.example.final_project.poolAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
            View view=layoutInflater.inflate(R.layout.item_list,parent,false);
            com.example.final_project.poolAdapter.ViewHolder viewHolder=new com.example.final_project.poolAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull com.example.final_project.poolAdapter.ViewHolder holder, int position) {
            final poolData poolDataList= poolData[position];

            holder.textViewTitle.setText(poolDataList.getPooltitle());
            holder.textViewData.setText(poolDataList.getPoolmembername());
            holder.texViewTime.setText(poolDataList.getPooltime());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.i("mid",mid);
                    Log.i("getid",poolDataList.getid());

                    if(mid.equals(poolDataList.getid()))
                    {
                        String note= poolDataList.getPoolmembername();
                        String fid=MainActivity.getfid();
                        String mid=MainActivity.getmid();
//                Toast.makeText(context,poolDataList.getPooltitle(),Toast.LENGTH_LONG).show();
                        Intent intent2 = new Intent(MainActivity.this,EditNote.class);
                        intent2.putExtra("edit",fid);
                        intent2.putExtra("thenote",note);
                        intent2.putExtra("member",poolDataList.getid());
                        intent2.putExtra("mid",poolDataList.getid());
                        startActivity(intent2);
                        finish();
                    }

                }
            });


        }

        @Override
        public int getItemCount() {
            return poolData.length;
        }

        public  class ViewHolder extends RecyclerView.ViewHolder{

            TextView textViewTitle;
            TextView textViewData;
            TextView texViewTime;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTitle=itemView.findViewById(R.id.textName);
                textViewData=itemView.findViewById(R.id.textdata);
                texViewTime=itemView.findViewById(R.id.texttime);


            }
        }
    }

}