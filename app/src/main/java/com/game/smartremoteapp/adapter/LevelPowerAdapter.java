package com.game.smartremoteapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.bean.LevelPowerBean;

import java.util.List;

/**
 * Created by mi on 2018/11/1.
 */

public class LevelPowerAdapter  extends RecyclerView.Adapter<LevelPowerAdapter.MyViewHolder1> {
    private Context mContext;
    private List<LevelPowerBean> mDatas;
    private LayoutInflater mInflater;
    private JoinEarnAdapter.OnItemClickListener mOnItemClickListener;

    public LevelPowerAdapter(Context context, List<LevelPowerBean> datas){
        this.mContext=context;
        this.mDatas=datas;
        mInflater=LayoutInflater.from(context);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
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
        holder.level_Lvalue.setText(mDatas.get(position).leveLvalue);
        holder.level_icon.setBackgroundResource(mDatas.get(position).icon);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
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

    public void setmOnItemClickListener(JoinEarnAdapter.OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }

    public void notify(List<LevelPowerBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }

}
