package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.Reason;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReasonAdapter extends BaseAdapter {
    private List<Reason> reasons;
    private Context context;
    private int clickPosition=-1;
    public ReasonAdapter(Context context, List<Reason> reasons) {
        this.context = context;
        this.reasons = reasons;
    }

    @Override
    public int getCount() {
        return reasons.size();
    }

    @Override
    public Object getItem(int position) {
        return reasons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_lv_reason, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.tvReason.setText(reasons.get(position).getIllustration());
        if(position==clickPosition){
            convertView.setBackgroundColor(Color.GRAY);
        }else{
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    public int getClickPosition() {
        return clickPosition;
    }

    public void setClickPosition(int clickPosition) {
        this.clickPosition = clickPosition;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.tv_reason)
        TextView tvReason;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
