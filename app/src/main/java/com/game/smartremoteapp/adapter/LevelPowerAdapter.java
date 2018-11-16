package com.game.smartremoteapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.bean.LevelPowerBean;
import com.game.smartremoteapp.utils.UserUtils;

import java.util.List;

/**
 * Created by mi on 2018/11/1.
 */

public class LevelPowerAdapter  extends RecyclerView.Adapter<LevelPowerAdapter.MyViewHolder1> {
    private Context mContext;
    private List<LevelPowerBean> mDatas;
    private LayoutInflater mInflater;
    private  OnItemClickListener mOnItemClickListener;

    public LevelPowerAdapter(Context context, List<LevelPowerBean> datas){
        this.mContext=context;
        this.mDatas=datas;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public LevelPowerAdapter.MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_level_power,parent,false);
        LevelPowerAdapter.MyViewHolder1 myviewHolder=new LevelPowerAdapter.MyViewHolder1(view);
        return myviewHolder;
    }

    @Override
    public void onBindViewHolder(LevelPowerAdapter.MyViewHolder1 holder, final int position) {
        holder.level_title.setText(mDatas.get(position).levelPowerTitle);
        holder.level_icon.setBackgroundResource(mDatas.get(position).icon);
         if(UserUtils.LEVEL>=mDatas.get(position).mlevel){
             setBackGroud(position, holder.level_Lvalue);
         }else{
             holder.level_Lvalue.setBackgroundResource(R.drawable.gary_border);
             holder.level_Lvalue.setText("LV"+mDatas.get(position).mlevel+"开启");
             holder.level_Lvalue.setTextColor(Color.parseColor("#c1c1c1"));
         }

        holder.level_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(true,position);
            }
        });
        holder.level_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(true,position);
            }
        });
        holder.level_Lvalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(false,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    private void setBackGroud(int index,TextView tv){
         switch (index){
             case 0:
                 tv.setBackgroundResource(R.drawable.bg_level_green_border);
                 tv.setTextColor(Color.parseColor("#73d789"));
                 tv.setText("特权已开放");
                 break;
             case 1:
                 tv.setBackgroundResource(R.drawable.bg_level_yellow_border);
                 tv.setTextColor(Color.parseColor("#f09648"));
                 if(UserUtils.LEVEL_16_TAG.equals("0")){
                     tv.setText("点击领取");
                 }else{
                     tv.setText("已领取");
                 }
                 break;
             case 2:
                 tv.setBackgroundResource(R.drawable.bg_level_blue_border);
                 tv.setTextColor(Color.parseColor("#66adf0"));
                 if(UserUtils.LEVEL_18_TAG.equals("0")){
                     tv.setText("点击领取");
                 }else{
                     tv.setText("已领取");
                 }
                 break;
             case 3:
                 tv.setBackgroundResource(R.drawable.bg_level_purple_border);
                 tv.setTextColor(Color.parseColor("#d48ef7"));
                 tv.setText("特权已开放");
                 break;
         }
    }


    class MyViewHolder1 extends RecyclerView.ViewHolder{
        TextView level_title,level_Lvalue;
        ImageView level_icon;
        public MyViewHolder1(View itemView) {
            super(itemView);
            level_icon= (ImageView) itemView.findViewById(R.id.iv_level_icon);
            level_title= (TextView) itemView.findViewById(R.id.tv_level_power_title);
            level_Lvalue= (TextView) itemView.findViewById(R.id.tv_level_Lvalue);
        }
    }

    public void setmOnItemClickListener(  OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }

    public void notify(List<LevelPowerBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onItemClick(boolean isHeader,int position);
    }

}
