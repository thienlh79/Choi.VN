package com.vgg.sdk;

/**
 * Created by LeHai on 3/7/2017.
 */

public interface SdkCodeAndMessage {
    public static final int	RESULT_SUCCESS				= 0;
    public static final int RESULT_UNKNOW				= -99;
    public static final int	RESULT_ACCESS_TOKEN_TIMEOUT = -103;
    public static final int	RESULT_INVALID_REQUEST_TIME	= -218;
    public static final int	RESULT_ERROR				= -1;
    public static final int RESULT_CANCEL				= -2;
    public static final int RESULT_RETRY				= -3;
    public static final int RESULT_LOGIN				= -4;
    public static final int RESULT_CLOSE_ACTION			= 12345;

    public static final String SDK_ACTION_LOGIN 		= "com.sdk.LOGIN";
    public static final String SDK_ACTION_RECHARGE		= "com.sdk.RECHARGE";
    public static final String SDK_ACTION_PROFILE		= "com.sdk.PROFILE";
    public static final String SDK_ACTION_WEB_LOGIN		= "com.sdk.WEB_LOGIN";
    public static final String SDK_ACTION_WEB_HOMEPAGE	= "com.sdk.WEB_HOMEPAGE";
    public static final String SDK_ACTION_EVENTS_NEWS	= "com.sdk.NEWS";
    public static final String SDK_ACTION_WEB_AWARD		= "com.sdk.AWARDS";

    public static final String SDK_SANDBOX 			= "sandbox";
    public static final String SDK_IDMGRAPH			= "idmgraph";
    public static final String SDK_BILLINGMGRAPH	= "billingmgraph";
    public static final String SDK_ID				= "id";

    public static final String CODE 	= "code";
    public static final String DATA 	= "data";
    public static final String MESSAGE 	= "message";
    public static final String ID 		= "id";
    public static final String ACCOUNT_ID 	= "account_id";
    public static final String CLIENT_KEY 	= "client_key";
    public static final String ACCOUNT_PASS = "up";
    public static final String CLIENT_SECRET 	= "client_secret";
    public static final String ACCESS_TOKEN 	= "access_token";
    public static final String REQUEST_TOKEN 	= "request_token";
    public static final String REFRESH_TOKEN 	= "refresh_token";
    public static final String REQUEST_TIME 	= "request_time";
    public static final String SIGN 		= "sign";
    public static final String AMOUNT 		= "amount";
    public static final String AGENCY_ID 	= "agency_id";
    public static final String SERVER_ID 	= "server_id";
    public static final String PRODUCT_ID 	= "product_id";
    public static final String REF_TRANSFER_ID 			= "reference_trans_id";
    public static final String ORDER_NO 				= "order_no";
    public static final String INAPP_PURCHASE_DATA 		= "inapp_purchase_data";
    public static final String INAPP_DATA_SIGNATURE 	= "inapp_data_signature";
    public static final String EXTRA_DATA 	= "extra_data";
    public static final String CLIENT_IP 	= "client_ip";
    public static final String VGG_COM		= "VgG.C0m";
    public static final String PREF_CONFIG	= "config";
    public static final String LAST_LOGIN	= "last_login";
    public static final String FB_TOKEN = "fb_token";
    public static final String VERSION	= "version";
}
