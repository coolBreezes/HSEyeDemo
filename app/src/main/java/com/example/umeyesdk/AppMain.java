package com.example.umeyesdk;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.Player.Core.PlayerClient;
import com.Player.web.websocket.ClientCore;
import com.example.umeyesdk.entity.PlayNode;

import java.util.ArrayList;
import java.util.List;

public class AppMain extends Application {
	public static final String FILTER = "com.example.umeyesdk.RefreshData";
	private ClientCore pc;
	private PlayerClient playerclient;
	private List<PlayNode> nodeList;
	public boolean isRun = false;
	private int currentNodeId = 0; // ��ǰ�б��ڵ��ID��

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		nodeList = new ArrayList<PlayNode>();
		pc = ClientCore.getInstance();
		//�������½֧�ֱ����������������֧�֣����� ����ClientCore.isSuportLocalAlarmPush=false��
		//Set to avoid landing support alarm, if the server does not support, must be set
		ClientCore.isSuportLocalAlarmPush=false; //Ĭ���ǲ�֧�� //Default is not supported
		playerclient = new PlayerClient();
		if (!isRun) {
			isRun = true;
			new Thread() {

				@Override
				public void run() {

					while (isRun) {
						String log = pc.CLTLogData(100);
						if (!TextUtils.isEmpty(log)) {
							Log.d("WriteLogThread", log);
						}
					}

				}

			}.start();
		}

		super.onCreate();
	}

	public synchronized PlayerClient getPlayerclient() {
		return playerclient;
	}

	public int getCurrentNodeId() {
		return currentNodeId;
	}

	public void setCurrentNodeId(int currentNodeId) {
		this.currentNodeId = currentNodeId;
	}

	public ClientCore getPc() {
		return pc;
	}

	public void setPc(ClientCore pc) {
		this.pc = pc;
	}

	public List<PlayNode> getNodeList() {
		return nodeList;
	}

	public synchronized List<PlayNode> getDvrAndCamera() {
		List<PlayNode> list = new ArrayList<PlayNode>();
		for (int i = 0; i < nodeList.size(); i++) {
			PlayNode dvrNode = nodeList.get(i);
			if (dvrNode.IsDvr()) {
				list.add(dvrNode);

			} else if (dvrNode.isCamera() && dvrNode.getParentId() == 0) {
				list.add(dvrNode);
			}
		}
		return list;
	}

	public void setNodeList(List<PlayNode> nodeList) {
		this.nodeList = nodeList;
	}

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

}
