package com.example.umeyesdk;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.Player.Core.PlayerClient;
import com.Player.Source.TAlarmSetInfor;
import com.Player.web.response.DevItemInfo;
import com.Player.web.response.ResponseDevList;
import com.Player.web.websocket.ClientCore;
import com.example.umeyesdk.adpter.DeviceManagerAdapter;
import com.example.umeyesdk.api.WebSdkApi;
import com.example.umeyesdk.entity.PlayNode;
import com.example.umeyesdk.utils.Constants;
import com.example.umeyesdk.utils.EditDevDialog;
import com.example.umeyesdk.utils.MoreFuncDialog;
import com.example.umeyesdk.utils.Show;
import com.getui.demo.AlarmUtils;

public class MainActivity extends Activity implements OnItemClickListener,
		OnClickListener, OnItemLongClickListener {
	public ClientCore clientCore;
	String imsi;
	public Context context;
	ListView listView;
	Button btnMoreFunc;
	DeviceManagerAdapter adapter;
	public List<PlayNode> nodeList = new ArrayList<PlayNode>();
	RefreshData refresh;
	AppMain ua;
	ResponseDevList responseDevList;
	MoreFuncDialog moreFuncDialog;
	/**
	 * ��Handler������UI
	 */
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case Constants.MODIFY_DEV_NUM_F:
				Show.toast(MainActivity.this, "�޸��豸ͨ����ʧ��");
				break;
			case Constants.MODIFY_DEV_NUM_S:
				WebSdkApi.getNodeList(MainActivity.this, clientCore, 0, 0, 0,
						this);
				Show.toast(MainActivity.this, "�޸��豸ͨ�����ɹ�");
				break;
			case Constants.DELETE_DEV_F:
				Show.toast(MainActivity.this, "ɾ���豸ʧ��");
				break;
			case Constants.DELETE_DEV_S:
				WebSdkApi.getNodeList(MainActivity.this, clientCore, 0, 0, 0,
						this);
				Show.toast(MainActivity.this, "ɾ���豸�ɹ�");
				break;

			case Constants.ADD_DEV_S:
				WebSdkApi.getNodeList(MainActivity.this, clientCore, 0, 0, 0,
						this);
				Show.toast(MainActivity.this, "����豸�ɹ�");
				break;
			case Constants.ADD_DEV_F:
				Show.toast(MainActivity.this, "����豸ʧ��");
				break;

			case Constants.GET_DEVLIST_F:
				Show.toast(MainActivity.this, "��ȡ�豸ʧ��");
				break;
			case Constants.GET_DEVLIST_S:
				nodeList.clear();
				List<PlayNode> reGetList = new ArrayList<PlayNode>();
				responseDevList = (ResponseDevList) msg.obj;
				List<DevItemInfo> items = responseDevList.b.nodes;
				for (int i = 0; i < items.size(); i++) {
					DevItemInfo devItemInfo = items.get(i);
					if (devItemInfo != null) {
						PlayNode node = PlayNode.ChangeData(devItemInfo);
						Log.d("PlayNode", "PlayNode.dwParentNodeId:"
								+ node.node.dwParentNodeId
								+ ",PlayNode.dwNodeId" + node.node.dwNodeId);
						reGetList.add(node);
					}
				}
				ua.setNodeList(reGetList);
				nodeList = ua.getDvrAndCamera();
				adapter.setNodeList(nodeList);
				adapter.notifyDataSetChanged();

				Show.toast(MainActivity.this, "��ȡ�豸�ɹ�");
				break;

			case Constants.DELETE_SUCCEED:
				adapter.notifyDataSetChanged();
				Show.toast(context, R.string.delete_succeed);

				break;
			case Constants.DELETE_FAILED:
				Show.toast(context, R.string.delete_failed);
				break;
			case Constants.ADD_FAILED:
				Show.toast(context, R.string.add_failed);
				break;
			case Constants.ADD_SUCCEED:
				adapter.notifyDataSetChanged();
				Show.toast(context, R.string.add_succeed);
				break;
			case Constants.SET_ALARM_F:
				Show.toast(context, "����ʧ��");
				break;
			case Constants.SET_ALARM_S:
				Show.toast(context, "���ųɹ�");
				break;
			case Constants.CANCEL_ALARM_F:
				Show.toast(context, "����ʧ��");
				break;
			case Constants.CANCEL_ALARM_S:
				Show.toast(context, "�����ɹ�");

			default:
				break;
			}

		}
	};
	private int currentDVR = 0;// ��ǰѡ�е�dvr�豸

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_live_list);
		context = this;
		ua = (AppMain) getApplication();

		initeView();
		clientCore = ClientCore.getInstance();

	}

	void initeView() {
		listView = (ListView) findViewById(R.id.lvLive);
		nodeList = ua.getDvrAndCamera();// �б����¸�ֵ
		adapter = new DeviceManagerAdapter(context, nodeList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(MainActivity.this);
		listView.setOnItemLongClickListener(MainActivity.this);
		adapter.setNodeList(nodeList);
		btnMoreFunc = (Button) findViewById(R.id.btnMore);
		btnMoreFunc.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		ua.setRun(false);

		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		PlayNode node = nodeList.get(arg2);
		// TODO Auto-generated method stub
		if (node.IsDirectory()) {
			Show.toast(context, "�����Ŀ¼");
			return;
		} else if (node.IsDvr()) {
			showChannleList(node.getNode().dwNodeId);
			// Show.toast(context, "�����DVR");

			return;
		} else {
			// node.isCamera()
			// �����ͨ���ڵ㣬�ͽ��벥��ҳ��
			Intent intent = new Intent(context, PlayActivity.class);
			// Intent intent = new Intent(context, AcVrPlay.class);
			intent.putExtra("id", node.connecParams);
			startActivity(intent);
		}
	}

	/**
	 * ��ʾͨ���б�
	 * 
	 * @param nodeId
	 */
	void showChannleList(int nodeId) {
		currentDVR = nodeId;
		// WebSdkApi.getNodeList(pc, nodeId, 0, 0, handler);
		nodeList.clear();
		nodeList = GetDevChildrenList(nodeId, ua.getNodeList());
		adapter.setNodeList(nodeList);
		adapter.notifyDataSetChanged();

	}

	/**
	 * ��ȡ�豸�µ�ͨ���ڵ�
	 * 
	 * 
	 */
	public List<PlayNode> GetDevChildrenList(int nodeId, List<PlayNode> NodeList) {
		List<PlayNode> list = new ArrayList<PlayNode>();

		for (int i = 0; i < NodeList.size(); i++)// ����node�µ�ֱ���ӽڵ�
		{
			PlayNode temp = NodeList.get(i);
			if (temp.node.dwParentNodeId == nodeId) { // �豸�ڵ��.node.dwNodeId�Ǹ��豸��ͨ���ڵ��.node.dwParentNodeId
				list.add(temp);
			}

		}
		return list;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnMore:
			if (moreFuncDialog == null) {
				moreFuncDialog = new MoreFuncDialog(this, clientCore, handler);
			}
			moreFuncDialog.setTitle(R.string.more);
			moreFuncDialog.show();
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			final int arg2, long arg3) {

		PlayNode node = nodeList.get(arg2);
		EditDevDialog editDevDialog = new EditDevDialog(this, clientCore, node,
				handler);
		editDevDialog.show();

		return true;
	}

	/**
	 * ��ʾ�Ƿ�����֪ͨ���ֻ�
	 */
	public boolean showIfNotify(TAlarmSetInfor alarmInfo) {
		boolean isRun = false;
		if (alarmInfo != null) {
			String token = AlarmUtils.GETUI_CID;
			Log.w("GETUI_CID.getToken", token + "");
			if (alarmInfo != null && alarmInfo.notifies != null) {
				for (int i = 0; i < alarmInfo.notifies.length; i++) {
					Log.w("alarmInfo", alarmInfo.notifies[i].notify_type + ","
							+ alarmInfo.notifies[i].notify_param);
					if (alarmInfo.notifies[i].notify_type == PlayerClient.NPC_D_MON_ALARM_NOTIFY_TYPE_PHONE_PUSH
							&& token.equals(alarmInfo.notifies[i].notify_param)) {
						return true;
					}
				}
			}
		}
		return isRun;
	}

	public class RefreshData extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("new Data", "Updata~~~~~~~~~~~~~~~~~~~~");

			// handler.sendEmptyMessage(Constants.NEW_DATA);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (currentDVR != 0) {
				showChannleList(0);
				return true;
			} else {
				// ע����¼
				WebSdkApi.logoutServer(this, clientCore, 1);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
