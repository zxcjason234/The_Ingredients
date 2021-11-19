package com.example.final_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.google.android.material.snackbar.Snackbar;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class set_user_hobby_register extends AppCompatActivity {

    String[] dislike_Ingredient_type1;
    String[] dislike_Ingredient_type2;
    String[] dislike_Ingredient_title1;
    String[] dislike_Ingredient_title2;
    List<String> dislike_Ingredient_type = new ArrayList<>();
    List<String> Ingredient_type = new ArrayList<>();
    List<String> dislike_Ingredient_title = new ArrayList<>();
    List<String> Ingredient_title = new ArrayList<>();
    final List<KeyPairBoolData> listArray1 = new ArrayList<>();
    final List<KeyPairBoolData> listArray2 = new ArrayList<>();
    String type_name,MorV;
    String url_send="http://192.168.0.195/yu/send_ingredient_type_back_to_db.php?member_id=";

    Boolean mov=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_hobby_register);
        View contentView = findViewById(android.R.id.content);


        // 接收登入者的id
        String username = getIntent().getStringExtra("memberid");//接收前一頁給的東西
        TextView txtloginName = (TextView) findViewById(R.id.txt_member_id5);//接收前一頁給的東西
        txtloginName.setText(username);//接收前一頁給的東西

        ImageView complete = (ImageView)findViewById(R.id.complete);

//        String refridgeid = getIntent().getStringExtra("refridgeid");//接收前一頁給的東西
//        TextView txtrefridgeid = (TextView) findViewById(R.id.txt_fridge_id5);//接收前一頁給的東西
//        txtrefridgeid.setText(refridgeid);//接收前一頁給的東西
        TextView tbtv=(TextView)findViewById(R.id.textView2); //toolbar上面的textview
        tbtv.setText("個人偏好");


        final List<String> list = new ArrayList<>();
        list.add(0,"葷食");
        list.add(1,"素食");


        final List<KeyPairBoolData> listArray0 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            h.setSelected(false);
            listArray0.add(h);
        }



        fetch_data_into_array1();
        fetch_data_into_array2();

        MultiSpinnerSearch searchMultiSpinnerM_or_V = (MultiSpinnerSearch) findViewById(R.id.searchMultiSpinnerM_or_V);
        searchMultiSpinnerM_or_V.setClearText("清除目前已勾選");


        MultiSpinnerSearch searchMultiSpinnerIngredients_type = (MultiSpinnerSearch) findViewById(R.id.searchMultiSpinnerUnlimitedIngredients_type);

        MultiSpinnerSearch searchMultiSpinnerIngredients_title = (MultiSpinnerSearch) findViewById(R.id.searchMultiSpinnerUnlimitedIngredients_title);

        searchMultiSpinnerM_or_V.setItems(listArray0,new MultiSpinnerListener() {



            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i("TAG", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                        MorV=items.get(i).getName();
                        mov=true;
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[2];
                                field[0] = "member_id";
                                field[1] = "MorV";
                                //Creating array for data
                                String[] data = new String[2];
                                data[0] = username;
                                data[1] = MorV;
                                PutData putData = new PutData("http://192.168.0.195/yu/insert_user_behavior_collect_MorV.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        if(result.equals("Insert Success")||result.equals("Update Success")){
                                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                            Snackbar.make(contentView, result, Snackbar.LENGTH_LONG).show();
                                            finish();
                                        }
                                        else {
                                            Snackbar.make(contentView, result, Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                //End Write and Read data with URL
                            }
                        });
                    }
                }
            }




        });
        searchMultiSpinnerM_or_V.setLimit(1, new MultiSpinnerSearch.LimitExceedListener() {
            @Override
            public void onLimitListener(KeyPairBoolData data) {
                Toast.makeText(getApplicationContext(),
                        "你只能選擇一項 ", Toast.LENGTH_LONG).show();
            }
        });

        searchMultiSpinnerIngredients_type.setItems(listArray1,new MultiSpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {


                url_send="http://192.168.0.195/yu/send_ingredient_type_back_to_db.php?member_id=";

                Handler handler1 = new Handler(Looper.getMainLooper());
                handler1.post(() -> {
                    String[] field1 = new String[1];
                    field1[0] = "member_id";
                    //Creating array for data
                    String[] data1 = new String[1];
                    data1[0] = username;
                    PutData putData1 = new PutData("http://192.168.0.195/yu/delete_user_dislike_type.php", "POST", field1, data1);
                    if (putData1.startPut()) {
                        if (putData1.onComplete()) {
                            String result = putData1.getResult();
                            Log.i("TAG",result);
                        }
                    }


                });



                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i("TAG", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                        type_name = items.get(i).getName();
                        Ingredient_type.add(type_name);
                    }
                }



                for (int j=0;j<Ingredient_type.size();j++)
                {
                    Log.i("Ingredient_type",Ingredient_type.get(j));
                    String type=Ingredient_type.get(j);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "member_id";
                            field[1] = "ingredient_type";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = type;
                            PutData putData = new PutData("http://192.168.0.195/yu/send_ingredient_type_back_to_db.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                    Log.i("result",result);
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                Ingredient_type.clear();
            }




        });


        searchMultiSpinnerIngredients_title.setItems(listArray2, new MultiSpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                String title_name;
                Handler handler1 = new Handler(Looper.getMainLooper());
                handler1.post(() -> {
                    String[] field1 = new String[1];
                    field1[0] = "member_id";
                    //Creating array for data
                    String[] data1 = new String[1];
                    data1[0] = username;
                    PutData putData1 = new PutData("http://192.168.0.195/yu/delete_user_dislike.php", "POST", field1, data1);
                    if (putData1.startPut()) {
                        if (putData1.onComplete()) {
                            String result = putData1.getResult();
                            Log.i("TAG",result);
                        }
                    }


                });

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i("TAG", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                        title_name=items.get(i).getName();
                        Ingredient_title.add(title_name);
                    }
                }

                for (int j=0;j<Ingredient_title.size();j++)
                {
                    Log.i("Ingredient_title",Ingredient_title.get(j));
                    String title=Ingredient_title.get(j);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            //傳入php之欄位名稱array
                            String[] field = new String[2];
                            field[0] = "member_id";
                            field[1] = "ingredient_title";

                            //欄位名稱對應的data array
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = title;
                            PutData putData = new PutData("http://192.168.0.195/yu/send_ingredient_title_back_to_db.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                    Log.i("result",result);
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                Ingredient_type.clear();

            }
        });


        int cmov = searchMultiSpinnerM_or_V.getAdapter().getCount();
//        int ctype = searchMultiSpinnerIngredients_type.getAdapter().getCount();
//        int ctitle = searchMultiSpinnerIngredients_title.getAdapter().getCount();
        Log.d("cmov", String.valueOf(cmov));
//        Log.d("ctype", String.valueOf(ctype));
//        Log.d("ctitle", String.valueOf(ctitle));




//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(set_user_hobby_register.this);
//        alertDialog.setTitle("沒有勾選葷食或素食");
//        alertDialog.setMessage("請勾選葷食或素食!" );
//        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        alertDialog.setCancelable(false);
//        alertDialog.show();


        complete.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {


                Log.i("mov", String.valueOf(mov));
                if (mov == true)
                {
                    Toast.makeText(getApplicationContext(), "註冊完成!", Toast.LENGTH_LONG).show();
                    mov=false;
                    Intent intent = new Intent(set_user_hobby_register.this, Login.class);
                    startActivity(intent);
                }
                else{

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(set_user_hobby_register.this);
                    alertDialog.setTitle("沒有勾選葷食或素食");
                    alertDialog.setMessage("請勾選葷食或素食!" );
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    mov=false;

                }

//                // TODO Auto-generated method stub
//                Intent intent = new Intent(set_user_hobby_register.this, Login.class);
//                startActivity(intent);

            }

        });



    }//End of oncreate




    public void fetch_data_into_array3(String finalurl)
    {


        class  dbManager extends AsyncTask<String, Void, String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    Log.v("finalurl",finalurl);

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.v("type fetch error",ex.getMessage());
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
                        Log.v("line",line);
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

    }





    public void fetch_data_into_array1()
    {
        String finalurl="http://192.168.0.195/yu/user_dislike_type_fetch.php";
        String finalurl4="http://192.168.0.195/yu/ingredient_type_FETCH.php";

        class  dbManager extends AsyncTask<String, Void, String>
        {
            protected void onPostExecute(String data)
            {
                try {
//                    JSONArray array = new JSONArray(data);
//                    JSONObject obj = new JSONObject(data);
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;


//                    dislike_Ingredient_type1 = new String[array.length()];
                    dislike_Ingredient_type1 = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        dislike_Ingredient_type1[i]=jo.getString("ingredient_type");
//                        dislike_Ingredient_type2[i]=jo.getString("ingredient_type");

                        Log.v("type1",dislike_Ingredient_type1[i]);

                        dislike_Ingredient_type.add(dislike_Ingredient_type1[i]);

                    }
//                    for(int i =0; i<dislike_Ingredient_type.size();i++)
//                    {
//                        Log.v("type list", dislike_Ingredient_type.get(i));
//                    }
                    for (int i = 0; i < dislike_Ingredient_type.size(); i++) {
                        KeyPairBoolData h = new KeyPairBoolData();
                        h.setId(i + 1);
                        h.setName(dislike_Ingredient_type.get(i));
                        h.setSelected(false);
                        listArray1.add(h);
                    }

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.v("type fetch error",ex.getMessage());
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
                        Log.v("line",line);
                    }
                    br.close();

                    return data.toString();

                } catch (Exception ex) {
                    return ex.getMessage();
                }

            }

        }
        dbManager obj=new dbManager();
        obj.execute(finalurl4);

    }

    public void fetch_data_into_array2()
    {
        String finalurl="http://192.168.0.195/yu/user_dislike_FETCH.php";
        String finalurl4="http://192.168.0.195/yu/ingredient.php";

        class  dbManager extends AsyncTask<String, Void, String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;
                    dislike_Ingredient_title1 = new String[ja.length()];
                    dislike_Ingredient_title2 = new String[ja.length()];

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        dislike_Ingredient_title1[i]=jo.getString("ingredient_title");

                        Log.v("title1",dislike_Ingredient_title1[i]);

                        dislike_Ingredient_title.add(dislike_Ingredient_title1[i]);

                    }
//                    for(int i =0; i<dislike_Ingredient_title.size();i++)
//                    {
//                        Log.v("title list", dislike_Ingredient_title.get(i));
//                    }
                    for (int i = 0; i < dislike_Ingredient_title.size(); i++) {
                        KeyPairBoolData h = new KeyPairBoolData();

                        h.setId(i + 1);
                        h.setName(dislike_Ingredient_title.get(i));
                        h.setSelected(false);
                        listArray2.add(h);
                    }

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.v("type fetch error",ex.getMessage());
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
                        Log.v("line",line);
                    }
                    br.close();

                    return data.toString();

                } catch (Exception ex) {
                    return ex.getMessage();
                }

            }

        }
        dbManager obj=new dbManager();
        obj.execute(finalurl4);

    }


}