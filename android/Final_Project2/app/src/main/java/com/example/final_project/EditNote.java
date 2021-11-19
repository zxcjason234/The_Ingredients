package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class EditNote extends AppCompatActivity {

    TextView txtEditFamilyNote, txt_member_id_main;
    Button btnAddNote, btnGoBackToNote;
    EditText Add_Note;
    String dd, gg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.edit_note); // 這個java與哪個xml連結再一起

        TextView tbtv=(TextView)findViewById(R.id.textView2); //toolbar上面的textview
        ImageView btn_back = (ImageView)findViewById(R.id.imageView); //圖片的按鈕
        tbtv.setText("編輯備忘錄");



//        btnAddNote = (Button)findViewById(R.id.btnAddNote) ;
        ImageView AddNote = (ImageView)findViewById(R.id.AddNote);
//        btnGoBackToNote = (Button)findViewById(R.id.btnGoBackToNote);
        Add_Note = (EditText)findViewById(R.id.Add_Note);
        txtEditFamilyNote = (TextView)findViewById(R.id.txtEditFamilyNote);
        txt_member_id_main = (TextView)findViewById(R.id.txt_member_id_main); // 我的家庭備忘錄還是要去接memberid，不然按完加號或是返回按鈕之後，memberid回不去系統就會壞掉

        // 接收登入者的id
        String NoteBefore = getIntent().getStringExtra("thenote");
        String mid = getIntent().getStringExtra("mid");
        Add_Note.setText(NoteBefore);


        AddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = txtEditFamilyNote.getText().toString();
                String y = Add_Note.getText().toString();

                if(!x.equals("")){
                    Handler handler = new Handler(Looper.myLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("mid",mid);
                            String[] field = new String[3];
                            field[0] = "Fridge_ID";
                            field[1] = "note";
                            field[2] ="member_id";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = x;
                            data[1] = y;
                            data[2] = mid;


                            PutData putData = new PutData("http://192.168.0.195/mine/edit_note.php","POST", field, data);
                            if(putData.startPut()){
                                Log.i("a","a");
                                if(putData.onComplete()){
                                    Log.i("b","b");
                                    String result = putData.getResult();
                                    Log.e("result",result);
                                    if(result.equals("update sucess")){
                                        Toast.makeText(getApplicationContext(),"更新成功",Toast.LENGTH_SHORT).show();
                                        //fetch_data_into_array(Edit_FamilyNote_lv);
                                        String id2 = txtEditFamilyNote.getText().toString();
                                        String id3 = txt_member_id_main.getText().toString();
                                        Intent intent3 = new Intent(getApplicationContext(),MainActivity.class);
                                        intent3.putExtra("refridgeid",id2);
                                        intent3.putExtra("memberid",id3); // 我這邊把memberid傳回去了，雖然我這個功能用不到
                                        startActivity(intent3);
                                        finish();
                                    } else{
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Log.e("else_result",result);
                                    }
                                }
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"Data incomplete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id2 = txtEditFamilyNote.getText().toString();
                String id3 = txt_member_id_main.getText().toString();
                Intent intent3 = new Intent(getApplicationContext(),MainActivity.class);
                intent3.putExtra("refridgeid",id2);
                intent3.putExtra("memberid",id3); // 我這邊把memberid傳回去了，雖然我這個功能用不到
                startActivity(intent3);
                finish();
            }
        });

        Intent intent = getIntent();
        dd = intent.getStringExtra("edit");
        gg = intent.getStringExtra("member"); // 這個叫gg的從MainActivity抓到了memberid的內容
        txtEditFamilyNote.setText(dd);
        txt_member_id_main.setText(gg); // 我把從從MainActivity抓到的memberid內容設定到這個名字也叫txt_member_id_main得textview裡
        // 這樣我不管是按返回，還是加號，回到上一頁的時候，memberid都不會因此消失，系統就可以正常運行

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Fridge);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.Fridge:
                        String str = txt_member_id_main.getText().toString();//讓別人吃要打的
                        Intent intent = new Intent(getApplicationContext(), Fridge.class);
                        intent.putExtra("memberid",str);//讓別人吃要打的

                        String fri = txtEditFamilyNote.getText().toString();//讓別人吃要打的
                        intent.putExtra("refridgeid",fri);//讓別人吃要打的
                        startActivity(intent);
//                        startActivity(new Intent(getApplicationContext()
//                                ,Fridge.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.home:
                        String hstr = txt_member_id_main.getText().toString();//讓別人吃要打的
                        Intent hintent = new Intent(getApplicationContext(), MainActivity.class);
                        hintent.putExtra("memberid",hstr);//讓別人吃要打的

                        String hfri = txtEditFamilyNote.getText().toString();//讓別人吃要打的
                        hintent.putExtra("refridgeid",hfri);//讓別人吃要打的
                        startActivity(hintent);
//                        startActivity(new Intent(getApplicationContext()
//                                ,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.shopping_list:
                        String sstr = txt_member_id_main.getText().toString();//讓別人吃要打的
                        Intent sintent = new Intent(getApplicationContext(), shopping_list.class);
                        sintent.putExtra("memberid",sstr);//讓別人吃要打的

                        String sfri = txtEditFamilyNote.getText().toString();//讓別人吃要打的
                        sintent.putExtra("refridgeid",sfri);//讓別人吃要打的
                        startActivity(sintent);
//                        startActivity(new Intent(getApplicationContext()
//                                ,shopping_list.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.User:
                        String ustr = txt_member_id_main.getText().toString();//讓別人吃要打的
                        Intent uintent = new Intent(getApplicationContext(), User_setting.class);
                        uintent.putExtra("memberid",ustr);//讓別人吃要打的

                        String ufri = txtEditFamilyNote.getText().toString();//讓別人吃要打的
                        uintent.putExtra("refridgeid",ufri);//讓別人吃要打的
                        startActivity(uintent);
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });

    } // End of onCreate

} // End of class
