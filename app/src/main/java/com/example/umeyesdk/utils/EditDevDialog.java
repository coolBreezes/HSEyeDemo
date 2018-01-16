package com.example.umeyesdk.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.Player.Source.TFileListNode;
import com.Player.web.response.ResponseQueryAlarmSettings;
import com.Player.web.websocket.ClientCore;
import com.example.umeyesdk.R;
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
	public String[] funcArray ;
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

        funcArray = new String[]{activity.getString(R.string.modify_dev),
                activity.getString(R.string.del_dev),
                activity.getString(R.string.search_defence),
                activity.getString(R.string.set_defence),
                activity.getString(R.string.un_defence),
                activity.getString(R.string.modify_ch)};

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
						Show.toast(activity, activity.getString(R.string.un_defence_fail_explain));
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
													activity.getString(R.string.query_alarm_info_succeed)
															+ responseQueryAlarmSettings.b
																	.toJsonString());
										} else {
											Log.e(WebSdkApi.WebSdkApi_Error,
													"��ѯ��������ʧ��!code="
															+ responseQueryAlarmSettings.h.e);
											Show.toast(activity, activity.getString(R.string.query_alarm_info_fail));
										}
									} else {
										Log.e(WebSdkApi.WebSdkApi_Error,
												"��ѯ��������ʧ��! error=" + msg.what);
										Show.toast(activity, activity.getString(R.string.query_alarm_info_fail));
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
