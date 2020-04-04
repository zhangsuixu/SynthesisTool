package com.common.widget.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;

public class PermissionDialog extends ConfirmDialog {
    private boolean needFinish = true;
    private boolean isConfirm;

    public PermissionDialog(Activity context) {
        super(context);
    }

    public PermissionDialog(Activity context, int style) {
        super(context, style);
    }

    public PermissionDialog(Activity context, int layout, int style, int windowGravity, boolean isFullScreen) {
        super(context, layout, style, windowGravity, isFullScreen);
    }

    public PermissionDialog(Activity context, View contentView, int style, int windowGravity, boolean isFullScreen) {
        super(context, contentView, style, windowGravity, isFullScreen);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setConfirmStr("权限设置");
        setEditEnable(false);
        isConfirm = false;
        setOnChooseListener(new OnChooseListener() {
            @Override
            public void confirm() {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                isConfirm = true;
                context.startActivity(intent);
                dismiss();
            }

            @Override
            public void cancel() {
                dismiss();
                //默认取消时关闭页面
                if (needFinish) {
                    context.finish();
                }
            }
        });
    }

    public boolean isConfirm(){
      return isConfirm;
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        //物理返回键不可取消
        setCancelable(false);
        //点击空白处不可取消
        setCanceledOnTouchOutside(false);
    }

    /**
     * 点击取消按钮时是否需要当前页面
     * @param needFinish
     */
    public void setNeedFinish(boolean needFinish) {
        this.needFinish = needFinish;
    }

    /**
     * 提醒用户需要设置的权限
     *
     * @param permissionStr
     */
    public void setPermissionStr(String permissionStr) {
        super.setContent(permissionStr);
    }
}
