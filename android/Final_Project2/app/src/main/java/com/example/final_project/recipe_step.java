package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class recipe_step extends AppCompatActivity
{
    String STR_STEP;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_step);
        ImageView btn_back = (ImageView)findViewById(R.id.imageView);
        TextView tbtv=(TextView)findViewById(R.id.textView2);
        tbtv.setText("步驟");
//        TextView tv_step = (TextView)findViewById(R.id.tv_step);
        Intent intent = getIntent();
        STR_STEP= intent.getStringExtra("step");
//        tv_step.setText(STR_STEP);


        btn_back.setOnClickListener(view -> {
//            Intent i = new Intent(this, dishes_info.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(i);
            finish();
        });





    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Toast.makeText(this, "Toast 基本用法", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, dishes_info.class);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
//                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch(item.getItemId()){
//            case android.R.id.home: //對用戶按home icon的處理，本例只需關閉activity，就可返回上一activity，即主activity。
//
//                finish();
//                return true;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
