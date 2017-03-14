package com.vgg.sdk;

import com.google.gson.annotations.SerializedName;
import com.kazan.util.AppsUtils;

import okhttp3.FormBody;

/**
 * Created by LeHai on 1/16/2017.
 */

enum SdkCore {
    INSTANCE;
    private boolean initialized = false;
    private ActionCallback<ApiObject> mCallback;

    private SdkToken mToken;
    private SdkConfig mConfig;

    private String mUserName;
    private String mMd5Pwd;

    public void initialize(SdkConfig config) {
        mConfig = config;
    }

    void loginViaApi(String userName, String md5pwd, ActionCallback<ApiObject> callback) {
        this.mUserName  = userName;
        this.mMd5Pwd    = md5pwd;
        this.mCallback  = callback;
        requestSalt();
    }

    boolean isLogin() {
        try {
            return mToken != null && mToken.getAccountId() > 0;
        } catch (Exception e) {

        }
        return false;
    }

    private void requestSalt() {
        FormBody body = new FormBody.Builder()
                .add("client_key", mConfig.getClientKey())
                .add("account_name", mUserName)
                .build();
        HttpRequestHelper.requestApiObject(mConfig.getSaltUrl(), body, new ActionCallback<ApiObject>() {

            @Override
            public void onAction(ApiObject action) {

                if (action != null && action.getCode() == SdkCodeAndMessage.RESULT_SUCCESS) {
                    requestLoginWithSalt(action.getData());
                } else {
                    postResult(action);
                }
            }
        });
    }

    private void requestLoginWithSalt(String salt) {
        String pwd_md5 = AppsUtils.getMD5(mUserName + mMd5Pwd + salt);
        FormBody body = new FormBody.Builder()
                .add("client_key", mConfig.getClientKey())
                .add("account_name", mUserName)
                .add("account_ip", AppsUtils.getIPAddress(true))
                .add("password_md5", pwd_md5)
                .add("agency_id", mConfig.getAgencyId())
                .build();
        HttpRequestHelper.requestApiObject(mConfig.getSaltLoginUrl(), body, new ActionCallback<ApiObject>() {

            @Override
            public void onAction(ApiObject action) {
                if (action != null && action.getCode() == SdkCodeAndMessage.RESULT_SUCCESS) {
                    loginAuthorization(action.getData());
                } else {
                    postResult(action);
                }
            }

        });
    }

    private void loginAuthorization(String authToken) {
        String clientKey 	= mConfig.getClientKey();
        String clientTime 	= mConfig.getClientTime();//mTime.getTime();
        String clientSecret = mConfig.getClientSecret();
        String sign = clientKey +
                "|" + authToken +
                "|" + clientTime +
                "|" + clientSecret;
        sign = SdkUtil.MD5(sign);

        FormBody params = new FormBody.Builder()
                .add(SdkCodeAndMessage.REQUEST_TOKEN, authToken)
                .add(SdkCodeAndMessage.CLIENT_KEY, clientKey)
                .add(SdkCodeAndMessage.REQUEST_TIME, clientTime)
                .add(SdkCodeAndMessage.SIGN, sign)
                .build();
        String url = mConfig.getAuthorizationUrl();
        HttpRequestHelper.requestApiObject(url, params, new ActionCallback<ApiObject>() {

            @Override
            public void onAction(ApiObject action) {
                try {
                    if (action.getCode() == SdkCodeAndMessage.RESULT_SUCCESS) {
                        updateSdkToken(action.getData(SdkToken.class));
                    }
                } catch (Exception e) {

                } finally {
                    postResult(action);
                }
            }

        });
    }

    private synchronized void updateSdkToken(SdkToken newToken) {
        if (newToken == null) {
            return;
        }
        if (mToken == null || newToken.getAccountId() == mToken.getAccountId()) {
            mToken = newToken;
        }
    }
    private void postResult(ApiObject result) {
        if (mCallback != null) {
            mCallback.onAction(result);
        }
    }

    String getAccessToken() {
        return isLogin()? mToken.getAccessToken() : "";
    }

    String getRefreshToken() {
        return isLogin()? mToken.getRefreshToken() : "";
    }

    private static class SdkToken {
        @SerializedName("account_id")
        long accountId;

        @SerializedName("account_name")
        String accountName;

        @SerializedName("access_token")
        String accessToken;

        @SerializedName("refresh_token")
        String refreshToken;

        @SerializedName("account_pwd")
        String accountPwd;

        public String getAccountPwd() {
            return accountPwd;
        }

        public void setAccountPwd(String accountPwd) {
            this.accountPwd = accountPwd;
        }

        public long getAccountId() {
            return accountId;
        }

        public void setAccountId(long accountId) {
            this.accountId = accountId;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}
