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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Calendar;

public class fragment1 extends Fragment {

    View view;
    String scanned_title,barcode, FridgeID, expiration_date;
    TextView tv_scan_title,tv_scan_barcode;
    ImageButton bt_scan,bt_insert_to_db;
    LinearLayout LY_scan_day,lin_scan_insert,lin_scan;
    TextView textView19;
    String bcode;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        tv_scan_title = view.findViewById(R.id.tv_scan_title);
        tv_scan_barcode = view.findViewById(R.id.tv_scan_barcode);
        tv_scan_title   = view.findViewById(R.id.tv_scan_title);
        LY_scan_day = view.findViewById(R.id.LY_scan_day);
        lin_scan_insert = view.findViewById(R.id.lin_scan_insert);
        lin_scan = view.findViewById(R.id.lin_scan);
        textView19 = view.findViewById(R.id.textView19);

        //點選沒有此食材
        TextView OpenBottomSheet = view.findViewById(R.id.txtres_scan);
        OpenBottomSheet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        BottomSheetDialog bottomSheet = new BottomSheetDialog();
                        Bundle bundle = new Bundle();
//                        String myMessage = "Stack Overflow is cool!";
                        bundle.putString("fid", test1.getfid() );
                        bundle.putString("mid", test1.getmid() );
                        bottomSheet.setArguments(bundle);
                        bottomSheet.show(getChildFragmentManager(),
                                "ModalBottomSheet");
                    }
                });


        //點掃條碼
        bt_scan= view.findViewById(R.id.bt_scan_scan);
        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode_scan();
            }
        });//第一頁掃描



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


        //點新增食材
        bt_insert_to_db= view.findViewById(R.id.bt_scan_insert);
        bt_insert_to_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                scanned_title = String.valueOf(tv_scan_title.getText());
                expiration_date = String.valueOf(tv_date.getText());
                String FridgeID=test1.getfid();
                String mid = test1.getmid();

//                Log.i("scanned_title",scanned_title);
//                Log.i("expiration_date",expiration_date);
//                Log.i("FridgeID",FridgeID);
//                Log.i("mid",mid);

                if (bcode.equals("") || expiration_date.equals("") || scanned_title.equals(""))
                {
                    Toast.makeText(getContext(), "自料未填寫完整", Toast.LENGTH_SHORT).show();

                }
                else {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("scanned_title",scanned_title);
                            Log.i("expiration_date",expiration_date);
                            Log.i("FridgeID",FridgeID);
                            Log.i("mid",mid);
                            Log.i("bcode",bcode);

                            String[] field = new String[3];
                            field[0] = "barcode";
                            field[1] = "FridgeID";
                            field[2] = "expiration_date";
                            String[] data = new String[3];
                            data[0] = bcode;
                            data[1] = FridgeID;
                            data[2] = expiration_date;
                            PutData putData = new PutData("http://192.168.0.195/mike/SearchBarcode_insert.php", "POST", field, data);//條碼有掃到東西
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("該食材冰箱數量已更新!")||result.equals("新增至冰箱成功!")||result.equals("UPDATE sucess")||result.equals("INSERT sucess")) {
//                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(),Fridge.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }

            }
        });


        return view;
    }



    private void scanCode_scan(){
//        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("請對準條碼以進行掃描");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult SR = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (SR != null) {
            if (SR.getContents() != null) {
                String SC = SR.getContents();
                if (!SC.equals("")) {

                    tv_scan_barcode.setText(SC);
                    bcode=SC;
                    fetch_title_into_textview(tv_scan_title);

                }
            }
        }else{
            super.onActivityResult(requestCode,resultCode,intent);
            tv_scan_barcode.setText("掃描出錯!請再試一次!");
        }
    }



    public void fetch_title_into_textview(View view) {

        String barcode;
        barcode = String.valueOf(tv_scan_barcode.getText());


        lin_scan.setVisibility(View.VISIBLE);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {


                String[] field = new String[1];
                field[0] = "barcode";
                String[] data = new String[1];
                data[0] = barcode;

                PutData putData = new PutData("http://192.168.0.195/mike/scanBarcode.php","POST", field, data);
                if(putData.startPut()){
                    if(putData.onComplete()){
                        String result = putData.getResult();
                        result=result.replace("\"", "");
                        if(!result.equals("No Data")){
//                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
//                            fetch_title_into_textview(tv_scan_title);
                            tv_scan_title.setText(result);
                            LY_scan_day.setVisibility(View.VISIBLE);
                            textView19.setVisibility(View.VISIBLE);
                            lin_scan_insert.setVisibility(View.VISIBLE);
                        }
                        else if(result.equals("No Data"))
                        {
                            tv_scan_title.setText("沒有此食材");
                        }
                        else{
//                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }




}