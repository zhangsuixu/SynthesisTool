package com.common.tools.sharePref;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.common.base.BaseApp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class AbsSharePref {

    private synchronized SharedPreferences getSharedPreferences() {
        return BaseApp.getContext().getSharedPreferences(getSharedPreferencesName(), Context.MODE_PRIVATE);
    }

    public abstract String getSharedPreferencesName();

    /**
     * 保存布尔值
     */
    public void putBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).apply();
    }

    /**
     * 保存字符串
     */
    public void putString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).apply();

    }

    public void clear() {
        getSharedPreferences().edit().clear().apply();
    }

    /**
     * 保存long型
     */
    public void putLong(String key, long value) {
        getSharedPreferences().edit().putLong(key, value).apply();
    }

    /**
     * 保存int型
     */
    public void putInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).apply();
    }

    /**
     * 保存float型
     */
    public void putFloat(String key, float value) {
        getSharedPreferences().edit().putFloat(key, value).apply();
    }

    /**
     * 获取字符值
     */
    public String getString(String key, String defValue) {
        return getSharedPreferences().getString(key, defValue);
    }

    /**
     * 获取int值
     */
    public int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    /**
     * 获取long值
     */
    public long getLong(String key, long defValue) {
        return getSharedPreferences().getLong(key, defValue);
    }

    /**
     * 获取float值
     */
    public float getFloat(String key, float defValue) {
        return getSharedPreferences().getFloat(key, defValue);
    }

    /**
     * 获取布尔值
     */
    public boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }


    /**
     * 没有找到则自动赋默认值
     */
    public String getString(String key) {
        return getString(key, "");
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }


    /**
     * key = key+userid
     * 将对象进行base64编码后保存到SharePref中
     *
     * @param key
     * @param object 必须Serializable化
     */
    public void putObj(String key, Serializable object) {
        putObj(key, object, true);
    }

    public void putObj(String key, Serializable object, boolean isAddUserId) {
        String myId = getPutObjId();
        if (isAddUserId && !TextUtils.isEmpty(myId)) {
            key = key + "_" + myId;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            // 将对象的转为base64码
            String objBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));

            getSharedPreferences().edit().putString(key, objBase64).apply();
            oos.close();
        } catch (IOException e) {
//			NotSerializableException
            e.printStackTrace();
        }
    }

    protected String getPutObjId() {
        return "";
    }

    /**
     * 将SharePref中经过base64编码的对象读取出来
     */
    public Object getObj(String key) {
        return getObj(key, true);
    }

    /**
     * 将SharePref中经过base64编码的对象读取出来
     */
    public Object getObj(String key, boolean isAddUserId) {
        String myId = getPutObjId();
        if (isAddUserId && !TextUtils.isEmpty(myId)) {
            key = key + "_" + myId;
        }
        String objBase64 = getSharedPreferences().getString(key, null);
        if (TextUtils.isEmpty(objBase64))
            return null;

        // 对Base64格式的字符串进行解码
        byte[] base64Bytes = Base64.decode(objBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);

        ObjectInputStream ois;
        Object obj = null;
        try {
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (TextUtils.isEmpty(obj.toString())) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }
}
