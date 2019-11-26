package com.service.base;

import java.io.Serializable;

/**
 * 时间 :  2019/6/26;
 * 版本号 :
 */
public class UserBean implements Serializable {

    public String result;
    public String msg;
    public Data data;

    public class Data{
        public String test;
    }
}
