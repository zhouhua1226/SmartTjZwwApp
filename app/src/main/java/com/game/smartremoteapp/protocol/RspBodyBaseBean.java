package com.game.smartremoteapp.protocol;

import android.content.Context;
import android.telephony.TelephonyManager;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 功能： 协议类，将传递信息变成json格式
 * 
 * @author Administrator
 */
public class RspBodyBaseBean {

	private static final String TAG = "RspBodyBaseBean";

	public static String getAuth(Context context ,String uid) {
		String version="1.0.1";
		String key = MD5.md5(MD5.MD5_key);
		String time = getTime();
		String imei = getIMEI(context);
		String info="{\"userID\":\""+uid+"\"}";
		//String info = "{\"userID\":\"" + UserUtils.USER_ID + "\",\"wzType\":\"" + "0" + "\",\"phone\":\""+ Build.BRAND+"\"}";
		String crc = getCrc(time, imei, key, info, uid);

		return "{\"loginType\":\"1\",\"app_key\":\"123456\",\"imei\":\""
				+ imei
				+ "\","
				+ "\"os\":\"Android\",\"os_version\":\"5.0\",\"app_version\":\""
				+ version + "\","
				+ "\"source_id\":\"Yek_test\",\"ver\":\"0.9\",\"UID\":\"" + uid
				+ "\"" + ",\"crc\":\"" + crc + "\",\"time_stamp\":" + "\""
				+ time + "\",\"wzType\":\"0\"}";
	}


	/**
	 * 得到手机识别码
	 */
	private static String getIMEI(Context context) {
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
	private static String getTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	/**
	 * 得到 特定 key --- Crc
	 */
	private static String getCrc(String time, String imei, String key,
								 String info, String uid) {
		// crc = MD5(auth.time_stamp + auth.imei + auth.uid +MD5（password+key）+
		// Info, "utf-8")
		return MD5.md5(time + imei + uid + key + info);
	}


}
