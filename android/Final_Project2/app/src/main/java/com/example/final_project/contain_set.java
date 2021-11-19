package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class contain_set extends AppCompatActivity {


    String ee;
    ListView Keep_lv;
    Button btnAddKeep, btnDeleteKeep;
    EditText Add_Keep;
    TextView txtKeepName;

    private static final String apiurl ="http://192.168.0.195/mine/keep.php"; // 這就是連結資料庫的程式
    private static String Ingredient_title[]; // 宣告要用到的東西

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_contain_set);

        TextView tbtv=(TextView)findViewById(R.id.textView2); //toolbar上面的textview
        ImageView btn_back = (ImageView)findViewById(R.id.imageView); //圖片的按鈕
        tbtv.setText("必備食材");

        ImageView AddKeep = (ImageView)findViewById(R.id.AddKeep);

        // 接收登入者的id
        String username = getIntent().getStringExtra("memberid");//接收前一頁給的東西
        TextView txtloginName = (TextView) findViewById(R.id.txt_member_id2);//接收前一頁給的東西
        txtloginName.setText(username);//接收前一頁給的東西

        String refridgeid = getIntent().getStringExtra("refridgeid");//接收前一頁給的東西
        TextView txtrefridgeid = (TextView) findViewById(R.id.txt_fridge_id3);//接收前一頁給的東西
        txtrefridgeid.setText(refridgeid);//接收前一頁給的東西


        Keep_lv = (ListView)findViewById(R.id.Keep_lv);
//        btnAddKeep = (Button)findViewById(R.id.btnAddKeep);
        Add_Keep = (EditText)findViewById(R.id.Add_Keep);
        txtKeepName = (TextView)findViewById(R.id.txtKeepName);



        AddKeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = Add_Keep.getText().toString();
                String y = txtKeepName.getText().toString();

                if(!x.equals("")){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "ingredient_title";
                            field[1] = "FridgeID";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = x;
                            data[1] = y;

                            PutData putData = new PutData("http://192.168.0.195/mine/keep_add.php","POST", field, data);
                            if(putData.startPut()){
                                if(putData.onComplete()){
                                    String result = putData.getResult();
                                    if(result.equals("UPDATE sucess") || result.equals("INSERT sucess")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        fetch_data_into_array(Keep_lv);
                                        Add_Keep.getText().clear();
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
        ee = intent.getStringExtra("gotokeep");
        txtKeepName.setText(ee);

        fetch_data_into_array(Keep_lv); // 把結果塞到listview當中

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Fridge);
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
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id2 = txtrefridgeid.getText().toString();
                String id3 = txtloginName.getText().toString();
                Intent intent3 = new Intent(getApplicationContext(),Fridge.class);
                intent3.putExtra("refridgeid",id2);
                intent3.putExtra("memberid",id3); // 我這邊把memberid傳回去了，雖然我這個功能用不到
                startActivity(intent3);
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
        String finalurl=apiurl+"?FridgeID="+ee; // aa更動後在這邊整合後顯示

        class  dbManager extends AsyncTask<String,Void,String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    Ingredient_title = new String[ja.length()]; // 這裡有幾個下方就會有幾個

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        if(jo == null || jo.length() == 0){
                            final String[] EMPTY_ARRAY1 = new String[0];
                            myadapter adptr = new myadapter(getApplicationContext(),Ingredient_title);
                            Keep_lv.setAdapter(adptr);
//                            setListViewHeight(purchasinglist_lv);
//                            adptr.notifyDataSetChanged();
                        }

                        Ingredient_title[i] = jo.getString("ingredient_title"); // 綠色的字是php那兒抓到的內容，大小寫要注意

                    }

                    myadapter adptr = new myadapter(getApplicationContext(), Ingredient_title); // 上方有幾個下方就有幾個
                    Keep_lv.setAdapter(adptr);
                    setListViewHeight(Keep_lv);
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

    class myadapter extends ArrayAdapter<String>
    {
        Context context;
        String ttl[];

        myadapter(Context c, String ttl[])
        {
            super(c,R.layout.activity_contain_set_delete,R.id.Keep_Delete_Title,ttl);
            context=c;
            this.ttl=ttl;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.activity_contain_set_delete,parent,false);

            TextView tv1=row.findViewById(R.id.Keep_Delete_Title);
            Button list_btn=row.findViewById(R.id.btnDeleteKeep);

            tv1.setText(ttl[position]); // 把文字放到陣列裡，已嘗試過，沒有position是不能跑的
            list_btn.setOnClickListener(v -> Toast.makeText(getApplicationContext(),ttl[position] , Toast.LENGTH_LONG).show());

            btnDeleteKeep = (Button)row.findViewById(R.id.btnDeleteKeep);
            btnDeleteKeep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String tyu = txtKeepName.getText().toString();

                    if(!ttl[position].equals("")){

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] field = new String[2];
                                field[0] = "ingredient_title";
                                field[1] = "FridgeID";
                                //Creating array for data
                                String[] data = new String[2];
                                data[0] = ttl[position];
                                data[1] = tyu;

                                PutData putData = new PutData("http://192.168.0.195/mine/keep_delete.php","POST", field, data);
                                if(putData.startPut()){
                                    if(putData.onComplete()){
                                        String result = putData.getResult();
                                        if(result.equals("Delete sucess")){
                                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                            fetch_data_into_array(Keep_lv);
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
            });
            return row;
        }
    }
}