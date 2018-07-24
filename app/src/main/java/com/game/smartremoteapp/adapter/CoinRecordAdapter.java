package com.game.smartremoteapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.bean.CoinListBean;

import java.util.List;

/**
 * Created by mi on 2018/5/31.
 */

public class CoinRecordAdapter extends RecyclerView.Adapter<CoinRecordAdapter.MyViewHolder> {

    private Context mContext;
    private List<CoinListBean.CoinBean> mDatas;
    private LayoutInflater mInflater;

    public CoinRecordAdapter(Context context, List<CoinListBean.CoinBean> datas){
        this.mContext=context;
        this.mDatas=datas;
        mInflater=LayoutInflater.from(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.coinrecord_item,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CoinListBean.CoinBean bean=mDatas.get(position);
        holder.periodsNum_tv.setText(bean.getId());
        holder.cost_gold_tv.setText("-"+bean.getCostGold());
        holder.return_gold_tv.setText("+"+bean.getReturnGold());

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView periodsNum_tv,cost_gold_tv,return_gold_tv;

        public MyViewHolder(View view){
            super(view);
            periodsNum_tv= (TextView) view.findViewById(R.id.periodsNum_tv);//期号
            cost_gold_tv= (TextView) view.findViewById(R.id.cost_gold_tv);
            return_gold_tv= (TextView) view.findViewById(R.id.return_gold_tv);

        }

    }

    public void notify(List<CoinListBean.CoinBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }

}
