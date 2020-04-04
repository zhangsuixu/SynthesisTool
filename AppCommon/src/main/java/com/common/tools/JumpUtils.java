package com.common.tools;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.common.R;

public class JumpUtils {

	public static void PageJump(Context context, Class<?> descClass) {
		Class<?> mClass = context.getClass();
		if (mClass == descClass) {
			return;
		}
		try {
			Intent intent = new Intent();
			intent.setClass(context, descClass);
			context.startActivity(intent);

			((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//从右到左退出

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void PageJump(Context context, Class<?> descClass, Bundle bundle) {
		Class<?> mClass = context.getClass();

		if (mClass == descClass) {
			return;
		}

		try {
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setClass(context, descClass);

			if (bundle != null) {
				intent.putExtras(bundle);
			}
			context.startActivity(intent);

			((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//从右到左退出
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void PageJump(Context context, Class<?> descClass, int requestCode) {
		Class<?> mClass = context.getClass();
		if (mClass == descClass) {
			return;
		}
		try {
			Intent intent = new Intent();
			intent.putExtra("requestCode", requestCode);
			intent.setClass(context, descClass);

			((Activity) context).startActivityForResult(intent, requestCode);
			((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//从右到左退出
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void PageJump(Context context, Class<?> descClass, Bundle bundle, int requestCode) {
		Class<?> mClass = context.getClass();
		if (mClass == descClass) {
			return;
		}
		try {
			Intent intent = new Intent();
			intent.putExtra("requestCode", requestCode);
			intent.setClass(context, descClass);

			if (bundle != null) {
				intent.putExtras(bundle);
			}

			((Activity) context).startActivityForResult(intent, requestCode);
			((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//从右到左退出
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void PageJumpUp(Context context, Class<?> descClass, Bundle bundle, int requestCode) {
		Class<?> mClass = context.getClass();
		if (mClass == descClass) {
			return;
		}
		try {
			Intent intent = new Intent();
			intent.putExtra("requestCode", requestCode);
			intent.setClass(context, descClass);

			if (bundle != null) {
				intent.putExtras(bundle);
			}

			((Activity) context).startActivityForResult(intent, requestCode);
			((Activity) context).overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);//从下到上出现
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void PageJump(Context context, Fragment fragment, Class<?> descClass, int requestCode) {
		Class<?> mClass = context.getClass();
		if (mClass == descClass) {
			return;
		}
		try {
			Intent intent = new Intent();
			intent.setClass(context, descClass);
			intent.putExtra("requestCode", requestCode);

			fragment.startActivityForResult(intent, requestCode);

			((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//从右到左退出

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void PageJump(Context context, Fragment fragment,
                                Class<?> descClass, Bundle bundle, int requestCode) {
		Class<?> mClass = context.getClass();
		if (mClass == descClass) {
			return;
		}
		try {
			Intent intent = new Intent();
			intent.setClass(context, descClass);
			intent.putExtra("requestCode", requestCode);

			if (bundle != null) {
				intent.putExtras(bundle);
			}

			fragment.startActivityForResult(intent, requestCode);

			((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//从右到左退出

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void PageJump(Context context, Fragment fragment, Class<?> descClass) {
		Class<?> mClass = context.getClass();
		if (mClass == descClass) {
			return;
		}
		try {
			Intent intent = new Intent();
			intent.setClass(context, descClass);

			fragment.startActivity(intent);

			((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//从右到左退出

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void PageJump(Context context, Fragment fragment,
                                Bundle bundle, Class<?> descClass) {
		Class<?> mClass = context.getClass();
		if (mClass == descClass) {
			return;
		}
		try {
			Intent intent = new Intent();
			intent.setClass(context, descClass);

			if (bundle != null) {
				intent.putExtras(bundle);
			}

			fragment.startActivity(intent);

			((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//从右到左退出

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void finish(Context context) {
		((Activity) context).finish();
		((Activity) context).overridePendingTransition(R.anim.finish_down1, R.anim.finish_down2);
	}

	public static void finishBase(Context context) {
		((Activity) context).finish();
		((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	/**
	 * 页面finish掉的时候setResult resultCode
	 * @param context
	 * @param intent
	 * @param resultCode
     */
	public static void finish(Context context, Intent intent, int resultCode) {
		if (00 != resultCode) {
			((Activity) context).setResult(resultCode, intent);
			((Activity) context).finish();
			((Activity) context).overridePendingTransition(R.anim.finish_down1, R.anim.finish_down2);
		}
	}

	public static void finishRight(Context context, Intent intent, int resultCode) {
		if (00 != resultCode) {
			((Activity) context).setResult(resultCode, intent);
			((Activity) context).finish();
			((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
	}
}
