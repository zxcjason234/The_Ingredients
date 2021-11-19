package com.example.final_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.SingleSpinnerListener;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BottomSheetDialog_1 extends BottomSheetDialogFragment {

    final List<KeyPairBoolData> listArray2 = new ArrayList<>();
    List<String> Ingredient_type = new ArrayList<>();
    String type,barcode;
    ArrayList<String> IngIDList = new ArrayList<>();
    ArrayAdapter<String> IngIDAdapter;
    RequestQueue requestQueue_Type;

    SingleSpinnerSearch sp2;
    ImageButton bt_insert_to_db,bt_scan_scan;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        setStyle(STYLE_NORMAL,R.style.BottomSheetDialogCircle);
        View v = inflater.inflate(R.layout.fragment_fragment3_1,
                container, false);
        RelativeLayout rl=v.findViewById(R.id.rl);
//        rl.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
//        rl.setClipToOutline(true);


        EditText ig_title_dialog= v.findViewById(R.id.ig_title_dialog);


        //接收並顯示會員id
        TextView tv_scan_fridgeID = v.findViewById(R.id.tv_scan_fridgeID);
        String fid = this.getArguments().getString("fid");
        tv_scan_fridgeID.setText(fid);

        //接收並顯示冰箱id
        TextView tv_scan_UserID = v.findViewById(R.id.tv_scan_UserID);
        String mid = this.getArguments().getString("mid");
        tv_scan_UserID.setText(mid);


        sp2 = v.findViewById(R.id.sp_scan_type);
        sp2.setColorseparation(true);
        sp2.setSearchEnabled(true);
        sp2.setSearchHint("選擇食材種類");

        requestQueue_Type = Volley.newRequestQueue(getContext());
        String url = "http://192.168.0.195/mike/TypeID.php";
        JsonObjectRequest jsonObjectRequest_Type = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("ingredient_type");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String str_ingID = jsonObject.optString("ingredient_type");
                        IngIDList.add(str_ingID);
//                        IngIDAdapter = new ArrayAdapter<>(ingredient_scan.this,
//                                android.R.layout.simple_spinner_item, IngIDList);
//                        IngIDAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        sp2.setAdapter(IngIDAdapter);
                        Ingredient_type.add(str_ingID);


                    }
                    for (int i = 0; i < Ingredient_type.size(); i++) {
                        KeyPairBoolData h = new KeyPairBoolData();

                        h.setId(i + 1);
                        h.setName(Ingredient_type.get(i));
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
        requestQueue_Type.add(jsonObjectRequest_Type);
        sp2.setItems(listArray2, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                type=selectedItem.getName();
            }

            @Override
            public void onClear() {
                Toast.makeText(getContext(), "Cleared Selected Item", Toast.LENGTH_SHORT).show();
            }
        });


        //抓選取日期
        ImageButton bt_date=v.findViewById(R.id.bt_scan_day);
        TextView tv_date= v.findViewById(R.id.tv_scan_day);
        bt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String dateTime = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
                        tv_date.setText(dateTime);
                    }

                }, year, month, day).show();
            }
        });






        bt_insert_to_db= v.findViewById(R.id.bt_scan_insert);
        bt_insert_to_db.setOnClickListener(new View.OnClickListener() {//新增至食材資料庫
            @Override
            public void onClick(View v) {


                String sp_ingerdient_type=String.valueOf(type);
                String expiration_date = String.valueOf(tv_date.getText());
                String FridgeID = String.valueOf(tv_scan_fridgeID.getText());
                String mid  = test1.getmid();




                    if(!sp_ingerdient_type.equals("")&&!ig_title_dialog.getText().toString().equals("")&&!expiration_date.equals("")){
//                        Handler handler = new Handler(Looper.getMainLooper());
//                        handler.post(() -> {
////                                String[] field = new String[4];
////                                field[0] = "ingredient_title";
////                                field[1] = "ingredient_type";
////                                field[2] = "expiration_date";
////                                field[3] = "FridgeID";
////                                String[] data = new String[4];
////                                data[1] = ed_ingredient_title;
////                                data[2] = sp_ingerdient_type;
////                                data[2] = expiration_date;
////                                data[3] = FridgeID;
//
//
//
////                                PutData putData = new PutData("http://192.168.0.195/mike/no_barcode_check.php", "POST", field, data);//沒有條碼
////                                if (putData.startPut()) {
////                                    if (putData.onComplete()) {
////                                        String result = putData.getResult();
////                                        if(result.equals("Update Contain Quantity sucess")){
////                                            Toast.makeText(getContext(),"成功更新此冰箱現有食材數量",Toast.LENGTH_SHORT).show();
////                                            Intent intent = new Intent(getContext(),Fridge.class);
////                                            intent.putExtra("memberid",mid);
////                                            intent.putExtra("refridgeid",FridgeID);
////                                            startActivity(intent);
////
////                                        }
////                                        else if(result.equals("Insert into fridge contain sucess"))
////                                        {
////                                            Toast.makeText(getContext(),"成功新增至冰箱!",Toast.LENGTH_SHORT).show();
////                                            Intent intent = new Intent(getContext(),Fridge.class);
////                                            intent.putExtra("memberid",mid);
////                                            intent.putExtra("refridgeid",FridgeID);
////                                            startActivity(intent);
////
////                                        }
////                                        else if(result.equals("Insert into ingredient sucess"))
////                                        {
////                                            Toast.makeText(getContext(),"成功新增至食材資料庫",Toast.LENGTH_SHORT).show();
////                                            Intent intent = new Intent(getContext(),Fridge.class);
////                                            intent.putExtra("memberid",mid);
////                                            intent.putExtra("refridgeid",FridgeID);
////                                            startActivity(intent);
////
////
////                                        }
////                                        else {
////                                            Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
////                                        }
////                                    }
////                                }
//
//
////
////                            try {
////                                Log.i("FridgeID",FridgeID);
////                                Log.i("expiration_date",expiration_date);
////                                Log.i("ingredient_type",sp_ingerdient_type);
////                                Log.i("ingredient_title",ig_title_dialog.getText().toString());
////
////                                String url1 = "http://192.168.0.195/mike/no_barcode_check.php?FridgeID="+FridgeID
////                                        +"&expiration_date="+expiration_date
////                                        +"&ingredient_type="+URLEncoder.encode(sp_ingerdient_type,"UTF-8")
////                                        +"&ingredient_title="+URLEncoder.encode(ig_title_dialog.getText().toString(),"UTF-8");
////
////                                Log.i("url1",url1);
////                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,
////                                        new Response.Listener<String>() {
////                                            @Override
////                                            public void onResponse(String response) {
////                                                Toast.makeText(getContext(),response.trim(),Toast.LENGTH_LONG).show();
////                                                Log.i("onResponse",response.trim());
////
////                                            }
////                                        },
////                                        new Response.ErrorListener() {
////                                            @Override
////                                            public void onErrorResponse(VolleyError error) {
////                                                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
////                                                Log.e("onErrorResponse",error.toString());
////                                            }
////                                        }){
//////                                @Override
//////                                protected Map<String, String> getParams() {
//////                                    Map<String,String> params = new HashMap<String, String>();
//////                                    params.put("FridgeID",FridgeID);
//////                                    Log.i("FridgeID",FridgeID);
//////
//////                                    params.put("expiration_date",expiration_date);
//////                                    Log.i("expiration_date",expiration_date);
//////
//////
//////                                    params.put("sp_ingerdient_type",sp_ingerdient_type);
//////                                    Log.i("ingredient_type",sp_ingerdient_type);
//////
//////
//////                                    params.put("ingredient_title",ig_title_dialog.getText().toString());
//////                                    Log.i("ingredient_title",ig_title_dialog.getText().toString());
//////
//////
//////                                    return params;
//////                                }
////                                };
////                                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
////                                requestQueue.add(stringRequest);
////
////
////                            } catch ( UnsupportedEncodingException e) {
////                                e.printStackTrace();
////                            }
//                        });
                        Log.i("FridgeID",FridgeID);
                        Log.i("expiration_date",expiration_date);
                        Log.i("ingredient_type",sp_ingerdient_type);
                        Log.i("ingredient_title",ig_title_dialog.getText().toString());

                        String url1 = null;
                        try {
                            url1 = "http://192.168.0.195/mike/no_barcode_check.php?FridgeID="+FridgeID
                                    +"&expiration_date="+expiration_date
                                    +"&ingredient_type="+ URLEncoder.encode(sp_ingerdient_type,"UTF-8")
                                    +"&ingredient_title="+URLEncoder.encode(ig_title_dialog.getText().toString(),"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                        class  dbManager extends AsyncTask<String,Void,String>
                        {


                            protected void onPostExecute(String data)
                            {
                                try {
//                                    JSONArray ja = new JSONArray(data);
//                                    JSONObject jo = null;

                                    Log.i("data",data);
//                                    if (data.equals("Insert into fridge contain sucess") == true)
//                                    {
//                                        Log.i("msg1","hi");
//                                        Toast.makeText(getContext(),data,Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(getContext(),"成功新增至冰箱",Toast.LENGTH_SHORT).show();
//
//                                    }
//                                    else if (data.equals("Update Contain Quantity sucess") == true)
//                                    {
//                                        Log.i("msg2","hi2");
//                                        Toast.makeText(getContext(),data,Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(getContext(),"成功更新冰箱食材數量!",Toast.LENGTH_SHORT).show();
//
//                                    }

                                    Intent intent_back = new Intent(getContext(),Fridge.class);
                                    intent_back.putExtra("memberid",mid);
                                    intent_back.putExtra("refridgeid",fid);
                                    startActivity(intent_back);
                                    Toast.makeText(getContext(),data,Toast.LENGTH_SHORT).show();


                                } catch (Exception ex) {
                                    ex.printStackTrace();
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
                        Log.i("url1",url1);
                        obj.execute(url1);

                    }
                    else{
                        Snackbar.make(v, "資料未填妥!", Snackbar.LENGTH_LONG).show();
//                        Toast.makeText(getContext(), "資料未填妥!", Toast.LENGTH_SHORT).show();
                    }
            }
        });


        return v;
    }



}
