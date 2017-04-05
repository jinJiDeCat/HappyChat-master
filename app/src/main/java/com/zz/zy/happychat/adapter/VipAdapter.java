package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.VipPrice;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VipAdapter extends BaseAdapter {
    private Context context;
    private List<VipPrice> vipPrices;
    private int clickItem=-1;
    public VipAdapter(Context context, List<VipPrice> vipPrices) {
        this.context = context;
        this.vipPrices = vipPrices;
    }

    public List<VipPrice> getVipPrices() {
        return vipPrices;
    }

    public void setVipPrices(List<VipPrice> vipPrices) {
        this.vipPrices = vipPrices;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return vipPrices.size();
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
            convertView = View.inflate(context, R.layout.item_lv_vip, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.tvName.setText(vipPrices.get(position).getMlong()+"个月");
        viewHolder.tvMoney.setText(vipPrices.get(position).getMprice()+"聊币");
        if(position==clickItem){
            convertView.setBackgroundColor(Color.GRAY);
        }else{
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    public int getClickItem() {
        return clickItem;
    }

    public void setClickItem(int clickItem) {
        this.clickItem = clickItem;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_money)
        TextView tvMoney;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
