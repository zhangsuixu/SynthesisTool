package com.service.net.utils;

import android.text.TextUtils;

import com.common.base.BaseApp;
import com.common.tools.LogUtil;
import com.google.gson.Gson;
import com.service.R;
import com.service.base.BaseBean;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RequestExceptionHandler {

    private static final int CUSTOM_NETWORK_EXCEPTION_INT = 50000;
    private static final String CUSTOM_NETWORK_EXCEPTION_STR = "50000";

    public static Response handlerException(Exception e, Request request) {
        LogUtil.e(e, "");

        BaseBean<String> errorBean = new BaseBean<>();
        errorBean.code = CUSTOM_NETWORK_EXCEPTION_STR;
        errorBean.message = getMessage(e);
        errorBean.result = "fail";

        return new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(CUSTOM_NETWORK_EXCEPTION_INT)
                .message("Unsatisfiable Request (only-if-cached)")
                .body(ResponseBody.create(MediaType.parse("application/json"), new Gson().toJson(errorBean)))
                .sentRequestAtMillis(-1L)
                .receivedResponseAtMillis(System.currentTimeMillis())
                .build();
    }

    public static Response getErrorResponse(Request request, Response response) {
            BaseBean<String> errorBean = new BaseBean<>();
            errorBean.code = String.valueOf(response.code());
            errorBean.message = getMessage(response.code());
            errorBean.result = "fail";

            return new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("Unsatisfiable Request (only-if-cached)")
                    .body(ResponseBody.create(MediaType.parse("application/json"), new Gson().toJson(errorBean)))
                    .sentRequestAtMillis(-1L)
                    .receivedResponseAtMillis(System.currentTimeMillis())
                    .build();
        }

    private static String getMessage(int code) {
        switch (code) {
            case 400:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_400_tips);
            case 401:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_401_tips);
            case 403:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_403_tips);
            case 404:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_404_tips);
            case 405:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_405_tips);
            case 406:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_406_tips);
            case 407:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_407_tips);
            case 408:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_408_tips);
            case 409:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_409_tips);
            case 410:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_410_tips);
            case 411:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_411_tips);
            case 412:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_412_tips);
            case 413:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_413_tips);
            case 414:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_414_tips);
            case 415:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_415_tips);
            case 416:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_416_tips);
            case 417:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_417_tips);
            case 500:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_500_tips);
            case 501:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_501_tips);
            case 502:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_502_tips);
            case 503:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_503_tips);
            case 504:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_504_tips);
            case 505:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_505_tips);
            default:
                return BaseApp.getBaseApp().getResources().getString(R.string.error_message_unknown_reason);
        }
    }

    private static final String ERROR_MESSAGE_60001 = "failed to connect to";
    private static final String ERROR_MESSAGE_60002 = "cleartext communication to";
    private static final String ERROR_MESSAGE_60003 = "timeout";

    /**
     * 获取网络错误原因,转化成用户能理解的文字.
     * <p>
     * TODO 还在增加中,因为很多原因未知
     */
    private static String getMessage(Exception e) {
        String message;

        if (null != e.getCause()) {
            message = e.getCause().getMessage();
        } else {
            message = e.getMessage();
        }

        if (TextUtils.isEmpty(message)) {
            message = "";
        }

        message = message.toLowerCase();

        //java.net.ConnectException: Failed to connect to /47.240.8.235:80
        //failed to connect to /47.240.8.235 (port 80) from /:: (port 0) after 30000ms: connect failed: ENETUNREACH (Network is unreachable)
        if (message.contains(ERROR_MESSAGE_60001)) {
            return BaseApp.getBaseApp().getResources().getString(R.string.error_message_60001_tips);
        }

        if (message.contains(ERROR_MESSAGE_60002)) {//java.net.UnknownServiceException: CLEARTEXT communication to 47.240.8.235 not permitted by network security policy
            return BaseApp.getBaseApp().getResources().getString(R.string.error_message_60002_tips);
        }

        if (message.contains(ERROR_MESSAGE_60003)) {
            return BaseApp.getBaseApp().getResources().getString(R.string.error_message_60003_tips);
        }

        return BaseApp.getBaseApp().getResources().getString(R.string.error_message_unknown_reason);
    }

}
