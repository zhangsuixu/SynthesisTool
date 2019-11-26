package com.service.net.utils;

import android.text.TextUtils;

import com.common.base.BaseApp;
import com.common.tools.LogUtil;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *  assets资源文件读取工具
 */
public class AssetsUtils {

    private static final String ENCODING_UTF8 = "utf-8";

    public interface CallBack<T> {
        void onSuccess(T result);

        void onFailure(int errorCode, String errorMsg);
    }

    public static void getLocalData(CallBack callBack , String name, Class cls) {
        String fileName = "txcode_" + name + ".pretend.json";
        InputStream is = asset_getFile(fileName);
        if (is == null) {
//            Logger.d( "文件不存在 : 文件名 " + fileName);
            callBack.onFailure(-1,"文件不存在");
        }

        try {
            String resultString = stream_2String(is, ENCODING_UTF8);
//            Logger.d("模拟数据响应 \n" + "  响应体： \n" + resultString);
            if (resultString != null) {
                callBack.onSuccess(new Gson().fromJson(resultString, cls));
            } else {
                callBack.onFailure(-1, "数据不存在");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getStringLocalData(String name){
        String fileName = "url_" + name + ".pretend.json";
        InputStream is = asset_getFile(fileName);
        if (is == null) {
            LogUtil.d( "文件不存在 : 文件名 " + fileName);
            return "";
        }

        try {
            String resultString = stream_2String(is, ENCODING_UTF8);

            if (TextUtils.isEmpty(resultString)) {
                return "";
            }

            return resultString;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取asset目录下文件
     *
     * @param fileName
     *            文件全称,包含后缀名 例如a.png , x.9.png , yy.jpg
     * @return inputStream
     */
    public static InputStream asset_getFile(String fileName) {
        try {
            return BaseApp.getAsset().open(fileName);
        } catch (IOException e) {
//            Logger.e("asset:" + fileName + ",no exist");
        }
        return null;
    }

    /**
     * 将InputStream 转换为String
     *
     * @param is 字节流
     * @param encoding 编码格式
     */
    private static String stream_2String(InputStream is, String encoding) throws IOException {
        if (is == null)
            return "";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        baos.close();

        return baos.toString(encoding);
    }

}
