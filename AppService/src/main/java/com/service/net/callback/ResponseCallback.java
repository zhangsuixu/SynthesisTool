package com.service.net.callback;

import android.text.TextUtils;

import com.common.tools.LogUtil;
import com.common.tools.sharePref.SharePrefUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.service.base.BaseBean;
import com.service.bean.TokenBean;
import com.service.net.Api;
import com.service.net.utils.BaseRequestAgent;
import com.service.net.utils.OtherErrorBody;

import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 对Retrofit2的Callback增加统一加载弹框处理
 *
 *     注意 ：
 *      仅限于响应{"code":"-10001","message":"Token失效","result":"fail","data":null}该格式的处理
 *      若需响应其他格式，需定制新的Callback进行响应数据解析
 *
 */
public class ResponseCallback<T extends BaseBean> implements Callback<T> {

    private BaseRequestAgent.ResponseListener<T> mResponseListener;

    private static final String REPONSE_SUCCESS = "success";
    private String url;
    private int applyTokenCount = 1;
    private Class<T> cls;

    public ResponseCallback(BaseRequestAgent.ResponseListener<T> responseListener, Class<T> cls) {
        mResponseListener = responseListener;

        this.cls = cls;
    }

    public ResponseCallback(BaseRequestAgent.ResponseListener<T> responseListener, Class<T> cls , int applyTokenCount) {
        mResponseListener = responseListener;

        this.cls = cls;

        this.applyTokenCount = applyTokenCount;
    }

    @Override
    public void onResponse(final Call call, final Response response) {
        url = call.request().url().toString();

        Gson gson = new Gson();

        BaseBean baseBean = null;
        String str = "";

        Object body = response.body();

        try {
            if(null != body){
                str = body.toString();
                baseBean = gson.fromJson(str, BaseBean.class);
            }else {

                ResponseBody errorBody = response.errorBody();
                String bodyStr = "";
                if (null != errorBody) {
                    BufferedSource source = errorBody.source();
                    bodyStr = source.buffer().readString(Charset.forName("UTF-8"));
                }

                if(!TextUtils.isEmpty(bodyStr)){
                   OtherErrorBody error = gson.fromJson(bodyStr, OtherErrorBody.class);
                   handlerError(getErrorBean(TextUtils.isEmpty(error.message) ? "" : error.message), call);
                    return;
                }
            }
        }catch (Exception e){
            handlerError(getErrorBean("类型转换异常"), call);
        }

        if (null == baseBean) {
            handlerError(getErrorBean(), call);
            return;
        }

        if (isStatusSuccess(baseBean.result)) {
            try{
                if (null != mResponseListener){
                    mResponseListener.onSuccess(gson.fromJson(str, cls));
                }
            }catch (Exception e){
                handlerError(baseBean, call);
                LogUtil.e(e,"");
            }
        } else {
            handlerError(baseBean, call);
        }
    }

    private BaseBean getErrorBean() {
        return getErrorBean("响应内容异常");
    }

    private BaseBean getErrorBean(String message) {
        BaseBean errorBean = new BaseBean();
        errorBean.message = message;
        errorBean.code = "-1";
        errorBean.result = "fail";
        return errorBean;
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        if (null != mResponseListener) {
            mResponseListener.onError(getErrorBean(t.getMessage()));
        }
    }

    /**
     * 判断token失效
     */
    private boolean isTokenInvalidate(String code) {
        return "-10001".equals(code);
    }

    /**
     * 被服务器强制退出登录
     */
    private boolean isLogoutByServer(String code) {
        return "-11001".equals(code) || "-10006".equals(code);
    }

    private boolean isStatusSuccess(String result) {
        return REPONSE_SUCCESS.equalsIgnoreCase(result);
    }

    private boolean isStatusFail(String result) {
        return !isStatusSuccess(result);
    }

    @SuppressWarnings("all")
    private void handlerError(final BaseBean errorBean, final Call call) {
        if (isStatusFail(errorBean.result) && isTokenInvalidate(errorBean.code) && isNotLoginInterface() && isNotTokenInterface()) {
            Api.getAppToken(SharePrefUtil.getPhone(), new BaseRequestAgent.ResponseListener<TokenBean>() {
                @Override
                public void onSuccess(TokenBean response) {

                    SharePrefUtil.setAppToken(response.data.accessToken); //保存Token

                    if (applyTokenCount == 2) {//如果请求因为token失效发了两次，就放弃了
                        errorBean.message = "请求发了两次都失败了";
                        if (null != mResponseListener) mResponseListener.onError(errorBean);
                        return;
                    }

                    //重新发送这个请求
                    call.clone().enqueue(new ResponseCallback(mResponseListener, cls, 2));
                }

                @Override
                public void onError(BaseBean errorBean) {
                    if (null != mResponseListener) mResponseListener.onError(errorBean);
                }
            });
        } else if (isLogoutByServer(errorBean.code) && isNotLoginInterface()) {
            //除登录接口外，其他接口响应被服务器强制退出登录的code
            //TODO 退出登录
        } else {
            if (null != mResponseListener) mResponseListener.onError(errorBean);
        }
    }

    /**
     * 是否非登录接口
     */
    private boolean isNotLoginInterface() {
        return false;
//        return !ApiService.url_account_userlogin.equalsIgnoreCase(url);
    }

    /**
     * 是否非获取Token接口
     */
    private boolean isNotTokenInterface() {
        return false;
//        return !ApiService.url_account_applyToken.equalsIgnoreCase(url);
    }
}
