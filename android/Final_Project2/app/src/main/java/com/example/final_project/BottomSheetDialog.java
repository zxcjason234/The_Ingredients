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
import android.view.ViewOutlineProvider;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.Volley;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.SingleSpinnerListener;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.vishnusivadas.advanced_httpurlconnection.PutData;




public class BottomSheetDialog extends BottomSheetDialogFragment {

    final List<KeyPairBoolData> listArray2 = new ArrayList<>();
    List<String> Ingredient_type = new ArrayList<>();
    String type,barcode;
    ArrayList<String> IngIDList = new ArrayList<>();
    ArrayAdapter<String> IngIDAdapter;
    RequestQueue requestQueue_Type;
    RequestQueue requestQueue_barcode;
    SingleSpinnerSearch sp2;
    ImageButton bt_insert_to_db,bt_scan_scan;
    TextView tv_scan_barcode;
    String bcode_dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        setStyle(STYLE_NORMAL,R.style.BottomSheetDialogCircle);
        View v = inflater.inflate(R.layout.fragment_fragment3,
                container, false);
        RelativeLayout rl=v.findViewById(R.id.rl);
//        rl.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
//        rl.setClipToOutline(true);


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

        requestQueue_barcode = Volley.newRequestQueue(getContext());
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



        bt_scan_scan= v.findViewById(R.id.bt_scan_scan);
        bt_scan_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                scanCode_add();
            }
        });




        tv_scan_barcode= v.findViewById(R.id.ed_scan_barcode);
        EditText ig_title= v.findViewById(R.id.ig_title);


        bt_insert_to_db= v.findViewById(R.id.bt_scan_insert);
        bt_insert_to_db.setOnClickListener(new View.OnClickListener() {//新增至食材資料庫
            @Override
            public void onClick(View v) {


                String ed_ingredient_title=String.valueOf(ig_title.getText());
                String sp_ingerdient_type=String.valueOf(type);
                String expiration_date = String.valueOf(tv_date.getText());
                String FridgeID = String.valueOf(tv_scan_fridgeID.getText());
                String mid  = String.valueOf(tv_scan_UserID.getText());

                Log.i("barcode",bcode_dialog);
                Log.i("ed_ingredient_title",ed_ingredient_title);
                Log.i("sp_ingerdient_type",sp_ingerdient_type);
                Log.i("expiration_date",expiration_date);
                Log.i("FridgeID",FridgeID);
                Log.i("mid",mid);


                if(!bcode_dialog.equals("")&&!sp_ingerdient_type.equals("")&&!ed_ingredient_title.equals("")&&!expiration_date.equals("")){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[5];
                            field[0] = "barcode";
                            field[1] = "ingredient_title";
                            field[2] = "ingredient_type";
                            field[3] = "expiration_date";
                            field[4] = "FridgeID";
                            String[] data = new String[5];
                            data[0] = bcode_dialog;
                            data[1] = ed_ingredient_title;
                            data[2] = sp_ingerdient_type;
                            data[3] = expiration_date;
                            data[4] = FridgeID;
                            Log.i("FridgeID",FridgeID);
                            Log.i("bcode_dialog",bcode_dialog);
                            Log.i("ingerdient_ID_ed",ed_ingredient_title);
                            Log.i("ingerdient_type",sp_ingerdient_type);
                            Log.i("expiration_date",expiration_date);



                            PutData putData = new PutData("http://192.168.0.195/mike/barcode_check.php", "POST", field, data);//新增有條碼食材資料庫PHP
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Insert into fridge contain sucess"))
                                    {
                                        Toast.makeText(getContext(),"成功新增至冰箱",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(),Fridge.class);
                                        startActivity(intent);
                                    }
                                    else if(result.equals("Insert into ingredient sucess"))
                                    {
                                        Toast.makeText(getContext(),"成功新增至食材資料庫",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(),Fridge.class);
                                        startActivity(intent);
                                    }
                                    else if(result.equals("Update Contain Quantity sucess"))
                                    {
                                        Toast.makeText(getContext(),"成功更新此冰箱現有食材數量",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(),Fridge.class);
                                        startActivity(intent);

                                    }
                                    else {
                                        Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }else{
                        Toast.makeText(getContext(), "資料未填妥!", Toast.LENGTH_SHORT).show();
                    }
                }


        });


        return v;
    }


    private void scanCode_add(){
        IntentIntegrator integrator_add = IntentIntegrator.forSupportFragment(this);
        integrator_add.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator_add.setPrompt("請對準條碼以進行掃描");
        integrator_add.setCameraId(0);
        integrator_add.setBeepEnabled(true);
        integrator_add.setBarcodeImageEnabled(true);
        integrator_add.setOrientationLocked(true);
        integrator_add.initiateScan();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult SR = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (SR != null) {
            if (SR.getContents() != null) {
                String SC = SR.getContents();
                if (!SC.equals("")) {
                    tv_scan_barcode.setText(SC.toString());
                    bcode_dialog = SC;
                }
            }
        }else{
            super.onActivityResult(requestCode,resultCode,intent);
            tv_scan_barcode.setText("掃描出錯!請再試一次!");
        }
    }
}
