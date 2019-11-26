package com.service.net.utils;

import com.service.base.BaseBean;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseRequestAgent {

    public interface ResponseListener<T extends BaseBean>{
        void onSuccess(T response);

        void onError(BaseBean errorBean);
    }

    /**获取请求参数*/
    public static Map<String, Object> getRequestParamsObject(String[] keys, Object[] values) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (keys != null) {
            for (int i = 0; i < keys.length; i++) {
                if (values[i] != null) {
                    params.put(keys[i], values[i]);
                }
            }
        }

        return params;
    }

    /**获取请求参数*/
    public static Map<String, String> getRequestParams(String[] keys, String[] values) {
        Map<String, String> params = new HashMap<String, String>();
        if (keys != null) {
            for (int i = 0; i < keys.length; i++) {
                if (values[i] != null) {
                    params.put(keys[i], values[i]);
                }
            }
        }

        return params;
    }
}
