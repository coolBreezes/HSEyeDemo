package com.example.umeyesdk.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.Player.web.response.ResponseCommon;
import com.Player.web.websocket.ClientCore;
import com.example.umeyesdk.R;
import com.example.umeyesdk.api.WebSdkApi;

/**
 * 更多功能
 *
 * @author Administrator
 */
public class MoreFuncDialog extends AlertDialog.Builder {
    ClientCore clientCore;
    Activity activity;
    Handler handler;
    public int[] funcArray = {R.string.add_dev, R.string.update_list, R.string.modify_user_pwd,
            R.string.send_mail_reset_pwd, R.string.search_alarm_info,
            R.string.delete_all_alarm_info, R.string.logout};
    String[] strFuncArray;

    public MoreFuncDialog(Activity arg0, ClientCore clientCore, Handler handler) {
        super(arg0);
        this.activity = arg0;
        this.clientCore = clientCore;
        this.handler = handler;
        intItems();
        setItems();
    }

    private void intItems() {

        strFuncArray = new String[funcArray.length];
        for (int i = 0; i < funcArray.length; i++) {
            strFuncArray[i] = activity.getString(funcArray[i]);
        }
    }

    public void setItems() {
        setItems(strFuncArray, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch (which) {
                    case 0:
                        // demo dev no use,use uvr
                        WebSdkApi.addNodeInfo(activity, clientCore,
                                String.valueOf(System.currentTimeMillis()), 0, 1, 2, 1009,
                                Constants.UMID, "", 0, Constants.user,
                                Constants.password, 10, 0, 1, handler);
                        break;
                    case 1:
                        // 重新获取列表(Regain the list)
                        WebSdkApi.getNodeList(activity, clientCore, 0, 0, 0,
                                handler);
                        break;
                    case 2:
                        // 修改登录用户密码(Change login user password)
                        WebSdkApi.modifyUserPassword(activity, clientCore, "1234",
                                "123456");
                        break;
                    case 3:
                        // 发送重置密码到注册时所填的邮箱 （Send the reset password to register the mailbox）
                        WebSdkApi.resetUserPassword(activity, clientCore, "yin", 2);
                        break;
                    case 4:
                        WebSdkApi.queryAlarmList(activity, clientCore);
                        break;
                    case 5:
                        /**
                         * 删除alarm_id的报警记录 clientCore.deleteAlarm(alarm_id,handler);
                         */
                        // 删除所有报警
                        clientCore.deleteAllAlarm(new Handler() {

                            @Override
                            public void handleMessage(Message msg) {
                                // TODO Auto-generated method stub
                                ResponseCommon responseCommon = (ResponseCommon) msg.obj;
                                if (responseCommon != null
                                        && responseCommon.h != null) {
                                    if (responseCommon.h.e == 200) {
                                        Show.toast(activity, "删除所有报警信息成功！");
                                    } else {
                                        Log.e(WebSdkApi.WebSdkApi_Error,
                                                "删除所有报警信息失败!code="
                                                        + responseCommon.h.e);
                                        Show.toast(activity, "删除所有报警信息失败");
                                    }
                                } else {
                                    Log.e(WebSdkApi.WebSdkApi_Error,
                                            "删除所有报警信息失败! error=" + msg.what);
                                    Show.toast(activity, "删除所有报警信息失败");
                                }
                                super.handleMessage(msg);
                            }
                        });
                        break;
                    case 6:
                        // 注销
                        WebSdkApi.logoutServer(activity, clientCore, 1);
                        break;

                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
    }
}
