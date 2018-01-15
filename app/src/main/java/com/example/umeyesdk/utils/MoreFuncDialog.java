package com.example.umeyesdk.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.Player.web.response.ResponseCommon;
import com.Player.web.response.ResponseGetInfomation;
import com.Player.web.websocket.ClientCore;
import com.example.umeyesdk.AppMain;
import com.example.umeyesdk.api.WebSdkApi;

/**
 * 更多功能
 * 
 * @author Administrator
 * 
 */
public class MoreFuncDialog extends AlertDialog.Builder {
	ClientCore clientCore;
	Activity activity;
	Handler handler;
	public String[] funcArray = { "添加设备", "刷新列表", "修改密码", "发送邮件重置密码", "查询报警记录",
			"删除所有报警", "查询布防信息", "查询公告信息", "查询宝宝信息", "查询食谱", "查询课程表", "注销" };

	public MoreFuncDialog(Activity arg0, ClientCore clientCore, Handler handler) {
		super(arg0);
		this.activity = arg0;
		this.clientCore = clientCore;
		this.handler = handler;
		setItems();
	}

	public void setItems() {
		setItems(funcArray, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which) {
				case 0:
					// 添加4路子码流的dvr
					WebSdkApi.addNodeInfo(activity, clientCore,
							String.valueOf(System.currentTimeMillis()), 0, 1, 2, 1009,
							Constants.UMID, "", 0, Constants.user,
							Constants.password, 8, 0, 1, handler);
					break;
				case 1:
					// 重新获取列表
					WebSdkApi.getNodeList(activity, clientCore, 0, 0, 0,
							handler);
					break;
				case 2:
					// 修改登录用户密码
					WebSdkApi.modifyUserPassword(activity, clientCore, "1234",
							"123456");
					break;
				case 3:
					// 发送重置密码到注册时所填的邮箱
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
					break;
				case 7:
					clientCore.queryInfomation(new Handler() {
						@Override
						public void handleMessage(Message msg) {
							// TODO Auto-generated method stub
							ResponseGetInfomation ResponseGetInfomation = (ResponseGetInfomation) msg.obj;
							if (ResponseGetInfomation != null
									&& ResponseGetInfomation.h != null) {
								if (ResponseGetInfomation.h.e == 200) {
									Show.toast(activity, "查询公告成功！");
								} else {
									Log.e(WebSdkApi.WebSdkApi_Error,
											"查询公告失败!code="
													+ ResponseGetInfomation.h.e);
									Show.toast(activity, "查询公告失败");
								}
							} else {
								Log.e(WebSdkApi.WebSdkApi_Error,
										"查询公告失败! error=" + msg.what);
								Show.toast(activity, "查询公告失败");
							}
							super.handleMessage(msg);
						}
					});
					break;
				case 8:
					clientCore.getBabyInfo(new Handler());
					break;
				case 9:
					clientCore.getCookbook(new Handler());
					break;
				case 10:
					clientCore.getCoursesInfo(new Handler());
					break;

				case 11:
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
