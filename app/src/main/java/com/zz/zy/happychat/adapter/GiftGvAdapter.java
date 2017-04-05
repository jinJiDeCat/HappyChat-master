package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.GiftPrice;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GiftGvAdapter extends BaseAdapter {
    private Context context;
    private List<GiftPrice> giftPriceList;

    public GiftGvAdapter(Context context, List<GiftPrice> giftPriceList) {
        this.context = context;
        this.giftPriceList = giftPriceList;
    }

    @Override
    public int getCount() {
        return 8;
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
            convertView = View.inflate(context, R.layout.item_gv_gift, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        viewHolder= (ViewHolder) convertView.getTag();
        if(position<(giftPriceList.size())){
            viewHolder.tvNumber.setText(giftPriceList.get(position).getGname()+"\n"+giftPriceList.get(position).getGprice()+"聊币");
            Glide.with(context).load(giftPriceList.get(position).getGpic()).into(viewHolder.ivImage);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_number)
        TextView tvNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
