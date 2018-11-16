package com.game.smartremoteapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.bean.UserBean;

import java.util.List;

/**
 * Created by mi on 2018/11/16.
 */

public class RecordRedWalletAdapter extends RecyclerView.Adapter<RecordRedWalletAdapter.ViewHolder> {
    private Context mContext;
    private List<UserBean>   mDatas;
    private ZWWAdapter.OnItemClickListener mOnItemClickListener;

    public RecordRedWalletAdapter(Context context, List<UserBean>  mUserBeans) {
        this.mContext = context;
        this.mDatas = mUserBeans;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public RecordRedWalletAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_record_rewallet_item, parent, false);
        RecordRedWalletAdapter.ViewHolder zwwViewHolder = new RecordRedWalletAdapter.ViewHolder(view);
        return zwwViewHolder;
    }

    @Override
    public void onBindViewHolder(RecordRedWalletAdapter.ViewHolder holder, final int position) {

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
    public void notify(List<UserBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }
    public void setmOnItemClickListener(ZWWAdapter.OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }
}
