package com.service.net;

import android.text.TextUtils;

import com.common.tools.ToastUtils;
import com.common.tools.sharePref.SharePrefUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.service.base.BaseBean;
import com.service.base.DebugConfig;
import com.service.bean.AppInfo;
import com.service.bean.AppTypeList;
import com.service.bean.FileListInfo;
import com.service.bean.LoggerList;
import com.service.bean.MainPageParam;
import com.service.bean.OriginApp;
import com.service.bean.TokenBean;
import com.service.bean.UserInfoResponse;
import com.service.net.converter.GsonConverterFactory;
import com.service.net.callback.ResponseCallback;
import com.service.net.interceptor.GetRequestInterceptor;
import com.service.net.interceptor.HeadersInterceptor;
import com.service.net.interceptor.LoggerInterceptor;
import com.service.net.interceptor.NetExceptionInterceptor;
import com.service.net.interceptor.PostRequestInterceptor;
import com.service.net.utils.AssetsUtils;
import com.service.net.utils.BaseRequestAgent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 说明 ：
 * 由于采用自定义错误码方式，所以后台响应成功时，需区分响应成功后判断根据响应内容判断接口成功失败，
 * 所以需自定义GsonConverter去除响应的Gson转换。（注意：代码中用的到并非完全的GsonConverter开源库）
 */
public class Api {

    private static final String BASE_URL = DebugConfig.getBaseUrl();

    private static final long TIMEOUT = 30;

    private static OkHttpClient mClient;
    private static Retrofit mRetrofit;
    private static ApiService mApiService;

    /**
     * 记录bug : 偶现获取到的retrofit对象为null，所以进行判空重初始化操作.
     */
    private static ApiService getApi() {
        if (null == mClient) {
            mClient = new OkHttpClient().newBuilder()
                    .retryOnConnectionFailure(true)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(new NetExceptionInterceptor())//将网络异常转换成所需错误格式数据的处理拦截器
                    .addInterceptor(new PostRequestInterceptor()) //Post请求通用处理(加解密,假数据等)
                    .addInterceptor(new GetRequestInterceptor())  //Get请求通用处理
                    .addInterceptor(new HeadersInterceptor())     //通用消息头拼接
                    .addInterceptor(new LoggerInterceptor())      //请求日志打印
                    .build();
        }

        if (null == mRetrofit) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(mClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        if (null == mApiService) {
            mApiService = mRetrofit.create(ApiService.class);
        }

        return mApiService;
    }

    public static void initHomePage(final BaseRequestAgent.ResponseListener<MainPageParam> responseListener) {
        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{""}, new Object[]{""});
        getApi().initHomePage(requestParams).enqueue(new ResponseCallback<>(responseListener, MainPageParam.class));
    }

    public static void getAppToken(String userName, final BaseRequestAgent.ResponseListener<TokenBean> responseListener) {
        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{"deviceId", "platform", "userName"}, new Object[]{SharePrefUtil.getUUId(), "0", userName});
        getApi().getAppToken(requestParams).enqueue(new ResponseCallback<>(responseListener, TokenBean.class));
    }

    public static void login(String userName, String password, String pushId, String userLoginType, BaseRequestAgent.ResponseListener<UserInfoResponse> responseListener) {
        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{"deviceId", "password", "pushId", "userName", "userType", "userLoginType"}, new String[]{SharePrefUtil.getUUId(), password, pushId, userName, "0", userLoginType});
        getApi().userLogin(requestParams).enqueue(new ResponseCallback<>(responseListener, UserInfoResponse.class));
    }

    public static void getFileList(String pc, BaseRequestAgent.ResponseListener<FileListInfo> responseListener) {
        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{"pc"}, new String[]{"1"});
        getApi().getFileList(requestParams).enqueue(new ResponseCallback<>( responseListener, FileListInfo.class));
    }

    /**
     * 获取应用类型列表
     */
    public static void getAppTypeList(BaseRequestAgent.ResponseListener<AppTypeList> responseListener) {
        getApi().getAppTypeList().enqueue(new ResponseCallback<>(responseListener, AppTypeList.class));
    }

    /**
     * 获取应用列表
     */
    public static void getAppList(String fileName, String appName, String env, String page, BaseRequestAgent.ResponseListener<AppInfo> responseListener) {
        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{"fileName", "appName", "page", "fileEnvrionment"}
                , new String[]{fileName, appName, page, env});
        getApi().getAppList(requestParams).enqueue(new ResponseCallback<>(responseListener, AppInfo.class));
    }

    /**
     * 删除指定应用
     */
    public static void appDelete(String fileName, String fileVersion, String env, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{"fileName", "fileVersion", "fileEnvrionment"}
                , new String[]{fileName, fileVersion, env});
        getApi().appDelete(requestParams).enqueue(new ResponseCallback<>(responseListener, BaseBean.class));
    }

    /**
     * 添加应用类型
     */
    public static void addAppType(String appName, String fileName, String fileDirs, String filePath, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{"appName", "fileName", "fileDirs", "filePath"},
                new String[]{appName, fileName, fileDirs, filePath});
        getApi().addAppType(requestParams).enqueue(new ResponseCallback<>(responseListener, BaseBean.class));
    }

    /**
     * 更新应用类型
     */
    public static void addAppTypes(BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        ArrayList arrayList = new Gson().fromJson(AssetsUtils.getStringLocalData("addAppTypes"), ArrayList.class);
        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{"appTypes"}, new Object[]{arrayList});
        getApi().addAppTypes(requestParams).enqueue(new ResponseCallback<>(responseListener, BaseBean.class));
    }

    /**
     * 添加应用
     */
    public static void addApp(String fileName, String fileVersion, String fileEnvrionment, String fileDes, String fileTime,
                              BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{"fileName", "fileVersion", "fileEnvrionment", "fileDes", "fileTime"},
                new String[]{fileName, fileVersion, fileEnvrionment, fileDes, fileTime});
        getApi().addApp(requestParams).enqueue(new ResponseCallback<>(responseListener, BaseBean.class));
    }

    public static void addApps(BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        List<OriginApp> apps = new Gson().fromJson(AssetsUtils.getStringLocalData("apps"), new TypeToken<List<OriginApp>>() {}.getType());
        ArrayList<AppInfo.AppItem> allApp = new ArrayList<>();
        AppInfo.AppItem appItem;
        for (int i = 0; i < apps.size(); i++) {
            appItem = new AppInfo.AppItem();
            appItem.fileName = apps.get(i).fileName;
            appItem.fileDes = getDes(apps.get(i).commitBy, apps.get(i).updateContent);
            appItem.fileEnvrionment = getEnvrionment(apps.get(i).fileName);
            appItem.fileTime = getFileTime(apps.get(i).createTime);
            appItem.fileVersion = apps.get(i).versionName;
            appItem.packageName = apps.get(i).appPkgName;
            allApp.add(appItem);
        }

        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{"apps"}, new Object[]{allApp});
        getApi().addApps(requestParams).enqueue(new ResponseCallback<>(responseListener, BaseBean.class));
    }

    private static final String DES_FORMAT = "提交者 : %s \n\n更新内容 : \n%s";

    private static String getDes(String commitBy, String updateContent) {
        return String.format(DES_FORMAT,
                TextUtils.isEmpty(commitBy) ? "未知" : commitBy,
                TextUtils.isEmpty(updateContent) ? "无" : updateContent);
    }

    public static String getFileTime(Long time) {
        try {
            return SimpleDateFormat.getDateInstance(0).format(new Date(time));
//            return new Date().format("yyyyMMdd", time);
//            return SimpleDateFormat.getDateInstance(0).format(time);
        } catch (Exception e) {
            return time + "";
        }
    }

    private static String getEnvrionment(String fileName) {
        if (fileName.contains("online")) {
            return "online";
        }

        if (fileName.contains("test")) {
            return "test";
        }

        if (fileName.contains("dev")) {
            return "dev";
        }

        if (fileName.contains("uat")) {
            return "uat";
        }

        return "";
    }

    public static void getLoggerList(String pc, BaseRequestAgent.ResponseListener<LoggerList> responseListener) {
        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{"pc"}, new String[]{"1"});
        getApi().getLoggerList(requestParams).enqueue(new ResponseCallback<>(responseListener, LoggerList.class));
    }

    public static void clearFiles(BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{"password"}, new String[]{"clearFiles"});
        getApi().clearFiles(requestParams).enqueue(new ResponseCallback<>(responseListener, BaseBean.class));
    }

    /**
     * 删除某个日志文件
     */
    public static void removeLog(String fileName, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{"password", "fileName"}, new String[]{"clearFiles", fileName});
        getApi().deleteLog(requestParams).enqueue(new ResponseCallback<>(responseListener, BaseBean.class));
    }

    public static void deleteFile(String filename, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams = BaseRequestAgent.getRequestParamsObject(new String[]{"filename"}, new String[]{filename});
        getApi().deleteFile(requestParams).enqueue(new ResponseCallback<>(responseListener, BaseBean.class));
    }

    public static void uploadFile1(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);

        String descriptionString = "This is a description";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        getApi().uploadFile1(description, body).enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                ToastUtils.showShortToast("上传成功");
            }

            @Override
            public void onFailure(Call<BaseBean> call, Throwable t) {
                ToastUtils.showShortToast("上传失败");
            }
        });
    }
}


