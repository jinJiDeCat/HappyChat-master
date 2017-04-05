package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wuxiaolong.androidutils.library.TimeUtil;
import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.History;
import com.zz.zy.happychat.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private List<History> historyList;

    public HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @Override
    public int getCount() {
        return historyList.size();
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
            convertView = View.inflate(context, R.layout.item_lv_history, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.tvTime.setText(TimeUtils.UnixToTime("yyyy-MM-dd",Long.parseLong(historyList.get(position).getWtime())));
        viewHolder.tvMoney.setText(historyList.get(position).getCoinnumber()+"元");
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_money)
        TextView tvMoney;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
