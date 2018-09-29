package com.game.smartremoteapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.bean.LoginRewardGoldBean;
import com.game.smartremoteapp.utils.Utils;

import java.util.List;

/**
 * Created by mi on 2018/9/27.
 */

public class RewardGoldAdapter extends RecyclerView.Adapter<RewardGoldAdapter.ViewHolder> {
    private Context mContext;
    private List<LoginRewardGoldBean.LoginRewardGold> mDatas;
    private ZWWAdapter.OnItemClickListener mOnItemClickListener;

    public RewardGoldAdapter(Context context, List<LoginRewardGoldBean.LoginRewardGold> list) {
        this.mContext = context;
        this.mDatas = list;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_reward_gold_item, parent, false);
        ViewHolder zwwViewHolder = new  ViewHolder(view);
        return zwwViewHolder;
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, final int position) {
        LoginRewardGoldBean.LoginRewardGold bean = mDatas.get(position);
        String  ss="抓币机累计抓"+"<font color='#FF0000'>" + bean.getGold() + "</font>"+"金币,奖励"+"<font color='#FF0000'>" + bean.getGold() + "</font>"+"金币";
        holder.reward_gold.setText(Html.fromHtml(ss));
        holder.reward_date.setText(bean.getCreateTime());
        if(bean.getTag().equals("Y")){ //已兑换
            holder.reward_option.setBackgroundResource(R.drawable.bg_reward_gold_nomal);
            holder.reward_option.setTextColor(Color.parseColor("#19180f"));
            holder.reward_option.setEnabled(false);
            holder.reward_option.setText("已领取");
        }else{ //未兑换
            if(Utils.getDateOver(bean.getCreateTime())){
                holder.reward_option.setBackgroundResource(R.drawable.bg_reward_gold_checked);
                holder.reward_option.setTextColor(Color.WHITE);
                holder.reward_option.setEnabled(true);
                holder.reward_option.setText("领取");
            }else{
                holder.reward_option.setBackgroundResource(R.drawable.bg_reward_gold_nomal);
                holder.reward_option.setTextColor(Color.parseColor("#19180f"));
                holder.reward_option.setEnabled(false);
                holder.reward_option.setText("已过期");
            }
        }
        holder.reward_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Button reward_option;
        private TextView reward_gold,reward_date;
        public ViewHolder(View itemView) {
            super(itemView);
            reward_gold = (TextView) itemView.findViewById(R.id.tv_reward_gold);
            reward_date = (TextView) itemView.findViewById(R.id.tv_reward_date);
            reward_option= (Button) itemView.findViewById(R.id.btn_reward_option);
        }
    }
    public void notify(List<LoginRewardGoldBean.LoginRewardGold> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }
    public void setmOnItemClickListener(ZWWAdapter.OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }
}
