package com.getui.demo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.Player.Source.TDevNodeInfor;
import com.Player.web.request.P2pConnectInfo;
import com.Player.web.response.ResponseCommon;
import com.Player.web.websocket.ClientCore;
import com.example.umeyesdk.entity.PlayNode;
import com.example.umeyesdk.utils.Constants;
import com.example.umeyesdk.utils.Show;
import com.igexin.sdk.PushManager;

/**
 * ����������
 * 
 * @author Administrator
 * 
 */
public class AlarmUtils {
	public static final String SETTING_PATH = "/sdcard/UMEyeDome/setting.dat";// ��������Ŀ¼

	public static boolean isRecived = true;
	/**
	 * ����ClientID PushDemoReceiver ���渳ֵ
	 */
	public static String GETUI_CID = "";

	public static void openPush(final Context context) {
		// XGPushConfig.enableDebug(this, true);

		PushManager.getInstance().initialize(context);

	}

	/**
	 * ��������������
	 * 
	 * @param opCode
	 *            Ϊ1ʱ���� Ϊ2ʱ���� Ϊ4���������豸����
	 */
	public static void setAlarmPush(final Context context,
			ClientCore clientCore, PlayNode node, final int opCode) {
		// ��������
		int[] alarm_event = { 1, 2, 3, 4, 5 };
		// ���½ģʽ��umidֱ��ģʽ�� ���������豸ͨ������ �� ֧�ֶ��ͨ�����ã���ѡ
		P2pConnectInfo p2pConnectInfo = createConnectInfo(clientCore, node);
		P2pConnectInfo[] p2pConnectInfos = { p2pConnectInfo };
		clientCore.alarmSettings(p2pConnectInfos);
		// AlarmUtils.GETUI_CIDΪ����token
		clientCore.alarmSettings(opCode, AlarmUtils.GETUI_CID, alarm_event,
				new Handler() {

					@Override
					public void handleMessage(Message msg) {
						ResponseCommon commonSocketText = (ResponseCommon) msg.obj;
						if (commonSocketText != null
								&& commonSocketText.h.e == 200) {
							if (opCode == 1) {
								Show.toast(context, "�����ɹ�");
							} else if (opCode == 2) {
								Show.toast(context, "�����ɹ�");
							}

						} else {
							if (opCode == 1) {
								Show.toast(context, "����ʧ��");
							} else if (opCode == 2) {
								Show.toast(context, "����ʧ��");
							}

						}
					}
				}, node.node.sDevId);
	}

	/**
	 * ���¼���� �豸ͨ������
	 * 
	 * @param clientCore
	 * @param node
	 * @return
	 */
	public static P2pConnectInfo createConnectInfo(ClientCore clientCore,
			PlayNode node) {
		P2pConnectInfo p2pConnectInfo = new P2pConnectInfo();
		if (node != null) {
			// �������Ӳ���
			TDevNodeInfor info = TDevNodeInfor.changeToTDevNodeInfor(
					node.getDeviceId(), node.node.iConnMode);
			if (info != null) {
				p2pConnectInfo = new P2pConnectInfo();
				p2pConnectInfo.umid = info.pDevId;
				p2pConnectInfo.user = info.pDevUser;
				p2pConnectInfo.passwd = info.pDevPwd;
				p2pConnectInfo.dev_name = node.getName();

				/**
				 * ���½ģʽ�� node.node.sDevId�� String sDevId
				 * =clientCore.encryptDevId
				 * (String.valueOf(node.node.dwNodeId),info.pDevId, info.iChNo);
				 */
				String sDevId = node.node.sDevId;
				p2pConnectInfo.dev_id = sDevId;
				p2pConnectInfo.channel = info.iChNo;
			}
		}
		return p2pConnectInfo;

	}

	/**
	 * umidֱ��ģʽ ��������������
	 * 
	 * 
	 * @param activity
	 * @param opCode
	 *            Ϊ1ʱ���� Ϊ2ʱ���� Ϊ4���������豸����
	 * @param devName
	 *            ����֪ͨ��ʾ �豸����
	 * @param devUmid
	 *            �豸umid
	 * @param devUser
	 *            �豸�û���
	 * @param devPassword
	 *            �豸����
	 * @param iChNo
	 *            �豸ͨ����
	 */
	public static void setAlarmPush(final Context activity, final int opCode,
			String devName, String devUmid, String devUser, String devPassword,
			int iChNo) {
		ClientCore clientCore = ClientCore.getInstance();
		// ��������
		int[] alarm_event = { 1, 2, 3, 4, 5 };
		// ���½ģʽ�¡� umidֱ��ģʽ���������豸ͨ������ �� ֧�ֶ��ͨ�����ã���ѡ
		P2pConnectInfo p2pConnectInfo = createConnectInfo1(clientCore, devName,
				devUmid, devUser, devPassword, iChNo);
		P2pConnectInfo[] p2pConnectInfos = { p2pConnectInfo };
		clientCore.alarmSettings(p2pConnectInfos);
		// AlarmUtils.GETUI_CIDΪ����token
		clientCore.alarmSettings(opCode, AlarmUtils.GETUI_CID, alarm_event,
				new Handler() {

					@Override
					public void handleMessage(Message msg) {
						ResponseCommon commonSocketText = (ResponseCommon) msg.obj;
						if (commonSocketText != null
								&& commonSocketText.h.e == 200) {
							if (opCode == 1) {
								Show.toast(activity, "�����ɹ�");
							} else if (opCode == 2) {
								Show.toast(activity, "�����ɹ�");
							}

						} else {
							if (opCode == 1) {
								Show.toast(activity, "����ʧ��");
							} else if (opCode == 2) {
								Show.toast(activity, "����ʧ��");
							}

						}
					}
				}, "");
	}

	/**
	 * umidֱ�� �豸ͨ������
	 * 
	 * @param clientCore
	 * @param node
	 * @return
	 */
	public static P2pConnectInfo createConnectInfo1(ClientCore clientCore,
			String devName, String devUmid, String devUser, String devPassword,
			int iChNo) {
		P2pConnectInfo p2pConnectInfo = new P2pConnectInfo();

		p2pConnectInfo = new P2pConnectInfo();
		p2pConnectInfo.umid = devUmid;
		p2pConnectInfo.user = devUser;
		p2pConnectInfo.passwd = devPassword;
		// �����豸������umid ��ͨ������ɣ����Զ���
		p2pConnectInfo.dev_name = devName;
		String sDevId = clientCore.encryptDevId("", devUmid, iChNo);
		p2pConnectInfo.dev_id = sDevId;
		p2pConnectInfo.channel = iChNo;
		return p2pConnectInfo;

	}
}
