package com.vgg.sdk;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import com.kazan.util.AppsUtils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

public class SdkUtil {
	private static boolean DEBUG_MODE = false;
	
	public static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (Exception e) {
		}
		return null;
	}

	@SuppressLint("TrulyRandom")
	public static String tripleDesEncrypt(String key, String data) {
		try {
			String md5 = MD5(key);
			byte[] md5Byte = md5.getBytes();
			byte[] buffer = new byte[24];
			for (int i = 0; i < buffer.length; i++) {
				buffer[i] = md5Byte[i];
			}
			KeySpec keySpec = new DESedeKeySpec(buffer);
			SecretKey sKey = SecretKeyFactory.getInstance("DESede")
					.generateSecret(keySpec);

			Cipher ecipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
			ecipher.init(Cipher.ENCRYPT_MODE, sKey);

			// Encode the string into bytes using utf-8
			byte[] utf8 = data.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			return Base64.encodeToString(enc, Base64.DEFAULT);
		} catch (Exception e) {
			return "";
		}
	}

	public static String tripleDesDecrypt(String key, String data) {
		try {
			String md5 = MD5(key);
			byte[] md5Byte = md5.getBytes();
			byte[] buffer = new byte[24];
			for (int i = 0; i < buffer.length; i++) {
				buffer[i] = md5Byte[i];
			}
			KeySpec keySpec = new DESedeKeySpec(buffer);
			SecretKey sKey = SecretKeyFactory.getInstance("DESede")
					.generateSecret(keySpec);

			Cipher dcipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
			dcipher.init(Cipher.DECRYPT_MODE, sKey);

			// Encode the string into bytes using utf-8
			byte[] utf8 = Base64.decode(data, Base64.DEFAULT);

			// Decrypt
			byte[] dnc = dcipher.doFinal(utf8);

			return new String(dnc);

		} catch (Exception e) {
			return "";
		}
	}
	
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		} catch (Exception e) {
		}
		return AppsUtils.getIPAddress(true);
	}
	public static String getIPAddess(String url) {
		if (TextUtils.isEmpty(url)) {
			url = "https://ifcfg.me/ip";
		}
		if (TextUtils.isEmpty(ip)) {
			HttpRequestHelper.requestString(url, null, new ActionCallback<String>() {

				@Override
				public void onAction(String action) {
					ip = action;
				}
			});
		}
		
		return ip;
	}
	
	private static String ip = "0.0.0.0";
/*	public static String getAgencyId(Context context) {
		if (context != null) {
			return context.getString(R.string.agency_id);
		}
		return "";
	}
	
	public static String getGaTrackingId(Context context) {
		if (context != null) {
			return context.getString(R.string.ga_trackingId);
		}
		return "";
	}
	
	public static String getServerCode() {
		return "";
	}
*/	
	public static String getUtc(String format, int seconds) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, seconds);
		return sdf.format(c.getTime());
	}
	
	public static String getUtc(String format) {
		return getUtc(format, 0);
	}
	
	public static String getUtc(int seconds) {
		return getUtc("yyyyMMddHHmmss", seconds);
	}
	
	public static String getUtc() {
		return getUtc(0);
	}
	
	public static Date getDateFrom(String value, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
			return sdf.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
					
		}
		return null;
	}
	
	public static Date getDateFrom(String value) {
		return getDateFrom(value, "yyyyMMddHHmmss");
	}
	
	public static String justifyHtml(String title, String text) {
		String html = 
				"<html>" +
					"<head>" +
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" +
						"<title>" +
						title + 
						"</title>" +
						"<link href=\"style_guide.css\" rel=\"stylesheet\" type=\"text/css\">" +
					"</head>" +
					"<body>" +
						"<div style=\"text-align:justify;\">" +
						text +
						"</div>" +
					"</body>" +
				"</html>";
		return html;
	}
	
	public static void log(View view, String log) {
		if (view instanceof TextView) {
			TextView txtView = (TextView) view;
			log = txtView.getText() + "\n" + log;
			txtView.setText(log);
		}
	}

	public static class Log {
		public static int logDisable() {
			return android.util.Log.i("Sdk DebugLog Manager", "Disable");
		}
		
		public static int d(String tag, String msg) {
			if (DEBUG_MODE) {
				return android.util.Log.d(tag, msg);
			}
			return logDisable();
		}
		
		public static int d(String tag, String msg, Throwable tr) {
			if (DEBUG_MODE) {
				return android.util.Log.d(tag, msg, tr);
			}
			return logDisable();
		}
		
		public static int i(String tag, String msg) {
			if (DEBUG_MODE) {
				return android.util.Log.i(tag, msg);
			}
			return logDisable();
		}
		
		public static int i(String tag, String msg, Throwable tr) {
			if (DEBUG_MODE) {
				return android.util.Log.i(tag, msg, tr);
			}
			return logDisable();
		}
		
		public static int e(String tag, String msg) {
			if (DEBUG_MODE) {
				return android.util.Log.e(tag, msg);
			}
			return logDisable();
		}
		
		public static int e(String tag, String msg, Throwable tr) {
			if (DEBUG_MODE) {
				return android.util.Log.e(tag, msg, tr);
			}
			return logDisable();
		}
		
		protected void setDebugMode(boolean mode) {
			DEBUG_MODE = mode;
		}
		
		protected void setLogEnable(boolean enable) {
			DEBUG_MODE = enable;
		}
	}
}
