package com.example.final_project;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class test1 extends AppCompatActivity {

    private static String mid,fid;
    private TabLayout tablayout1;
    private ViewPager viewPager1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test1);
        TextView tbtv=(TextView)findViewById(R.id.textView2); //toolbar上面的textview
        tbtv.setText("新增食材");
        ImageView btn_back = (ImageView)findViewById(R.id.imageView); //圖片的按鈕



        tablayout1=findViewById(R.id.tablayout1);
        viewPager1=findViewById(R.id.viewPager1);

        tablayout1.setupWithViewPager(viewPager1);


        String username = getIntent().getStringExtra("memberid");//接收前一頁給的東西
        TextView txtloginName = (TextView) findViewById(R.id.tv_scan_UserID);//接收前一頁給的東西
        txtloginName.setText(username);//接收前一頁給的東西
        mid = username;

        String refridgeid = getIntent().getStringExtra("refridgeid");//接收前一頁給的東西
        TextView txtrefridgeid = (TextView) findViewById(R.id.tv_scan_fridgeID);//接收前一頁給的東西
        txtrefridgeid.setText(refridgeid);//接收前一頁給的東西
        fid=refridgeid;


        vpadapter vpadapter = new vpadapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpadapter.addFragment(new fragment1(),"掃條碼新增");
        vpadapter.addFragment(new fragment2(),"手動新增");
        viewPager1.setAdapter(vpadapter);

//        tablayout1.getTabAt(0).setIcon(R.drawable.ic_barcode_2);
//        tablayout1.getTabAt(1).setIcon(R.drawable.ic_instructions);
        tablayout1.getTabAt(0).setCustomView(getview(R.drawable.ic_barcode_2));
        tablayout1.getTabAt(1).setCustomView(getview(R.drawable.ic_instructions));

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id2 = txtrefridgeid.getText().toString();
                String id3 = txtloginName.getText().toString();
                Intent intent3 = new Intent(getApplicationContext(),Fridge.class);
                intent3.putExtra("refridgeid",id2);
                intent3.putExtra("memberid",id3); // 我這邊把memberid傳回去了，雖然我這個功能用不到
                startActivity(intent3);
                finish();
            }
        });

    }

    public static String getmid()
    {
        return mid;
    }


    public static String getfid()
    {
        return fid;
    }


    //創建Tablayout的CustomView
    private View getview( int icon) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
//        TextView tv = view.findViewById(R.id.tv);
        ImageView iv = view.findViewById(R.id.iv);
//        tv.setText(title);
        iv.setImageResource(icon);
        return view;
    }






}
