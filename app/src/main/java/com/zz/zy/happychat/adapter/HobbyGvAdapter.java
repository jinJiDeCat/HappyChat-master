package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zz.zy.happychat.R;


public class HobbyGvAdapter extends RecyclerView.Adapter<HobbyGvAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private String[] hobbies;
    public HobbyGvAdapter(Context context,String[] hobbies){
        this.context=context;
        this.hobbies=hobbies;
        layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(layoutInflater.inflate(R.layout.item_gv_hobby,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(hobbies[position]);
    }

    @Override
    public int getItemCount() {
        return hobbies.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv= (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
