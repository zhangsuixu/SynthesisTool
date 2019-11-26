package com.common.tools;

public interface CallBack<T> {
    void onSuccess(T result);

    void onFailure(int errorCode, String errorMsg);
}
