package com.vgg.choi.data.host;

import com.vgg.choi.data.BadgeInfo;
import com.vgg.choi.data.GameItem;
import com.vgg.choi.data.GiftItem;
import com.vgg.choi.data.MissionItem;
import com.vgg.sdk.ActionCallback;
import com.vgg.sdk.ApiObject;
import com.vgg.sdk.HttpRequest;
import com.vgg.sdk.SdkCodeAndMessage;

import okhttp3.FormBody;

/**
 * Created by LeHai on 1/16/2017.
 */

public enum CachedData {
    INSTANCE;
    GameItem[] gameItems;
    MissionItem[] missionItems;
    GiftItem[] giftItems;
    BadgeInfo badgeInfo;

    HostInfo hostInfo;

    public void load(boolean sandbox) {
        hostInfo = new HostInfo(sandbox);
        getGameItems(null, true);
        getEvenItems(null, true);
        getGiftItems(null, true);
        getUnreadInfo(null, null, true);
    }
    public synchronized void getGameItems(final ActionCallback<GameItem[]> callback, boolean forceload) {
        if (gameItems == null || forceload) {
            HttpRequest.requestApi(hostInfo.getGameListUrl(), null, new ActionCallback<ApiObject>() {
                @Override
                public void onAction(ApiObject action) {
                    if (action != null && action.getCode() == SdkCodeAndMessage.RESULT_SUCCESS) {
                        gameItems = action.getData(GameItem[].class);
                        //Log.d("GameItems", new Gson().toJson(gameItems));
                        if (callback != null) {
                            callback.onAction(gameItems);
                        }
                    }
                }
            });
        } else if (callback != null) {
            callback.onAction(gameItems);
        }
    }
    public synchronized void getEvenItems(final  ActionCallback<MissionItem[]> callback, boolean forceload) {
        if (missionItems == null || forceload) {
            HttpRequest.requestApi(hostInfo.getMissionListUrl(), null, new ActionCallback<ApiObject>() {
                @Override
                public void onAction(ApiObject action) {
                    if (action != null && action.getCode() == SdkCodeAndMessage.RESULT_SUCCESS) {
                        missionItems = action.getData(MissionItem[].class);
                        if (callback != null) {
                            callback.onAction(missionItems);
                        }
                    }
                }
            });
        } else if (callback != null) {
            callback.onAction(missionItems);
        }
    }
    public synchronized void getGiftItems(final ActionCallback<GiftItem[]> callback, boolean forceload) {
        if (giftItems == null || forceload) {
            HttpRequest.requestApi(hostInfo.getGiftListUrl(), null, new ActionCallback<ApiObject>() {
                @Override
                public void onAction(ApiObject action) {
                    if (action != null && action.getCode() == SdkCodeAndMessage.RESULT_SUCCESS) {
                        giftItems = action.getData(GiftItem[].class);
                        if (callback != null) {
                            callback.onAction(giftItems);
                        }
                    }
                }
            });
        } else if (callback != null) {
            callback.onAction(giftItems);
        }
    }

    public synchronized void getGift(String id, String token, final ActionCallback<ApiObject> callback) {
        String url = hostInfo.getGiftDetailUrl() + token;
        FormBody body = new FormBody.Builder()
                .add("giftcode_cate_id", id)
                .build();
        HttpRequest.requestApi(url, body, new ActionCallback<ApiObject>() {
            @Override
            public void onAction(ApiObject action) {
                if (callback != null) {
                    callback.onAction(action);
                }
            }
        });
    }

    public synchronized void getGameItemDetail(String id, final ActionCallback<ApiObject> callback) {
        String url = hostInfo.getGameDetailUrl() + id;
        HttpRequest.requestApi(url, null, new ActionCallback<ApiObject>() {
            @Override
            public void onAction(ApiObject action) {
                if (callback != null) {
                    callback.onAction(action);
                }
            }
        });
    }

    public synchronized void getUnreadInfo(String token, final ActionCallback<BadgeInfo> callback, boolean forceLoad) {
        if (badgeInfo == null || forceLoad) {
            String url = hostInfo.getUnreadUrl(token);
            HttpRequest.requestApi(url, null, new ActionCallback<ApiObject>() {
                @Override
                public void onAction(ApiObject action) {
                    if (action != null && action.getCode() == SdkCodeAndMessage.RESULT_SUCCESS) {
                        badgeInfo = action.getData(BadgeInfo.class);
                        if (callback != null) {
                            callback.onAction(badgeInfo);
                        }
                    }

                }
            });
        } else if (callback != null) {
            callback.onAction(badgeInfo);
        }
    }

    public synchronized void getMissionDetail(String id, final ActionCallback<MissionItem> callback) {
        HttpRequest.requestApi(hostInfo.getMissionDetailUrl() + id, null, new ActionCallback<ApiObject>() {
            @Override
            public void onAction(ApiObject action) {
                if (action != null && action.getCode() == SdkCodeAndMessage.RESULT_SUCCESS) {
                    MissionItem item = action.getData(MissionItem.class);
                    if (callback != null) {
                        callback.onAction(item);
                    }
                }
            }
        });
    }
}
