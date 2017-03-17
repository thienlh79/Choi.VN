package com.vgg.choi.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.vgg.sdk.ActionCallback;
import com.vgg.sdk.ApiObject;
import com.vgg.sdk.HttpRequestHelper;
import com.vgg.sdk.SdkHelper;

import okhttp3.FormBody;

public class ChoiInstanceIdService extends FirebaseInstanceIdService {
    public ChoiInstanceIdService() {
    }

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FirebaseToken", token);
        SdkHelper sdkHelper = new SdkHelper();
        String url = "http://appmgraph.sandbox.vgg.vn/AppChoi/CreateFcmInfo/?access_token=" + sdkHelper.getAccessToken();
        FormBody body = new FormBody.Builder()
                .add("fcm_token", token)
                .build();
        HttpRequestHelper.requestApiObject(url, body, new ActionCallback<ApiObject>() {
            @Override
            public void onAction(ApiObject action) {

            }
        });
        super.onTokenRefresh();
    }
}
