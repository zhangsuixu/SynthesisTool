package com.service.net.interceptor;

import android.text.TextUtils;

import com.common.tools.LogUtil;
import com.service.net.utils.RequestExceptionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**网络请求日志打印*/
public class LoggerInterceptor implements Interceptor {

    private static final String LOGGER_FORMAT_POST = "%s  %s  \n%s \n\n 请求参数:\n%s \n\n 响应数据:\n%s  \n\n格式化响应:\n%s";
    private static final String LOGGER_FORMAT_GET =  "%s  %s  \n%s";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        try{
            Response response = chain.proceed(request);

            if("POST".equalsIgnoreCase(request.method())){

                String bodyStr = "";
                if(notLoggerUrl(request.url().toString())){
                    bodyStr = "数据太长，不进行日志打印";
                }else {
                    RequestBody body = request.body();

                    if (null != body) {
                        Buffer buffer = new Buffer();
                        body.writeTo(buffer);
                        bodyStr = buffer.readString(Charset.forName("UTF-8"));
                    }
                }

                if(TextUtils.isEmpty(bodyStr)) bodyStr = "无请求体";

                String responseBody = response.peekBody(1024 * 1024).string();
                LogUtil.d(String.format(LOGGER_FORMAT_POST, request.method(), request.url().toString(),
                        request.headers().toString(), bodyStr, responseBody, formatJsonStr(responseBody)));
                return response;
            }

            if ("GET".equalsIgnoreCase(request.method())){
                LogUtil.d(String.format(LOGGER_FORMAT_GET, request.method(), request.url().toString(),
                        request.headers().toString()));
                return response;
            }
        }catch (Exception e){
            return RequestExceptionHandler.handlerException(e, request);
        }

        return chain.proceed(request);
    }

    private static final int JSON_INDENT = 2;
    private String formatJsonStr(String json) {
        if (TextUtils.isEmpty(json)) {
            return "Invalid Json";
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                return jsonObject.toString(JSON_INDENT);
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                return jsonArray.toString(JSON_INDENT);
            }
            return "Invalid Json";
        } catch (Exception e) {
            return "Invalid Json";
        }
    }

    /*** 部分数据过长，导致日志写操作会严重影响界面流畅性，不进行日志打印。*/
    private boolean notLoggerUrl(String url){
        return false;
    }
}
