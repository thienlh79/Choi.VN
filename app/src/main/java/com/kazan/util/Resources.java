package com.kazan.util;

import android.content.Context;

public class Resources {
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
}
