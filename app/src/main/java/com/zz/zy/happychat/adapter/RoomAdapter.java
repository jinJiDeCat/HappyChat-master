package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.CommendData;
import com.zz.zy.happychat.mvp.model.Room;
import com.zz.zy.happychat.view.SquareImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {

    private Context context;
    private List<CommendData.InfoBean> roomList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public RoomAdapter(Context context, List<CommendData.InfoBean> rooms) {
        this.context = context;
        this.roomList = rooms;
        layoutInflater = LayoutInflater.from(context);
    }
    public void setDataChange(List<CommendData.InfoBean> rooms){
        this.roomList=rooms;
        notifyDataSetChanged();
    }

    public List<CommendData.InfoBean> getRoomList() {
        return roomList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(layoutInflater.inflate(R.layout.item_gv_room, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.ratingBar.setRating(Float.parseFloat(roomList.get(position).getGrade()));
        holder.ivVip.setVisibility(roomList.get(position).getMember().equals("0")?View.GONE:View.VISIBLE);
        Glide.with(context).load(MyApplication.BaseImage+roomList.get(position).getPict()).into(holder.ivIcon);
        Glide.with(context).load(MyApplication.BaseImage+roomList.get(position).getPict()).into(holder.ivImage);
        holder.tvName.setText(roomList.get(position).getNickname());
        holder.tvStar.setText(roomList.get(position).getGrade()+"分");
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        if(roomList.get(position).getOnline().equals("1")){
            holder.ivStatus.setImageResource(roomList.get(position).getState().equals("2")?R.drawable.zaixian:R.drawable.lixian);
            holder.tvStatus.setText(roomList.get(position).getState().equals("2")?"在线":"离线");
            holder.tvStatus.setTextColor(roomList.get(position).getState().equals("2")?Color.GREEN:Color.GRAY);
        }else{
            holder.ivStatus.setImageResource(R.drawable.manglu);
            holder.tvStatus.setText("忙碌");
            holder.tvStatus.setTextColor(Color.RED);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View parent;
        @BindView(R.id.iv_image)
        SquareImageView ivImage;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.iv_icon)
        CircleImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_vip)
        ImageView ivVip;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.tv_star)
        TextView tvStar;
        public MyViewHolder(View itemView) {
            super(itemView);
            parent = itemView;
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
