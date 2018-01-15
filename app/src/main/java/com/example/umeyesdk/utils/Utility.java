package com.example.umeyesdk.utils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Utility {

	public static boolean isZh(Context con) {
		Locale locale = con.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("zh"))
			return true;
		else
			return false;
	}

	public static void Logout(Activity c)// ע����Ϊ
	{
		// Intent intent=new Intent(c,Settings.class);
		// intent.putExtra("logout", true);
		// c.startActivity(intent);
		c.finish();
	}

	private static class OnDialogBack implements
			android.content.DialogInterface.OnClickListener// ȷ�϶Ի����ȷ����ť�¼�
	{
		private Activity context;

		public OnDialogBack(Activity c) {
			context = c;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Utility.Logout(context);
		}
	}



	public static String FormateTime(int time)// hh:mm:ss,time�Ǻ��뼶��ʱ��
	{
		int hour = time / (1000 * 60 * 60);
		int minute = (time % (1000 * 60 * 60)) / (1000 * 60);
		int second = ((time % (1000 * 60 * 60)) % (1000 * 60)) / 1000;
		String h, m, s;
		if (hour < 10) {
			h = "0" + hour;
		} else {
			h = String.valueOf(hour);
		}
		if (minute < 10) {
			m = "0" + minute;
		} else {
			m = String.valueOf(minute);
		}
		if (second < 10) {
			s = "0" + second;
		} else {
			s = String.valueOf(second);
		}
		String str = h + ":" + m + ":" + s;

		return str;
	}

	/**
	 * ��������IP��ַ�Ƿ�Ϸ�
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isValidIP(String ip) {
		ip = ip.replace('.', '#');
		String[] numbers = ip.split("#");
		if (numbers.length != 4)
			return false;
		for (int i = 0; i < numbers.length; i++) {
			int number;
			try {
				number = Integer.parseInt(numbers[i]);
			} catch (Exception e) {
				return false;
			}
			if ((number < 0) || (number > 255))
				return false;
		}
		return true;
	}

	/**
	 * ����Ƿ���Ч����
	 * 
	 * @param n
	 * @return
	 */
	public static boolean isValidNumber(String n) {

		try {
			Integer.parseInt(n);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * �õ����ĵĵ�ǰʱ�䣬��ȷ����
	 * 
	 * @return
	 */
	public static String GetCurrentTime() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String s = c.get(Calendar.YEAR) + "��" + (c.get(Calendar.MONDAY) + 1)
				+ "��" + c.get(Calendar.DAY_OF_MONTH) + "��"
				+ c.get(Calendar.HOUR_OF_DAY) + "ʱ" + c.get(Calendar.MINUTE)
				+ "��" + c.get(Calendar.SECOND) + "��";
		return s;
	}

	/**
	 * ��ȡͼƬ��С����ͼ
	 * 
	 * @param fileName
	 *            �ļ�����
	 * @param scale
	 * @return
	 */
	public static Bitmap GetThumbImage(String fileName, int w, int h) {
		Bitmap result = null;
		try {
			BitmapFactory.Options op = new Options();
			op.inJustDecodeBounds = true;
			Bitmap bmp = BitmapFactory.decodeFile(fileName, op);
			int x = (int) (op.outWidth / (w * 1.0));
			int y = (int) (op.outHeight / (h * 1.0));
			int scale = x > y ? y : x;
			BitmapFactory.Options options = new Options();
			options.inSampleSize = scale;
			result = BitmapFactory.decodeFile(fileName, options);
			// System.out.println("scale:"+scale);
		} catch (RuntimeException e) {
			System.out.println("RuntimeException��ȡ����ͼ����" + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("��ȡ����ͼ����" + e.getMessage());
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static Bitmap getBitmap(String fileName) {
		Bitmap bmp = null;
		try {

			bmp = BitmapFactory.decodeFile(fileName);

		} catch (RuntimeException e) {
			System.out.println("RuntimeException��ȡ����ͼ����" + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("��ȡ����ͼ����" + e.getMessage());
			e.printStackTrace();
			return null;
		}

		return bmp;
	}

	/**
	 * ��ֵд���������ļ�
	 * 
	 * @param c
	 * @param fileName
	 * @param key
	 * @param value
	 */
	public static void WriteLocal(Context c, String fileName, String key,
			String value) {
		SharedPreferences pref = c.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * ��ȡ��Ӧ��ֵ
	 * 
	 * @param c
	 * @param fileName
	 * @param key
	 * @return
	 */
	public static String ReadLocal(Context c, String fileName, String key) {
		SharedPreferences pref = c.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return pref.getString(key, null);
	}

	/**
	 * ɾ����Ӧ�ļ�
	 * 
	 * @param c
	 * @param fileName
	 * @param key
	 */
	public static void RemoveLocal(Context c, String fileName, String key) {
		SharedPreferences pref = c.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * ���û��������뼰������������һ����һ�޶����ļ�����
	 * 
	 * @param server
	 *            ��������
	 * @param userName
	 *            �û���
	 * @param password
	 *            ����
	 * @return �����ļ���
	 */
	public static String ToFileName(String server, String userName,
			String password) {
		String ser = server.replace(":", "").replace(".", "").replace("//", "");// ��.��:ȥ��
		// return ser+userName+password;
		return ser + userName;
	}

	/**
	 * �ڴ濨�Ƿ����ʹ��
	 * 
	 * @return ������Ϊtrue,��������Ϊfalse
	 */
	public static boolean isSDCardAvaible() {
		String state = Environment.getExternalStorageState();
		if (!state.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}


	/**
	 * �õ��汾��VersionCode
	 * 
	 * @param c
	 * @return
	 */
	public static int GetVersionCode(Context c) {
		String pName = c.getPackageName();
		try {
			PackageInfo pinfo = c.getPackageManager().getPackageInfo(pName,
					PackageManager.GET_CONFIGURATIONS);
			return pinfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * �õ��汾��VersionCode
	 * 
	 * @param c
	 * @return
	 */
	public static String GetVersionName(Context c) {
		String pName = c.getPackageName();
		try {
			PackageInfo pinfo = c.getPackageManager().getPackageInfo(pName,
					PackageManager.GET_CONFIGURATIONS);
			return pinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "3.0";
	}



	public static void OpenAPKFile(String fileName, Context context) {
		File file = new File(fileName);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static String GetIMSI(Context c) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) c
				.getSystemService(Context.TELEPHONY_SERVICE);
		String secureId = android.provider.Settings.Secure.getString(
				c.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		String subscriberId = mTelephonyMgr.getSubscriberId();
		String deviceId = mTelephonyMgr.getDeviceId();

		Log.w("imsi", "SubscriberId:" + subscriberId);
		Log.w("imsi", "secureId:" + secureId);
		Log.w("imsi", "DeviceId:" + deviceId);
		return secureId;

	}
	/**
	 * ��ȡimsi
	 * 
	 * @param con
	 * @return
	 */
	public static String getImsi(Context con) {

		TelephonyManager mTelephonyMgr = (TelephonyManager) con
				.getSystemService(Context.TELEPHONY_SERVICE);
		String secureId = android.provider.Settings.Secure.getString(
				con.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		String subscriberId = mTelephonyMgr.getSubscriberId();
		String deviceId = mTelephonyMgr.getDeviceId();

		// Log.w("imsi", "SubscriberId:" + subscriberId);
		Log.w("imsi", "secureId:" + secureId);
		// Log.w("imsi", "DeviceId:" + deviceId);
		return secureId;

	}
	
}
