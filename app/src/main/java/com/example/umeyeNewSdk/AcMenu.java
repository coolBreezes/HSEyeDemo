package com.example.umeyeNewSdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.Player.Core.PlayerClient;
import com.example.umeyesdk.AppMain;
import com.example.umeyesdk.R;
import com.example.umeyesdk.utils.Constants;
import com.getui.demo.AlarmUtils;
import com.macrovideo.smartlink.SmarkLinkTool;

public class AcMenu extends Activity implements OnClickListener {
	private AppMain appMain;
	private Context activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_menu);
		activity = this;
		appMain = (AppMain) this.getApplicationContext();
		findViewById(R.id.btn_photo).setOnClickListener(this);
		findViewById(R.id.btn_video).setOnClickListener(this);
		findViewById(R.id.btn_door_settings).setOnClickListener(this);
		findViewById(R.id.btn_search_wifi).setOnClickListener(this);
		findViewById(R.id.btn_motion).setOnClickListener(this);
		findViewById(R.id.btn_dev_alarm_push).setOnClickListener(this);
		findViewById(R.id.btn_dev_dis_alarm_push).setOnClickListener(this);
		findViewById(R.id.btn_remote_video).setOnClickListener(this);
		findViewById(R.id.btn_dev_ip).setOnClickListener(this);
		findViewById(R.id.btn_dev_ip_1).setOnClickListener(this);
		findViewById(R.id.btn_dev_time).setOnClickListener(this);
		findViewById(R.id.btn_dev_storage).setOnClickListener(this);
		findViewById(R.id.btn_dev_code).setOnClickListener(this);
		findViewById(R.id.btn_download_video).setOnClickListener(this);
		findViewById(R.id.btn_download_img).setOnClickListener(this);
		findViewById(R.id.btn_MultiCh).setOnClickListener(this);
		findViewById(R.id.btn_smartLink).setOnClickListener(this);
		findViewById(R.id.btn_change_dev_pwd).setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_photo:
			// ����ͼƬ
			intent = new Intent(this, AcMediaList.class);
			intent.putExtra("isImage", true);
			startActivityForResult(intent, 0);
			break;
		case R.id.btn_video:
			// ����¼��
			intent = new Intent(this, AcMediaList.class);
			intent.putExtra("getChannel", 0);
			intent.putExtra("isImage", false);
			startActivityForResult(intent, 0);
			break;
		case R.id.btn_door_settings:
			intent = new Intent(this, AcModifyDoorSetting.class);
			startActivity(intent);
			break;
		case R.id.btn_search_wifi:
			// ����wifi
			intent = new Intent(this, AcWifiList.class);
			startActivity(intent);
			break;
		case R.id.btn_dev_alarm_push:
			AlarmUtils.setAlarmPush(this, 1, Constants.UMID + " "
					+ Constants.iChNo, Constants.UMID, Constants.user,
					Constants.password, Constants.iChNo);
			break;
		case R.id.btn_dev_dis_alarm_push:
			AlarmUtils.setAlarmPush(this, 2, Constants.UMID + " "
					+ Constants.iChNo, Constants.UMID, Constants.user,
					Constants.password, Constants.iChNo);
			break;
		case R.id.btn_motion:
			// �ƶ����
			intent = new Intent(this, AcAlertSettings.class);
			startActivity(intent);
			break;
		case R.id.btn_remote_video:
			// Զ��¼��
			intent = new Intent(this, AcSearchRecord.class);
			startActivity(intent);
			break;
		case R.id.btn_dev_ip:

			intent = new Intent(this, AcDeviceIpSettings.class);
			startActivity(intent);
			break;
		case R.id.btn_dev_ip_1:

			intent = new Intent(this, AcDeviceIpSettingsJson.class);
			startActivity(intent);
			break;
		case R.id.btn_dev_time:
			intent = new Intent(this, AcDevTime.class);
			startActivity(intent);
			break;
		case R.id.btn_dev_storage:
			intent = new Intent(this, AcDevStorage.class);
			startActivity(intent);
			break;
		case R.id.btn_dev_code:
			intent = new Intent(this, AcDevCodeInfo.class);
			startActivity(intent);
			break;
		case R.id.btn_download_video:
			intent = new Intent(this, AcSearchRecord.class);
			intent.putExtra("isDownLoad", true);
			intent.putExtra("isvideo", true);
			startActivity(intent);
			break;
		case R.id.btn_download_img:
			intent = new Intent(this, AcSearchRecord.class);
			intent.putExtra("isDownLoad", true);
			intent.putExtra("isvideo", false);
			startActivity(intent);
			break;
		case R.id.btn_MultiCh:
			intent = new Intent(this, AcZeroChSettings.class);

			startActivity(intent);
			break;
		case R.id.btn_smartLink:
			smartLink();
			break;
		case R.id.btn_change_dev_pwd:
			changeDevPassword();
			break;
		default:
			break;
		}

	}

	/**
	 * ���1���ӳ�ʱ���أ���ȷ���豸�Ƿ��жԽ��޸��豸����Э��
	 */
	public void changeDevPassword() {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				PlayerClient pc = appMain.getPlayerclient();
				int ret = pc.CameraSetPasswordByUmid(Constants.UMID,
						Constants.user, "", "123456");
				if (ret == 1) {
					Log.d("changeDevPassword", "�޸��豸����ɹ�1" + "");
				} else {
					Log.d("changeDevPassword", "�޸��豸����ʧ��1" + ",ret=" + ret);
				}
				ret = pc.CameraSetPasswordByUmid(Constants.UMID,
						Constants.user, "123456", "");
				if (ret == 1) {
					Log.d("changeDevPassword", "�޸��豸����ɹ�2" + "");
				} else {
					Log.d("changeDevPassword", "�޸��豸����ʧ��2" + ",ret=" + ret);
				}
				super.run();
			}

		}.start();
	}

	public void smartLink() {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SmarkLinkTool
						.StartSmartConnection("HSKJ-DevlopeAP", "h123h123");

				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				SmarkLinkTool.StopSmartConnection();
				super.run();
			}

		}.start();
	}

}
