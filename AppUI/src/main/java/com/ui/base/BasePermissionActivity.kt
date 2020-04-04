package com.ui.base

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.NonNull
import com.afollestad.materialdialogs.MaterialDialog
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import com.yanzhenjie.permission.runtime.PermissionDef
import java.lang.StringBuilder

/**
 *  权限处理
 */
abstract class BasePermissionActivity : AppCompatActivity() {

    private lateinit var mPagePermissionDialog: MaterialDialog
    private lateinit var mClickPermissionDialog: MaterialDialog

    private lateinit var mGroups: Array<out Array<String>>
    private lateinit var mPermissions: Array<out String>

    override fun onResume() {
        //如果有请求权限并且失败，则permissionDialog会被初始化且展示.想要关闭权限提醒弹框当然是检查用户是否去打开了权限,成功则关闭
        if (this::mPagePermissionDialog.isInitialized && mPagePermissionDialog.isShowing) {
            mPagePermissionDialog.dismiss()

            if (this::mGroups.isInitialized) {
                requestPagePermission(*mGroups)
            }

            if (this::mPermissions.isInitialized) {
                requestPagePermission(*mPermissions)
            }
        }

        super.onResume()
    }

    /**
     *  界面打开时权限组申请
     *
     *  不支持联系人组权限申请,因为含有联系人写操作，andPermission检查权限会尝试删除操作，
     *  而部分手机限制联系人删除操作导致。比如小米mix2,会弹出警告窗.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun requestPagePermission(vararg groups: Array<String>) { //java的 String... 对应  kotlin 的vararg
        mGroups = groups
        AndPermission.with(this)
                .runtime()
                .permission(*groups)
                .onGranted {
                    if (this::mPagePermissionDialog.isInitialized && mPagePermissionDialog.isShowing) {
                        mPagePermissionDialog.dismiss()
                    }

                    onPagePermissionSuccess()
                }.onDenied {
                    showPagePermissionDialog(getDialogMessage(it))
                }
                .start()
    }

    /**
     * 不支持联系人写操作,因为含有联系人写操作，andPermission检查权限会尝试删除操作，
     * 而部分手机限制联系人删除操作导致。比如小米mix2,会弹出警告窗.
     */
    fun requestPagePermission(vararg permissions: String) {
        mPermissions = permissions
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .onGranted {

                    if (this::mPagePermissionDialog.isInitialized && mPagePermissionDialog.isShowing) {
                        mPagePermissionDialog.dismiss()
                    }

                    onPagePermissionSuccess()
                }.onDenied {
                    showPagePermissionDialog(getDialogMessage(it))
                }
                .start()
    }

    private fun getDialogMessage(permissionNames: List<String>): String {
        val names = HashSet<String>()

        for (name in permissionNames) {
            names.add(getPermissionName(name))
        }

        val strBuild = StringBuilder()
        strBuild.append("缺失以下权限 :")
        for (name in names) {
            strBuild.append(name)
            strBuild.append(",")
        }

        return strBuild.toString()
    }

    private fun getPermissionName(name: String): String {
        when (name) {
            Permission.READ_CALENDAR,
            Permission.WRITE_CALENDAR -> return "访问日历"

            Permission.CAMERA -> return "访问相机"

            Permission.READ_CONTACTS,
            Permission.WRITE_CONTACTS,
            Permission.GET_ACCOUNTS -> return "访问手机账号/通讯录"

            Permission.ACCESS_FINE_LOCATION,
            Permission.ACCESS_COARSE_LOCATION,
            Permission.ACCESS_BACKGROUND_LOCATION -> return "访问位置信息"

            Permission.RECORD_AUDIO -> return "访问麦克风"

            Permission.READ_PHONE_STATE,
            Permission.CALL_PHONE,
            Permission.USE_SIP,
            Permission.READ_PHONE_NUMBERS,
            Permission.ANSWER_PHONE_CALLS,
            Permission.ADD_VOICEMAIL -> return "访问电话"


            Permission.READ_CALL_LOG,
            Permission.WRITE_CALL_LOG,
            Permission.PROCESS_OUTGOING_CALLS -> return "访问通话记录"

            Permission.BODY_SENSORS -> return "访问身体传感器"

            Permission.ACTIVITY_RECOGNITION -> return "访问健身运动"

            Permission.SEND_SMS,
            Permission.RECEIVE_SMS,
            Permission.READ_SMS,
            Permission.RECEIVE_WAP_PUSH,
            Permission.RECEIVE_MMS -> return "访问短信"

            Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE -> return "存储空间"
        }

        return ""
    }

    /***  点击时权限申请 */
    fun requestClickPermission(@NonNull @PermissionDef vararg permissions: String) {
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .onGranted {

                    if (this::mClickPermissionDialog.isInitialized && mClickPermissionDialog.isShowing) {
                        mClickPermissionDialog.dismiss()
                    }

                    onClickPermissionSuccess()
                }.onDenied {
                    showPagePermissionDialog(getDialogMessage(it))
                }
                .start()
    }


    /** 获取页面打开权限成功后 */
    open fun onPagePermissionSuccess() {}

    /** 获取点击权限成功后 */
    open fun onClickPermissionSuccess() {}

    /** 点击触发的权限对话框可取消,因为点击可重复触发 */
    private fun showClickPermissionDialog() {
        if (!this::mClickPermissionDialog.isInitialized) {
            mPagePermissionDialog = MaterialDialog.Builder(this)
                    .title("警告!!")
                    .content("权限获取失败,请授予权限")
                    .positiveText("前往授权")
                    .onPositive { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .build()
        }
    }

    private fun showPagePermissionDialog(msg: String) {
        if (!this::mPagePermissionDialog.isInitialized) {
            mPagePermissionDialog = MaterialDialog.Builder(this)
                    .cancelable(false)
                    .autoDismiss(false)
                    .title("警告!!")
                    .content(msg)
                    .positiveText("前往授权")
                    .onPositive { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .build()
        }


        if (!mPagePermissionDialog.isShowing) {
            mPagePermissionDialog.show()
        }
    }
}
