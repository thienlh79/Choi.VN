package com.kazan.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class TypefaceUtil {
	static Typeface normalFont;
	static Typeface titleFont;
	
	public static void setNormalTypeface(Typeface typeface) {
		normalFont = typeface;
	}
	public static void setNormalTypeface(View view) {
		try {
			((TextView)view).setTypeface(normalFont);
		} catch (Exception e) {
			
		}
	}
	public static void setBoldTypeface(Typeface typeface) {
		titleFont = typeface;
	}
	public static void setBoldTypeface(View view) {
		try {
			((TextView) view).setTypeface(titleFont);
		} catch (Exception e) {
			
		}
	}
	public static Typeface getNormalTypeface() {
		return normalFont;
	}
	
	public static Typeface getBoldTypeface() {
		return titleFont;
	}
	
	public static void overrideFont(Context context, String defaultFontNameToOverride, Typeface customFont) {
        try {
            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFont);
        } catch (Exception e) {
            //Log.e("Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
        }
    }
	
	public static void setText(View view, CharSequence text) {
		try {
			TextView textView = (TextView) view;
			if (normalFont != null) {
				textView.setTypeface(normalFont);
			}
			textView.setText(text);
		} catch (Exception e) {
			
		}
	}
	public static void setText(View view, int stringId) {
		try {
			String text = view.getContext().getString(stringId);
			TextView textView = (TextView) view;
			if (normalFont != null) {
				textView.setTypeface(normalFont);
			}
			textView.setText(text);
		} catch (Exception e) {
			
		}
	}
	
	public static void setTextBold(View view, CharSequence text) {
		try {
			TextView textView = (TextView) view;
			if (titleFont != null) {
				textView.setTypeface(titleFont);
			}
			textView.setText(text);
		} catch (Exception e) {
			
		}
	}
}
