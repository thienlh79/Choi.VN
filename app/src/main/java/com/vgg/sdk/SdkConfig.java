package com.vgg.sdk;

import android.content.Context;
import android.util.Log;

import java.util.Date;

/**
 * Created by LeHai on 3/13/2017.
 */

public class SdkConfig implements SdkCodeAndMessage {
    private String mClientKey;
    private String mClientSecret;
    private String mAgencyId;

    private boolean mSandboxMode;

    private SdkTime mTime;
    private SdkHost mSdkHost;
    private Context mContext;

    private SdkConfig() {

    }

    public boolean isSandboxMode() {
        return mSandboxMode;
    }

    public String getClientKey() {
        return mClientKey;
    }

    public String getClientSecret() {
        return mClientSecret;
    }

    public String getAgencyId() {
        return mAgencyId;
    }

    public String getSaltUrl() {
        return mSdkHost.getApi(SDK_IDMGRAPH, "oauth/", "authentication/", "salt");
    }

    public String getSaltLoginUrl() {
        return mSdkHost.getApi(SDK_IDMGRAPH, "oauth/", "authentication/", "login");
    }

    public String getTimeServerUrl() {
        return mSdkHost.getApi(SDK_IDMGRAPH, "oauth/", "login/", "getTime");
    }

    public String getClientTime() {
        return mTime != null? mTime.getTime() : SdkUtil.getUtc();
    }

    public String getAuthorizationUrl() {
        return mSdkHost.getApi(SDK_IDMGRAPH, "oauth/", "accesstoken?");
    }

    public static class Builder{
        private SdkConfig config;

        public Builder() {
            config = new SdkConfig();
        }

        public Builder setContext(Context context) {
            if (context != null) {
                config.mContext = context.getApplicationContext();
            }
            return this;
        }

        public Builder setAgencyId(String agencyId) {
            config.mAgencyId = agencyId;
            return this;
        }

        public Builder setClientKey(String clientKey) {
            config.mClientKey = clientKey;
            return this;
        }

        public Builder setClientSecret(String clientSecret) {
            config.mClientSecret = clientSecret;
            return this;
        }

        public Builder setSandboxMode(boolean mode) {
            config.mSandboxMode = mode;
            return this;
        }

        public SdkConfig build() {
            config.mSdkHost = new SdkHost(config.mSandboxMode);
            config.mTime = new SdkTime(config.getTimeServerUrl());
            Log.d("Salt Url", config.getSaltUrl());
            Log.d("Auth URL", config.getAuthorizationUrl());
            Log.d("SaltLogin URL", config.getSaltLoginUrl());
            Log.d("Server Time URL", config.getTimeServerUrl());
            Log.d("", "");
            Log.d("", "");
            return config;
        }
    }
    static class SdkTime {
        long timeDiff = -1;
        boolean syncTime = false;

        SdkTime(String timeUrl) {
            HttpRequestHelper.requestString(timeUrl, null, new ActionCallback<String>() {
                @Override
                public void onAction(String action) {
                    updateTime(action);
                }
            });
        }

        public String getTime() {
            return SdkUtil.getUtc((int)timeDiff);
        }

        private void updateTime(String newTime) {
            try {
                Date serverDate = SdkUtil.getDateFrom(newTime.replace("\"", ""));
                Date localDate = SdkUtil.getDateFrom(SdkUtil.getUtc((int)timeDiff));
                long t = (serverDate.getTime() - localDate.getTime()) /1000;
                if (Math.abs(t) > 10) {
                    timeDiff = t;
                }
                syncTime = true;
            } catch (Exception e) {
                syncTime = false;
            }
        }
    }

    static class SdkHost implements SdkCodeAndMessage {
        static final String SDK_DOMAIN          = "vgg.vn/";
        static final String LIVE_SCHEME         = "https://";
        static final String SANDBOX_SCHEME      = "http://";

        static final String PROFILE_API         = "";
        static final String HOME_PAGE_API       = "";
        boolean sandbox;
        public SdkHost(boolean sandbox) {
            this.sandbox = sandbox;
        }

        String getApi(String api, String... params) {

            return sandbox?
                    String.format("%s%s.%s.%s%s",SANDBOX_SCHEME, api, SDK_SANDBOX, SDK_DOMAIN, buildParams(params))
                    :
                    String.format("%s%s.%s%s", LIVE_SCHEME, api, SDK_DOMAIN, buildParams(params)) ;
        }

        String buildParams(String... params) {
            String result = "";
            if (params != null) for (int i = 0; i < params.length; i++) {
                result += params[i];
            }
            return result;
        }
    }
}
