package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.Hobby;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HobbyAdapter extends BaseAdapter {
    private Context context;
    private List<Hobby> hobbies;
    private Map<String,String> indexs;
    public HobbyAdapter(Context context, List<Hobby> hobbies) {
        this.context = context;
        this.hobbies = hobbies;
    }

    @Override
    public int getCount() {
        return hobbies.size();
    }

    @Override
    public Object getItem(int position) {
        return hobbies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_lv_hobby, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        if(TextUtils.isEmpty(indexs.get(position+""))){
            viewHolder.iv.setImageResource(R.drawable.bg_white);
        }else{
            viewHolder.iv.setImageResource(R.drawable.dui2);
        }
        viewHolder.tv.setText(hobbies.get(position).getHobby());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv)
        TextView tv;
        @BindView(R.id.iv)
        ImageView iv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public void setIndex(Map<String,String> indexs) {
        this.indexs = indexs;
        notifyDataSetChanged();
    }
}
