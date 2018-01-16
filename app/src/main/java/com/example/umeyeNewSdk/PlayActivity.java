package com.example.umeyeNewSdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.Player.Core.PlayerClient;
import com.Player.Core.PlayerCore;
import com.Player.Source.SDKError;
import com.Player.Source.TAlarmFrame;
import com.example.umeyesdk.AppMain;
import com.example.umeyesdk.R;
import com.example.umeyesdk.utils.Constants;
import com.example.umeyesdk.utils.Show;
import com.example.umeyesdk.utils.ShowProgress;

public class PlayActivity extends Activity implements OnTouchListener,
		OnClickListener {

	public static final int CREATE_CILENT = 0x123;
	public static final int DESTORY_CILENT = 0x124;
	// ��̨��������
	public static final byte MD_STOP = 0; // ֹͣ
	public static final byte MD_LEFT = 11; // ��
	public static final byte MD_RIGHT = 12; // ��
	public static final byte MD_UP = 9; // ��
	public static final byte MD_DOWN = 10; // ��
	public static final byte CORE_UP_LEFT = 35; // ����
	public static final byte CORE_UP_RIGHT = 36; // ����
	public static final byte CORE_DOWN_LEFT = 37; // ����
	public static final byte CORE_DOWN_RIGHT = 38; // ����
	public static final byte ACTION_ZOOMADD = 6; // ����
	public static final byte ACTION_ZOOMReduce = 5;// ��Զ
	public static final byte ACTION_FOCUSADD = 7;// ����+
	public static final byte ACTION_FOCUSReduce = 8;// �����
	public static final byte ACTION_Circle_Add = 13; // ��Ȧ+
	public static final byte ACTION_Circle_Reduce = 14;// ��Ȧ-
	public static final int NPC_D_MON_PTZ_CMD_SET_PRESET = 15; // ����Ԥ�õ�
	public static final int NPC_D_MON_PTZ_CMD_CLE_PRESET = 16; // ���Ԥ�õ�
	public static final byte ACTION_GOTO_PRESET_POSITION = 39;

	public static final byte SHOW_STATE = 0;

	public static final byte ALARM_STATE = 1;
	private PlayerCore pc;
	private String id = "";
	private ImageView img;
	private TextView txtState, txtRec;
	private boolean isStopCloudCommand = false;
	private ImageButton btnUp, btnDown, btnLeft, btnRight, btnZoomIn,
			btnZoomOut, btnNear, btnFar, btnCircleAdd, btnCircleReduce,
			btnTalk, btnSound;
	private Button btnPlay, btnPause, btnSnap, btnVideo, btnSearch;
	private ImageButton btnMenu, btnUnLock;
	public static boolean isRun = true;
	PlayerClient playClient;
	private EditText server, port, umid, user, password;
	AppMain appMain;

	// private boolean isStart = false;

	private Handler handler = new Handler() {

		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == SHOW_STATE) {
				txtState.setText(GetDescription(PlayActivity.this, msg.arg1));
				// �Ƿ���ʾ¼��
				txtRec.setVisibility(msg.arg2 == 1 ? View.VISIBLE : View.GONE);

			} else if (msg.what == CREATE_CILENT) {
				showProgress.dismiss();
			} else if (msg.what == DESTORY_CILENT) {
				showProgress.dismiss();
				finish();
			}
			// else if (msg.what == SetWIFIRunable.SET_WIFI_SUCCESS) {
			// Toast.makeText(PlayActivity.this, "����wifi�ɹ�",
			// Toast.LENGTH_SHORT).show();
			// } else if (msg.what == SetWIFIRunable.SET_WIFI_FAILED) {
			// Toast.makeText(PlayActivity.this, "����wifiʧ��~~",
			// Toast.LENGTH_SHORT).show();
			// }

			super.handleMessage(msg);
		}

	};
	private ShowProgress showProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.play_p2p);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initeView();
		appMain = (AppMain) this.getApplicationContext();
		playClient = appMain.getPlayerclient();
		initePlayCore();
		// pc.SetPPtMode(true);
		EditEditetext();
	}

	public void initePlayCore() {
		pc = new PlayerCore(this);
		pc.InitParam("", -1, img);
		pc.SetPPtMode(false);
		pc.isQueryDevInfo = true;
	}

	public void EditEditetext() {

		user.setText(Constants.user);
		password.setText(Constants.password);
		umid.setText(Constants.UMID);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		isRun = true;
		umid.setText(Constants.UMID);
		new StateThread().start();
		super.onResume();
	}

	void initeView() {
		img = (ImageView) findViewById(R.id.imgLive);
		txtState = (TextView) findViewById(R.id.txt_state);
		txtRec = (TextView) findViewById(R.id.tvwRec);
		btnPlay = (Button) findViewById(R.id.btnPlay);
		btnPause = (Button) findViewById(R.id.btnPause);
		btnSnap = (Button) findViewById(R.id.btnSnap);
		btnVideo = (Button) findViewById(R.id.btnVideo);
		btnSearch = (Button) findViewById(R.id.search);

		btnMenu = (ImageButton) findViewById(R.id.btnMenu);
		btnUnLock = (ImageButton) findViewById(R.id.btnLock);

		btnPlay.setOnClickListener(this);
		btnPause.setOnClickListener(this);
		btnSnap.setOnClickListener(this);
		btnVideo.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		btnUnLock.setOnClickListener(this);

		// �豸��Ϣ
		server = (EditText) findViewById(R.id.server);
		port = (EditText) findViewById(R.id.port);
		umid = (EditText) findViewById(R.id.umid);
		user = (EditText) findViewById(R.id.user);
		password = (EditText) findViewById(R.id.password);
		// ��̨���ư�ť
		btnTalk = (ImageButton) findViewById(R.id.btnTalk);
		btnTalk.setOnClickListener(this);

		btnUp = (ImageButton) findViewById(R.id.btnUp);
		btnUp.setOnTouchListener(this);

		btnDown = (ImageButton) findViewById(R.id.btnDown);
		btnDown.setOnTouchListener(this);

		btnLeft = (ImageButton) findViewById(R.id.btnLeft);
		btnLeft.setOnTouchListener(this);

		btnRight = (ImageButton) findViewById(R.id.btnRight);
		btnRight.setOnTouchListener(this);

	}

	/**
	 * ִ����̨����
	 * 
	 * @param action
	 *            ��ť������
	 * @param command
	 *            ��̨����
	 */
	private void ExcuteCommand(View btn, int bg0, int bg1, int action,
			byte command) {
		if (action == MotionEvent.ACTION_DOWN) {

			btn.setBackgroundResource(bg1);
			isStopCloudCommand = false;
			// new ColudThread(command).start();
			if (command == MD_DOWN || command == MD_UP || command == MD_LEFT
					|| command == MD_RIGHT) {
				int length = 4;
				System.out.println("������̨���" + command + ",��̨������" + length);
				if (pc.GetPlayerState() == SDKError.Statue_PLAYING)
					pc.SetPtz(command, length);
			} else {
				if (pc.GetPlayerState() == SDKError.Statue_PLAYING)
					pc.SetPtz(command, 0);
				System.out.println("������̨���" + command + ",��̨������" + 0);
			}

		} else if (action == MotionEvent.ACTION_UP) {

			btn.setBackgroundResource(bg0);
			isStopCloudCommand = true;
			if (pc.GetPlayerState() == SDKError.Statue_PLAYING)
				pc.SetPtz(MD_STOP, 0);

		}
	}

	/**
	 * ״̬��ʾ�߳�
	 * 
	 * @author Simula
	 * 
	 */
	class StateThread extends Thread {

		@Override
		public void run() {

			try {
				while (isRun) {

					Thread.sleep(1000);
					Message msg = new Message();
					msg.what = SHOW_STATE;
					msg.arg1 = pc.PlayCoreGetCameraPlayerState();
					if (pc.GetIsSnapVideo()) {
						msg.arg2 = 1;
					}
					Log.w("state", "state: " + msg.arg1 + ",pc.GetIsPPT():"
							+ pc.GetIsPPT());
					handler.sendMessage(msg);

					TAlarmFrame tAlarmFrame = pc.CameraGetAlarmInfo();
					if (tAlarmFrame != null) {
						handler.sendMessage(Message.obtain(handler,
								ALARM_STATE, tAlarmFrame));
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	public void Stop() {
		Stop(null);
	}

	public void Stop(final Handler handler) {
		if (pc.GetIsPPT()) {
			pc.StopPPTAudio();
			btnTalk.setBackgroundResource(R.drawable.ch_talk);
		}
		new Thread() {
			@Override
			public void run() {

				pc.Stop();
				if (handler != null) {
					handler.sendEmptyMessage(0);
				}
			}
		}.start();
	}

	int stream = 1;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnPlay:
			selectChannel();
			break;
		case R.id.btnPause:
			pc.StopPPTAudio();
			Stop();
			//

			// pc.SetPtz(NPC_D_MON_PTZ_CMD_SET_PRESET, 2);//����Ԥ�õ�
			break;
		case R.id.btnSnap:// Ĭ��·�� //sdcard/snapShot
			// pc.SetPtz(ACTION_GOTO_PRESET_POSITION, 2);//��תԤ�õ�

			// �ж�SDCard
			// .......
//			 pc.SetAlbumPath(TempALBUM_PATH); ����ͼƬ����Ƶ����·��
//			 pc.SetSnapPicture(SnapPicture, TempFilenamePrefix),��ͼ
			// pc.SetVideoPath(TempVIDEO_PATH)
			// if (pc.GetPlayerState() == SDKError.Statue_PLAYING)
			pc.SetSnapPicture(true);
			Show.toast(this,"save in /sdcard/snapShot/");
			// new SetWIFIRunable("HSKJ-Develope2", "h123h123",
			// "192.168.10.103","",34567, playClient, handler).start();

//			if (stream == 2) {
//				stream = 0;
//			} else
//				stream++;
//			int ret = pc.CameraSwitchChannel(stream);
//			Log.d("CameraSwitchChannel", " �л�����ֵ:" + stream + ",���أ�" + ret);
			break;
		case R.id.btnVideo:// Ĭ��·�� //sdcard/videorecord
			// �ж�SDCard
			// .......

			if (pc.GetIsSnapVideo()) {
				pc.SetSnapVideo(false);
				Show.toast(this,"video save in /sdcard/videorecord");
			} else {
				if (pc.GetPlayerState() == SDKError.Statue_PLAYING) {
					pc.SetSnapVideo(true);
					Show.toast(this,"start record");
				}

			}

			// setWifi();

			break;
		case R.id.btnTalk:// �����Խ�

			if (pc.GetIsPPT()) {
				pc.StopPPTAudio();
				btnTalk.setBackgroundResource(R.drawable.ch_talk);
			} else {
				pc.StartPPTAudio();
				btnTalk.setBackgroundResource(R.drawable.ch_talk_h);
			}
			// pc.OpenAudio();
			break;
		case R.id.search:// ������������ͷ
		{
			startActivityForResult(new Intent(this, AcSearchDevice.class), 3);
			/*
			 * int SearchDevnum=playClient.StartSearchDev(5);
			 * System.out.println("SearchDevnum is "+SearchDevnum);
			 * if(SearchDevnum>0) for(int i=0;i<SearchDevnum;i++) { TSearchDev
			 * tempSearchDev=playClient.SearchDevByIndex(i);
			 * if(tempSearchDev.iDevPort==0) tempSearchDev.iDevPort=34567;
			 * System
			 * .out.println("SearchDev infor is "+tempSearchDev.sIpaddr_1+":"
			 * +tempSearchDev.iDevPort + "  UMID:"+tempSearchDev.sDevId); }
			 * playClient.StopSearchDev();
			 */
			/*
			 * TDevWifiInfor tmpTDevWifiInfor=
			 * playClient.CameraGetWIFIConfig(Constants
			 * .UMID,"admin","");//("umum06pi8w3j","admin","123456");
			 * if(tmpTDevWifiInfor!=null) {
			 * System.out.println("tmpTDevWifiInfor infor is "
			 * +tmpTDevWifiInfor.sWifiSSID + "  iRSSI:"+tmpTDevWifiInfor.iRSSI);
			 * }
			 * 
			 * 
			 * int
			 * ret=playClient.CameraSearchWifiAp(Constants.UMID,"admin","");/
			 * /("umum06pi8w3j","admin","123456");
			 * System.out.println("CameraSearchWifiAp  is "+ ret); int index=0;
			 * while(true) { TWifiApInfor
			 * tmpTWifiApInfor=playClient.CLTGetWifiApInfo(index);
			 * if(tmpTWifiApInfor!=null) { if(tmpTWifiApInfor.sSSID.length()>0)
			 * System
			 * .out.println("tmpTDevWifiInfor infor is "+tmpTWifiApInfor.sSSID +
			 * "  iRSSI:"+tmpTWifiApInfor.iRSSI); else break; }else break;
			 * index++; }
			 */
			break;
		}
		case R.id.btnMenu:
			startActivity(new Intent(this, AcMenu.class));
			break;
		case R.id.btnLock:
			/**
			 * ���� ������8λ��������
			 */
			// pc.SendOpenLockCmd("88888888");
			if (!pc.GetIsVoicePause()) {
				pc.CloseAudio();
				btnUnLock.setBackgroundResource(R.drawable.live_sound);
			} else {
				pc.OpenAudio();
				btnUnLock.setBackgroundResource(R.drawable.live_sound_pre);
			}
			break;
		default:
			break;
		}
	}

	private void setWifi() {
		// TODO Auto-generated method stub
		new SetWifi(this, playClient, "E0008", "12345678").execute();
	}

	class SetWifi extends AsyncTask<Void, Integer, Void> {
		Context context;
		ShowProgress showProgress;
		String ssid;
		String password;
		PlayerClient playClient;

		public SetWifi(Context context, PlayerClient playClient, String ssid,
				String password) {
			super();
			this.context = context;
			this.ssid = ssid;
			this.password = password;
			this.playClient = playClient;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			showProgress = new ShowProgress(context);
			showProgress.setMessage("����WIFI��");
			showProgress.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			showProgress.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			int time = 30;
			while (time > 0) {
				time--;
				playClient.YXSmartWifiConfig(ssid, password);
				Log.d("YXSmartWifiConfig", "YXSmartWifiConfig" + time);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				publishProgress(time);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			showProgress.setMessage("�������óɹ���ʣ" + values[0] + 1 + "��");
			super.onProgressUpdate(values);
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();

		switch (v.getId()) {
		case R.id.btnUp:
			ExcuteCommand(btnUp, R.drawable.btn_up_0, R.drawable.btn_up_1,
					action, MD_UP);
			break;
		case R.id.btnDown:
			ExcuteCommand(btnDown, R.drawable.btn_down_0,
					R.drawable.btn_down_1, action, MD_DOWN);
			break;
		case R.id.btnLeft:
			ExcuteCommand(btnLeft, R.drawable.btn_left_0,
					R.drawable.btn_left_1, action, MD_LEFT);
			break;
		case R.id.btnRight:
			ExcuteCommand(btnRight, R.drawable.btn_right_0,
					R.drawable.btn_right_1, action, MD_RIGHT);
		}
		return true;

	}

	@Override
	protected void onPause() {
		Constants.UMID = umid.getText().toString();
		Constants.user = user.getText().toString();
		Constants.password = password.getText().toString();
		// TODO Auto-generated method stub
		Stop();
		isRun = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// pc.Stop();
		isRun = false;
		super.onDestroy();
	}

	/*
	 * private static final int NPC_D_MPI_MON_ERROR_USERID_ERROR = -101; //
	 * �û�ID���û������� private static final int NPC_D_MPI_MON_ERROR_USERPWD_ERROR =
	 * -102; // �û�������� private static final int
	 * NPC_D_MPI_MON_ERROR_REJECT_ACCESS = -111; // Ȩ�޲���
	 */
	public String GetDescription(Context con, int state) {
		String des = con.getString(R.string.connect_fail);
		switch (state) {
		case 0:
			des = con.getString(R.string.ready);
			break;
		case 1:
			des = con.getString(R.string.connecting);
			break;
		case 2:
			des = con.getString(R.string.playing);
			break;
		case 3:
			des = con.getString(R.string.connect_fail);
			break;
		case 4:
			des = con.getString(R.string.stop);
			break;
		case 7:
			des = con.getString(R.string.stop);
			break;
		case SDKError.NPC_D_MPI_MON_ERROR_USERID_ERROR:
			des = con.getString(R.string.usererro);
			break;
		case SDKError.NPC_D_MPI_MON_ERROR_USERPWD_ERROR:
			des = con.getString(R.string.passworderro);
			break;
		case 10:
			des = "������";
			break;
		case SDKError.NPC_D_MPI_MON_ERROR_REJECT_ACCESS:
			des = con.getString(R.string.NPC_D_MPI_MON_ERROR_REJECT_ACCESS);
			break;
		}
		return des;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

			isRun = false;
			if (showProgress == null) {
				showProgress = new ShowProgress(this);

			}
			showProgress.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					playClient.PlayerClient_RTS_DestroyClient();
					handler.sendEmptyMessage(DESTORY_CILENT);

				}
			}).start();
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	public void Vibrate(Context context, long milliseconds) {
		Vibrator vib = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	public void selectChannel() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
				PlayActivity.this);
		String[] items = new String[16];
		for (int i = 0; i < items.length; i++) {
			items[i] = "play " + (i + 1) + " channel";
		}
		alertBuilder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Constants.UMID = umid.getText().toString();
				Constants.user = user.getText().toString();
				Constants.password = password.getText().toString();
				Constants.iChNo = which;
				Stop(new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub

						pc.PlayP2P(Constants.UMID, Constants.user,
								Constants.password, Constants.iChNo, 1);
//						pc.PlayAddress(1009, "192.168.10.247", 5800, "admin",
//								"", 0, 1);
						super.handleMessage(msg);
					}
				});

			}
		}).setNegativeButton(R.string.negative, null).show();
	}
}
