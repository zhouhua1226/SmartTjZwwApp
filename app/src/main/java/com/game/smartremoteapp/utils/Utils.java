/*
 * Copyright (C) 2017 Gatz.
 * All rights, including trade secret rights, reserved.
 */
package com.game.smartremoteapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.RadioButton;

import com.game.smartremoteapp.R;
import com.game.smartremoteapp.bean.ConsigneeBean;
import com.game.smartremoteapp.view.CatchDollResultDialog;
import com.game.smartremoteapp.view.GuessingSuccessDialog;
import com.gatz.netty.global.ConnectResultEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouh on 2017/3/03.
 */
public class Utils {


    public static String connectStatus = ConnectResultEvent.CONNECT_FAILURE;
    private static final boolean D = true;
    private static final String TAG_DELIMETER = "---";

    public static final String TAG_SESSION_INVALID = "TAG_SESSION_INVALID";
    public static final String TAG_CONNECT_ERR = "TAG_CONNECT_ERR";
    public static final String TAG_CONNECT_SUCESS = "TAG_CONNECT_SUCESS";
    public static final String TAG_GATEWAT_USING = "TAG_GATEWAT_USING";
    public static final String TAG_GET_DEVICE_STATUS = "TAG_GET_DEVICE_STATUS";

    public static final String TAG_MOVE_RESPONSE = "TAG_MOVE_RESPONSE";
    public static final String TAG_MOVE_FAILE = "TAG_MOVE_FAILE";
    public static final String TAG_ROOM_IN = "TAG_ROOM_IN";
    public static final String TAG_ROOM_OUT = "TAG_ROOM_OUT";
    public static final String TAG_DOLL_CONVERSION_GOLD ="TAG_DOLL_CONVERSION_GOLD" ;
    public static final String TAG_DOLL_MACHINE_TYPE ="TAG_DOLL_MACHINE_TYPE" ;
    public static final String TAG_GATEWAY_SINGLE_CONNECT = "TAG_GATEWAY_SINGLE_CONNECT";
    public static final String TAG_GATEWAY_SINGLE_DISCONNECT = "TAG_GATEWAY_SINGLE_DISCONNECT";
    public static final String TAG_DOLL_ROOMID = "TAG_ROOM_ID";

    public static final String TAG_DEVICE_FREE = "TAG_DEVICE_FREE";
    public static final String TAG_DEVICE_ERR = "TAG_DEVICE_ERR";

    public static final String TAG_LOTTERY_DRAW = "TAG_LOTTERY_DRAW";
    public static final String TAG_DOWN_LOAD = "TAG_DOWN_LOAD";

    public static final String TAG_COIN_RESPONSE = "TAG_COIN_RESPONSE";
    public static final String TAG_COIN_DEVICE_STATE = "TAG_COIN_DEVICE_STATE";

    public static final String FREE  = "FREE";
    public static final String BUSY= "USING";
    public static final String OK = "正常";

    public static final String TAG_ROOM_NAME = "room_name";
    public static final String TAG_ROOM_STATUS = "status";
    public static final String TAG_DOLL_GOLD="doll_gold";
    public static final String TAG_DOLL_Id="doll_id";
    public static final String TAG_URL_MASTER = "url_master";
    public static final String TAG_URL_SECOND = "url_second";
    public static final String TAG_ROOM_PROB="room_prob";   //房间概率
    public static final String TAG_ROOM_REWARD="room_reward";  //房间竞猜预计奖金
    public static final String TAG_ROOM_DOLLURL="room_dollUrl";
    public static final String TAG_LIVE_DURL="live_url";
    public static final int PROVINCE_TYPE = 2;
    public static final int CITY_TYPE = 3;

    public static boolean isExit = false;

    public static String token = "";
    public static boolean isVibrator;  //是否开启震动  11/18 11:20
    public static final String HTTP_OK = "success";
    public static String appVersion="";
    public static String osVersion="";
    public static String deviceType="";
    public static String IMEI="";

    public static final int CATCH_TIME_OUT = 20;
    public static final long GET_STATUS_DELAY_TIME = 3*60*1000;
    public static final long GET_STATUS_PRE_TIME = 2*60*1000;
    public static final long OTHER_PLAYER_DELAY_TIME =6*1000 ;

    public static int getInt(String c) {
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(c);
        return Integer.valueOf(m.replaceAll("").trim());
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 正则匹配电话号码
     */

    public static boolean checkPhoneREX(String value) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(value);
        return m.find();
    }

    public static boolean getDateOver(String value)  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dNow = new Date();   //当前时间
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
      //  Date  dBefore = calendar.getTime();   //得到前一天的时间
        String dBefore=sdf.format(calendar.getTime());
        return   dBefore.equals(value);
    }


    /**
     * 身份证正则匹配
     * @param idcard
     * @return  符合 regID的返回true, 否则返回 false;
     */
    public static boolean checkIDcard(String idcard){
        String regID="^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        return match(regID,idcard);
    }

    /**
     * 验证输入密码条件(字符与数据同时出现)
     *
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isTextPassword(String str) {
        String regex = "(?![a-z|_]+$|[0-9]+$)^[a-zA-Z0-9,_]{6,20}$";
        return match(regex, str);
    }
    /**
     * @param regex
     *            正则表达式字符串
     * @param str
     *            要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    /**
     * 得到手机识别码
     */
    public static String getIMEI(Context context) {
        TelephonyManager mTelephonyMgr = null;
        if (context != null) {
            mTelephonyMgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return mTelephonyMgr.getDeviceId();
        }
        return "";
    }

    /**
     * 将当前时间 格式化
     */
    public static String getTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    //dp转px
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    //dp转px
    public static void setDrawableSize(Context context, RadioButton radioButton) {
            int size =  dip2px(context,25f);
            int space =  dip2px(context,10f);
            Drawable drawable = radioButton.getCompoundDrawables()[1];
            drawable.setBounds(0, space, size, size + space);
            radioButton.setCompoundDrawables(null, drawable, null, null);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue
     * @return
     * @author SHANHY
     * @date   2015年10月28日
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断网络是否连接
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        } else {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null) {
              return info.isAvailable();
            }
        }
        return false;
    }
    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}''\\[\\].<>/~@#￥%……&*（）——+|{}【】‘”“’]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static boolean delFile(String fileName){
        File file = new File(fileName);
        if (file.isFile()) {file.delete();}
        return file.exists();
    }
    //删除指定文件夹下所有文件
    public static void deleteAll(String path) {
        File filePar = new File(path);
        if (filePar.exists()) {
            File files[] = filePar.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                } else if (files[i].isDirectory()) {
                    deleteAll(files[i].getAbsolutePath());
                    files[i].delete();
                }
            }
        }
    }

    /**
     * 时间拆分
     * 2017/11/30  15：55
     */
    public static String getTime(String times){
        if(times.length()>=14) {
            String year = times.substring(0, 4);
            String month = times.substring(4, 6);
            String day = times.substring(6, 8);
            String hour = times.substring(8, 10);
            String minte = times.substring(10, 12);
            String second = times.substring(12, 14);
            return year + "/" + month + "/" + day + "  " + hour + ":" + minte + ":" + second;
        }else {
            return  "年/" +  "月/" +  "日  " + "时:分:秒";
        }
    }


    /**
     * 获取版本信息
     * 2017/11/30  15：55
     * i=0 获取版本号，i=1 获取版本名
     */
    public static String getAppCodeOrName(Context context,int i) {
        String version="";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            if(i==0){
                version=packInfo.versionCode+"";
            }else {
                version = packInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取当前手机系统版本号
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return "Android"+android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机品牌以及型号
     * @return  手机品牌和型号
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND+android.os.Build.MODEL;
    }

    public static int getWidthSize(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int x = dm.widthPixels;
        int y = dm.heightPixels;
        return x;
    }


    /**
     * 收发货信息拆分
     * 2017/12/05 11：11
     */
    public static ConsigneeBean getConsigneeBean(String s) {
        ConsigneeBean consigneeBean = new ConsigneeBean();
        String ss[] = s.split(",");
        if (ss.length == 0) {
            return null;
        }
        if ((ss.length == 3) || (ss.length == 4)) {
            consigneeBean.setName(ss[0]);
            consigneeBean.setPhone(ss[1]);
            consigneeBean.setAddress(ss[2]);
            consigneeBean.setRemark("");
            if (ss.length == 4) {
                consigneeBean.setRemark(ss[3]);
            }
        }
        return consigneeBean;
    }

    /**
     * 读取assets下的txt文件，返回utf-8 String
     * @param context
     * @param fileName 不包括后缀
     * @return
     */
    public static String readAssetsTxt(Context context,String fileName){
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(fileName+".txt");
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
            return "读取错误，请检查文件名";
        }

    }

    //竞彩成功弹窗
    public static void getGuessSuccessDialog(Context context){
        final GuessingSuccessDialog guessingSuccessDialog=new GuessingSuccessDialog(context, R.style.easy_dialog_style);
        guessingSuccessDialog.setCancelable(true);
        guessingSuccessDialog.show();
        guessingSuccessDialog.setDialogResultListener(new GuessingSuccessDialog.DialogResultListener() {
            @Override
            public void getResult(int resultCode) {
                if(resultCode==0){
                    guessingSuccessDialog.dismiss();
                }
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                guessingSuccessDialog.dismiss();
            }
        },3000);
    }

    public static void getCatchResultDialog(Context context){
        final CatchDollResultDialog resultDialog=new CatchDollResultDialog(context,R.style.easy_dialog_style);
        resultDialog.setCancelable(false);
        resultDialog.show();
        resultDialog.setDialogResultListener(new CatchDollResultDialog.DialogResultListener() {
            @Override
            public void getResult(int resultCode) {
                if(resultCode==0){
                    //取消
                    resultDialog.dismiss();
                }else {
                    //再试一次
                    resultDialog.dismiss();
                }
            }
        });

    }

    //房间背景音乐控制方法
    public static boolean getIsOpenMusic(Context context){
        return  SPUtils.getBoolean(context, UserUtils.SP_TAG_ISOPENMUSIC, true);
    }

    /**
     * 根据不通省份调整金币抵扣金额
     * @param address
     * @return
     */
    public static String getJBDKNum(String address) {
        String money = "";
        if (address.contains("上海") || address.contains("江苏")
                || address.contains("浙江") || address.contains("安徽")) {
            money = "120";
        } else if (address.contains("江西") || address.contains("山东")
                || address.contains("湖北") || address.contains("湖南")
                || address.contains("广东") || address.contains("福建")
                || address.contains("北京") || address.contains("天津")|| address.contains("海南")) {
            money = "150";
        } else if (address.contains("河北") || address.contains("山西")
                || address.contains("河南") || address.contains("广西")
                || address.contains("重庆")
                || address.contains("四川") || address.contains("贵州")
                || address.contains("云南") || address.contains("陕西")) {
            money = "180";
        } else if(address.contains("宁夏")|| address.contains("青海")|| address.contains("甘肃")){
              money = "200";
        }else if (address.contains("内蒙古") || address.contains("甘肃")
                   || address.contains("吉林") || address.contains("辽宁")
                   || address.contains("黑龙江")) {
            money = "250";
        }
        else if (address.contains("新疆") || address.contains("西藏")) {
            money = "300";
        }
        return money;
    }

    /**
     * 与后台约定省份对应编号(港澳台暂不支持)
     * @param provinceCity
     * @return
     */
    public static String getProvinceNum(String provinceCity){
        String costNum="";
        if(provinceCity.contains("黑龙江")){
            costNum="1";
        }else if(provinceCity.contains("吉林")){
            costNum="2";
        }else if(provinceCity.contains("辽宁")){
            costNum="3";
        }else if(provinceCity.contains("北京")){
            costNum="4";
        }else if(provinceCity.contains("天津")){
            costNum="5";
        }else if(provinceCity.contains("河北")){
            costNum="6";
        }else if(provinceCity.contains("山西")){
            costNum="7";
        }else if(provinceCity.contains("内蒙古")){
            costNum="8";
        }else if(provinceCity.contains("上海")){
            costNum="9";
        }else if(provinceCity.contains("江苏")){
            costNum="10";
        }else if(provinceCity.contains("浙江")){
            costNum="11";
        }else if(provinceCity.contains("安徽")){
            costNum="12";
        }else if(provinceCity.contains("江西")){
            costNum="13";
        }else if(provinceCity.contains("山东")){
            costNum="14";
        }else if(provinceCity.contains("河南")){
            costNum="15";
        }else if(provinceCity.contains("湖北")){
            costNum="16";
        }else if(provinceCity.contains("湖南")){
            costNum="17";
        }else if(provinceCity.contains("广东")){
            costNum="18";
        }else if(provinceCity.contains("广西")){
            costNum="19";
        }else if(provinceCity.contains("海南")){
            costNum="20";
        }else if(provinceCity.contains("甘肃")){
            costNum="21";
        }else if(provinceCity.contains("青海")){
            costNum="22";
        }else if(provinceCity.contains("宁夏")){
            costNum="23";
        }else if(provinceCity.contains("新疆")){
            costNum="24";
        }else if(provinceCity.contains("重庆")){
            costNum="25";
        }else if(provinceCity.contains("四川")){
            costNum="26";
        }else if(provinceCity.contains("贵州")){
            costNum="27";
        }else if(provinceCity.contains("云南")){
            costNum="28";
        }else if(provinceCity.contains("西藏")){
            costNum="29";
        }else if(provinceCity.contains("福建")){
            costNum="30";
        }else if(provinceCity.contains("陕西")){
            costNum="31";
        }

        return costNum;
    }

    /**
     * 转码昵称中的表情
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        String string = null;
        if (!containsEmoji(source)) {
            return source;// 如果不包含，直接返回
        }
        StringBuilder buf = null;
        int len = source.length();
        System.out.println("filter running len = " + len);
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (buf == null) {
                buf = new StringBuilder(source.length());
            }
            if (!isEmojiCharacter(codePoint)) {
                string = String.valueOf(codePoint);
            } else {
                try {
                    StringBuilder builder = new StringBuilder(2);
                    byte[] str = builder.append(String.valueOf(codePoint))
                            .append(String.valueOf(source.charAt(i+1)))
                            .toString().getBytes("UTF-8");
                    String strin = Arrays.toString(str);
                    String newString = strin.substring(1, strin.length() - 1);
                    string = ":"+newString+":";
                    System.out.println("filters running newStr = " + string);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                i++;
            }
            buf.append(string+"%");
        }
        if (buf == null) {
            return "";
        } else {
            if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                System.out.println("filter running buf.toString() = " + buf.toString());
                String bufStr = buf.toString();
                String newBufStr= bufStr.substring(0, bufStr.length() - 1);
                return newBufStr;
            }
        }
    }

    //得到服务器的数据之后进行解析，显示在UI上
    public static String decodeEmoji(String string) {
        String newsString;
        StringBuilder stringBuilder = new StringBuilder();
        String arrays[] = string.split("%");
        for (int j = 0; j < arrays.length; j++) {
            System.out.println("filter running arrays[] = "+arrays[j]);
            String  ss = arrays[j];
            char char_ss = ss.charAt(0);
            System.out.println("filter running String.valueOf(char_ss) = "+String.valueOf(char_ss));
            if (String.valueOf(char_ss).equals(":")){
                String new_SS = ss.substring(1, ss.length() - 1);
                String strArrays[] = new_SS.split(", ");
                byte[] chars = new byte[strArrays.length];
                for (int i = 0; i < strArrays.length; ++i) {
                    System.out.println("strArrays[i]:" + strArrays[i]);
                    chars[i] = Byte.decode(strArrays[i]);
                }
                newsString = new String(chars);
            }else{
                newsString =ss;
            }
            stringBuilder.append(newsString);
            System.out.println("filter running stringBuilder.toString() = "+stringBuilder.toString());
            //textView.setText(stringBuilder.toString());
        }
        return stringBuilder.toString();
    }

    // 判别是否包含Emoji表情
    public static boolean containsEmoji(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    public static void toActivity(Context ctx, Class<?> act) {
        try {
            Intent intent = new Intent(ctx, act);
            toActivity(ctx, intent);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    public static void toActivity(Context ctx, Intent intent) {
        try {
            ctx.startActivity(intent);
         //  ((Activity) ctx).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 800;
    private  static long lastClickTime;
    public  static boolean isTimeLimit() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /*
     * 判断是否为整数
    * @param str 传入的字符串
    * @return 是整数返回true,否则返回false
   */
    public final static boolean isNumeric(String s) {
        if (!s.isEmpty()&&s != null && !"".equals(s.trim()))
            return s.matches("^[0-9]*$");
        else
            return false;
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;

    }
    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
