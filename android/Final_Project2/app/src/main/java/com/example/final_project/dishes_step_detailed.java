package com.example.final_project;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class dishes_step_detailed extends AppCompatActivity {

    private static String step_url="http://192.168.0.195/yu/fetch_dish_step.php";
    ListView list_step;
    TextView activity_step;
    String did;
    String[] step,
            step_pic,
            STEP,
            step_pic_PAT,
            Dishes_id,
            Dishes_step_Num,
            LENGTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_step);
        ImageView btn_back = (ImageView)findViewById(R.id.imageView);


        list_step=(ListView)findViewById(R.id.list_step);
        activity_step=(TextView)findViewById(R.id.textView2);
        activity_step.setText("步驟");
        Intent intent = getIntent();
        did= intent.getStringExtra("did");
        fetch_data_into_array(list_step);

        btn_back.setOnClickListener(view -> {
//            Intent i = new Intent(this, dishes_info.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(i);
           onBackPressed();
        });


    }


    public void fetch_data_into_array(ListView view)
    {

        String url1 = step_url +"?Dishes_id="+did;
        Log.v("url",url1);

        class  dbManager extends AsyncTask<String, Void, String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    Dishes_id = new String[ja.length()];
                    step = new String[ja.length()];
                    step_pic = new String[ja.length()];
                    Dishes_step_Num = new String[ja.length()];

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        Dishes_id[i]= jo.getString("Dishes_id");
                        step[i] = jo.getString("step");



                        step_pic[i] =jo.getString("step_pic");
                        if(step_pic[i]=="null"|| step_pic[i]==null)
                        {
                            step_pic[i] = "http://192.168.0.195/img/white.jpg";
                        }
                        else{
                            step_pic[i] ="http://192.168.0.195/img/" + jo.getString("step_pic");
                        }
                        Dishes_step_Num[i] = jo.getString("Dishes_step_Num");



                    }
                    STEP=step;
                    step_pic_PAT = step_pic;
                    myadapter adptr = new myadapter(getApplicationContext(), step, step_pic,Dishes_id,Dishes_step_Num);
                    view.setAdapter(adptr);




                } catch (Exception ex) {
//                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Exception: ", ex.getMessage());
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
        obj.execute(url1);

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
        String rstep_pic[];
        String id[];
        String num[];





        myadapter(Context c, String ttl[], String rstep_pic[],String id[],String num[])
        {
            super(c, R.layout.row_step, R.id.textView_step,ttl);
            context=c;
            this.ttl=ttl;
            this.id=id;
            this.rstep_pic=rstep_pic;
            this.num=num;
        }


        @Override
        public void remove(@Nullable String object) {
            super.remove(object);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.row_step,parent,false);

            TextView Step_num =row.findViewById(R.id.Step_num);
            TextView tv1=row.findViewById(R.id.textView_step);
            ImageView step_pic=row.findViewById(R.id.imageView_step);





            Step_num.setText(num[position]);
            tv1.setText(ttl[position]);


            String did=id[position];



            String url=rstep_pic[position];


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
                        Bitmap resized = Bitmap.createScaledBitmap(myBitmap, 800, 400, true);
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

            if(url.equals("http://192.168.0.195/img/white.jpg"))
            {
                Log.i("hi","hi");
                step_pic.setVisibility(View.GONE);
            }
            else{
                ImageLoadTask obj=new ImageLoadTask(url,step_pic);
                obj.execute();
            }


            Log.i("width", String.valueOf(step_pic.getWidth()));
            Log.i("height", String.valueOf(step_pic.getHeight()));

            return row;
        }


    }


}
