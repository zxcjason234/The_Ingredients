package com.example.final_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.SingleSpinnerListener;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class fragment2 extends Fragment {

    View view;

    SingleSpinnerSearch sp1;
    final List<KeyPairBoolData> listArray1 = new ArrayList<>();
    List<String> Ingredient_title = new ArrayList<>();
    ArrayList<String> IngTitleList = new ArrayList<>();
    ArrayAdapter<String> IngTitleAdapter;
    RequestQueue requestQueue_Title;
    String title;

    ImageButton bt_insert_to_db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment2, container, false);

        TextView OpenBottomSheet = view.findViewById(R.id.txtres_scan);
        OpenBottomSheet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        BottomSheetDialog_1 bottomSheet = new BottomSheetDialog_1();
                        Bundle bundle = new Bundle();
//                        String myMessage = "Stack Overflow is cool!";
                        bundle.putString("fid", test1.getfid() );
                        bundle.putString("mid", test1.getmid() );
                        bottomSheet.setArguments(bundle);
                        bottomSheet.show(getChildFragmentManager(),
                                "ModalBottomSheet");
                    }
                });



        sp1 = view.findViewById(R.id.sp_scan_title);
        sp1.setColorseparation(true);
        sp1.setSearchEnabled(true);
        sp1.setSearchHint("選擇食材名稱");

        requestQueue_Title = Volley.newRequestQueue(getContext());
        String url_title = "http://192.168.0.195/mike/select_ingredient.php";
        JsonObjectRequest jsonObjectRequest_Title = new JsonObjectRequest(Request.Method.POST,
                url_title, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("ingredient");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String str_titleID = jsonObject.optString("ingredient_title");
                        IngTitleList.add(str_titleID);
//                        IngTitleAdapter = new ArrayAdapter<>(ingredient_scan.this,
//                                android.R.layout.simple_spinner_item, IngTitleList);
//                        IngTitleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        sp1.setAdapter(IngTitleAdapter);
                        Ingredient_title.add(str_titleID);
                    }
                    for (int i = 0; i < Ingredient_title.size(); i++) {
                        KeyPairBoolData h = new KeyPairBoolData();

                        h.setId(i + 1);
                        h.setName(Ingredient_title.get(i));
                        h.setSelected(false);
                        listArray1.add(h);
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
        requestQueue_Title.add(jsonObjectRequest_Title);


        sp1.setItems(listArray1, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                title=selectedItem.getName();
            }

            @Override
            public void onClear() {
                Toast.makeText(getContext(), "Cleared Selected Item", Toast.LENGTH_SHORT).show();
            }
        });


        //抓選取日期
        ImageButton bt_date=view.findViewById(R.id.bt_scan_day);
        TextView tv_date= view.findViewById(R.id.tv_scan_day);
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


        bt_insert_to_db= view.findViewById(R.id.bt_scan_insert);
        bt_insert_to_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                String ingerdient_ID_sp = String.valueOf(title);
                String expiration_date = String.valueOf(tv_date.getText());
                String FridgeID=test1.getfid();
                String mid = test1.getmid();
                if (!ingerdient_ID_sp.equals("") && !expiration_date.equals(""))
                {

                    Log.i("ingerdient_ID_sp",ingerdient_ID_sp);
                    Log.i("expiration_date",expiration_date);
                    Log.i("FridgeID",FridgeID);
                    Log.i("mid",mid);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[3];
                            field[0] = "ingredient_title";
                            field[1] = "FridgeID";
                            field[2] = "expiration_date";
                            String[] data = new String[3];
                            data[0] = ingerdient_ID_sp;
                            data[1] = FridgeID;
                            data[2] = expiration_date;
                            PutData putData = new PutData("http://192.168.0.195/mike/SearchID_insert.php", "POST", field, data);//沒有掃到東西 直接選食材新增
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("該食材冰箱數量已更新!")||result.equals("新增至冰箱成功!")||result.equals("UPDATE sucess")||result.equals("INSERT sucess")) {
                                        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(),Fridge.class);
                                        intent.putExtra("memberid",mid);
                                        intent.putExtra("refridgeid",FridgeID);
                                        startActivity(intent);
                                    } else {

                                        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
                else {
                    Snackbar.make(view, "資料未填妥!", Snackbar.LENGTH_LONG).show();
//                    Toast.makeText(getContext(), "資料未填妥!", Toast.LENGTH_SHORT).show();

                }

            }

        });

        return view;
    }
}