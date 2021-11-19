package com.example.final_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class dishes_info extends AppCompatActivity {


    DisplayMetrics dm = new DisplayMetrics();
    int ScreenWidth = dm.widthPixels;
    HttpURLConnection urlConnection = null;

    private static String title[];
//    private static String dishes_type[];
//    private static String food_main[];
    private static String country_cusine[];
    private static String m_or_v[];
    private static String description[];
    private static String img[];
    private static String author[];
    private static String Provenance[];
    private static String lacked_ing[];
    private static String Dishes_id[];


    String
            descr,
            img_url,
            DISHES_TYPE,
            FOOD_MAIN,
            COUNTRY_CUSINE,
            M_OR_V,
            PROVENANCE,
            STEP,AUTHOR,
            mid,
            did,
            fid,
            aut,combinedStr;
    private ActionBar mActionBar;
    List<String> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tv_mid =findViewById(R.id.tv_mid);
        TextView tv_fid =findViewById(R.id.tv_fid);
//        TextView dishname=findViewById(R.id.dishname_tv);\
        TextView dish_source =findViewById(R.id.dish_source_tv1);
        TextView dish_description =findViewById(R.id.dish_description1);
        TextView dishn_type=findViewById(R.id.dishType_tv1);
        TextView food_main_tv=findViewById(R.id.food_main_tv1);
        TextView dish_cousine=findViewById(R.id.dish_cousine1);
        TextView dish_m_or_v=findViewById(R.id.dish_m_or_v1);

        TextView tv_title=findViewById(R.id.tv_title);
        ImageView dish_pic= findViewById(R.id.imv1);
//        Button btn_ingredient = (Button) findViewById(R.id.btn_ingredient);
//        Button btn_steps = (Button)findViewById(R.id.btn_steps);


//        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
//        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
//        collapsingToolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));


        Intent intent = getIntent();


        mid = intent.getStringExtra("mid");
        tv_mid.setText(mid);
        Log.i("mid",mid);


        did= intent.getStringExtra("did");
        Log.i("did",did);

        fid= intent.getStringExtra("fid");
        tv_fid.setText(fid);
        Log.i("fid",fid);

        fetch_Fridge_data_into_array(did);


//        for (int i = 0; i < itemList.size(); i++){
//            Log.i("message", Arrays.toString(new String[]{itemList.get(i)}));
//        }

//        Intent intent = new Intent(MainActivity2.this, recipe_step.class);
//        intent.putExtra("st", STEP);
//        startActivity(intent);

//        dish_description.setText(descr);
//        dish_description.setHorizontallyScrolling(true);


//        mActionBar = getSupportActionBar();
//        if (mActionBar != null) {
//            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//            mActionBar.setTitle("食譜推薦");
//            mActionBar.setHomeButtonEnabled(true);//设置左上角的图标是否可以点击
//            mActionBar.setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
//            mActionBar.setDisplayShowCustomEnabled(true);// 使自定义的普通View能在title栏显示，即actionBar.setCustomView能起作用
//        }


        FloatingActionButton fab_step = (FloatingActionButton) findViewById(R.id.fab_step);
        fab_step.setOnClickListener(view -> {

            // TODO Auto-generated method stub
            Intent intent12 = new Intent(dishes_info.this, dishes_step_detailed.class);

            intent12.putExtra("fid", fid);
            startActivity(intent12);



        });

        FloatingActionButton fab_ingredient = (FloatingActionButton) findViewById(R.id.fab_ingredient);
        fab_ingredient.setOnClickListener(view -> {
            // TODO Auto-generated method stub
            Intent intent1 = new Intent(dishes_info.this, recipe_ingredient_v2.class);
            intent1.putExtra("did", did);
            intent1.putExtra("mid", mid);
            intent1.putExtra("fid", fid);
            startActivity(intent1);
        });


        FloatingActionButton btn_back = (FloatingActionButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(view -> {
            // TODO Auto-generated method stub
            Intent hintent = new Intent(getApplicationContext(), MainActivity.class);
            hintent.putExtra("memberid",mid);//讓別人吃要打的
            hintent.putExtra("refridgeid",fid);//讓別人吃要打的
//            startActivity(hintent);
            onBackPressed();


        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_collect);
        fab.setOnClickListener(view -> {
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
                            PutData putData = new PutData("http://192.168.0.195/yu/insert_user_behavior_collect.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    Log.i("message: ", result);
                                    if(result.equals("UPDAYTE sucess") || result.equals("INSERT sucess")){
//                                        Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
//                                        Snackbar.make(view, result, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        Log.i("message: ", result);
                                    }
                                    else {
                                        Log.i("error: ", result);
//                                        Snackbar.make(view, result, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }


        );




    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        FloatingActionMenu menu = findViewById(R.id.fab_menu);
        if (ev.getAction() == MotionEvent.ACTION_UP && menu.isOpened()){
            menu.close(true);
        }
        return super.dispatchTouchEvent(ev);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:


                Intent i = new Intent(dishes_info.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
//                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void fetch_Fridge_data_into_array(String did)
    {
        String ip="http://192.168.0.195/yu/get_dish_info.php?Dishes_id="+did;
        Log.i("ip",ip);

        TextView dishname=findViewById(R.id.tv_title);
        TextView dish_source =findViewById(R.id.dish_source_tv1);
        TextView dish_description =findViewById(R.id.dish_description1);
        TextView dishn_type=findViewById(R.id.dishType_tv1);
        TextView food_main_tv=findViewById(R.id.food_main_tv1);
        TextView dish_cousine=findViewById(R.id.dish_cousine1);
        TextView dish_m_or_v=findViewById(R.id.dish_m_or_v1);

        ImageView dish_pic= findViewById(R.id.imv1);

        class  dbManager extends AsyncTask<String, Void, String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    Log.i("data", data);

                    title = new String[ja.length()];
//                    dishes_type = new String[ja.length()];
//                    food_main  = new String[ja.length()];
                    country_cusine = new String[ja.length()];
                    m_or_v  = new String[ja.length()];
                    description  = new String[ja.length()];
                    img = new String[ja.length()];
                    author  = new String[ja.length()];
                    Provenance  = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);


                        title[i] = jo.getString("title");
                        dishname.setText(title[i]);
                        Log.i("title",title[i]);

//                        dishes_type[i] = jo.getString("dishes_type");
//                        dishn_type.setText(dishes_type[i]);
//                        Log.i("dishes_type",dishes_type[i]);
//
//                        food_main[i] = jo.getString("food_main");
//                        food_main_tv.setText(food_main[i]);
//                        Log.i("food_main",food_main[i]);

                        country_cusine[i] = jo.getString("country_cusine");
                        dish_cousine.setText(country_cusine[i]);
                        Log.i("country_cusine",country_cusine[i]);

                        m_or_v[i] = jo.getString("M_or_V");
                        dish_m_or_v.setText(m_or_v[i]);
                        Log.i("m_or_v",m_or_v[i]);


                        description[i] = jo.getString("description");
                        dish_description.setText(description[i]);
                        Log.i("description",description[i]);

                        author[i] = jo.getString("author");
                        Provenance[i] = jo.getString("Provenance");
                        combinedStr="<a href=\""+Provenance[i]+"\">"+author[i] +"</a>";
                        Log.i("combinedStr",combinedStr);
                        dish_source.setText(Html.fromHtml(combinedStr));
                        dish_source.setMovementMethod(LinkMovementMethod.getInstance());


                        img[i] ="http://192.168.0.195/img/"+jo.getString("img");
                        Log.i("img",img[i]);
                        ImageLoadTask obj=new ImageLoadTask(img[i],dish_pic);
                        obj.execute();

                    }


                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("Exception", ex.getMessage());
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



    class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }
        //400, 400
        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL connection = new URL(url);
                InputStream input = connection.openStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Bitmap resized = Bitmap.createScaledBitmap(myBitmap, 400, 400, true);
                return resized;
            } catch (Exception e) {
                //                   Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }
}