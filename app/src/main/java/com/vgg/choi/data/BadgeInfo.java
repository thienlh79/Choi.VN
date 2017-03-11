package com.vgg.choi.data;

import com.google.gson.annotations.SerializedName;

public class BadgeInfo {
	@SerializedName("appIds")
	int[] appIds;
	@SerializedName("offerIds")
	int[] offersId;
	@SerializedName("giftcodeCateIds")
	int[] giftIds;
	
	public int[] getAppIds() {
		return appIds;
	}
	public int[] getOfferIds() {
		return offersId;
	}
	public int[] getGiftIds() {
		return giftIds;
	}
	public int count() {
		int total = 0;
		if (appIds != null) {
			total += appIds.length;
		}
		if (offersId != null) {
			total += offersId.length;
		}
		if (giftIds != null) {
			total += giftIds.length;
		}
		return total;
	}
	public int[] getData(int position) {
		switch (position) {
		case 0:
			return appIds;
		case 1:
			return offersId;
		case 2: 
			return giftIds;
		}
		return null;
	}
}
