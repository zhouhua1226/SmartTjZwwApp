package com.game.smartremoteapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.bean.UserInfoBean;
import com.game.smartremoteapp.utils.UrlUtils;
import com.game.smartremoteapp.utils.UserUtils;
import com.game.smartremoteapp.view.GlideCircleTransform;

import java.util.List;

/**
 * Created by mi on 2018/11/16.
 */

public class RecordRedWalletAdapter extends RecyclerView.Adapter<RecordRedWalletAdapter.ViewHolder> {
    private Context mContext;
    private List<UserInfoBean>   mDatas;
    private ZWWAdapter.OnItemClickListener mOnItemClickListener;

    public RecordRedWalletAdapter(Context context, List<UserInfoBean>  mUserBeans) {
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
        UserInfoBean bean = mDatas.get(position);
        if(UserUtils.USER_ID.equals(bean.getUserId())){
            holder.username.setTextColor(Color.RED);
            holder.gold_number.setTextColor(Color.RED);
        }
        holder.username.setText(bean.getNickname());
        holder.gold_number.setText(bean.getGold()+"金币");
        Glide.with(mContext).load(UrlUtils.APPPICTERURL + bean.getImgurl())
                .error(R.mipmap.app_mm_icon)
                .dontAnimate()
                .centerCrop()
                .transform(new GlideCircleTransform(mContext))
                .into(holder.userImag);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null){
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
        private ImageView userImag;
        private TextView username,gold_number;
        public ViewHolder(View itemView) {
            super(itemView);
            userImag = (ImageView) itemView.findViewById(R.id.item_userImag);
            username = (TextView) itemView.findViewById(R.id.item_username);
            gold_number= (TextView) itemView.findViewById(R.id.item_gold_number);
        }
    }
    public void notify(List<UserInfoBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }
    public void setmOnItemClickListener(ZWWAdapter.OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }
}
