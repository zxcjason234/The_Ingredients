package com.example.final_project;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.android.material.snackbar.Snackbar;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class recipe_ingredient_v2 extends AppCompatActivity{


    String apiurl ="http://192.168.0.195/yu/ingredient_fetch.php";
    ListView lv1,lv2;
    String  did,fid,mid;
    String testurl ="http://192.168.0.195/yu/test.php";


    private String[] Ingredient_ID;
    private String[] ingredient_title;
    private String[] Subtract_Quantity;
    private String[] State_and_Unit;
    private String[] Ingredient_title;
    private String[] Quantity;
    private String[] ingredient_ID;
    private String[] company;
    private String[] website;
    private String[] product;
    private String[] price;
    private String[] Unit;
    private String[] combined_string;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_ingredient);
        lv1=(ListView)findViewById(R.id.list1);
        lv2=(ListView)findViewById(R.id.list2);
        Intent intent = getIntent();
        mid = intent.getStringExtra("mid");
        did= intent.getStringExtra("did");
        fid= intent.getStringExtra("fid");
        testurl = testurl+"?test="+did;

        TextView tbtv= findViewById(R.id.textView2);
        tbtv.setText("所需食材");

        ImageView btn_back = findViewById(R.id.imageView);
        btn_back.setOnClickListener(view -> {
            onBackPressed();
        });

        fetch_ingredient_into_array(testurl);
        fetch_lacked_ingredient_into_array(did,fid);

    }


    public void fetch_ingredient_into_array(String URL1)
    {
        Log.i("testurl",testurl);

        class  dbManager extends AsyncTask<String, Void, String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;



                    List<String> result_title = new ArrayList<>();



                    ingredient_ID = new String[ja.length()];
                    Ingredient_title = new String[ja.length()];
                    Quantity = new String[ja.length()];
                    State_and_Unit  = new String[ja.length()];
//                    ing_row=new String[ja.length()];
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
                        Log.i("tags", State_and_Unit[i]);
//                        ing_row[i]=Ingredient_title[i]+" "+Quantity[i]+" "+State_and_Unit[i];
//                        Log.i("tags", ing_row[0]);
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


    public void fetch_lacked_ingredient_into_array(String did, String fid)
    {
        String ip="http://192.168.0.195/yu/ingredient_fetch.php?Dishes_id="+did+"&FridgeID="+fid;
        Log.i("ip",ip);

        class  dbManager extends AsyncTask<String, Void, String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    Log.i("data", data);

                    Ingredient_ID = new String[ja.length()];
                    ingredient_title = new String[ja.length()];
                    Subtract_Quantity = new String[ja.length()];
                    State_and_Unit = new String[ja.length()];
                    company = new String[ja.length()];
                    website = new String[ja.length()];
                    product = new String[ja.length()];
                    price = new String[ja.length()];
                    Unit = new String[ja.length()];
                    combined_string  = new String[ja.length()];

                    List<String> result_title_Ingredient_ID = new ArrayList<>();
                    List<String> result_title_ingredient_title = new ArrayList<>();
                    List<String> result_title_Subtract_Quantity = new ArrayList<>();
                    List<String> result_title_State_and_Unit = new ArrayList<>();
                    List<String> result_price = new ArrayList<>();
                    List<String> result_website = new ArrayList<>();
                    List<String> result_title_Unit = new ArrayList<>();
                    List<String> result_combined_string = new ArrayList<>();


                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);


                        Ingredient_ID[i] = jo.getString("Ingredient_ID");
                        Log.i("Ingredient_ID", Ingredient_ID[i]);

                        ingredient_title[i] = jo.getString("ingredient_title");
                        Log.i("ingredient_title", ingredient_title[i]);


                        Subtract_Quantity[i] = jo.getString("Subtract_Quantity");
                        Log.i("Subtract_Quantity", Subtract_Quantity[i]);

                        State_and_Unit[i] = jo.getString("State_and_Unit");
                        if(State_and_Unit[i]=="null"|| State_and_Unit[i]==null)
                        {
                            State_and_Unit[i] = "";
                        }



                        company[i] = jo.getString("company");
                        website[i] = jo.getString("website");
                        if(company[i]=="null"|| company[i]==null || website[i]=="null"|| website[i]==null)
                        {
                            company[i] = "";
                            website[i] = "";
                            combined_string[i] = "";
                        }
                        else {
                            combined_string[i]="<a href=\""+website[i]+"\">"+company[i] +"</a>";
                        }
                        Log.i("company", company[i]);
                        Log.i("website", website[i]);
                        Log.i("combined_string", combined_string[i]);

                        price[i] = jo.getString("price");
                        if(price[i]=="null"|| price[i]==null)
                        {
                            price[i] = "";
                        }
                        Log.i("price", price[i]);

                        Unit[i] = jo.getString("Unit");
                        if(Unit[i]=="null"|| Unit[i]==null)
                        {
                            Unit[i] = "";
                        }
                        Log.i("Unit", Unit[i]);
                    }

                    for (int i = 0; i < Subtract_Quantity.length; i++) {
                        if(Integer.parseInt(Subtract_Quantity[i])>0)
                        {
                            result_title_Ingredient_ID.add(Ingredient_ID[i]);
                            result_title_ingredient_title.add(ingredient_title[i]);
                            result_title_Subtract_Quantity.add(Subtract_Quantity[i]);
                            result_title_State_and_Unit.add(State_and_Unit[i]);
                            result_combined_string.add(combined_string[i]);
                            result_price.add(price[i]);
                            result_title_Unit.add(Unit[i]);
                            result_website.add(website[i]);

                        }
                    }


                    String[]title = (String[]) result_title_ingredient_title.toArray(new String[result_title_ingredient_title.size()]);
//
//                    Log.i("Ingredient_ID", Arrays);
//                    Log.i("ingredient_title", ingredient_title[j]);
//                    Log.i("Subtract_Quantity", Subtract_Quantity[j]);
//                    Log.i("State_and_Unit", State_and_Unit[j]);

                    myadapter_lacked lacked_adapter =
                            new myadapter_lacked(getApplicationContext(),
                                    result_title_ingredient_title.toArray(new String[result_title_ingredient_title.size()]),
                                    result_title_Subtract_Quantity.toArray(new String[result_title_Subtract_Quantity.size()]),
                                    result_title_State_and_Unit.toArray(new String[result_title_State_and_Unit.size()]),
                                    result_combined_string.toArray(new String[result_combined_string.size()]),
                                    result_price.toArray(new String[result_price.size()]),
                                    result_title_Unit.toArray(new String[result_title_Unit.size()]),
                                    result_website.toArray(new String[result_website.size()]));


                    lv2.setAdapter(lacked_adapter);
                    setListViewHeight(lv2);
                    lacked_adapter.notifyDataSetChanged();


                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Exception", ex.getMessage());
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
        obj.execute(ip);

    }


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
            tv11.setText(dsc[position]+" "+rimg[position]);






            return row;
        }


    }



    class myadapter_lacked extends ArrayAdapter<String>
    {
        Context context;
        String ttl[];
        String dsc[];
        String rimg[];
        String combined[];
        String price[];
        String unit_fetched[];
        String Web[];

        myadapter_lacked(Context c, String ttl[],String dsc[],String rimg[],
                         String combined[],String price[],String unit_fetched[],String Web[])
        {
            super(c, R.layout.rows3, R.id.lacked_tv2,ttl);
            context=c;
            this.ttl=ttl;
            this.dsc=dsc;
            this.rimg=rimg;
            this.combined=combined;
            this.price=price;
            this.unit_fetched=unit_fetched;
            this.Web=Web;

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
            TextView tv_com=row.findViewById(R.id.tv_com);
            TextView textView_str=row.findViewById(R.id.textView_str);
            TextView tv_price=row.findViewById(R.id.tv_price);
            TextView Unit=row.findViewById(R.id.unit);
            ImageButton button_add_to_purchasing=row.findViewById(R.id.ig_add);
            TextView Quantity =row.findViewById(R.id.Quantity);
            ImageButton list_btn_minus=row.findViewById(R.id.btnDeleteIngredients);
            ImageButton list_btn_add=row.findViewById(R.id.btnADDIngredients);





            Quantity.setText(dsc[position]);
            tv1.setText(ttl[position]);
            if(combined[position].equals("")||
                    combined[position].equals(null)||
                    combined[position]==null||
                    combined[position]=="")

            {
                textView_str.setVisibility(View.INVISIBLE);
                tv_com.setText(combined[position]);

            }
            else {
                tv_com.setText(Html.fromHtml(combined[position]));
//                tv_com.setMovementMethod(LinkMovementMethod.getInstance());
                tv_com.setOnClickListener(v -> {
//                    Snackbar.make(v," hi", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                Uri uri = Uri.parse(Web[position]);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                });
            }



            tv_price.setText(price[position]);
            Unit.setText(unit_fetched[position]);
            Log.i("unit_fetched",unit_fetched[position]);


            list_btn_add.setOnClickListener(v -> {

                int qty_add=Integer.parseInt((String) Quantity.getText());
                qty_add=qty_add+1;
                Quantity.setText(String.valueOf(qty_add));
            });


            list_btn_minus.setOnClickListener(v -> {

                int qty_minus=Integer.parseInt((String) Quantity.getText());
                if (qty_minus>0)
                {
                    qty_minus=qty_minus-1;
                    Quantity.setText(String.valueOf(qty_minus));
                }
                else {
                    Snackbar.make(v," 數量不對", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }



            });

            button_add_to_purchasing.setOnClickListener(v -> {

                String qty_remain= (String) Quantity.getText();


                if(Integer.parseInt(qty_remain)<=0)
                {
                    Snackbar.make(v," 數量不對", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Log.i(String.valueOf(getApplicationContext()), "數量不對");
                }
                else{
                    Log.i("fid",fid);
                    Log.i("qty_remain",qty_remain);
                    Log.i("ttl[position]",ttl[position]);
//                    Snackbar.make(v," fid , qty_remain, ttl[position]" +fid+qty_remain+ttl[position], Snackbar.LENGTH_LONG).setAction("Action", null).show();



                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[3];
                            field[0] = "Fridge_ID";
                            field[1] = "qty";
                            field[2] = "ingredient_title";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = fid;
                            data[1] = qty_remain;
                            data[2] = ttl[position];
                            com.vishnusivadas.advanced_httpurlconnection.PutData putData = new PutData("http://192.168.0.195/yu/add_to_fridge_purchasing_list.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    Log.i("message: ", result);
                                    if(result.equals("INSERT sucess")){
//                                            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                        Log.i("message: ", result);
                                        Snackbar.make(v,"以新增 食材名稱: "+ttl[position]+" 數量: "+qty_remain+" 到採購清單", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        Log.i("INSERT sucess: ", "以新增 食材名稱: "+ttl[position]+" 數量: "+qty_remain+" 到採購清單");
                                    }
                                    else if (result.equals("UPDATE sucess")){
//                                            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                        Log.i("message: ", result);
                                        Snackbar.make(v,"以把 食材名稱: "+ttl[position]+" 在採購清單的數量更新成: "+qty_remain, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        Log.i("UPDATE sucess: ", "以把 食材名稱: "+ttl[position]+" 在採購清單的數量更新成: "+qty_remain);
                                    }
                                    else {
                                        Log.i("error: ", result);
                                        Snackbar.make(v, result, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }


            });


            return row;
        }




    }

}
