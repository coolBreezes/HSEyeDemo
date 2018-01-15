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
 * ���๦��
 * 
 * @author Administrator
 * 
 */
public class MoreFuncDialog extends AlertDialog.Builder {
	ClientCore clientCore;
	Activity activity;
	Handler handler;
	public String[] funcArray = { "����豸", "ˢ���б�", "�޸�����", "�����ʼ���������", "��ѯ������¼",
			"ɾ�����б���", "��ѯ������Ϣ", "��ѯ������Ϣ", "��ѯ������Ϣ", "��ѯʳ��", "��ѯ�γ̱�", "ע��" };

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
					// ���4·��������dvr
					WebSdkApi.addNodeInfo(activity, clientCore,
							String.valueOf(System.currentTimeMillis()), 0, 1, 2, 1009,
							Constants.UMID, "", 0, Constants.user,
							Constants.password, 8, 0, 1, handler);
					break;
				case 1:
					// ���»�ȡ�б�
					WebSdkApi.getNodeList(activity, clientCore, 0, 0, 0,
							handler);
					break;
				case 2:
					// �޸ĵ�¼�û�����
					WebSdkApi.modifyUserPassword(activity, clientCore, "1234",
							"123456");
					break;
				case 3:
					// �����������뵽ע��ʱ���������
					WebSdkApi.resetUserPassword(activity, clientCore, "yin", 2);
					break;
				case 4:
					WebSdkApi.queryAlarmList(activity, clientCore);
					break;
				case 5:
					/**
					 * ɾ��alarm_id�ı�����¼ clientCore.deleteAlarm(alarm_id,handler);
					 */
					// ɾ�����б���
					clientCore.deleteAllAlarm(new Handler() {

						@Override
						public void handleMessage(Message msg) {
							// TODO Auto-generated method stub
							ResponseCommon responseCommon = (ResponseCommon) msg.obj;
							if (responseCommon != null
									&& responseCommon.h != null) {
								if (responseCommon.h.e == 200) {
									Show.toast(activity, "ɾ�����б�����Ϣ�ɹ���");
								} else {
									Log.e(WebSdkApi.WebSdkApi_Error,
											"ɾ�����б�����Ϣʧ��!code="
													+ responseCommon.h.e);
									Show.toast(activity, "ɾ�����б�����Ϣʧ��");
								}
							} else {
								Log.e(WebSdkApi.WebSdkApi_Error,
										"ɾ�����б�����Ϣʧ��! error=" + msg.what);
								Show.toast(activity, "ɾ�����б�����Ϣʧ��");
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
									Show.toast(activity, "��ѯ����ɹ���");
								} else {
									Log.e(WebSdkApi.WebSdkApi_Error,
											"��ѯ����ʧ��!code="
													+ ResponseGetInfomation.h.e);
									Show.toast(activity, "��ѯ����ʧ��");
								}
							} else {
								Log.e(WebSdkApi.WebSdkApi_Error,
										"��ѯ����ʧ��! error=" + msg.what);
								Show.toast(activity, "��ѯ����ʧ��");
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
					// ע��
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
