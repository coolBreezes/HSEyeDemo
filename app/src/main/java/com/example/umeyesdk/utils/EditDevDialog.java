package com.example.umeyesdk.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.Player.Source.TDevNodeInfor;
import com.Player.Source.TFileListNode;
import com.Player.web.request.P2pConnectInfo;
import com.Player.web.response.ResponseCommon;
import com.Player.web.response.ResponseQueryAlarmSettings;
import com.Player.web.websocket.ClientCore;
import com.example.umeyesdk.api.WebSdkApi;
import com.example.umeyesdk.entity.PlayNode;
import com.getui.demo.AlarmUtils;

/**
 * ���๦��
 * 
 * @author Administrator
 * 
 */
public class EditDevDialog extends AlertDialog.Builder {
	ClientCore clientCore;
	Activity activity;
	Handler handler;
	public String[] funcArray = { "�޸��豸", "ɾ���豸", "��ѯ����", "���ò���", "��������",
			"�޸�ͨ����" };
	PlayNode node;

	public EditDevDialog(Activity arg0, ClientCore clientCore, PlayNode node,
			Handler handler) {
		super(arg0);
		this.node = node;
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
				TFileListNode tFileListNode = node.node;
				switch (which) {

				case 0:
					// �޸��豸���Ӳ�����ͨ�����ơ��豸�û������롢�������������ͨ����
					WebSdkApi.modifyNodeInfo(activity, clientCore,
							String.valueOf(node.node.dwNodeId), "test_modify",
							2, tFileListNode.usVendorId, node.umid, "", 0,
							"admin", "", 0, 1, handler);
					break;
				case 1:
					WebSdkApi.deleteNodeInfo(activity, clientCore,
							String.valueOf(node.node.dwNodeId),
							node.node.ucNodeKind, node.node.id_type, handler);
					break;
				case 2:
					/**
					 * ֻ�����umid��ipc���豸��ͨ���ſ��Բ���(tFileListNode.iConnMode
					 * Ϊ2ʱ����umid�豸)
					 */
					if (node.IsDvr() || node.IsDirectory()
							|| tFileListNode.iConnMode != 2) {
						Show.toast(activity, "ֻ�����umid��ipc���豸��ͨ���ſ��Գ���");
						break;
					}
					// ָ����ѯĳ���豸��ͨ����������
					clientCore.queryAlarmSettings(tFileListNode.sDevId,
							new Handler() {

								@Override
								public void handleMessage(Message msg) {
									// TODO Auto-generated method stub
									ResponseQueryAlarmSettings responseQueryAlarmSettings = (ResponseQueryAlarmSettings) msg.obj;
									if (responseQueryAlarmSettings != null
											&& responseQueryAlarmSettings.h != null) {
										if (responseQueryAlarmSettings.h.e == 200) {

											Show.toast(
													activity,
													"��ѯ���������ɹ���"
															+ responseQueryAlarmSettings.b
																	.toJsonString());
										} else {
											Log.e(WebSdkApi.WebSdkApi_Error,
													"��ѯ��������ʧ��!code="
															+ responseQueryAlarmSettings.h.e);
											Show.toast(activity, "��ѯ��������ʧ��");
										}
									} else {
										Log.e(WebSdkApi.WebSdkApi_Error,
												"��ѯ��������ʧ��! error=" + msg.what);
										Show.toast(activity, "��ѯ��������ʧ��");
									}
									super.handleMessage(msg);
								}
							});
					break;
				case 3:
					/**
					 * ֻ�����umid��ipc���豸��ͨ���ſ��Բ���(tFileListNode.iConnMode
					 * Ϊ2ʱ����umid�豸)
					 */
					if (node.IsDvr() || node.IsDirectory()
							|| tFileListNode.iConnMode != 2) {
						Show.toast(activity, "ֻ�����umid��ipc���豸��ͨ���ſ��Բ���");
						break;
					}
					AlarmUtils.setAlarmPush(activity, clientCore, node, 1);
					break;
				case 4:
					/**
					 * ֻ�����umid��ipc���豸��ͨ���ſ��Բ���(tFileListNode.iConnMode
					 * Ϊ2ʱ����umid�豸)
					 */
					if (node.IsDvr() || node.IsDirectory()
							|| tFileListNode.iConnMode != 2) {
						Show.toast(activity, "ֻ�����umid��ipc���豸��ͨ���ſ��Գ���");
						break;
					}
					AlarmUtils.setAlarmPush(activity, clientCore, node, 2);
					break;
				case 5:

					if (node.IsDvr()) {
						WebSdkApi.modifyDevNum(clientCore,
								String.valueOf(node.node.dwNodeId),
								node.node.sDevId, 16, handler);
					}
	
					break;

				default:
					break;

				}
				dialog.cancel();
			}
		});
	}

}
