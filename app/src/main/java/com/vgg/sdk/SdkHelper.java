package com.vgg.sdk;

/**
 * Created by LeHai on 1/16/2017.
 */

public class SdkHelper {
    public boolean isLogin() {
        return SdkCore.INSTANCE.isLogin();
    }

    public String getAccessToken() {
        return SdkCore.INSTANCE.getAccessToken();
    }

    public String getRefreshToken() {
        return SdkCore.INSTANCE.getRefreshToken();
    }

    public void login(String userName, String md5pwd, ActionCallback<ApiObject> callback) {
        SdkCore.INSTANCE.loginViaApi(userName, md5pwd, callback);
    }

    protected void setConfig(SdkConfig config) {
        SdkCore.INSTANCE.initialize(config);
    }
}
