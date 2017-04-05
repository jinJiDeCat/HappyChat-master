package com.zz.zy.happychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zz.zy.happychat.R;
import com.zz.zy.happychat.mvp.model.GiftPrice;

import java.util.ArrayList;
import java.util.List;

public class GiftPagerAdapter extends RecyclerView.Adapter<GiftPagerAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;

    private List<GiftPrice> giftPriceList;
    private OnItemClickListener onItemClickListener;
    public GiftPagerAdapter(Context context,List<GiftPrice> giftPriceList){
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
        this.giftPriceList=giftPriceList;
    }
    //    private String[] names = {
//            "新房",
//            "买二手房",
//            "卖二手房",
//            "租房",
//            "资讯",
//            "经纪人",
//            "中介公司",
//            "房贷计算器"
////            ,
////            "问题",
////            "查房价"
//    };
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.item_recyclerpager,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        List<GiftPrice> giftPrices=new ArrayList<>();
        for (int i = (position)*8; i < (giftPriceList.size()>=((position+1)*8)?(position+1)*8:giftPriceList.size()); i++) {
            giftPrices.add(giftPriceList.get(i));
        }
        holder.gv_gift.setAdapter(new GiftGvAdapter(context,giftPrices));
        holder.gv_gift.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onItemClickListener.onItemClick(position,i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return (int) Math.ceil(giftPriceList.size()/8.0);
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        GridView gv_gift;
        public MyViewHolder(View itemView) {
            super(itemView);
            gv_gift= (GridView) itemView.findViewById(R.id.gv_gift);
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(int page,int position);
    }
}
