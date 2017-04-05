package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.BlackList;
import com.zz.zy.happychat.mvp.model.PersonList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class BlackAdapter extends RecyclerView.Adapter<BlackAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<BlackList> blackLists;
    private OnItemClickListener onItemClickListener;
    public BlackAdapter(Context context, List<BlackList> blackLists) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.blackLists = blackLists;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<BlackList> getPersonLists() {
        return blackLists;
    }

    public void setPersonLists(List<BlackList> blackLists) {
        this.blackLists = blackLists;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(layoutInflater.inflate(R.layout.item_lv_fan, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });
        holder.tvNickname.setText(blackLists.get(position).getNickname());
        holder.tvId.setText("聊天号:"+blackLists.get(position).getKid());
        Glide.with(context).load(MyApplication.BaseImage+blackLists.get(position).getPict()).into(holder.ivIcon);
        if(blackLists.get(position).getSex().equals("男")){
            Glide.with(context).load(R.drawable.fennanxing).into(holder.ivSex);
        }else{
            Glide.with(context).load(R.drawable.fennanxing).into(holder.ivSex);
        }
    }

    @Override
    public int getItemCount() {
        return blackLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        CircleImageView ivIcon;
        @BindView(R.id.tv_nickname)
        TextView tvNickname;
        @BindView(R.id.iv_sex)
        ImageView ivSex;
        @BindView(R.id.tv_id)
        TextView tvId;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public interface OnItemClickListener{
        public void onItemClick(int position);
    }
}
