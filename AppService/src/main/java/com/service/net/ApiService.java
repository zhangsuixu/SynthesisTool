package com.service.net;


import com.service.base.BaseBean;
import com.service.bean.AppInfo;
import com.service.bean.AppTypeList;
import com.service.bean.FileListInfo;
import com.service.bean.LoggerList;
import com.service.bean.MainPageParam;
import com.service.bean.TokenBean;
import com.service.bean.UserInfoResponse;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Api接口集
 */
public interface ApiService {

    /*********************   基本使用案例   start       *********************/
    //     1. 定义以下自定义消息头进行控制数据是否加解密 (注:会在拦截器中将该消息头去除)
    //     isDecode = YES 加密
    //     isDecode = NO  不加密
    //     当前默认值不加密
    //     @Headers({"isDecode:YES"})
    //
    //     2. 定义一下自定义消息头控制是否响应数据通过假数据返回 (注:会在拦截器中将该消息头去除)
    //     isUseLocalData = YES 使用本地数据
    //     isUseLocalData = NO  不使用本地数据
    //     默认不使用本地数据
    //     @Headers({"isUseLocalData:YES"})
    //
    //     3.  1和2一起使用案例
    //     @Headers({"isUseLocalData:YES"})
    //     @Headers({"isDecode:YES"})
    //     @Headers({"isUseLocalData:YES","isDecode:YES"})
    //     @POST("api/account/applyToken")
    //     Call<TokenBean> getAppToken(@Body Map<String,Object> body);
    /*********************   基本使用案例   start       *********************/

    /*****************   与baseurl不同url的使用案例   start       ****************/
    // 若响应内容非{"code":"-10001","message":"Token失效","result":"fail","data":null}标准格式，需自定义ResponseCallback进行数据解析(定制方式参考ResponseCallback.java)
    //@POST("xxx/xxx/xxx")
    //Callback<BaseBean> getXxx(@Url String url,@Body Map<String, Object> body);
    /*****************   与baseurl不同url的使用案例   end        ****************/

    /*****************   Get请求使用案例   start       ****************/
    //    @GET("group/{id}/users")
    //    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    //    @GET("group/{id}/users")
    //    Call<List<User>> groupList(@Path("id") int groupId, @QueryMap Map<String, String> options);

    /*****************   Get请求使用案例   end       ****************/


    @POST("api/account/applyToken")
    Call<TokenBean> getAppToken(@Body Map<String, Object> body);

    //用户登录
    @POST("api/account/userLogin")
    Call<UserInfoResponse> userLogin(@Body Map<String, Object> body);

    @GET("api/account/userLogin")
    Call<UserInfoResponse> testGet(@Query("sort") String sort);

    @POST("WitHome/apk/getList")
    Call<FileListInfo> getFileList(@Body Map<String, Object> body);

    @POST("WitHome/DeleteFileListApp")
    Call<BaseBean> clearFiles(@Body Map<String, Object> body);

    @POST("WitHome/DeleteFileApp")
    Call<BaseBean> deleteFile(@Body Map<String, Object> body);

    @POST("WitHome/logger/deletes")
    Call<BaseBean> deleteLog(@Body Map<String, Object> body);

    @Multipart
    @POST("WitHome/logger/upload")
    Call<BaseBean> uploadFile1(@Part("description") RequestBody description,
                               @Part MultipartBody.Part file);

    @POST("WitHome/logger/getList")
    Call<LoggerList> getLoggerList(@Body Map<String, Object> body);

    @POST("WitHome/gitlab/appTypelist")
    Call<AppTypeList> getAppTypeList();

    @POST("WitHome/gitlab/applist")
    Call<AppInfo> getAppList(@Body Map<String, Object> body);

    @POST("WitHome/gitlab/appDelete")
    Call<BaseBean> appDelete(@Body Map<String, Object> body);

    @POST("WitHome/gitlab/appTypeAdd")
    Call<BaseBean> addAppType(@Body Map<String, Object> body);

    @POST("WitHome/gitlab/appTypeAdds")
    Call<BaseBean> addAppTypes(@Body Map<String, Object> body);

    @Multipart
    @POST("拼接参数")
    Call<BaseBean> uploadFile2(@PartMap HashMap<String, RequestBody> bodyMap);

    @POST("WitHome/gitlab/appAdd")
    Call<BaseBean> addApp(@Body Map<String, Object> body);

    @POST("WitHome/gitlab/fastAppAdd")
    Call<BaseBean> addApps(@Body Map<String, Object> body);

    @Headers({"isUseLocalData:YES"})
    @POST("WitHome/homePage/main")
    Call<MainPageParam> initHomePage(@Body Map<String,Object> body);
}
