package com.game.smartremoteapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.bean.RedWalletlistInfoBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mi on 2019/1/14.
 */
public class RedWalletRecordAdapter extends RecyclerView.Adapter<RedWalletRecordAdapter.ViewHolder> {
    private Context mContext;
    private boolean isSend;
    private List<RedWalletlistInfoBean> mDatas;
    private  OnItemClickListener mOnItemClickListener;

    public RedWalletRecordAdapter(Context context, List<RedWalletlistInfoBean>  mUserBeans,boolean isSend) {
        this.mContext = context;
        this.mDatas = mUserBeans;
        this.isSend = isSend;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public RedWalletRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_rewallet_record_item, parent, false);
        RedWalletRecordAdapter.ViewHolder zwwViewHolder = new RedWalletRecordAdapter.ViewHolder(view);
        return zwwViewHolder;
    }

    @Override
    public void onBindViewHolder(RedWalletRecordAdapter.ViewHolder holder, final int position) {
        RedWalletlistInfoBean bean = mDatas.get(position);
        if(isSend){
            holder.redwallet_type.setText("普通红包");
            holder.redwallet_gold.setVisibility(View.VISIBLE);
            holder.redwallet_num.setVisibility(View.VISIBLE);
            holder.redwallet_gold.setText(bean.getREDGOLD()+"金币");
            holder.redwallet_num.setText("已领完"+bean.getREDCOUNT()+"/"+bean.getREDNUM()+"个");
            holder.redwallet_golds.setVisibility(View.GONE);
        }else{
            holder.redwallet_type.setText(bean.getNICKNAME());
            holder.redwallet_gold.setVisibility(View.GONE);
            holder.redwallet_num.setVisibility(View.GONE);
            holder.redwallet_golds.setVisibility(View.VISIBLE);
            holder.redwallet_golds.setText(bean.getGOLD()+"金币");
        }
        setTime( bean.getCREATETIME(),holder.redwallet_time);
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

        private TextView redwallet_type,redwallet_time,redwallet_gold,redwallet_num,redwallet_golds;
        public ViewHolder(View itemView) {
            super(itemView);
            redwallet_type = (TextView) itemView.findViewById(R.id.item_redwallet_type);
            redwallet_time = (TextView) itemView.findViewById(R.id.item_redwallet_time);
            redwallet_gold= (TextView) itemView.findViewById(R.id.item_redwallet_gold);
            redwallet_num = (TextView) itemView.findViewById(R.id.item_redwallet_num);
            redwallet_golds= (TextView) itemView.findViewById(R.id.item_redwallet_golds);
        }
    }
    public void notify(List<RedWalletlistInfoBean> lists,boolean isSend) {
        this.mDatas = lists;
        this.isSend = isSend;
        notifyDataSetChanged();
    }
    public void setmOnItemClickListener(OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }

    private void setTime(String mTime, TextView redwallet_time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date dt = null;
        try {
            dt = sdf.parse(mTime);
            cal.setTime(dt);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        redwallet_time.setText(month+"-"+day);
       }
}