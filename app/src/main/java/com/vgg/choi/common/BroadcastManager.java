package com.vgg.choi.common;

import java.util.HashMap;
import java.util.Map;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

public class BroadcastManager {
	static private Map<String, BroadcastReceiver> gameDownloadStateBroadcast;
	static private Context mContext;
	static public void onCreate(Context context) {
		gameDownloadStateBroadcast = new HashMap<String, BroadcastReceiver>();
		mContext = context.getApplicationContext();
	}
	static public void registerDownloadBroadcast(String id, BroadcastReceiver br) {
		mContext.registerReceiver(br, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		gameDownloadStateBroadcast.put(id, br);
	}
	static public BroadcastReceiver getDownloadBroadcast(String id) {
		return gameDownloadStateBroadcast.get(id);
	}
	static public void unregisterDownloadBroadcast(String id) {
		try {
			mContext.unregisterReceiver(gameDownloadStateBroadcast.get(id));
			gameDownloadStateBroadcast.remove(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static public void onDestroy(Context context) {
		try {
			for (String key: gameDownloadStateBroadcast.keySet()) {
				mContext.unregisterReceiver(gameDownloadStateBroadcast.get(key));
				gameDownloadStateBroadcast.remove(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mContext = null;
			gameDownloadStateBroadcast = null;
		}
	}
}
