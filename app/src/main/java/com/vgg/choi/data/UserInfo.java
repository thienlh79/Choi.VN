package com.vgg.choi.data;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
	public static class UserActivityLog {
		@SerializedName("Id")
		String id;
		
		@SerializedName("Description")
		String desc;
		
		@SerializedName("ActionType")
		int actionType;
		
		@SerializedName("ObjectType")
		int objectType;
		
		@SerializedName("ObjectTypeName")
		String objectTypeName;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public int getActionType() {
			return actionType;
		}

		public void setActionType(int actionType) {
			this.actionType = actionType;
		}

		public int getObjectType() {
			return objectType;
		}

		public void setObjectType(int objectType) {
			this.objectType = objectType;
		}

		public String getObjectTypeName() {
			return objectTypeName;
		}

		public void setObjectTypeName(String objectTypeName) {
			this.objectTypeName = objectTypeName;
		}
	}
	@SerializedName("AccountName")
	String accountName;
	
	@SerializedName("FullName")
	String fullName;
	
	@SerializedName("Email")
	String email;
	
	@SerializedName("Avatar")
	String avatar;
	
	@SerializedName("Point")
	int point;
	
	@SerializedName("UserLogs")
	UserActivityLog[] userLog;
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getFullName() {
		if (TextUtils.isEmpty(fullName)) {
			return accountName;
		}
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getEmail() {
		if (TextUtils.isEmpty(email)) {
			return "please_update@your_email.com";
		}
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserActivityLog[] getUserLog() {
		return userLog;
	}
	
}
