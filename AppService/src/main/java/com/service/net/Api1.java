package com.service.net;

import com.orhanobut.logger.Logger;
import com.service.base.DebugConfig;
import com.service.net.converter.GsonConverterFactory;
import com.service.net.utils.CustomTrust;

import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 说明 1：
 * 由于采用自定义错误码方式，所以后台响应成功时，需区分响应成功后判断根据响应内容判断接口成功失败，
 * 所以需自定义GsonConverter去除响应的Gson转换。（注意：代码中用的到并非完全的GsonConverter开源库）
 * <p>
 * 说明 2：
 * 测试https证书强校验(单向校验,仅客户端校验服务端证书)
 */
public class Api1 {

    private static final String BASE_URL = DebugConfig.getBaseUrl();

    private static final long TIMEOUT = 30;

    private static OkHttpClient mClient;
    private static Retrofit mRetrofit;
    private static ApiService1 mApiService;

    private static ApiService1 getApi() {
        if (null == mClient) {
            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;
            try {
                trustManager = CustomTrust.trustManagerForCertificates(CustomTrust.trustedCertificatesInputStream());
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, null);
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }

            mClient = new OkHttpClient().newBuilder()
                    .retryOnConnectionFailure(true)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();
        }

        if (null == mRetrofit) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl("https://publicobject.com/")
                    .client(mClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        if (null == mApiService) {
            mApiService = mRetrofit.create(ApiService1.class);
        }

        return mApiService;
    }

    @SuppressWarnings("unused")
    public static void testHttps() {
        //noinspection NullableProblems
        Api1.getApi().testHttps().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
}


