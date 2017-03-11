package com.vgg.choi.data.host;

import android.text.TextUtils;

/**
 * Created by LeHai on 1/18/2017.
 */

public class HostInfo {
    boolean sandbox = true;
    String host = "http%s://appmgraph.%svgg.vn/";
    String gameListUrl;
    String gameDetailUrl;
    String eventListUrl;
    String eventDetailUrl;
    String giftListUrl;
    String giftDetailUrl;
    String userActivitiesUrl;

    public HostInfo(boolean sandbox) {
        this.sandbox = sandbox;
    }
    //private static final String gameListUrl = "http://appmgraph.sandbox.vgg.vn/AppChoiWi/GetListApp/?os_type=1&page_index=1&page_size=10&total_record=0&hot_page_size=4";
    //private static final String gameDetailUrl = "http://appmgraph.sandbox.vgg.vn/AppChoiWi/GetApp/?app_id=";
    //private static final String userInfoUrl = "http://appmgraph.sandbox.vgg.vn/AppChoi/GetUser/?access_token=";
    //private static final String avatarUpdateUrl = "http://appmgraph.sandbox.vgg.vn/AppChoi/UpdateAvatar/?access_token=";
    //private static final String giftCodeListUrl = "http://appmgraph.sandbox.vgg.vn/AppChoiWi/GetListGiftCode/?os_type=0&page_index=1&page_size=10&total_record=0";
    //private static final String giftCodeDetailUrl = "http://appmgraph.sandbox.vgg.vn//AppChoi/GetGiftCode/?access_token=";
    //private static final String missionListSandboxUrl = "http://appmgraph.sandbox.vgg.vn/AppChoiWi/GetListOffer/?os_type=1&type=0&page_index=1&page_size=10&total_record=0";
    //private static final String missionDetailSandboxUrl = "http://appmgraph.sandbox.vgg.vn/AppChoiWi/GetDetailsOffer/?offer_id=";
    //private static final String missionInitSandboxUrl = "http://appmgraph.sandbox.vgg.vn/AppChoi/GetOffer/?access_token=";
    //private static final String missionCompletedSandboxUrl = "http://appmgraph.sandbox.vgg.vn/AppChoi/CompleteOffer/?access_token=";
    //private static final String unreadCountSandboxWithTokenUrl = "http://appmgraph.sandbox.vgg.vn/AppChoi/GetIdListNotSeen/?access_token=%s&os_Type=1";
    //private static final String unreadCountSandboxUrl = "http://appmgraph.sandbox.vgg.vn/AppChoiWi/GetIdListNotSeen/?os_Type=1";

    public String getHost() {
        return String.format(host, sandbox? "" : "s", sandbox? "sandbox." : "");
    }
    public String getGameListUrl() {
        return getHost() + "AppChoiWi/GetListApp/?os_type=1&page_index=1&page_size=10&total_record=0&hot_page_size=4";
    }
    public String getGameDetailUrl() {
        return getHost() + "AppChoiWi/GetApp/?app_id=";
    }
    public String getMissionListUrl() {
        return getHost() + "AppChoiWi/GetListOffer/?os_type=1&type=0&page_index=1&page_size=10&total_record=0";
    }
    public String getMissionDetailUrl() {
        return getHost() + "AppChoiWi/GetDetailsOffer/?offer_id=";
    }
    public String getGiftListUrl() {
        return getHost() + "AppChoiWi/GetListGiftCode/?os_type=0&page_index=1&page_size=10&total_record=0";
    }
    public String getGiftDetailUrl() {
        return getHost() + "AppChoi/GetGiftCode/?access_token=";
    }
    public String getUserActivitiesUrl() {
        return getHost();
    }
    public String getUnreadUrl(String token) {
        String url = "AppChoiWi/GetIdListNotSeen/?os_Type=1";
        if (!TextUtils.isEmpty(token)) {
            url = String.format("AppChoi/GetIdListNotSeen/?access_token=%s&os_Type=1", token);
        }
        return getHost() + url;
    }
}
