package com.kazan.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import com.android.vending.billing.util.Base64;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Toast;

public class AppsUtils {

	public static boolean isTabletDevice(Context activityContext) {
        boolean device_large = ((activityContext.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE);

        if (device_large) {
            DisplayMetrics metrics = activityContext.getResources().getDisplayMetrics();
            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_LOW) {
                return true;
            }
        }
        return device_large;
    }
	
	public static boolean isEmpty(String string) {
		return TextUtils.isEmpty(string);
	}
	
	/**
     * The threshold used calculate if a color is light or dark
     */
    private static final int BRIGHTNESS_THRESHOLD = 130;

    /**
     * Used to determine if the device is running Jelly Bean or greater
     * 
     * @return True if the device is running Jelly Bean or greater, false
     *         otherwise
     */
    public static final boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static final boolean hasHoneyComb() {
    	return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
    
    /**
     * Used to determine if the device is running
     * Jelly Bean MR2 (Android 4.3) or greater
     *
     * @return True if the device is running Jelly Bean MR2 or greater,
     *         false otherwise
     */
    public static final boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }
    
    public static final boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
    
    public static final boolean hasLollipop() {
    	return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    
/*    public static final boolean hasLollipopMR1() {
    	return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }
*/    /**
     * Used to determine if the device is a tablet or not
     * 
     * @param context The {@link Context} to use.
     * @return True if the device is a tablet, false otherwise.
     */
    public static final boolean isTablet(final Context context) {
        /*final int layout = context.getResources().getConfiguration().screenLayout;
        return (layout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;*/
    	return isTabletDevice(context);
    }

    /**
     * Used to determine if the device is currently in landscape mode
     * 
     * @param context The {@link Context} to use.
     * @return True if the device is in landscape mode, false otherwise.
     */
    public static final boolean isLandscape(final Context context) {
        final int orientation = context.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Calculate whether a color is light or dark, based on a commonly known
     * brightness formula.
     * 
     * @see {@literal http://en.wikipedia.org/wiki/HSV_color_space%23Lightness}
     */
    public static final boolean isColorDark(final int color) {
        return (30 * Color.red(color) + 59 * Color.green(color) + 11 * Color.blue(color)) / 100 <= BRIGHTNESS_THRESHOLD;
    }
    /**
     * Used to determine if there is an active data connection and what type of
     * connection it is if there is one
     * 
     * @param context The {@link Context} to use
     * @return True if there is an active data connection, false otherwise.
     *         Also, if the user has checked to only download via Wi-Fi in the
     *         settings, the mobile data and other network connections aren't
     *         returned at all
     */
    public static final boolean isOnline(final Context context) {
        /*
         * This sort of handles a sudden configuration change, but I think it
         * should be dealt with in a more professional way.
         */
        if (context == null) {
            return false;
        }

        boolean state = false;
        final boolean onlyOnWifi = false;//PreferenceUtils.getInstance(context).onlyOnWifi();

        /* Monitor network connections */
        final ConnectivityManager connectivityManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        /* Wi-Fi connection */
        final NetworkInfo wifiNetwork = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null) {
            state = wifiNetwork.isConnectedOrConnecting();
        }

        /* Mobile data connection */
        final NetworkInfo mbobileNetwork = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mbobileNetwork != null) {
            if (!onlyOnWifi) {
                state = mbobileNetwork.isConnectedOrConnecting();
            }
        }

        /* Other networks */
        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (!onlyOnWifi) {
                state = activeNetwork.isConnectedOrConnecting();
            }
        }

        return state;
    }

    /**
     * Display a {@link Toast} letting the user know what an item does when long
     * pressed.
     * 
     * @param view The {@link View} to copy the content description from.
     */
    public static void showCheatSheet(final View view) {

        final int[] screenPos = new int[2]; // origin is device display
        final Rect displayFrame = new Rect(); // includes decorations (e.g.
                                              // status bar)
        view.getLocationOnScreen(screenPos);
        view.getWindowVisibleDisplayFrame(displayFrame);

        final Context context = view.getContext();
        final int viewWidth = view.getWidth();
        final int viewHeight = view.getHeight();
        final int viewCenterX = screenPos[0] + viewWidth / 2;
        final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        final int estimatedToastHeight = (int)(48 * context.getResources().getDisplayMetrics().density);

        final Toast cheatSheet = Toast.makeText(context, view.getContentDescription(),
                Toast.LENGTH_SHORT);
        final boolean showBelow = screenPos[1] < estimatedToastHeight;
        if (showBelow) {
            // Show below
            // Offsets are after decorations (e.g. status bar) are factored in
            cheatSheet.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, viewCenterX
                    - screenWidth / 2, screenPos[1] - displayFrame.top + viewHeight);
        } else {
            // Show above
            // Offsets are after decorations (e.g. status bar) are factored in
            cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, viewCenterX
                    - screenWidth / 2, displayFrame.bottom - screenPos[1]);
        }
        cheatSheet.show();
    }
    /**
     * Execute an {@link AsyncTask} on a thread pool
     * 
     * @param forceSerial True to force the task to run in serial order
     * @param task Task to execute
     * @param args Optional arguments to pass to
     *            {@link AsyncTask#execute(Object[])}
     * @param <T> Task argument type
     */
    @SuppressLint("NewApi")
    public static <T> void execute(final boolean forceSerial, final AsyncTask<T, ?, ?> task,
            final T... args) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.DONUT) {
            throw new UnsupportedOperationException(
                    "This class can only be used on API 4 and newer.");
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB || forceSerial) {
            task.execute(args);
        } else {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args);
        }
    }

    /**
     * Runs a piece of code after the next layout run
     * 
     * @param view The {@link View} used.
     * @param runnable The {@link Runnable} used after the next layout run
     */
    @SuppressLint("NewApi")
    public static void doAfterLayout(final View view, final Runnable runnable) {
        final OnGlobalLayoutListener listener = new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                /* Layout pass done, unregister for further events */
                if (hasJellyBean()) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                runnable.run();
            }
        };
        view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }
    
	public static String getHashKey(Context context)
	{
		// Add code to print out the key hash
		try
		{
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature: info.signatures)
			{
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				return android.util.Base64.encodeToString(md.digest(), android.util.Base64.DEFAULT);
			}
		}
		catch (NameNotFoundException e)	{

		}
		catch (NoSuchAlgorithmException e) {

		}
		catch (Exception e) {
			
		}
		return null;
	}
	
	public static String getDeviceId(Context context) {
		String myAndroidDeviceId = "UnknowDeviceId";
	    
		try {
			if(context != null) {
				TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			    if (mTelephony.getDeviceId() != null){
			        myAndroidDeviceId = mTelephony.getDeviceId(); 
			    }else{
			        myAndroidDeviceId = getUuid(context);//Secure.getString(context.getApplicationContext().getContentResolver(), Secure.ANDROID_ID); 
			    }
				//return myAndroidDeviceId;
			}
		}
		catch (Exception e) {
			
		}
		return myAndroidDeviceId;
	}
	
	public static String getDeviceName(Context context) {
		return null;
	}
	
	public static String getAppVersionName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(getPackageName(context), 0).versionName;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static int getAppVersionCode(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(getPackageName(context), 0).versionCode;
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static String getPackageName(Context context) {
		try {
			return context.getPackageName();
		}
		catch (Exception e) {}
		return null;
	}
	
	public static String getSimOperator(Context context) {
		if (context != null) try {
			TelephonyManager telMgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			return telMgr.getSimOperator();
		} catch (Exception e) {}
		return null;
	}
	
	public static String getSimOperatorName(Context context) {
		if (context != null) try {
			TelephonyManager telMgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			return telMgr.getSimOperatorName();
		} catch (Exception e) {}
		return null;
	}
	
	public static String getSimSerial(Context context) {
		if (context != null) try {
			TelephonyManager telMgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			return telMgr.getSimSerialNumber();
		} catch (Exception e) {}
		return null;
	}
	
	public static String findAnEmail(Context context) {
		try {
			Pattern mEmailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
			Account[] accounts = AccountManager.get(context).getAccounts();
			for (Account account : accounts) {
				if (mEmailPattern.matcher(account.name).matches()) {
					String possibleEmail = account.name;
					return possibleEmail;
				}
			}
		}
		catch (Exception e) {
			
		}
		return null;
	}
	
	public static String getNetworkOperatorName(Context context) {
		try {
			TelephonyManager telephonyManager =((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
			return telephonyManager.getNetworkOperatorName();
		} catch (Exception e) {
		}
		return null;
	}
	
	public static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes());

			byte byteData[] = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}

			// convert the byte to hex format method 2
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			return "";
		}
	}
	private static byte[] getKeyBytes() {
		return Base64.keyBytes;
	}
	
    // encrypt the clearText, base64 encode the cipher text and return it.
    public static String encrypt(String clearText) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(getKeyBytes(), "AES");
 
        // init cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] cipherText = cipher.doFinal(clearText.getBytes());
        return android.util.Base64.encodeToString(cipherText, android.util.Base64.DEFAULT);
    }
    
    // decrypt our resource string back into it's source format.
    public static String decrypt(String cipherText) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(getKeyBytes(), "AES");
 
        // init cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] clearText = cipher.doFinal(android.util.Base64.decode(cipherText.getBytes(), android.util.Base64.DEFAULT));
 
        return new String(clearText);
    }

    public static boolean hasMarketInstalled(Context context) {
        try {
			Intent market = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName(context)));
			PackageManager manager = context.getPackageManager();
			List<ResolveInfo> list = manager.queryIntentActivities(market, 0);

			if (list != null && list.size() > 0) {
			    return true;
			}
		} catch (Exception e) {}
        
        return false;
    }
    
    public static boolean isDebugMode(Context context) {
    	if (context == null) {
    		return false;
    	}
    	return ( 0 == ( context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
    }
    
    /**
     * Get the device's Universally Unique Identifier (UUID).
     *
     * @return
     */
    public static String getUuid(Context context) {
        String uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return uuid;
    }

    public static void lockOrientationPortrait(Activity act) {
		if (act != null) {
			act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
	
	public static void lockOrientationLandscape(Activity act) {
		if (act != null) {
			act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	/* Resources */
	public static int getResource(Context context, String type, String name) {
		if (context == null) {
			return 0;
		}
		return context.getResources().getIdentifier(name, type, context.getPackageName());
	}
	
	public static int getIdByName(Context context, String name) {
		return getResource(context, "id", name);
	}
	
	public static int getDrawableIdByName(Context context, String name) {
		return getResource(context, "drawable", name);
	}
	
	public static int getLayoutIdByName(Context context, String name) {
		return getResource(context, "layout", name);
	}
	
	public static int getRawIdByName(Context context, String name) {
		return getResource(context, "raw", name);
	}
	
	public static int getStringIdByName(Context context, String name) {
		return getResource(context, "string", name);
	}
	
	public static int getColorIdByName(Context context, String name) {
		return getResource(context, "color", name);
	}
	
	public static String getStringByName(Context context, String name) {
		return context.getString(getStringIdByName(context, name));
	}

	public static String getRawText(Context ctx, int rawId) {
	    InputStream inputStream = ctx.getResources().openRawResource(rawId);

	    InputStreamReader inputreader = new InputStreamReader(inputStream);
	    BufferedReader buffreader = new BufferedReader(inputreader);
	    String line;
	    StringBuilder text = new StringBuilder();

	    try {
	        while (( line = buffreader.readLine()) != null) {
	            text.append(line);
	        }
	    } catch (IOException e) {
	        return null;
	    }
	    return text.toString();
	}

	public static int getScreenWidth(Context context) {
		try {
			return context.getResources().getDisplayMetrics().widthPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int getScreenHeight(Context context) {
		try {
			return context.getResources().getDisplayMetrics().heightPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static boolean isOnMainThread() {
		return Looper.myLooper() == Looper.getMainLooper();
	}
	
	public static int dp2px(Context context, int dp) {
		Resources r = context.getResources();
		int px = (int) TypedValue.applyDimension(
		        TypedValue.COMPLEX_UNIT_DIP,
		        dp, 
		        r.getDisplayMetrics()
		);
		return px;
	}
	
	public static File getRawCachedFile(Context context, int rawId) {
		InputStream is = null;
        try {
            is = context.getResources().openRawResource(rawId);
            String outPath = context.getCacheDir() + "/tmp" + rawId + ".raw";
            File file = new File(outPath);
            if (file.exists()) {
            	return file;
            }
            byte[] buffer = new byte[is.available()];
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));

            int l = 0;
            while((l = is.read(buffer)) > 0)
                bos.write(buffer, 0, l);

            bos.close();
            return file;
        }
        catch(NotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
    public static <T extends View> T getView(View view, int id) {
        SparseArray<T> viewHolder = (SparseArray<T>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<T>();
            view.setTag(viewHolder);
        }
        T childView = viewHolder.get(id);
        if (childView == null) {
            childView = (T)view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
    	try {
    		context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return context.getPackageManager().getApplicationInfo(packageName, 0).enabled;
        }
        catch (Exception e) {
            
        }
    	return false;
    }
    
    public static void openAppMarket(Context context, String packageName) {
    	if (context == null) {
    		return;
    	}
    	try {
    	    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
    	} catch (android.content.ActivityNotFoundException anfe) {
    	    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
    	}
    }
    
    /**
     * Get IP address from first non-localhost interface
     * @param ipv4  true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4) 
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { 
        
        } // for now eat exceptions
        return "";
    }
}
