package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zz.zy.happychat.MyApplication;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.GiftList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GiftRecyclerAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<GiftList> giftLists;

    public GiftRecyclerAdapter(Context context, List<GiftList> giftLists) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.giftLists = giftLists;
    }


    @Override
    public int getCount() {
        return giftLists.size() > 4 ? 4 : giftLists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(context, R.layout.item_gv_gift, null);
            view.setTag(new ViewHolder(view));
        }
        viewHolder= (ViewHolder) view.getTag();
        viewHolder.tvNumber.setText("x1");
        Glide.with(context).load(MyApplication.BaseImage+giftLists.get(i).getGpic()).into(viewHolder.ivImage);
        return view;
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
