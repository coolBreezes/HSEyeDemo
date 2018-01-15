package com.example.umeyesdk.thread;

import android.os.Handler;
import android.util.Log;

import com.Player.Core.PlayerClient;
import com.Player.Source.TDevWifiInfor;

/**
 * ����ǰ���Ȼ�ȡһ��wifi����
 * 
 * @author Administrator
 * 
 */
public class WifiSetThread extends Thread {
	public static final int SET_OK = 0;
	public static final int SET_FALL = 1;
	PlayerClient pc;
	String umid = "";
	String userName = "";
	String password = "";
	TDevWifiInfor devWifiInfo;
	Handler handler;

	public WifiSetThread(PlayerClient pc, String umid, String userName,
			String password, TDevWifiInfor devWifiInfo, Handler handler) {
		this.pc = pc;
		this.umid = umid;
		this.userName = userName;
		this.password = password;
		this.devWifiInfo = devWifiInfo;
		this.handler = handler;
	}

	@Override
	public void run() {

		TDevWifiInfor getdevWifiInfo = pc.CameraGetWIFIConfigEx(umid, userName,
				password);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (getdevWifiInfo != null) {
			// ����wifi�Ƿ���
			getdevWifiInfo.bEnable = devWifiInfo.bEnable;
			// ����DHCP����
			getdevWifiInfo.bDhcpEnable = devWifiInfo.bDhcpEnable;
			// // �ֶ�ʹ�ܣ���ȫ����
			getdevWifiInfo.bFieldEnable_AuthType = 0;
			// �ֶ�ʹ�ܣ���������
			getdevWifiInfo.bFieldEnable_EncrypType = 0;
			getdevWifiInfo.bFieldEnable_Channel = 0;
			// �Ƿ�������������������TRUE�����ã����²�����Ч��FALSE�������ã�����ԭ�������
			// ҪʹDHCP��IP��ַ��Ч����������bIfSetNetParam=1.
			getdevWifiInfo.bIfSetNetParam = 1;
			// ����·��ssid
			getdevWifiInfo.sWifiPwd = devWifiInfo.sWifiPwd;
			// ����·������
			getdevWifiInfo.sWifiSSID = devWifiInfo.sWifiSSID;
			Log.d("setWifiInfo",
					"�豸ID��" + umid + ",wifi SSID:" + getdevWifiInfo.sWifiSSID
							+ ", " + getdevWifiInfo.toString());
			int ret = pc.CameraSetWIFIConfigEx(umid, userName, password,
					getdevWifiInfo);

			if (ret > 0) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(SET_OK);
			} else
				handler.sendEmptyMessage(SET_FALL);

		} else {
			handler.sendEmptyMessage(SET_FALL);
		}
		pc.RtsCameraDisconnect();
	}
}
