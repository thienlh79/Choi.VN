package com.vgg.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by LeHai on 1/16/2017.
 */

public class ApiObject implements Parcelable{

    private ApiObject(Parcel in) {

    }

    public ApiObject() {

    }

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private String data;

    @SerializedName("response_time")
    private String responseTime;

    @SerializedName("sign")
    private String sign;

    private Object tag;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public <T> T getData(Class<T> cls) {
        try {
            if (!TextUtils.isEmpty(data)) {
                return new Gson().fromJson(data, cls);
            }
        } catch (Exception e) {

        }
        return null;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static final Parcelable.Creator<ApiObject> CREATOR = new Parcelable.Creator<ApiObject>(){
        @Override
        public ApiObject createFromParcel(Parcel parcel) {
            return new ApiObject(parcel);
        }

        @Override
        public ApiObject[] newArray(int i) {
            return new ApiObject[i];
        }
    };
}
