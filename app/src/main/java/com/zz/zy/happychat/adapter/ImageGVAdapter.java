package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zz.zy.happychat.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ImageGVAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> stringList;
    public ImageGVAdapter(Context context,ArrayList<String> stringList) {
        this.context = context;
        this.stringList=stringList;
    }

    @Override
    public int getCount() {
        return stringList.size();
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
            convertView = View.inflate(context, R.layout.item_gv_image, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        Glide.with(context).load(stringList.get(position)).into(viewHolder.ivImg);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_img)
        ImageView ivImg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
