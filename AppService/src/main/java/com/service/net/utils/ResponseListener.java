package com.service.net.utils;

import com.service.base.BaseBean;

public interface ResponseListener<T extends BaseBean> {

    void onSuccess(T data);

    void onError(BaseBean errorBean);
}
