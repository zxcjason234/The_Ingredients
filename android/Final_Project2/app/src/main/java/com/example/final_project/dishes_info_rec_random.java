package com.example.final_project;

import android.content.Intent;
import android.content.res.ColorStateList;
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
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class dishes_info_rec_random extends AppCompatActivity {


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

    String
            descr,
            img_url,
            DISHES_TYPE,
            FOOD_MAIN,
            COUNTRY_CUSINE,
            M_OR_V,
            PROVENANCE,
            STEP,AUTHOR,
            LACK_ING_TITLE,
            mid,
            did,
            fid,
            aut,combinedStr,mid2,fid2,did2;
    private ActionBar mActionBar;
    List<String> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        TextView tv_mid =findViewById(R.id.tv_mid);
        TextView tv_fid =findViewById(R.id.tv_fid);
        TextView tv_did =findViewById(R.id.tv_did);

        TextView dish_source =findViewById(R.id.dish_source_tv1);
        TextView dish_description =findViewById(R.id.dish_description1);
//        TextView dishn_type=findViewById(R.id.dishType_tv1);
//        TextView food_main_tv=findViewById(R.id.food_main_tv1);
        TextView dish_cousine=findViewById(R.id.dish_cousine1);
        TextView dish_m_or_v=findViewById(R.id.dish_m_or_v1);
        ImageView dish_pic= findViewById(R.id.imv1);
        TextView tv_title=findViewById(R.id.tv_title);

        Intent intent = getIntent();



        mid = intent.getStringExtra("mid");
        tv_mid.setText(mid);
        mid2=tv_mid.getText().toString();

        did= intent.getStringExtra("did");
        tv_did.setText(did);
        did2=tv_did.getText().toString();

        fid= intent.getStringExtra("fid");
        tv_fid.setText(fid);

        Log.v("mid",mid);
        Log.v("did",did);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
        collapsingToolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

        fetch_data_into_array(did);



//        toolbar.setTitle(title);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));


//        dish_description.setText(descr);
//        ImageLoadTask obj=new ImageLoadTask(img_url,dish_pic);
//        obj.execute();


        FloatingActionButton fab_step = (FloatingActionButton) findViewById(R.id.fab_step);
        fab_step.setOnClickListener(view -> {

            // TODO Auto-generated method stub


            Intent intent12 = new Intent(dishes_info_rec_random.this, dishes_step_detailed.class);

            intent12.putExtra("did", did);

            intent12.putExtra("t", title);
            intent12.putExtra("fid", fid);
            startActivity(intent12);


        });

        FloatingActionButton fab_ingredient = (FloatingActionButton) findViewById(R.id.fab_ingredient);
        fab_ingredient.setOnClickListener(view -> {
            // TODO Auto-generated method stub
            Intent intent1 = new Intent(dishes_info_rec_random.this, recipe_ingredient_v2.class);
//            intent1.putExtra("fid", tv_fid.getText());
            intent1.putExtra("did", did);
            intent1.putExtra("mid", mid);
            intent1.putExtra("fid", fid);
            startActivity(intent1);
//            finish();
        });


        FloatingActionButton btn_back = (FloatingActionButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(view -> {
            // TODO Auto-generated method stub
            Intent intent_back = new Intent(dishes_info_rec_random.this, MainActivity.class);
            intent_back.putExtra("memberid", tv_mid.getText());
            intent_back.putExtra("refridgeid", tv_fid.getText());
//
//            startActivity(intent_back);
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
                            String[] field1 = new String[2];
                            field1[0] = "member_id";
                            field1[1] = "Dishes_ID";
                            //Creating array for data
                            String[] data1 = new String[2];
                            data1[0] = mid2;
                            data1[1] = did2;

                            Log.v("insert_url","http://192.168.0.195/yu/insert_user_behavior_collect.php?member_id="+mid2+"&Dishes_ID="+did2);

                            PutData putData = new PutData("http://192.168.0.195/yu/insert_user_behavior_collect.php", "POST", field1, data1);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    Log.i("message: ", result);
                                    if(result.equals("UPDAYTE sucess") || result.equals("INSERT sucess")){
//                                        Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                        Snackbar.make(view, "收藏成功!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        Log.i("message: ", result);
                                    }
                                    else if(result.equals("ALREADY IN COLLECTION"))
                                    {
                                        Snackbar.make(view, "以收藏過此食譜", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        Log.i("message: ", result);

                                    }
                                    else {
                                        Log.i("error: ", result);
                                        Snackbar.make(view, result, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                }
                            }
                            else
                                {
                                    Log.i("error:",mid+" , "+fid);
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


                Intent i = new Intent(dishes_info_rec_random.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
//                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void fetch_data_into_array(String did)
    {
        String ip="http://192.168.0.195/yu/get_dish_info.php?Dishes_id="+did;
        Log.i("ip",ip);

        TextView dish_source =findViewById(R.id.dish_source_tv1);
        TextView dish_description =findViewById(R.id.dish_description1);
        TextView dishn_type=findViewById(R.id.dishType_tv1);
        TextView food_main_tv=findViewById(R.id.food_main_tv1);
        TextView dish_cousine=findViewById(R.id.dish_cousine1);
        TextView dish_m_or_v=findViewById(R.id.dish_m_or_v1);
        TextView tv_title=findViewById(R.id.tv_title);

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
                        tv_title.setText(title[i]);

//                        dishes_type[i] = jo.getString("dishes_type");
//                        dishn_type.setText(dishes_type[i]);

//                        food_main[i] = jo.getString("food_main");
//                        food_main_tv.setText(food_main[i]);

                        country_cusine[i] = jo.getString("country_cusine");
                        dish_cousine.setText(country_cusine[i]);

                        m_or_v[i] = jo.getString("M_or_V");
                        dish_m_or_v.setText(m_or_v[i]);

                        description[i] = jo.getString("description");
                        dish_description.setText(description[i]);

                        author[i] = jo.getString("author");
                        Provenance[i] = jo.getString("Provenance");
                        combinedStr="<a href=\""+Provenance[i]+"\">"+author[i] +"</a>";
                        dish_source.setText(Html.fromHtml(combinedStr));
                        dish_source.setMovementMethod(LinkMovementMethod.getInstance());

                        img[i] ="http://192.168.0.195/img/"+jo.getString("img");
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