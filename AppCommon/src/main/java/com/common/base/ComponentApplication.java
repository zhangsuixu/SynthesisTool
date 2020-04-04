package com.common.base;

import android.app.Application;

/**
 *  1. 规范各组件需要在application中初始化的方法
 *  2. 提供方法获取BaseApp,让各组件通过各自的Application获取BaseApp,降低各组件对BaseApp的直接耦合
 */
public abstract class ComponentApplication {

    protected abstract void init(Application application);

    public abstract void lazyInit(Application application);
}
