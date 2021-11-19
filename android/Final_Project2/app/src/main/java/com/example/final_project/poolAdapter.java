package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class poolAdapter extends  RecyclerView.Adapter<poolAdapter.ViewHolder> {


    poolData[] poolData;
    Context context;

    public poolAdapter(poolData[] poolData, MainActivity activity) {

        this.poolData=poolData;
        this.context=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.item_list,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final poolData poolDataList= poolData[position];

        holder.textViewTitle.setText(poolDataList.getPooltitle());
        holder.textViewData.setText(poolDataList.getPoolmembername());
        holder.texViewTime.setText(poolDataList.getPooltime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String note= poolDataList.getPoolmembername();
//                String fid=MainActivity.getfid();
                Toast.makeText(context,poolDataList.getPooltitle(),Toast.LENGTH_LONG).show();
//                Intent intent2 = new Intent(MainActivity.this,EditNote.class);
//                intent2.putExtra("edit",id2);
//                intent2.putExtra("thenote",s);
//                intent2.putExtra("member",id3);
//                startActivity(intent2);
//                finish();

            }
        });


    }

    @Override
    public int getItemCount() {
        return poolData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle;
        TextView textViewData;
        TextView texViewTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.textName);
            textViewData=itemView.findViewById(R.id.textdata);
            texViewTime=itemView.findViewById(R.id.texttime);


        }
    }
}
