package com.service.net.interceptor;

import com.common.tools.CommonUtils;
import com.common.tools.sharePref.SharePrefUtil;
import com.service.net.utils.RequestExceptionHandler;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 添加消息头
 */
public class HeadersInterceptor implements Interceptor {

    private static final String PLATFORM = "0"; //渠道类型 0 : android

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        try {
            if ("POST".equalsIgnoreCase(request.method())) {
                RequestBody body = request.body();
                String bodyStr = "";
                String contentLength = "0";
                if (null != body) {
                    Buffer buffer = new Buffer();
                    body.writeTo(buffer);
                    bodyStr = buffer.readString(Charset.forName("UTF-8"));
                    contentLength = String.valueOf(body.contentLength());
                }

                request = chain.request().newBuilder()
                        .addHeader("deviceId", SharePrefUtil.getUUId())
                        .addHeader("platform", PLATFORM)
                        .addHeader("channelType", "0")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Content-Length", contentLength)
                        .removeHeader("Transfer-Encoding") //移除无用消息头
                        .removeHeader("isUseLocalData")
                        .removeHeader("isDecode")
                        .addHeader("version", CommonUtils.getVersionName() + ":" + CommonUtils.getVersionCode())
                        .build();

            } else if ("GET".equalsIgnoreCase(request.method())) {
                request = chain.request().newBuilder()
                        .addHeader("deviceId", SharePrefUtil.getUUId())
                        .addHeader("platform", PLATFORM)
                        .addHeader("version", CommonUtils.getVersionName() + ":" + CommonUtils.getVersionCode())
                        .removeHeader("Transfer-Encoding") //移除无用消息头
                        .removeHeader("isUseLocalData")
                        .removeHeader("isDecode")
                        .build();
            }
        } catch (Exception e) {
            return RequestExceptionHandler.handlerException(e, request);
        }

        return chain.proceed(request);
    }
}
