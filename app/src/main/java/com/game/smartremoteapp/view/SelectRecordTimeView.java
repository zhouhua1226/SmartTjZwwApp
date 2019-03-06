package com.game.smartremoteapp.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.game.smartremoteapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mi on 2019/1/8.
 */

public class SelectRecordTimeView implements View.OnClickListener {
    private ListView mListView;
    private Context mCtx;
    private List<String>  mDatas=new ArrayList<>();
    private SelectRecordTimeView.OnDismissListListener onDismissListListener;
    private Dialog mAlertDialog;

    public SelectRecordTimeView(Context context,  SelectRecordTimeView.OnDismissListListener _onDismissListListener) {
        this.mCtx = context;
        this.onDismissListListener=_onDismissListListener;
        mAlertDialog = new Dialog(context, R.style.dialog);
        mAlertDialog.setCanceledOnTouchOutside(true);
        mAlertDialog.show();
        mAlertDialog.setContentView(R.layout.view_selsect_record_time);

        mListView = (ListView)mAlertDialog. findViewById(R.id.lv_record_time);
        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mAlertDialog.getWindow().setGravity(Gravity.TOP|Gravity.RIGHT);
        setListView();
    }


    private void setListView() {
        Calendar calendar = Calendar.getInstance();
        int year  = calendar.get(Calendar.YEAR);
         for (int i=year;i>=2018;i--){
             mDatas.add(i+"");
         }
        mListView.setAdapter(new TimeAdapter(mDatas));
    }

    public void onClick(View view) {

    }
    //接口日期选择接口
    public interface OnDismissListListener {
        void onnDismissList(int index);
    }

    private class TimeAdapter extends BaseAdapter {
        private List<String> datas;
        public TimeAdapter(List<String> mDatas) {
            this.datas = mDatas;
        }

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public Object getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                // 通过LayoutInflater填充xml布局文件，将获取到的convertView对象返回
                convertView = LayoutInflater.from(mCtx).inflate(R.layout.list_record_time_item, null);
                // 初始化holder，并为其属性赋值
                holder = new ViewHolder();
                holder.text1 = (TextView) convertView.findViewById(R.id.tv_record_time);
                // 给convertView添加标签holder
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 为每个item中的所有UI控件设置属性值
            holder.text1.setText(datas.get(i));
            return convertView;
        }

        class ViewHolder {
            TextView text1;
        }
    }
}