package com.game.smartremoteapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.bean.BetRecordBean;

import java.util.List;

/**
 * Created by yincong on 2017/12/6 17:03
 * 修改人：
 * 修改时间：
 * 类描述：
 */
public class BetRecordAdapter extends RecyclerView.Adapter<BetRecordAdapter.MyViewHolder> {

    private Context mContext;
    private List<BetRecordBean.DataListBean> mDatas;
    private LayoutInflater mInflater;

    public BetRecordAdapter(Context context, List<BetRecordBean.DataListBean>datas){
        this.mContext=context;
        this.mDatas=datas;
        mInflater=LayoutInflater.from(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.betrecord_item,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            BetRecordBean.DataListBean bean=mDatas.get(position);
                holder.amount_tv.setText(String.valueOf(bean.getSETTLEMENT_GOLD()));
                String s=bean.getGUESS_ID();
                //holder.periodsNum_tv.setText(s.substring(5,12));
                holder.periodsNum_tv.setText(s);
                //1 抓中   0没抓中   -流局判断标识，有-表示流局，无-标识不是流局
                holder.results_tv.setText(bean.getGUESS_TYPE().replace("-",""));
                holder.bettingResults_tv.setText(bean.getGUESS_KEY());

                if (bean.getGUESS_KEY().equals(bean.getGUESS_TYPE().replace("-",""))){
                    holder.bettingResults_tv1.setText("/对");
                    holder.bettingResults_tv.setTextColor(mContext.getResources().getColor(R.color.betrecordcolor1));
                    holder.bettingResults_tv1.setTextColor(mContext.getResources().getColor(R.color.betrecordcolor1));
                }else {
                    holder.bettingResults_tv1.setText("/错");
                    holder.bettingResults_tv.setTextColor(mContext.getResources().getColor(R.color.betrecordcolor));
                    holder.bettingResults_tv1.setTextColor(mContext.getResources().getColor(R.color.betrecordcolor));
                }

//        else if(bean.getGUESS_TYPE().contains("-")) {
//            holder.bettingResults_tv1.setText("/流局");
//            holder.bettingResults_tv.setTextColor(mContext.getResources().getColor(R.color.apptheme_bg));
//            holder.bettingResults_tv1.setTextColor(mContext.getResources().getColor(R.color.apptheme_bg));
//        }


            }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView periodsNum_tv,results_tv,bettingResults_tv,bettingResults_tv1,amount_tv,guessnum_tv;

        public MyViewHolder(View view){
            super(view);
            periodsNum_tv= (TextView) view.findViewById(R.id.periodsNum_tv);//期号
            results_tv= (TextView) view.findViewById(R.id.results_tv);//抓取结果
            bettingResults_tv= (TextView) view.findViewById(R.id.bettingResults_tv);//投注结果
            amount_tv= (TextView) view.findViewById(R.id.amount_tv);//我的奖金
            bettingResults_tv1= (TextView) view.findViewById(R.id.bettingResults_tv1);
            guessnum_tv= (TextView) view.findViewById(R.id.guessnum_tv);
        }

    }

    public void notify(List<BetRecordBean.DataListBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }


}

