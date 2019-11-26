package com.common.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Paint;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.common.base.BaseApp;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class CommonUtils {

    /*** 获取版本号*/
    public static String getVersionName() {
        Context context = BaseApp.getContext().getApplicationContext();
        String version = "";
        try {

            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /*** 获取应用程序名称*/
    public static String getAppName() {
        try {
            Context context = BaseApp.getContext().getApplicationContext();
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*** 获取应用程序包名*/
    public static String getPackageName() {
        try {
            Context context = BaseApp.getContext().getApplicationContext();
            String packageName = context.getPackageName();
            return packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getVersionCode() {
        Context context = BaseApp.getContext().getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        String code = "-1";
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            code = String.valueOf(packInfo.versionCode);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return code;
    }

    /*** 获取Android Manifest配置信息，比如极光key，友盟渠道，地图key等等*/
    public static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(key);
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }


    public static boolean isNullOrEmpty(Map map) {
        return map == null || map.isEmpty();
    }


    /*** 判断字符串是否为null或""空字符串*/
    public static boolean isNullOrEmpty(String str) {
        boolean result = false;
        if (null == str || "".equals(str.trim())) {
            result = true;
        }
        return result;
    }

    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static boolean isNullOrEmpty(List<?> list) {
        boolean result = false;
        if (null == list || list.size() == 0) {
            result = true;
        }
        return result;
    }

    public static ArrayList<String> getStringList() {
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            dataList.add("" + i);
        }
        return dataList;
    }

    public static boolean isNotNullOrEmpty(List<?> list) {
        return !isNullOrEmpty(list);
    }


    /*** 打开系统软键盘*/
    public static void openSoftKeyBoard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /*** 关闭系统软键盘*/
    public static void closeSoftKeyBoard(Activity activity) {
        if (activity == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void closeSoftKeyBoard(Window window, Context context) {
        if (window == null || context == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && window.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(window.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /*** 设置textview中划线*/
    public static void setTextViewLineThrough(TextView textView) {
        textView.getPaint().setAntiAlias(true);//抗锯齿
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
//		textView.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
    }


    /*** 开启activity不带参数*/
    public static void startActivity(Context packageContext, Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        packageContext.startActivity(intent);
    }

    /*** 调用系统拨打电话*/
    public static void callPhone(Context mcontext, String phoneStr) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneStr);
        intent.setData(data);
        mcontext.startActivity(intent);
    }

    public static void setTextWithSpan(final TextView tv, final boolean needUnderLine, String str1, final String str2, int str1ColorId, final int str2ColorId, final View.OnClickListener onClickListener) {
        String str3 = str1 + str2;
        SpannableString msp = new SpannableString(str3);
        msp.setSpan(new ForegroundColorSpan(BaseApp.getContext().getResources().getColor(str1ColorId)), 0, str1.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(BaseApp.getContext().getResources().getColor(str2ColorId));       //设置文字颜色
                ds.setUnderlineText(needUnderLine);      //设置下划线
            }

            @Override
            public void onClick(View widget) {
                if (onClickListener != null) {
                    onClickListener.onClick(widget);
                }
            }
        }, str1.length(), str3.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        tv.setText(msp);
    }

    public static void setTextWithSpan(final TextView tv, String str1, final String str2, int str1ColorId, final int str2ColorId) {
        tv.setText(StringUtils.getSpannableString(str1, str2, str1ColorId, str2ColorId));
    }


    private static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /*** 防止按钮重复被点击*/
    public static long lastClickTime;

    /*** 防止按钮重复被点击*/
    private static int lastClickId = 0;

    /*** 防止按钮重复被点击*/
    public static boolean isFastDoubleClick(int clickId) {
        if (clickId == lastClickId) {
            return isFastDoubleClick();
        }

        lastClickTime = System.currentTimeMillis();
        lastClickId = clickId;
        return false;
    }

    //判断是否安装目标应用
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName)
                .exists();
    }

    //去掉字符串空格以后的时间格式如 2017-03-23 21:03:41
    public static String delTime(String time) {
        int index = time.indexOf(" ");
        if (index >= 0) {
            time = time.substring(0, time.indexOf(" "));
        }
        return time;
    }


    public static void turnToBrowser(Context context, String url) {
        if (!TextUtils.isEmpty(url)) {
            final Uri uri = Uri.parse(url);
            final Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        }
    }

    public static void setTextWithSpan(TextView tv, final boolean needUnderLine, String str1, String str2, String str3, String str4, int str1ColorId, final int str2ColorId, final View.OnClickListener onClickListener1, final View.OnClickListener onClickListener2) {
        String str5 = str1 + str2 + str3 + str4;
        SpannableString msp = new SpannableString(str5);
        msp.setSpan(new ForegroundColorSpan(BaseApp.getContext().getResources().getColor(str1ColorId)), 0, str1.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(BaseApp.getContext().getResources().getColor(str2ColorId));
                ds.setUnderlineText(needUnderLine);      //设置下划线
            }

            @Override
            public void onClick(View widget) {
                if (onClickListener1 != null) {
                    onClickListener1.onClick(widget);
                }
            }
        }, str1.length(), (str1 + str2).length(), SPAN_EXCLUSIVE_EXCLUSIVE);

        msp.setSpan(new ForegroundColorSpan(BaseApp.getContext().getResources().getColor(str1ColorId)), (str1 + str2).length(), (str1 + str2 + str3).length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(BaseApp.getContext().getResources().getColor(str2ColorId));
                ds.setUnderlineText(needUnderLine);      //设置下划线
            }

            @Override
            public void onClick(View widget) {
                if (onClickListener2 != null) {
                    onClickListener2.onClick(widget);
                }
            }
        }, (str1 + str2 + str3).length(), str5.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        tv.setText(msp);
    }

    public static void setTextSpan(TextView tv, final boolean needUnderline, String prefix, final String[] protocol, String fDivider, String sDivider, int ordinaryColorId, final int procotolColorId, final View.OnClickListener[] onClickListeners) {
        if (protocol == null || protocol.length == 0) {
            return;
        }
        String str5 = prefix;
        for (int i = 0; i < protocol.length; i++) {
            str5 += protocol[i];
            if (i == 0 && i != protocol.length - 1) {
                str5 += fDivider;
            } else if (i != protocol.length - 1) {
                str5 += sDivider;
            }
        }

        SpannableString msp = new SpannableString(str5);
        msp.setSpan(new ForegroundColorSpan(BaseApp.getContext().getResources().getColor(ordinaryColorId)), 0, prefix.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        int beginIndex = prefix.length();
        for (int i = 0; i < protocol.length; i++) {
            final int temp = i;
            msp.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(BaseApp.getContext().getResources().getColor(procotolColorId));
                    ds.setUnderlineText(needUnderline);      //设置下划线
                }

                @Override
                public void onClick(View widget) {
                    if (onClickListeners[temp] != null) {
                        if (protocol.length != onClickListeners.length) {
                            LogUtil.e("string length not equals click length ,so click not available");
                        } else if (onClickListeners[temp] != null) {
                            onClickListeners[temp].onClick(widget);
                        }
                    }

                }
            }, beginIndex, beginIndex + protocol[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            beginIndex += protocol[i].length();
            if (i == 0 && i != protocol.length - 1) {
                msp.setSpan(new ForegroundColorSpan(BaseApp.getContext().getResources().getColor(ordinaryColorId)), beginIndex, beginIndex + fDivider.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
                beginIndex += fDivider.length();
            } else if (i != protocol.length - 1) {
                msp.setSpan(new ForegroundColorSpan(BaseApp.getContext().getResources().getColor(ordinaryColorId)), beginIndex, beginIndex + sDivider.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
                beginIndex += sDivider.length();
            }
        }
        tv.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        tv.setText(msp);
    }

    public static void setTextSpan(TextView tv, String prefix, final String[] protocol, String fDivider, String sDivider, int ordinaryColorId, final int procotolColorId, final View.OnClickListener[] onClickListeners) {
        setTextSpan(tv, false, prefix, protocol, fDivider, sDivider, ordinaryColorId, procotolColorId, onClickListeners);
    }

    /*** 调用第三方浏览器打开*/
    public static  void openBrowser(Context context,String url){
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            LogUtil.d("componentName = " + componentName.getClassName());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(context.getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();
        }
    }
}
