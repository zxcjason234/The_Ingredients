 package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.SingleSpinnerListener;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class shopping_list extends AppCompatActivity {

    String aa;
    ListView purchasinglist_lv;
    Button btnAddIngredients ;
    EditText  Add_Ingredient_Quantity;//Add_Ingredient,
    TextView txt_fridge_id_shop;
    ImageButton btnDeleteIngredients,btnADDIngredients;
    //下拉式選單
    SingleSpinnerSearch spn_shopping;
    final List<KeyPairBoolData> listArray2 = new ArrayList<>();//下拉式選單
    List<String> Ingredient_Name = new ArrayList<>();//下拉式選單
    String type;//下拉式選單
    ArrayList<String> IngNameList = new ArrayList<>();
    ArrayAdapter<String> IngIDAdapter;
    RequestQueue requestQueue_ingredient;
    //下拉式選單
    private static final String apiurl ="http://192.168.0.195/mine/purchasing.php"; // 這就是連結資料庫的程式
    private static final String apiurl2 ="http://192.168.0.195/mine/check_keep.php"; // 這就是連結資料庫的程式

    private static String Ingredient_title[]; // 宣告要用到的東西
    private static String Quantity[]; // 宣告要用到的東西

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_shopping_list);

        purchasinglist_lv = (ListView)findViewById(R.id.purchasinglist_lv);
        Add_Ingredient_Quantity = (EditText)findViewById(R.id.Add_Ingredient_Quantity);
        txt_fridge_id_shop = (TextView)findViewById(R.id.txt_fridge_id_shop);

        CardView btnAddIngredients = (CardView)findViewById(R.id.iconadd);


//------//下拉式選單


        spn_shopping = findViewById(R.id.spn_shopping);
        spn_shopping.setColorseparation(true);
        spn_shopping.setSearchEnabled(true);
        //spn_shopping.setSearchHint("選擇食材");

        requestQueue_ingredient = Volley.newRequestQueue(this);

        String url = "http://192.168.0.195/Graduate/spn_shopping_ing.php";
        JsonObjectRequest jsonObjectRequest_Type = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("ingredient_title");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String str_ingID = jsonObject.optString("ingredient_title");
                        IngNameList.add(str_ingID);
                        Ingredient_Name.add(str_ingID);


                    }
                    for (int i = 0; i < Ingredient_Name.size(); i++) {
                        KeyPairBoolData h = new KeyPairBoolData();

                        h.setId(i + 1);
                        h.setName(Ingredient_Name.get(i));
                        h.setSelected(false);
                        listArray2.add(h);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue_ingredient.add(jsonObjectRequest_Type);
        spn_shopping.setItems(listArray2, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                type=selectedItem.getName();
            }

            @Override
            public void onClear() {
//                Toast.makeText(shopping_list.this, "Cleared Selected Item", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = spn_shopping.getSelectedItem().toString();
                String y = Add_Ingredient_Quantity.getText().toString();
                String z = txt_fridge_id_shop.getText().toString();

                if(!x.equals("")&&!y.equals("")){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[3];
                            field[0] = "ingredient_title";
                            field[1] = "Quantity";
                            field[2] = "FridgeID";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = x;
                            data[1] = y;
                            data[2] = z;

                            PutData putData = new PutData("http://192.168.0.195/mine/purchasing_add.php","POST", field, data);
                            if(putData.startPut()){
                                if(putData.onComplete()){
                                    String result = putData.getResult();
                                    if(result.equals("UPDATE sucess") || result.equals("INSERT sucess")){
                                        Toast.makeText(getApplicationContext(),"新增成功",Toast.LENGTH_SHORT).show();
                                        fetch_data_into_array(purchasinglist_lv);
                                        Add_Ingredient_Quantity.setText("");
                                        spn_shopping.setAdapter(null);
                                    }else {
                                        Toast.makeText(getApplicationContext(),"資料不完整",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"資料不完整", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = getIntent();
        aa = intent.getStringExtra("refridgeid");

        fetch_data_into_array2();
        fetch_data_into_array(purchasinglist_lv);

        // 接收登入者的id
        String username = getIntent().getStringExtra("memberid");//接收前一頁給的東西
        TextView txtloginName = (TextView) findViewById(R.id.txt_member_id_shop);//接收前一頁給的東西
        txtloginName.setText(username);//接收前一頁給的東西

        String refridgeid = getIntent().getStringExtra("refridgeid");//接收前一頁給的東西
        TextView txtrefridgeid = (TextView) findViewById(R.id.txt_fridge_id_shop);//接收前一頁給的東西
        txtrefridgeid.setText(refridgeid);//接收前一頁給的東西


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_shoping_list);

        bottomNavigationView.setSelectedItemId(R.id.shopping_list);
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
                        fetch_data_into_array(purchasinglist_lv);
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




    }//End of onstart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_shopping_list);

        purchasinglist_lv = (ListView)findViewById(R.id.purchasinglist_lv);
        Add_Ingredient_Quantity = (EditText)findViewById(R.id.Add_Ingredient_Quantity);
        txt_fridge_id_shop = (TextView)findViewById(R.id.txt_fridge_id_shop);

        CardView btnAddIngredients = (CardView)findViewById(R.id.iconadd);


//------//下拉式選單


        spn_shopping = findViewById(R.id.spn_shopping);
        spn_shopping.setColorseparation(true);
        spn_shopping.setSearchEnabled(true);
        spn_shopping.setSearchHint("選擇食材種類");

        requestQueue_ingredient = Volley.newRequestQueue(this);

        String url = "http://192.168.0.195/Graduate/spn_shopping_ing.php";
        JsonObjectRequest jsonObjectRequest_Type = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("ingredient_title");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String str_ingID = jsonObject.optString("ingredient_title");
                        IngNameList.add(str_ingID);
                        Ingredient_Name.add(str_ingID);


                    }
                    for (int i = 0; i < Ingredient_Name.size(); i++) {
                        KeyPairBoolData h = new KeyPairBoolData();

                        h.setId(i + 1);
                        h.setName(Ingredient_Name.get(i));
                        h.setSelected(false);
                        listArray2.add(h);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue_ingredient.add(jsonObjectRequest_Type);
        spn_shopping.setItems(listArray2, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                type=selectedItem.getName();
            }

            @Override
            public void onClear() {
                //Toast.makeText(shopping_list.this, "Cleared Selected Item", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = spn_shopping.getSelectedItem().toString();
                String y = Add_Ingredient_Quantity.getText().toString();
                String z = txt_fridge_id_shop.getText().toString();

                if(!x.equals("")&&!y.equals("")){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[3];
                            field[0] = "ingredient_title";
                            field[1] = "Quantity";
                            field[2] = "FridgeID";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = x;
                            data[1] = y;
                            data[2] = z;

                            PutData putData = new PutData("http://192.168.0.195/mine/purchasing_add.php","POST", field, data);
                            if(putData.startPut()){
                                if(putData.onComplete()){
                                    String result = putData.getResult();
                                    if(result.equals("UPDATE sucess") || result.equals("INSERT sucess")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        fetch_data_into_array(purchasinglist_lv);
                                        Add_Ingredient_Quantity.setText("");

                                    }else {
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"Data incomplete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = getIntent();
        aa = intent.getStringExtra("refridgeid");

        fetch_data_into_array2();

        fetch_data_into_array(purchasinglist_lv);

        // 接收登入者的id
        String username = getIntent().getStringExtra("memberid");//接收前一頁給的東西
        TextView txtloginName = (TextView) findViewById(R.id.txt_member_id_shop);//接收前一頁給的東西
        txtloginName.setText(username);//接收前一頁給的東西

        String refridgeid = getIntent().getStringExtra("refridgeid");//接收前一頁給的東西
        TextView txtrefridgeid = (TextView) findViewById(R.id.txt_fridge_id_shop);//接收前一頁給的東西
        txtrefridgeid.setText(refridgeid);//接收前一頁給的東西


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_shoping_list);

        bottomNavigationView.setSelectedItemId(R.id.shopping_list);
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
//                       startActivity(new Intent(getApplicationContext()
//                               ,Fridge.class));
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
//                        String sstr = txtloginName.getText().toString();//讓別人吃要打的
//                        Intent sintent = new Intent(getApplicationContext(), shopping_list.class);
//                        sintent.putExtra("memberid",sstr);//讓別人吃要打的
//
//                        String sfri = txtrefridgeid.getText().toString();//讓別人吃要打的
//                        sintent.putExtra("refridgeid",sfri);//讓別人吃要打的
//                        startActivity(sintent);
//                        fetch_data_into_array(purchasinglist_lv);
////                        startActivity(new Intent(getApplicationContext()
////                                ,shopping_list.class));
//                        overridePendingTransition(0,0);
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
        });
    } // End of onCreate

    //讓listview的高度，可以和項目內容剛好一樣高
    public void setListViewHeight(ListView listView) {
        //獲取listView的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //listAdapter.getCount()返回資料項的數目
        for (int i = 0,len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // listView.getDividerHeight()獲取子項間分隔符佔用的高度
        // params.height最後得到整個ListView完整顯示需要的高度
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter .getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void fetch_data_into_array(View view)
    {
        String finalurl=apiurl+"?FridgeID="+aa; // aa更動後在這邊整合後顯示

        class  dbManager extends AsyncTask<String,Void,String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    Ingredient_title = new String[ja.length()]; // 這裡有幾個下方就會有幾個
                    Quantity = new String[ja.length()]; // 這裡有幾個下方就會有幾個

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        if(jo == null || jo.length() == 0){
                            final String[] EMPTY_ARRAY1 = new String[0];
                            final String[] EMPTY_ARRAY2 = new String[0];
                            myadapter adptr = new myadapter(getApplicationContext(), Ingredient_title, Quantity); // 上方有幾個下方就有幾個
                            purchasinglist_lv.setAdapter(adptr);
//                            setListViewHeight(purchasinglist_lv);
//                            adptr.notifyDataSetChanged();
                        }

                        Ingredient_title[i] = jo.getString("Ingredient_title"); // 綠色的字是php那兒抓到的內容，大小寫要注意
                        Quantity[i] = jo.getString("Quantity"); // 綠色的字是php那兒抓到的內容，大小寫要注意

                    }

                    myadapter adptr = new myadapter(getApplicationContext(), Ingredient_title, Quantity); // 上方有幾個下方就有幾個
                    purchasinglist_lv.setAdapter(adptr);
                    setListViewHeight(purchasinglist_lv);
                    adptr.notifyDataSetChanged();

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show(); // 上方迴圈例外的時候，顯示錯誤訊息
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
        obj.execute(finalurl); // 整合後顯示

    } // End of fetch_data_into_array


    public void fetch_data_into_array2()
    {
        String finalurl2=apiurl2+"?FridgeID="+aa; // aa更動後在這邊整合後顯示

        class  dbManager extends AsyncTask<String,Void,String>
        {
            protected void onPostExecute(String data)
            {
                try {

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show(); // 上方迴圈例外的時候，顯示錯誤訊息
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

    } // End of fetch_data_into_array


    class myadapter extends ArrayAdapter<String>
    {
        Context context;
        String ttl[];
        String dsc[];

        myadapter(Context c, String ttl[], String dsc[])
        {
            super(c,R.layout.activity_shopping_list_delete,R.id.purchasinglist_title,ttl);
            context=c;
            this.ttl=ttl;
            this.dsc=dsc;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.activity_shopping_list_delete,parent,false);

            TextView tv1=row.findViewById(R.id.purchasinglist_title);
            TextView tv2=row.findViewById(R.id.Quantity);
            ImageButton list_btn=row.findViewById(R.id.btnDeleteIngredients);
            ImageButton list_btnadd=row.findViewById(R.id.btnADDIngredients);


            tv1.setText(ttl[position]); // 把文字放到陣列裡，已嘗試過，沒有position是不能跑的
            tv2.setText(dsc[position]); // 把文字放到陣列裡，已嘗試過，沒有position是不能跑的
            list_btn.setOnClickListener(v -> Toast.makeText(getApplicationContext(),ttl[position] , Toast.LENGTH_LONG).show());

            btnDeleteIngredients = (ImageButton)row.findViewById(R.id.btnDeleteIngredients);
            btnDeleteIngredients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String abc = txt_fridge_id_shop.getText().toString();

                    if(dsc[position].equals("1") ){
                        if(!ttl[position].equals("")&&!dsc[position].equals("")){

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String[] field = new String[3];
                                    field[0] = "ingredient_title";
                                    field[1] = "Quantity";
                                    field[2] = "FridgeID";
                                    //Creating array for data
                                    String[] data = new String[3];
                                    data[0] = ttl[position];
                                    data[1] = dsc[position];
                                    data[2] = abc;

                                    PutData putData = new PutData("http://192.168.0.195/mine/purchasing_delete.php","POST", field, data);
                                    if(putData.startPut()){
                                        if(putData.onComplete()){
                                            String result = putData.getResult();
                                            if(result.equals("Delete sucess")){
                                                Toast.makeText(getApplicationContext(),"刪除成功",Toast.LENGTH_SHORT).show();
                                                fetch_data_into_array(purchasinglist_lv);
                                            }else {
                                                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(),"You didn't click the button", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        if(!ttl[position].equals("")&&!dsc[position].equals("")){

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String[] field = new String[3];
                                    field[0] = "ingredient_title";
                                    field[1] = "Quantity";
                                    field[2] = "FridgeID";
                                    //Creating array for data
                                    String[] data = new String[3];
                                    data[0] = ttl[position];
                                    data[1] = dsc[position];
                                    data[2] = abc;

                                    PutData putData = new PutData("http://192.168.0.195/mine/purchasing_delete_check.php","POST", field, data);
                                    if(putData.startPut()){
                                        if(putData.onComplete()){
                                            String result = putData.getResult();
                                            if(result.equals("Delete sucess")){
                                                Toast.makeText(getApplicationContext(),"更新成功",Toast.LENGTH_SHORT).show();
                                                fetch_data_into_array(purchasinglist_lv);
                                            }else {
                                                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(),"more than one", Toast.LENGTH_SHORT).show();
                        }

                    }//End of 減一減一

                }
            });//End of delete

            list_btnadd.setOnClickListener(v -> Toast.makeText(getApplicationContext(),ttl[position] , Toast.LENGTH_LONG).show());

            btnADDIngredients = (ImageButton)row.findViewById(R.id.btnADDIngredients);
            btnADDIngredients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String abc = txt_fridge_id_shop.getText().toString();

                    if(!ttl[position].equals("")&&!dsc[position].equals("")){

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] field = new String[3];
                                field[0] = "ingredient_title";
                                field[1] = "Quantity";
                                field[2] = "FridgeID";
                                //Creating array for data
                                String[] data = new String[3];
                                data[0] = ttl[position];
                                data[1] = dsc[position];
                                data[2] = abc;
                                PutData putData = new PutData("http://192.168.0.195/mine/shoppinglist_add_button.php","POST", field, data);
                                if(putData.startPut()){
                                    if(putData.onComplete()){
                                        String result = putData.getResult();
                                        if(result.equals("UPDATE success")){
                                            Toast.makeText(getApplicationContext(),"更新成功",Toast.LENGTH_SHORT).show();
                                            fetch_data_into_array(purchasinglist_lv);
                                        }else {
                                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(),"You didn't click the button", Toast.LENGTH_SHORT).show();
                    }
                }
            });//End of Add

            return row;
        }//End of getview
    }//End of myadapter
}