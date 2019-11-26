package com.service.net.interceptor;

import com.common.tools.LogUtil;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 仅处理Get请求响应的拦截器
 */
public class GetRequestInterceptor implements Interceptor {

    private static final String LOGGER_FORMAT_GET = "%s  %s  \n%s";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (!"GET".equalsIgnoreCase(request.method())) {
            return chain.proceed(request);
        }

        try {
            return chain.proceed(request);
        } catch (Exception e) {
            LogUtil.e(e, "");

            LogUtil.d(String.format(LOGGER_FORMAT_GET, request.method(), request.url().toString(),
                    request.headers().toString()));

            return chain.proceed(request);
        }

    }
}
