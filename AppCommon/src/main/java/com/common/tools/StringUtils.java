package com.common.tools;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.EditText;

import com.common.base.BaseApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtils {

    public static boolean isNotNull(String string) {
        return null != string && !"".equals(string.trim());
    }

    public static boolean isNull(String string) {
        return null == string || "".equals(string.trim());
    }

    /*** 验证是否是11位数字*/
    public static boolean checkPhoneNumberAndTipError(String phoneNum) {
        if (!Validator.isMobile(phoneNum)){
            ToastUtils.showShortToast("请输入正确的手机号码");
            return false;
       }

        return true;
    }

    /*** 验证是否是19位数字*/
    public static boolean isCardNum(String password) {
        Pattern p = Pattern.compile("^0?\\d{19}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /*** 验证邮箱格式是否正确 */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static SpannableString getSpannableString(CharSequence str1, final String str2, int str1ColorId, final int str2ColorId) {
        String str3 = str1 + str2;
        SpannableString msp = new SpannableString(str3);
        msp.setSpan(new ForegroundColorSpan(BaseApp.getContext().getResources().getColor(str1ColorId)), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(BaseApp.getContext().getResources().getColor(str2ColorId)), str1.length(), str3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    public static SpannableString getSpannableStringBold(CharSequence str1, final String str2, int str1ColorId, final int str2ColorId) {
        String str3 = str1 + str2;
        SpannableString msp = new SpannableString(str3);
        msp.setSpan(new ForegroundColorSpan(BaseApp.getContext().getResources().getColor(str1ColorId)), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(BaseApp.getContext().getResources().getColor(str2ColorId)), str1.length(), str3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), str1.length(), str3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //粗体
        return msp;
    }

    public static SpannableString getSizeSpannable(CharSequence str1, final String str2, int str1Dip, final int str2Dip) {
        String str3 = str1 + str2;
        SpannableString msp = new SpannableString(str3);
        msp.setSpan(new AbsoluteSizeSpan(str1Dip, true), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new AbsoluteSizeSpan(str2Dip, true), str1.length(), str3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    public static SpannableString getColorSpannable(CharSequence str1, CharSequence str2, int str1ColorId, int str2ColorId) {
        String str3 = str1.toString() + str2.toString();
        SpannableString msp = new SpannableString(str3);
        msp.setSpan(new ForegroundColorSpan(BaseApp.getContext().getResources().getColor(str1ColorId)), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(BaseApp.getContext().getResources().getColor(str2ColorId)), str1.length(), str3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    /*** 读取地址Json*/
    public static String loadAddress(Context context) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = context.openFileInput("addressJson");//文件名
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    /*** 保存地址json至本地*/
    public static void saveAddress(Context context, String addressJson) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = context.openFileOutput("addressJson", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(addressJson);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	/*** 保留小数*/
    public static String format2(Float value) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }
    /*** 保留小数*/
    public static String format2(String value) {
        try{
            double dv = Double.parseDouble(value);
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df.format(dv);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /*** 昵称是否含有特殊字符*/
    public static boolean checkUserNameAndTipError(String nickname) {
        if (TextUtils.isEmpty(nickname)) {
            ToastUtils.showShortToast("请填写姓名");
            return false;
        }
        if (nickname.length() < 2 || nickname.length() > 18) {
            ToastUtils.showShortToast("请输入正确的姓名");
            return false;
        }
        if (!nickname.matches("[\\u4e00-\\u9fa5_a-zA-Z]{1,14}[\\?:•.]{0,1}[\\u4e00-\\u9fa5_a-zA-Z]{1,13}+$")) {
            ToastUtils.showShortToast("您输入的姓名含特殊符号");
            return false;
        }
        return true;
    }


    /*** 将null转成“”*/
    public static String nullToEmptyString(String str) {
        return str == null ? "" : str;
    }


    public static String getTextFromEditText(EditText etName) {
        return etName == null ? "" : etName.getText().toString().trim();
    }

	/*** 将"2016-11-26T00:00:00"变成2016/11/26*/
    public static String billFormatDate(String promiseRepayDate) {
		String result = "";
		if (isNotNull(promiseRepayDate)){
			String[] ts = promiseRepayDate.split("T");
			if (ts.length == 2){
				String[] split = ts[0].split("-");
				if (split.length == 3){
					result = split[0]+"/"+split[1]+"/"+split[2];
				}
			}
		}
		return result;
	}
}
