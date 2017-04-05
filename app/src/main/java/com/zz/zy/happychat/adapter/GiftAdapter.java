package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.GiftList;
import com.zz.zy.happychat.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GiftAdapter extends BaseAdapter {
    private Context context;
    private List<GiftList> giftLists;
    public GiftAdapter(Context context,List<GiftList> giftLists) {
        this.context = context;
        this.giftLists=giftLists;
    }

    @Override
    public int getCount() {
        return giftLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_lv_gift, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.tvName.setText(giftLists.get(position).getNickname());
        viewHolder.tvTime.setText(TimeUtils.UnixToTime("yyyy-MM-dd HH:mm",Long.parseLong(giftLists.get(position).getTime())));
        viewHolder.ivVip.setVisibility(giftLists.get(position).getMember().equals("0")?View.GONE:View.VISIBLE);
        viewHolder.tvPicname.setText(giftLists.get(position).getGname()+"x1");
        viewHolder.tvScore.setText("魅力值+"+giftLists.get(position).getGcharm());
        Glide.with(context).load(MyApplication.BaseImage+giftLists.get(position).getGpic()).into(viewHolder.ivImg);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_vip)
        ImageView ivVip;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_picname)
        TextView tvPicname;
        @BindView(R.id.tv_score)
        TextView tvScore;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
