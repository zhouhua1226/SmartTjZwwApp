package com.game.smartremoteapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartremoteapp.R;
import com.game.smartremoteapp.bean.RedPackageBean;
import com.game.smartremoteapp.utils.LogUtils;
import com.game.smartremoteapp.utils.UrlUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mi on 2018/11/14.
 */

public class GrabRedWalletAdapter extends RecyclerView.Adapter<GrabRedWalletAdapter.ZWWViewHolder> {
    private static final String TAG ="GrabRedWalletAdapter" ;
    private Context mContext;
    private List<RedPackageBean> mDatas;
    private ZWWAdapter.OnItemClickListener mOnItemClickListener;

    public GrabRedWalletAdapter(Context context, List<RedPackageBean> list) {
        this.mContext = context;
        this.mDatas = list;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public GrabRedWalletAdapter.ZWWViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_grad_redwallet, parent, false);
        GrabRedWalletAdapter.ZWWViewHolder zwwViewHolder = new GrabRedWalletAdapter.ZWWViewHolder(view);
        return zwwViewHolder;
    }

    @Override
    public void onBindViewHolder(GrabRedWalletAdapter.ZWWViewHolder holder, final int position) {
        RedPackageBean bean = mDatas.get(position);
        String name= bean.getNickName();
        int nameSize=bean.getNickName().length();
        if(nameSize>8){
           name=name.substring(0, 3)+"···"+name.substring(nameSize-4, nameSize);
        }
        holder.redwallet_number.setText(name+"发了"+bean.getRedGold()+"红包");
        holder.redwallet_time.setText(bean.getCreatetime());
        setTime(bean.getCreatetime(),holder.redwallet_time);
        holder.redwallet_gold.setText(bean.getRedGold());
        holder.user1.setVisibility(View.GONE);
        holder.user2.setVisibility(View.GONE);
        holder.user3.setVisibility(View.GONE);
        if(bean.getUserInfo()!=null) {
             int size = bean.getUserInfo().size();
             if (size == 1&&!bean.getUserInfo().get(0).getGold().equals("0")) {
                 setImage(UrlUtils.APPPICTERURL + bean.getUserInfo().get(0).getImgurl(),holder.user1);
             } else if (size == 2) {
                setImage(UrlUtils.APPPICTERURL + bean.getUserInfo().get(0).getImgurl(),holder.user1);
                setImage(UrlUtils.APPPICTERURL + bean.getUserInfo().get(1).getImgurl(),holder.user2);
             } else if(size >=3) {
                setImage(UrlUtils.APPPICTERURL + bean.getUserInfo().get(0).getImgurl(),holder.user1);
                setImage(UrlUtils.APPPICTERURL + bean.getUserInfo().get(1).getImgurl(),holder.user2);
                setImage(UrlUtils.APPPICTERURL + bean.getUserInfo().get(2).getImgurl(),holder.user3);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
    }
    private void setImage(String url,ImageView imageView){
        imageView.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(url)
                .error(R.mipmap.app_mm_icon)
                .dontAnimate()
                .into(imageView);
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ZWWViewHolder extends RecyclerView.ViewHolder {
        private ImageView   user1, user2, user3;
        private TextView redwallet_number,redwallet_time,redwallet_gold;
        public ZWWViewHolder(View itemView) {
            super(itemView);
            redwallet_gold = (TextView) itemView.findViewById(R.id.tv_item_redwallet_gold);
            redwallet_number = (TextView) itemView.findViewById(R.id.tv_item_redwallet_number);
            redwallet_time = (TextView) itemView.findViewById(R.id.tv_item_redwallet_time);
            user1 = (ImageView) itemView.findViewById(R.id.iv_item_user1);
            user2 = (ImageView) itemView.findViewById(R.id.iv_item_user2);
            user3 = (ImageView) itemView.findViewById(R.id.iv_item_user3);
        }
    }

    public void notify(List<RedPackageBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }

    public void setmOnItemClickListener(ZWWAdapter.OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }

    private void setTime(String mTime, TextView redwallet_time){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            Date d1 = df.parse(mTime);
            Date d2 = new Date();
            //Date   d2 = new   Date(System.currentTimeMillis());//你也可以获取当前时间
            long diff = d2.getTime() - d1.getTime();//这样得到的差值是微秒级别
            long years = diff / (1000 * 60 * 60 * 24*365);
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
            long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
            if(years>0){
                redwallet_time.setText(years+"年前");
            }else{
                if(days>0){
                    if(days>1){
                        redwallet_time.setText(days+"天前");
                    }else{
                        redwallet_time.setText( "昨天");
                    }
                }else{
                    if(hours>0){
                        redwallet_time.setText(hours+"小时前");
                    }else if(minutes>0){
                        redwallet_time.setText(minutes+"分钟前");
                    }else{
                        redwallet_time.setText("刚刚");
                    }
                }
            }
            LogUtils.loge(years+"年"+days+"天"+hours+"小时"+minutes+"分",TAG);
        }
        catch (Exception e) {}
    }
}
