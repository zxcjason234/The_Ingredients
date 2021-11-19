package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

public class recipe_ingredient extends AppCompatActivity
{
    String apiurl ="http://192.168.0.195/yu/test.php";
    String dish_title;
    String url2;
    String lack_ing_title,fid;
    ListView lv1,lv2;
    String title,
            descr,
            img_url,
            DISHES_TYPE,
            FOOD_MAIN,
            COUNTRY_CUSINE,
            M_OR_V,
            PROVENANCE,
            STEP,
            LACK_ING_TITLE,
            mid,
            did;
    String it,qty,sau;
    private String[] Ingredient_title;
    private String[] Quantity;
    private String[] State_and_Unit;
    private String[] ingredient_ID;
    public String[] ing_row;
    public String[] lack_ing_title_arr;
    private ListAdapter listAdapter;
    MaterialButton add;
    TextView t1, t2, t3;
    int count = 0;
    String str_count;
    int count1 = 0;
    String str_count1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_ingredient);
        lv1=(ListView)findViewById(R.id.list1);
        lv2=(ListView)findViewById(R.id.list2);
        lv1.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
        TextView tbtv=(TextView)findViewById(R.id.textView2);
        ImageView btn_back = (ImageView)findViewById(R.id.imageView);


        Intent intent = getIntent();
        tbtv.setText("所需食材");



        did= intent.getStringExtra("did");
        fid= intent.getStringExtra("fid");
//        Toast.makeText(getApplicationContext(),title,Toast.LENGTH_SHORT).show();
        url2 = apiurl+"?test="+did;
//        Toast.makeText(getApplicationContext(),url2,Toast.LENGTH_SHORT).show();
        fetch_data_into_array(url2);
//        split_String_into_array(lack_ing_title);
        btn_back.setOnClickListener(view -> {
            onBackPressed();
        });

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//
//        bottomNavigationView.setSelectedItemId(R.id.home);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                switch(menuItem.getItemId()){
//                    case R.id.Fridge:
//                        Intent intent = new Intent(getApplicationContext()
//                                ,Fridge.class);
//                        intent.putExtra("refridgeid", fid);
//                        intent.putExtra("memberid", mid);
//                        overridePendingTransition(0,0);
//                        startActivity(intent);
//
//                        return  true;
//                    case R.id.home:
//                        Intent intent1 = new Intent(getApplicationContext()
//                                ,MainActivity.class);
//                        intent1.putExtra("refridgeid", fid);
//                        intent1.putExtra("memberid", mid);
//                        overridePendingTransition(0,0);
//                        startActivity(intent1);
//                        return true;
//
//                    case R.id.User:
//                        Intent intent2 = new Intent(getApplicationContext()
//                                ,User_setting.class);
//                        intent2.putExtra("refridgeid", fid);
//                        intent2.putExtra("memberid", mid);
//                        overridePendingTransition(0,0);
//                        startActivity(intent2);
//                        return  true;
//                }
//                return false;
//            }
//        });


    }

    public void  split_String_into_array(String str1)
    {
        lack_ing_title_arr = str1.split(",");
        int num = lack_ing_title_arr.length;
        for (int i=0; i<num; i++){
            Log.i("tags", lack_ing_title_arr[i]);
        }

        myadapter_lacked lacked_adapter = new myadapter_lacked(getApplicationContext(), lack_ing_title_arr);
        lv2.setAdapter(lacked_adapter);
        setListViewHeight(lv2);
        lacked_adapter.notifyDataSetChanged();

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                Intent i = new Intent(recipe_ingredient.this, dishes_info.class);
//                i.putExtra("t", dish_title);
//                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(i);
////                finish();
//                return true;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //為listview動態設定高度（有多少條目就顯示多少條目）
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

    public void fetch_data_into_array(String URL1)
    {


        class  dbManager extends AsyncTask<String, Void, String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    ingredient_ID = new String[ja.length()];
                    Ingredient_title = new String[ja.length()];
                    Quantity = new String[ja.length()];
                    State_and_Unit  = new String[ja.length()];
                    ing_row=new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);

                        ingredient_ID[i] = jo.getString("ingredient_ID");
                        Log.i("tags", ingredient_ID[i]);
                        Ingredient_title[i] = jo.getString("Ingredient_title");
                        Log.i("tags", Ingredient_title[i]);
                        Quantity[i] = jo.getString("Quantity");
                        if(Quantity[i]=="null"|| Quantity[i]==null)
                        {
                            Quantity[i] = "";
                        }
                        Log.i("tags", Quantity[i]);

                        State_and_Unit[i] = jo.getString("State_and_Unit");
                        if(State_and_Unit[i]=="null"|| State_and_Unit[i]==null)
                        {
                            State_and_Unit[i] = "";
                        }
                        Log.i("State_and_Unit", State_and_Unit[i]);
                        ing_row[i]=Ingredient_title[i]+" "+Quantity[i]+" "+State_and_Unit[i];
                        Log.i("tags", ing_row[0]);
                    }
//                    Toast.makeText(getApplicationContext(),Ingredient_title[0]+Quantity[0] +State_and_Unit[0]+ing_row[0], Toast.LENGTH_LONG).show();


                    myadapter adptr = new myadapter(getApplicationContext(), Ingredient_title, Quantity, State_and_Unit);
                    lv1.setAdapter(adptr);
                    setListViewHeight(lv1);
                    adptr.notifyDataSetChanged();


                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("msg", ex.getMessage());
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
        obj.execute(URL1);

    }






    class myadapter extends ArrayAdapter<String>
    {
        Context context;
        String ttl[];
        String dsc[];
        String rimg[];


        myadapter(Context c, String ttl[], String dsc[], String rimg[])
        {
            super(c, R.layout.row, R.id.tv_title,ttl);
            context=c;
            this.ttl=ttl;
            this.dsc=dsc;
            this.rimg=rimg;

        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.rows1,parent,false);

//            ImageView img=row.findViewById(R.id.img1);
            TextView tv1=row.findViewById(R.id.tv8);
//            TextView tv10=row.findViewById(R.id.tv10);
            TextView tv11=row.findViewById(R.id.tv11);
//            TextView tv6=row.findViewById(R.id.tv6);

            Log.i("rimg[position]",rimg[position]);
            if(rimg[position]==null)
            {
                Log.i("rimg1","rimg is null!");
            }
            else if(rimg[position]=="null")
            {
                Log.i("rimg2","rimg is null!");
            }


            tv1.setText(ttl[position]);
//            tv10.setText(dsc[position]);
            tv11.setText(dsc[position]+" "+rimg[position]);

//            Button button_add=(Button)row.findViewById(R.id.button_add1);
//            EditText lacked_amount = row.findViewById(R.id.lacked_amount);
//
//            button_add.setOnClickListener(new Button.OnClickListener(){
//
//
//                @Override
//                public void onClick(View v) {
//                    count = Integer.parseInt(lacked_amount.getText().toString());
//                    str_count=lacked_amount.getText().toString();
//
//
//                    if(count < 0){
//                        Toast.makeText(getApplicationContext()," 數量不對", Toast.LENGTH_LONG).show();
//                        Log.i(String.valueOf(getApplicationContext()), "數量不對");
//                    }
//                    else{
//                        Handler handler = new Handler(Looper.getMainLooper());
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                //Starting Write and Read data with URL
//                                //Creating array for parameters
//                                String[] field = new String[3];
//                                field[0] = "Fridge_ID";
//                                field[1] = "qty";
//                                field[2] = "ingredient_title";
//                                //Creating array for data
//                                String[] data = new String[3];
//                                data[0] = fid;
//                                data[1] = str_count;
//                                data[2] = ttl[position];
//                                PutData putData = new PutData("http://192.168.0.195/yu/add_to_fridge_purchasing_list.php", "POST", field, data);
//                                if (putData.startPut()) {
//                                    if (putData.onComplete()) {
//                                        String result = putData.getResult();
//                                        Log.i("message: ", result);
//                                        if(result.equals("INSERT sucess")){
//                                            Snackbar.make(v,"以新增 食材名稱: "+ttl[position]+" 數量: "+str_count+" 到採購清單", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                                            Log.i("message: ", result);
////                                            Toast.makeText(getApplicationContext(),"以新增 食材名稱: "+ttl[position]+" 數量: "+str_count+" 到採購清單", Toast.LENGTH_LONG).show();
//                                            Log.i("INSERT sucess: ", "以新增 食材名稱: "+ttl[position]+" 數量: "+str_count+" 到採購清單");
//                                        }
//                                        else if (result.equals("UPDAYTE sucess")){
//                                            Snackbar.make(v,"以把 食材名稱: "+ttl[position]+" 在採購清單的數量更新成: "+str_count, Snackbar.LENGTH_LONG).setAction("Action", null).show();
////                                            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
//                                            Log.i("message: ", result);
////                                            Toast.makeText(getApplicationContext(),"以把 食材名稱: "+ttl[position]+" 在採購清單的數量更新成: "+str_count, Toast.LENGTH_LONG).show();
//                                            Log.i("UPDAYTE sucess: ", "以把 食材名稱: "+ttl[position]+" 在採購清單的數量更新成: "+str_count);
//                                        }
//                                        else {
//                                            Log.i("error: ", result);
//                                            Snackbar.make(v, result, Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                                        }
//                                    }
//                                }
//                                //End Write and Read data with URL
//                            }
//                        });
//                    }
//
//
//                }
//
//            });




            return row;
        }


    }

    class myadapter_lacked extends ArrayAdapter<String>
    {
        Context context;
        String ttl[];
        String dsc[];
        String rimg[];

        myadapter_lacked(Context c, String ttl[])
        {
            super(c, R.layout.rows3, R.id.lacked_tv2,ttl);
            context=c;
            this.ttl=ttl;
        }


        @Override
        public int getCount() {
            return super.getCount();
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return super.getItem(position);
        }


        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {



            LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.rows3,parent,false);

            TextView tv1=row.findViewById(R.id.lacked_tv2);
            CardView button_add1=(CardView)row.findViewById(R.id.ig_add);
//            ElegantNumberButton lacked_amount1 = row.findViewById(R.id.enbt);
//            lacked_amount1.setRange(0,1000000000);
//
//            button_add1.setOnClickListener(v -> {
//                try {
//
////                    count1 = Integer.parseInt(lacked_amount1.getText().toString());
//                    str_count1=lacked_amount1.getNumber();
////                    Toast.makeText(getApplicationContext(),str_count1, Toast.LENGTH_LONG).show();
////                    int quantity = Integer.parseInt(String.valueOf(lacked_amount1.getText()));
//
//                    if(count1 < 0){
//                        Toast.makeText(getApplicationContext()," 數量不對", Toast.LENGTH_LONG).show();
//                        Log.i(String.valueOf(getApplicationContext()), "數量不對");
//                    }
//                    else{

//                    }
//
//
//
//
//                }
//                catch (Exception ex) {
//                    Log.i("error: ", ex.getMessage());
//                }
//
//
//            });



//            list_btn.setOnClickListener(v -> Toast.makeText(getApplicationContext(),ttl[position] , Toast.LENGTH_LONG).show());





            tv1.setText(" "+ttl[position]);


            return row;
        }




    }




}
