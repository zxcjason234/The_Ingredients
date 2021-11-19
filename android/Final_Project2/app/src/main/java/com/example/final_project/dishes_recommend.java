package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class dishes_recommend extends AppCompatActivity {

    private static final String apiurl="http://192.168.0.195/yu/dish_recommended_fetch.php";
    ListView lv;
    private ActionBar mActionBar;
    HttpURLConnection urlConnection = null;



    private static String title[];
    private static String img[];
    private static String Dishes_id[];
    private static String Score[];


    String i_path,mid,did,fid;
    String[] DID,IMG_PAT;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_dishes_recommend);

        TextView tbtv=(TextView)findViewById(R.id.textView2); //toolbar上面的textview
        ImageView btn_back = (ImageView)findViewById(R.id.imageView); //圖片的按鈕
        tbtv.setText("食譜推薦");

        // 接收登入者的id
        String username = getIntent().getStringExtra("memberid");//接收前一頁給的東西
        TextView txtloginName = (TextView) findViewById(R.id.txt_member_id3);//接收前一頁給的東西
        txtloginName.setText(username);//接收前一頁給的東西

        String refridgeid = getIntent().getStringExtra("refridgeid");//接收前一頁給的東西
        TextView txtrefridgeid = (TextView) findViewById(R.id.txt_fridge_id2);//接收前一頁給的東西
        txtrefridgeid.setText(refridgeid);//接收前一頁給的東西


        lv=(ListView)findViewById(R.id.lv_rec);
        lv.setBackgroundColor(Color.parseColor("#F3F8F8"));
        Intent intent = getIntent();
        mid = intent.getStringExtra("mid");
        Log.i("mid",mid);
        fid = intent.getStringExtra("fid");
        Log.i("fid",fid);

        fetch_data_into_array(lv);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            final String s = lv.getItemAtPosition(position).toString();
            did = DID[position];

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
                    PutData putData = new PutData("http://192.168.0.195/yu/insert_user_behavior_click.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            Log.i("message: ", result);
                            if(result.equals("UPDAYTE sucess") || result.equals("INSERT sucess")){
//                                    Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(dishes_recommend.this, dishes_info_rec_random.class);
                                intent1.putExtra("mid", mid);
                                intent1.putExtra("did", did);
                                intent1.putExtra("fid", fid);
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


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);
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
                Intent intent3 = new Intent(getApplicationContext(),MainActivity.class);
                intent3.putExtra("refridgeid",id2);
                intent3.putExtra("memberid",id3); // 我這邊把memberid傳回去了，雖然我這個功能用不到
                startActivity(intent3);
                finish();
            }
        });


    } // End of onCreate

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

    public void fetch_data_into_array(View view)
    {
        String finalurl=apiurl+"?memID="+mid;
        Log.v("finalurl",finalurl);

        class  dbManager extends AsyncTask<String,Void,String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    title = new String[ja.length()];
                    img = new String[ja.length()];
                    Dishes_id= new String[ja.length()];
                    Score= new String[ja.length()];

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        Dishes_id[i]=jo.getString("Dishes_id");
                        title[i] = jo.getString("title");
                        img[i] ="http://192.168.0.195/img/" + jo.getString("img");;
                        Score[i] = jo.getString("Score");

                    }
                    DID=Dishes_id;
                    IMG_PAT = img;


                    myadapter adptr = new myadapter(getApplicationContext(), title, img,Score);
                    lv.setAdapter(adptr);
                    setListViewHeight(lv);

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
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

    }


    class myadapter extends ArrayAdapter<String>
    {
        Context context;
        String ttl[];
        String rimg[];
        String score[];

        myadapter(Context c, String ttl[],String rimg[],String score[])
        {
            super(c, R.layout.row, R.id.tv_title,ttl);
            context=c;
            this.ttl=ttl;
            this.rimg=rimg;
            this.score=score;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.dish_rec_row,parent,false);

            ImageView img=row.findViewById(R.id.img1);
            ImageView img_star1=row.findViewById(R.id.iv_star1);
            ImageView img_star2=row.findViewById(R.id.iv_star2);
            ImageView img_star3=row.findViewById(R.id.iv_star3);
            ImageView img_star4=row.findViewById(R.id.iv_star4);
            ImageView img_star5=row.findViewById(R.id.iv_star5);

            TextView tv1=row.findViewById(R.id.tv_title);


            tv1.setText(ttl[position]);
            String url=rimg[position];
            if(position > 4 && position <= 9){
                img_star5.setImageResource(R.drawable.ic_star_empty);
            }
            else if(position > 9 && position <= 14){
                img_star5.setImageResource(R.drawable.ic_star_empty);
                img_star4.setImageResource(R.drawable.ic_star_empty);

            }
            else if(position > 14 && position <= 19){
                img_star5.setImageResource(R.drawable.ic_star_empty);
                img_star4.setImageResource(R.drawable.ic_star_empty);
                img_star3.setImageResource(R.drawable.ic_star_empty);


            }
            else if(position > 19 && position <= 24){
                img_star5.setImageResource(R.drawable.ic_star_empty);
                img_star4.setImageResource(R.drawable.ic_star_empty);
                img_star3.setImageResource(R.drawable.ic_star_empty);
                img_star2.setImageResource(R.drawable.ic_star_empty);

            }
            else if(position > 24){
                img_star5.setImageResource(R.drawable.ic_star_empty);
                img_star4.setImageResource(R.drawable.ic_star_empty);
                img_star3.setImageResource(R.drawable.ic_star_empty);
                img_star2.setImageResource(R.drawable.ic_star_empty);
                img_star1.setImageResource(R.drawable.ic_star_empty);

            }

            class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
                private String url;
                private ImageView imageView;

                public ImageLoadTask(String url, ImageView imageView) {
                    this.url = url;
                    this.imageView = imageView;
                }

                @Override
                protected Bitmap doInBackground(Void... params) {
                    try {
                        URL connection = new URL(url);
                        InputStream input = connection.openStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        Bitmap resized = Bitmap.createScaledBitmap(myBitmap, 400, 400, true);
                        return resized;
                    } catch (Exception e) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        Looper.loop();

                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    imageView.setImageBitmap(result);
                }
            }
            ImageLoadTask obj=new ImageLoadTask(url,img);
            obj.execute();

            return row;
        }
    }
}