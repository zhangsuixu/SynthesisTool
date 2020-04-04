package com.service.net.interceptor;

import android.text.TextUtils;

import com.common.tools.LogUtil;
import com.service.net.utils.AssetsUtils;
import com.service.net.utils.RequestExceptionHandler;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * 仅处理Post请求响应的拦截器
 * <p>
 * 假数据使用说明
 * <p>
 * 1.于ApiService中对应请求增加 @Headers({"isUseLocalData","YES"}) ，如下方式：
 *
 * @POST("api/account/applyToken")
 * @Headers({"isUseLocalData","YES"}) Call<TokenBean> getAppToken(@Body Map<String, Object> body);
 * <p>
 * 2.于assets中新增如下的文件，其中account_applyToken随url变更而变更，取url后两位进进拼接
 * url_account_applyToken.pretend.json
 * <p>
 * 3.响应成功与失败
 * 通过对应假数据文件url_account_applyToken.pretend.json 里的 result进行控制
 * "result": "success"  响应成功
 * "result": "fail"     响应失败
 */
@SuppressWarnings("all")
public class PostRequestInterceptor implements Interceptor {

    private static final String LOGGER_FORMAT = "假数据 ：\n%s \n%s";
    private static final String LOCAL_DATA_URL = "%s_%s";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        try {
            //该拦截器仅处理Post请求
            if (!"POST".equalsIgnoreCase(request.method())) {
                return chain.proceed(request);
            }

            Response response = chain.proceed(request);


            /*************************   使用假数据 start  *********************************/
            String isUseLocalData = request.headers().get("isUseLocalData");
            if (!TextUtils.isEmpty(isUseLocalData) && "YES".equals(isUseLocalData)) {//存在使用假数据返回的消息头
                String url = request.url().toString();

                String responseDataStr = "";
                if (!TextUtils.isEmpty(url)) {//读取本地假数据
                    String[] sp = url.split("/");
                    responseDataStr = AssetsUtils.getStringLocalData(String.format(LOCAL_DATA_URL, sp[sp.length - 2], sp[sp.length - 1]));
                }

                if (!TextUtils.isEmpty(responseDataStr)) {//通过假数据构造ResponseBody
                    response = response.newBuilder()
                            .code(200)
                            .body(ResponseBody.create(MediaType.parse("application/json"), responseDataStr))
                            .build();

                    LogUtil.d(String.format(LOGGER_FORMAT, request.url().toString(), response.peekBody(1024 * 1024).string()));
                }

                return response;
            }


            /*************************   数据加密操作 start  *********************************/
            if(response.code() == 200){
                RequestBody body = request.body();
                String bodyStr = "";
                if (null != body) {
                    Buffer buffer = new Buffer();
                    body.writeTo(buffer);
                    bodyStr = buffer.readString(Charset.forName("UTF-8"));
                }

//            LogUtil.d("\n 消息体 : \n" + bodyStr);

                if ("{}".equalsIgnoreCase(bodyStr)) {
                    request = request.newBuilder()
//                            .method(request.method(), RequestBody.create(MediaType.parse("application/json"), "{\"xxx\": \"xxx\"}"))
                            .method(request.method(), RequestBody.create(MediaType.parse("application/json"), ""))
                            .build();
                }

//                if (!TextUtils.isEmpty(bodyStr)) { //将请求数据进行加密,下面只是打个比喻
//                    bodyStr = bodyStr + "进行加密";
//                    request = request.newBuilder()
//                            .method(request.method(), RequestBody.create(MediaType.parse("application/json"), bodyStr))
//                            .build();
//                }


                /*************************   数据解密操作 start  *********************************/
                ResponseBody responseBody = response.body();
//
                if (null != responseBody) {//组件自定义异常消息体返回
//                String responseBodyStr = response.peekBody(1024 * 1024).string();//注意 ： body.string()只能调用一次，因为调用后，响应流会被关闭,
//                LogUtil.d("原数据响应体" + responseBodyStr);
//                String replaceBody = bodyStr.replace("message", "message1");
//
//                ResponseBody.create(MediaType.parse("application/json"), replaceBody);
//                return response.newBuilder()
//                        .body(ResponseBody.create(MediaType.parse("application/json"), replaceBody))
//                        .build();
                }

                return response;
            }

            return RequestExceptionHandler.getErrorResponse(request,response);
        } catch (Exception e) {
            return RequestExceptionHandler.handlerException(e, request);
        }
    }
}