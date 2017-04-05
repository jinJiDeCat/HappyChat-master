package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.Job;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JobAdapter extends BaseAdapter {
    private List<Job> jobs;
    private Context context;

    public JobAdapter(Context context, List<Job> jobs) {
        this.context = context;
        this.jobs = jobs;
    }

    @Override
    public int getCount() {
        return jobs.size();
    }

    @Override
    public Object getItem(int position) {
        return jobs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_lv_job, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.tvJob.setText(jobs.get(position).getProfession());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_job)
        TextView tvJob;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
