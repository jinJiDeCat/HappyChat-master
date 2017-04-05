package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.TimeUtil;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.NearList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearAdapter extends RecyclerView.Adapter<NearAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<NearList> nearLists;
    private OnItemLongClickListener onItemLongClickListener;

    public NearAdapter(Context context, List<NearList> nearLists) {
        this.context = context;
        this.nearLists = nearLists;
        layoutInflater = LayoutInflater.from(context);
    }

    public List<NearList> getNearLists() {
        return nearLists;
    }

    public void setNearLists(List<NearList> nearLists) {
        this.nearLists = nearLists;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(layoutInflater.inflate(R.layout.item_lv_near, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemLongClickListener.OnItemLongClick(position);
                return false;
            }
        });
        Glide.with(context).load(MyApplication.BaseImage+nearLists.get(position).getPict()).into(holder.ivIcon);
        holder.tvSex.setText(nearLists.get(position).getSex());
        holder.ivSex.setImageResource(nearLists.get(position).getSex().equals("男")?R.drawable.bainanxing:R.drawable.bainvxing);
        holder.tvNickname.setText(nearLists.get(position).getNickname());
        Date date=new Date(Long.parseLong(nearLists.get(position).getEnd())*1000);

        holder.tvTime.setText(new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA).format(date));
        holder.tvDialogTime.setText("通话时长:"+nearLists.get(position).getLongtime()+"分钟");
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public int getItemCount() {
        return nearLists.size()>=20?20:nearLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvNickname;
        ImageView ivSex;
        TextView tvSex;
        TextView tvDialogTime;
        TextView tvTime;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivIcon= (ImageView) itemView.findViewById(R.id.iv_icon);
            tvNickname= (TextView) itemView.findViewById(R.id.tv_nickname);
            ivSex= (ImageView) itemView.findViewById(R.id.iv_sex);
            tvSex= (TextView) itemView.findViewById(R.id.tv_sex);
            tvDialogTime= (TextView) itemView.findViewById(R.id.tv_DialogTime);
            tvTime= (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(int position);
    }
}
