package com.game.smartremoteapp.service;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by mi on 2018/7/24.
 */

public class DownloadReceiver extends BroadcastReceiver {
    private String title = "汤姆抓娃娃.apk";

    @Override
    public void onReceive(Context context, Intent intent) {
         Log.d("BroadcastReceiver", intent.getAction());
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
          long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
           installApk(context, id);
        } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            // DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            //获取所有下载任务Ids组
            //long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            ////点击通知栏取消所有下载
            //manager.remove(ids);
            //Toast.makeText(context, "下载任务已取消", Toast.LENGTH_SHORT).show();
            //处理 如果还未完成下载，用户点击Notification ，跳转到下载中心
            Intent viewDownloadIntent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
            viewDownloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(viewDownloadIntent);
        }
    }


    /**
     * 根据任务id打开安装界面
     *
     * @param context
     * @param downloadApkId
     */
    public void installApk(Context context, long downloadApkId) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        File file= null;
        try {
            file =  context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/"+title);
         } catch (NullPointerException e) {
            file=new File(Environment.getExternalStorageDirectory(), "/temp/"+title);
        }
        if (file != null) {
            String path = file.getAbsolutePath();
             Uri    downloadFileUri = Uri.parse("file://" + path);
            install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }

    }

}

