package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;


public class dishes_search_activity extends AppCompatActivity {
    private static final String apiurl1="http://192.168.0.195/yu/search_dishes.php";
    private static final String apiurl2="http://192.168.0.195/yu/dishes_search.php";
    ListView slst1;

    ArrayList<String> title=new ArrayList();

    ArrayList<String> img=new ArrayList();
    ArrayList<String> Dishes_id=new ArrayList();

    String mid,did,fid;
    String[] DID;


    private SearchView searchView;
    private String selectedFilter = "all";
    private String currentSearchText = "";
    SwipyRefreshLayout srl;
    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlist1_test);
        SearchView sv1=findViewById(R.id.sv1);
        TextView tbtv=(TextView)findViewById(R.id.textView2);
        ImageView btn_back = (ImageView)findViewById(R.id.imageView);

        // 接收登入者的id
        String username = getIntent().getStringExtra("memberid");//接收前一頁給的東西
//        TextView txtloginName = (TextView) findViewById(R.id.txt_member_id2);//接收前一頁給的東西
//        txtloginName.setText(username);//接收前一頁給的東西

        String refridgeid = getIntent().getStringExtra("refridgeid");//接收前一頁給的東西
//        TextView txtrefridgeid = (TextView) findViewById(R.id.txt_fridge_id3);//接收前一頁給的東西
//        txtrefridgeid.setText(refridgeid);//接收前一頁給的東西

//        tbtv.setText("食譜搜尋");
        slst1= findViewById(R.id.slst1);
        slst1.setTextFilterEnabled(true);
//        Intent intent = getIntent();
//        mid = intent.getStringExtra("mid");
//        fid = intent.getStringExtra("fid");
        mid =  getIntent().getStringExtra("mid");
        fid =  getIntent().getStringExtra("fid");
        srl=findViewById(R.id.srl);
        srl.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
//        fetch_data_into_array(slst1,apiurl1);
        initSearchWidgets();


        srl.setOnRefreshListener(direction -> {


            try {
                String more_data="http://192.168.0.195/yu/search_more.php";
                int NumOfRows=slst1.getAdapter().getCount();
                Log.i("more data",more_data+"?keyword="+keyword+"&RowNum="+ NumOfRows);
                String more_data_url = null;
                more_data_url = more_data+"?keyword="+ URLEncoder.encode(keyword,"UTF-8")+"&RowNum="+NumOfRows;
                fetch_data_into_array2(slst1,Dishes_id,title,img,more_data_url,NumOfRows);
                Log.i("hi",more_data_url);
                srl.setRefreshing(false);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        });





//        btn_back.setOnClickListener(view -> {
////            String id2 = txtrefridgeid.getText().toString();
////            String id3 = txtloginName.getText().toString();
//            Intent intent3 = new Intent(getApplicationContext(),MainActivity.class);
//            intent3.putExtra("refridgeid",refridgeid);
//            intent3.putExtra("memberid",username); // 我這邊把memberid傳回去了，雖然我這個功能用不到
//            intent3.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent3);
//            finish();
//
//        });

        slst1.setOnItemClickListener((parent, view, position, id) -> {
            final String s = slst1.getItemAtPosition(position).toString();
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
//                                Snackbar.make(view, result, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                Intent intent1 = new Intent(dishes_search_activity.this, dishes_info_rec_random.class);
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
//                        String str = txtloginName.getText().toString();//讓別人吃要打的
                        Intent intent = new Intent(getApplicationContext(), Fridge.class);
                        intent.putExtra("memberid",mid);//讓別人吃要打的

//                        String fri = txtrefridgeid.getText().toString();//讓別人吃要打的
                        intent.putExtra("refridgeid",fid);//讓別人吃要打的
                        startActivity(intent);
//                        startActivity(new Intent(getApplicationContext()
//                                ,Fridge.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.home:
//                        String hstr = txtloginName.getText().toString();//讓別人吃要打的
                        Intent hintent = new Intent(getApplicationContext(), MainActivity.class);
                        hintent.putExtra("memberid",mid);//讓別人吃要打的

//                        String hfri = txtrefridgeid.getText().toString();//讓別人吃要打的
                        hintent.putExtra("refridgeid",fid);//讓別人吃要打的
                        startActivity(hintent);
//                        startActivity(new Intent(getApplicationContext()
//                                ,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.shopping_list:
//                        String sstr = txtloginName.getText().toString();//讓別人吃要打的
                        Intent sintent = new Intent(getApplicationContext(), shopping_list.class);
                        sintent.putExtra("memberid",mid);//讓別人吃要打的

//                        String sfri = txtrefridgeid.getText().toString();//讓別人吃要打的
                        sintent.putExtra("refridgeid",fid);//讓別人吃要打的
                        startActivity(sintent);
//                        startActivity(new Intent(getApplicationContext()
//                                ,shopping_list.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.User:
//                        String ustr = txtloginName.getText().toString();//讓別人吃要打的
                        Intent uintent = new Intent(getApplicationContext(), User_setting.class);
                        uintent.putExtra("memberid",mid);//讓別人吃要打的

//                        String ufri = txtrefridgeid.getText().toString();//讓別人吃要打的
                        uintent.putExtra("refridgeid",fid);//讓別人吃要打的
                        startActivity(uintent);
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });


    }//End of oncreate

    private void initSearchWidgets()
    {
        searchView = (SearchView) findViewById(R.id.sv1);
        searchView.setSubmitButtonEnabled(true);


        searchView.setOnCloseListener(() -> {
            ArrayList<String> Dishes_idempty1=new ArrayList();
            ArrayList<String> Dishes_idempty2=new ArrayList();
            ArrayList<String> Dishes_idempty3=new ArrayList();


            myadapter adptr = new myadapter(getApplicationContext(), Dishes_idempty1, Dishes_idempty2,Dishes_idempty3);
//            adptr.clear();
            slst1.setAdapter(null);

            adptr.notifyDataSetChanged();


//            fetch_data_into_array(slst1,apiurl2+"?keyword="+"");
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                currentSearchText = s;
                keyword=s;
                if (!s.isEmpty())
                {
                    fetch_data_into_array(slst1,apiurl2+"?keyword="+s);
//                    slst1.setFilterText(s);

                }
                else {
//                    fetch_data_into_array(slst1,apiurl1);
//                    Snackbar.make(slst1, "s is empty", Snackbar.LENGTH_SHORT)
//                            .show();
                    myadapter adptr = new myadapter(getApplicationContext(), null, null,null);
                    slst1.setAdapter(adptr);
                    adptr.notifyDataSetChanged();

//                    Log.i("line134", "s is empty");
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
//                if(s.isEmpty())
//                {
////                    slst1.clearTextFilter();
//                    fetch_data_into_array(slst1,apiurl1);
//                    slst1.setOnItemClickListener((parent, view, position, id) -> {
////                        Snackbar.make(view, "沒按過搜尋鍵", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//
//                        final String s2 = slst1.getItemAtPosition(position).toString();
//                        dt = DISHES_TYPE[position];
//                        fm = FOOD_MAIN[position];
//                        cc = COUNTRY_CUSINE[position];
//                        mv = M_OR_V[position];
//                        dc = DESCRIPTION[position];
//                        i_path = IMG_PAT[position];
//                        pro = PROVENANCE[position];
//                        st = STEP[position];
////                        LIT = LACKED_ING[position];
//                        did = DID[position];
//
//                        Handler handler = new Handler(Looper.getMainLooper());
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                //Starting Write and Read data with URL
//                                //Creating array for parameters
//                                String[] field = new String[2];
//                                field[0] = "member_id";
//                                field[1] = "Dishes_ID";
//                                //Creating array for data
//                                String[] data = new String[2];
//                                data[0] = mid;
//                                data[1] = did;
//                                PutData putData = new PutData("http://192.168.0.195/yu/insert_user_behavior_click.php", "POST", field, data);
//                                if (putData.startPut()) {
//                                    if (putData.onComplete()) {
//                                        String result = putData.getResult();
//                                        Log.i("message: ", result);
//                                        if(result.equals("UPDAYTE sucess") || result.equals("INSERT sucess")){
////                                    Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
////                                            Snackbar.make(view, result, Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                                            Intent intent1 = new Intent(dishes_search_activity.this, dishes_info_rec_random.class);
//                                            intent1.putExtra("t", s);
//                                            intent1.putExtra("dt", dt);
//                                            intent1.putExtra("fm", fm);
//                                            intent1.putExtra("cc", cc);
//                                            intent1.putExtra("mv", mv);
//                                            intent1.putExtra("dc", dc);
//                                            intent1.putExtra("i_p", i_path);
//                                            intent1.putExtra("pro", pro);
//                                            intent1.putExtra("ST", st);
////                                            intent1.putExtra("LIT", LIT);
//                                            intent1.putExtra("mid", mid);
//                                            intent1.putExtra("did", did);
//                                            intent1.putExtra("fid", fid);
//                                            startActivity(intent1);
//                                        }
//                                        else {
//                                            Log.i("error: ", result);
////                                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//                                //End Write and Read data with URL
//                            }
//                        });
//
//                    });
//                }
                return false;
            }
        });
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


    public void fetch_data_into_array(View view, String url)
    {

        class  dbManager extends AsyncTask<String, Void, String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    title.clear();
                    img.clear();
                    Dishes_id.clear();

                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
//                        Dishes_id[i]=jo.getString("Dishes_id");
//                        title[i] = jo.getString("title");
//                        img[i] ="http://192.168.0.195/img/" + jo.getString("img");;
                        Dishes_id.add(jo.getString("Dishes_id"));
                        title.add(jo.getString("title"));
                        img.add("http://192.168.0.195/img/" + jo.getString("img"));

                    }

//                    STEP =step;
//                    LACKED_ING = lacked_ing;


                    myadapter adptr = new myadapter(getApplicationContext(), title, img,Dishes_id);
                    slst1.setAdapter(adptr);
                    adptr.notifyDataSetChanged();


                    Log.i("資料筆數", String.valueOf(slst1.getAdapter().getCount()));

//                    setListViewHeight(slst1);
//                    adptr.notifyDataSetChanged();

                    slst1.setOnItemClickListener((parent, view, position, id) -> {
                        did = Dishes_id.get(position);

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
                                PutData putData = new PutData("http://192.168.0.195/yu/insert_user_behavior_search_click.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        Log.i("message: ", result);
                                        if(result.equals("UPDAYTE sucess") || result.equals("INSERT sucess")){
//                                    Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
//                                            Snackbar.make(view, result, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                            Intent intent1 = new Intent(dishes_search_activity.this, dishes_info_rec_random.class);
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


                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("fetch_data error",ex.getMessage());
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
        obj.execute(url);

    }

    public void fetch_data_into_array2(View view, ArrayList<String> t, ArrayList<String> u, ArrayList<String> k, String url,int rownum)
    {

        class  dbManager extends AsyncTask<String, Void, String>
        {
            protected void onPostExecute(String data)
            {
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;
                    Log.i("Listview內筆數", String.valueOf(rownum));
                    Log.i("新抓的筆數", String.valueOf(ja.length()));



                    if(ja.length()==0)
                    {
                        Snackbar.make(view,"已經到底了~", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    }
                    else {
                        for (int i = 0; i < ja.length(); i++) {
                            jo = ja.getJSONObject(i);
                            t.add(jo.getString("Dishes_id"));
                            u.add(jo.getString("title")) ;
                            k.add("http://192.168.0.195/img/" + jo.getString("img"));

                        }
                        myadapter adptr1 = new myadapter(getApplicationContext(), u, k,t);
                        slst1.setAdapter(adptr1);
                        adptr1.notifyDataSetChanged();
                    }


                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("fetch_data2 error",ex.getMessage());
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
//        String furl=url.toString();
        dbManager obj=new dbManager();
        Log.i("furl",url);
        obj.execute(url);

    }

//    class myadapter extends  ArrayAdapter<String> {
//        Context context;
//        String[] ttl;
////        String[] dsc;
//        String[] rimg;
//        String[] prov;
//        String[] filterList;
//        ArrayList<String> originalitem;
//        ArrayList<String> items;
//        ArrayList<String> filtered;
//
//        myadapter(Context c, String ttl[], String rimg[]) {
//            super(c, R.layout.row, R.id.tv1, ttl);
//            context = c;
//            this.ttl = ttl;
////            this.dsc = dsc;
//            this.rimg = rimg;
//            originalitem = new ArrayList<>(Arrays.asList(ttl));
//
//        }
//
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View row = inflater.inflate(R.layout.row_search, parent, false);
//
//            ImageView img = row.findViewById(R.id.row_img);
//            TextView tv1 = row.findViewById(R.id.row_tv1);
////            TextView tv2 = row.findViewById(R.id.row_tv2);
//
//
//            tv1.setText(ttl[position]);
////            tv2.setText(dsc[position]);
//
//            String url = rimg[position];
//
//
//            class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
//                private String url;
//                private CircleImageView imageView;
//
//                public ImageLoadTask(String url, CircleImageView imageView) {
//                    this.url = url;
//                    this.imageView = imageView;
//                }
//
//                @Override
//                protected Bitmap doInBackground(Void... params) {
//                    try {
//                        URL connection = new URL(url);
//                        InputStream input = connection.openStream();
//                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                        Bitmap resized = Bitmap.createScaledBitmap(myBitmap, 400, 400, true);
//                        return resized;
//                    } catch (Exception e) {
//                        Looper.prepare();
//                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                        Looper.loop();
//                        Log.e("Bitmap",e.getMessage());
//                    }
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Bitmap result) {
//                    super.onPostExecute(result);
//                    imageView.setImageBitmap(result);
//                }
//            }
//
//            ImageLoadTask obj = new ImageLoadTask(url, (CircleImageView) img);
//            obj.execute();
//
//            return row;
//        }
//
//
//    }

    class myadapter extends  ArrayAdapter<String> {
        Context context;
//        String[] ttl;
        //        String[] dsc;
//        String[] rimg;
        ArrayList<String> ttl;
        ArrayList<String> rimg;
        ArrayList<String> id;

        myadapter(Context c, ArrayList<String> ttl, ArrayList<String> rimg,ArrayList<String> id) {
            super(c, R.layout.row, R.id.tv1, ttl);
            context = c;
            this.ttl = ttl;
            this.id = id;
            this.rimg = rimg;

        }

        @Override
        public void clear() {
            super.clear();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_search, parent, false);

            ImageView img = row.findViewById(R.id.row_img);
            TextView tv1 = row.findViewById(R.id.row_tv1);
            TextView d_id = row.findViewById(R.id.tv_did);


            tv1.setText(ttl.get(position));
            d_id.setText(id.get(position));

            String url = rimg.get(position);


            class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
                private String url;
                private CircleImageView imageView;

                public ImageLoadTask(String url, CircleImageView imageView) {
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
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                        Log.e("Bitmap",e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    imageView.setImageBitmap(result);
                }
            }

            ImageLoadTask obj = new ImageLoadTask(url, (CircleImageView) img);
            obj.execute();


            return row;
        }





    }

}
