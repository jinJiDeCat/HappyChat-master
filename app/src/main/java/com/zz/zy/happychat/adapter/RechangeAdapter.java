package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.ChatCoin;
import com.zz.zy.happychat.mvp.model.Three;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RechangeAdapter extends BaseAdapter {
    private Context context;
    private List<Three.DataBean> chatCoinList;
    private int clickPosition=-1;
    public RechangeAdapter(Context context, List<Three.DataBean> chatCoinList) {
        this.context = context;
        this.chatCoinList = chatCoinList;
    }

    public int getClickPosition() {
        return clickPosition;
    }

    public void setClickPosition(int clickPosition) {
        this.clickPosition = clickPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return chatCoinList.size();
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
            convertView = View.inflate(context, R.layout.item_lv_rechange, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.tvNumber.setText(chatCoinList.get(position).getCnum());
        viewHolder.tvPresent.setText("赠送"+chatCoinList.get(position).getGive()+"聊币");
        viewHolder.tvPrice.setText(chatCoinList.get(position).getCprice()+"元");
        if(position==clickPosition){
            convertView.setBackgroundColor(0xff7FCDE9);
        }else{
            convertView.setBackgroundColor(0xffF1F1F1);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_present)
        TextView tvPresent;
        @BindView(R.id.tv_price)
        TextView tvPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
