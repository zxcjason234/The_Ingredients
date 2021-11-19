package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Fridge extends AppCompatActivity {

    TextView txt_fridge_id, txt_member_id, txtqua;
    String bb;
    ListView foodhave_lv;
    ImageButton btnDeleteFoodHave, btnWarning;

    private static final String apiurl ="http://192.168.0.195/mine/ig.php"; // 這就是連結資料庫的程式
    private static String Ingredient_title[]; // 宣告要用到的東西
    private static String expiration_date[]; // 宣告要用到的東西
    private static String Quantity[]; // 宣告要用到的東西

    @Override
    protected void onStart() {
        super.onStart();

//        foodhave_lv = (ListView)findViewById(R.id.foodhave_lv);
//        txt_fridge_id = (TextView)findViewById(R.id.txt_fridge_id);
//        Intent intent = getIntent();
//        bb = intent.getStringExtra("refridgeid");

        fetch_data_into_array(foodhave_lv);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_fridge);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_fridge);


//        Button ingredient_scan=findViewById(R.id.btn_ingredient_add);
        CardView ingredient_add = (CardView)findViewById(R.id.btn_dishes_recommeder);
//        Button btn_contain_set=findViewById(R.id.btn_contain_set);
        CardView contain_set = (CardView)findViewById(R.id.btn_contain_set);
        // 接收登入者的id
        String username = getIntent().getStringExtra("memberid");//接收前一頁給的東西
        TextView txtloginName = (TextView) findViewById(R.id.txt_member_id);//接收前一頁給的東西
        txtloginName.setText(username);//接收前一頁給的東西

        String refridgeid = getIntent().getStringExtra("refridgeid");//接收前一頁給的東西
        TextView txtrefridgeid = (TextView) findViewById(R.id.txt_fridge_id);//接收前一頁給的東西
        txtrefridgeid.setText(refridgeid);//接收前一頁給的東西


        bottomNavigationView.setSelectedItemId(R.id.Fridge);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.Fridge:
//                        String str = txtloginName.getText().toString();//讓別人吃要打的
//                        Intent intent = new Intent(getApplicationContext(), Fridge.class);
//                        intent.putExtra("memberid",str);//讓別人吃要打的
//
//                        String fri = txtrefridgeid.getText().toString();//讓別人吃要打的
//                        intent.putExtra("refridgeid",fri);//讓別人吃要打的
//                        startActivity(intent);
////                        startActivity(new Intent(getApplicationContext()
////                                ,Fridge.class));
//                        overridePendingTransition(0,0);
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

        foodhave_lv = (ListView)findViewById(R.id.foodhave_lv);
        txt_fridge_id = (TextView)findViewById(R.id.txt_fridge_id);
        txtqua = (TextView)findViewById(R.id.txtqua);
        Intent intent = getIntent();
        bb = intent.getStringExtra("refridgeid");

        fetch_data_into_array(foodhave_lv);

        ingredient_add.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub

                String fridge_idcon = txt_fridge_id.getText().toString();
                String txt_member_idcon = txt_member_id.getText().toString();
                Intent intent = new Intent(Fridge.this, test1.class);
                intent.putExtra("refridgeid",fridge_idcon);
                intent.putExtra("memberid",txt_member_idcon);
                startActivity(intent);


            }

        });


        txt_member_id = (TextView)findViewById(R.id.txt_member_id);

        contain_set.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                String fridge_idcon = txt_fridge_id.getText().toString();
                String txt_member_idcon = txt_member_id.getText().toString();
                String keep = txt_fridge_id.getText().toString();
                Intent intentKeep = new Intent(getApplicationContext(),contain_set.class);
                intentKeep.putExtra("refridgeid",fridge_idcon);
                intentKeep.putExtra("memberid",txt_member_idcon);
                intentKeep.putExtra("gotokeep",keep);
                startActivity(intentKeep);
                finish();

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
        String finalurl=apiurl+"?FridgeID="+bb; // bb更動後在這邊整合後顯示

        class  dbManager extends AsyncTask<String,Void,String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    Ingredient_title = new String[ja.length()]; // 這裡有幾個下方就會有幾個
                    expiration_date = new String[ja.length()]; // 這裡有幾個下方就會有幾個
                    Quantity = new String[ja.length()]; // 這裡有幾個下方就會有幾個

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        if(jo == null || jo.length() == 0){
                            final String[] EMPTY_ARRAY1 = new String[0];
                            final String[] EMPTY_ARRAY2 = new String[0];
                            final String[] EMPTY_ARRAY3 = new String[0];
                            myadapter adptr = new myadapter(getApplicationContext(), Ingredient_title, expiration_date, Quantity); // 上方有幾個下方就有幾個
                            foodhave_lv.setAdapter(adptr);
//                            setListViewHeight(purchasinglist_lv);
//                            adptr.notifyDataSetChanged();
                        }

                        Ingredient_title[i] = jo.getString("Ingredient_title"); // 綠色的字是php那兒抓到的內容，大小寫要注意
                        expiration_date[i] = jo.getString("expiration_date"); // 綠色的字是php那兒抓到的內容，大小寫要注意
                        Quantity[i] = jo.getString("Quantity"); // 綠色的字是php那兒抓到的內容，大小寫要注意

                    }

                    myadapter adptr = new myadapter(getApplicationContext(), Ingredient_title, expiration_date, Quantity); // 上方有幾個下方就有幾個
                    foodhave_lv.setAdapter(adptr);
                    setListViewHeight(foodhave_lv);
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
    }

    class myadapter extends ArrayAdapter<String> // 這個class和row.xml有直接的關係
    {
        Context context;
        String ttl[];
        String dsc[];
        String qua[];

        myadapter(Context c, String ttl[], String dsc[], String qua[])
        {
            super(c,R.layout.foodhave_row,R.id.tv1,ttl);
            context=c;
            this.ttl=ttl;
            this.dsc=dsc;
            this.qua=qua;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.foodhave_row,parent,false);

            TextView tv1=row.findViewById(R.id.tv1);
            TextView tv2=row.findViewById(R.id.tv2);
            TextView txtqua=row.findViewById(R.id.txtqua);
            ImageButton list_btn=row.findViewById(R.id.btnDeleteFoodHave);

            tv1.setText(ttl[position]); // 把文字放到陣列裡，已嘗試過，沒有position是不能跑的
            tv2.setText(dsc[position]); // 把文字放到陣列裡，已嘗試過，沒有position是不能跑的
            txtqua.setText(qua[position]); // 把文字放到陣列裡，已嘗試過，沒有position是不能跑的
            list_btn.setOnClickListener(v -> Toast.makeText(getApplicationContext(),ttl[position] , Toast.LENGTH_LONG).show());

            btnWarning = (ImageButton)row.findViewById(R.id.btnWarning);

            String date1_str = dsc[position];
            LocalDate date1 = LocalDate.parse(date1_str);

            LocalDateTime localDateTime = LocalDateTime.now();
            LocalDate date2 = localDateTime.toLocalDate();

            // 兩個日期相減
            long ok = ChronoUnit.DAYS.between(date2, date1);
            // 顯示相差了幾天
            System.out.println("兩個日期相差了"+ok+"天");
//          if(7>= ok && ok>=0)
            if(ok <= 7){
                btnWarning.setVisibility(View.VISIBLE);
                System.out.println(ttl[position]);
                System.out.println(dsc[position]);
                System.out.println("VISIBLE");
                System.out.println(ok);
            }else{
                btnWarning.setVisibility(View.INVISIBLE);
                System.out.println(ttl[position]);
                System.out.println(dsc[position]);
                System.out.println("INVISIBLE");
                System.out.println(ok);
            }



            btnDeleteFoodHave = (ImageButton)row.findViewById(R.id.btnDeleteFoodHave);
            btnDeleteFoodHave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String efg = txt_fridge_id.getText().toString();
                    String qua = txtqua.getText().toString();
                    if (Integer.parseInt(qua) <= 1) {
                        if(!ttl[position].equals("")&&!dsc[position].equals("")){

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String[] field = new String[3];
                                    field[0] = "ingredient_title";
                                    field[1] = "Ingredient_date";
                                    field[2] = "FridgeID";
                                    //Creating array for data
                                    String[] data = new String[3];
                                    data[0] = ttl[position];
                                    data[1] = dsc[position];
                                    data[2] = efg;

                                    PutData putData = new PutData("http://192.168.0.195/mine/ig_delete.php","POST", field, data);
                                    if(putData.startPut()){
                                        if(putData.onComplete()){
                                            String result = putData.getResult();
                                            if(result.equals("Delete sucess")){
                                                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                                fetch_data_into_array(foodhave_lv);
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
                    }else {//如果數量不是1就會跳這邊

                        if(!ttl[position].equals("")&&!dsc[position].equals("")){

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String[] field = new String[3];
                                    field[0] = "ingredient_title";
                                    field[1] = "expiration_date";
                                    field[2] = "FridgeID";
                                    //Creating array for data
                                    String[] data = new String[3];
                                    data[0] = ttl[position];
                                    data[1] = dsc[position];
                                    data[2] = efg;

                                    PutData putData = new PutData("http://192.168.0.195/mine/ig_check_quantity.php","POST", field, data);
                                    if(putData.startPut()){
                                        if(putData.onComplete()){
                                            String result = putData.getResult();
                                            if(result.equals("Delete sucess")){
                                                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                                fetch_data_into_array(foodhave_lv);
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
                    }

                }
            });

            return row;
        }
    }
}