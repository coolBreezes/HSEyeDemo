package com.example.umeyesdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.Player.Core.PlayerCore;
import com.Player.Source.AudioDecodeListener;
import com.Player.Source.SDKError;
import com.Player.Source.StopRecodeVideoListener;
import com.example.umeyesdk.utils.MyAudioDecodeThread;
import com.example.umeyesdk.utils.MyRecoredThread;
import com.example.umeyesdk.utils.MyVideoDecodeThread;
import com.mp4.maker.MP4make;
import com.video.h264.DecodeDisplay;

public class PlayActivity extends Activity implements OnTouchListener,
		OnClickListener {
	// ��̨��������
	public static final byte MD_STOP = 0; // ֹͣ
	public static final byte MD_LEFT = 11; // ��
	public static final byte MD_RIGHT = 12; // ��
	public static final byte MD_UP = 9; // ��
	public static final byte MD_DOWN = 10; // ��
	public static final byte ACTION_ZOOMADD = 6; // ����
	public static final byte ACTION_ZOOMReduce = 5;// ��Զ
	public static final byte ACTION_FOCUSADD = 7;// ����+
	public static final byte ACTION_FOCUSReduce = 8;// �����
	public static final byte ACTION_Circle_Add = 13; // ��Ȧ+
	public static final byte ACTION_Circle_Reduce = 14;// ��Ȧ-

	public static final byte SHOW_STATE = 0;
	private PlayerCore pc;
	private String id = "";
	private ImageView img;
	private TextView txtState, txtRec;
	private boolean isStopCloudCommand = false;
	private ImageButton btnUp, btnDown, btnLeft, btnRight, btnZoomIn,
			btnZoomOut, btnNear, btnFar, btnCircleAdd, btnCircleReduce,
			btnTalk, btnSound;
	private Button btnPlay, btnPause, btnSnap, btnVideo;
	private boolean isRun = true;
	private Handler handler = new Handler() {

		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == SHOW_STATE) {
				txtState.setText(GetDescription(PlayActivity.this, msg.arg1));
				// �Ƿ���ʾ¼��
				txtRec.setVisibility(msg.arg2 == 1 ? View.VISIBLE : View.GONE);

			}

			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		initeView();
		pc = new PlayerCore(this);
		id = getIntent().getStringExtra("id");
		pc.InitParam(id, 0, img);
		// �رղ�����־���
		pc.SetOpenLog(true);
		pc.setStopRecodeVideoListener(new StopRecodeVideoListener() {

			@Override
			public void finish(boolean isSuccess, String path) {
				// TODO Auto-generated method stub
				Log.d("setStopRecodeVideoListener", "isSuccess:" + isSuccess
						+ ",path=" + path);
			}
		});
		// ����ƽ������
		// pc.SetPlayModel(1);
		// �����Զ�����룬�����̣߳�ʹ��Ĭ�ϵ���������

		/*
		 * pc.setAudioDecodeListener(new AudioDecodeListener() {
		 * 
		 * @Override public void StartTalk(PlayerCore playercore) { // TODO
		 * Auto-generated method stub // �Խ���¼���߳� MyRecoredThread myRecoredThread
		 * = new MyRecoredThread( playercore); myRecoredThread.start(); }
		 * 
		 * @Override public void StartAudioDecode(PlayerCore playercore,
		 * DecodeDisplay decodeDisplay) { // TODO Auto-generated method stub //
		 * ��Ƶ���벥���߳� MyAudioDecodeThread AudioThreadDecode = new
		 * MyAudioDecodeThread( playercore, decodeDisplay);
		 * AudioThreadDecode.start(); }
		 * 
		 * @Override public void startVideoDecode(DecodeDisplay arg0) { // TODO
		 * Auto-generated method stub // ��Ƶ�����߳� MyVideoDecodeThread
		 * defualtVideoDecodeThread = new MyVideoDecodeThread( arg0);
		 * defualtVideoDecodeThread.start();
		 * 
		 * }
		 * 
		 * });
		 */
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		pc.Play();
		isRun = true;
		new StateThread().start();
		super.onResume();
	}

	void initeView() {
		img = (ImageView) findViewById(R.id.imgLive);
		img.setOnTouchListener(this);
		txtState = (TextView) findViewById(R.id.txt_state);
		txtRec = (TextView) findViewById(R.id.tvwRec);
		btnPlay = (Button) findViewById(R.id.btnPlay);
		btnPause = (Button) findViewById(R.id.btnPause);
		btnSnap = (Button) findViewById(R.id.btnSnap);
		btnVideo = (Button) findViewById(R.id.btnVideo);

		btnPlay.setOnClickListener(this);
		btnPause.setOnClickListener(this);
		btnSnap.setOnClickListener(this);
		btnVideo.setOnClickListener(this);
		// ��̨���ư�ť

		btnUp = (ImageButton) findViewById(R.id.btnUp);
		btnUp.setOnTouchListener(this);
		btnUp.setBackgroundResource(R.drawable.btn_down_0);
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
			new ColudThread(command).start();

		} else if (action == MotionEvent.ACTION_UP) {

			btn.setBackgroundResource(bg0);
			isStopCloudCommand = true;

		}
	}

	class ColudThread extends Thread {
		private byte cmd;

		public ColudThread(byte cmd) {
			this.cmd = cmd;
		}

		@Override
		public void run() {
			while (!isStopCloudCommand
					&& pc.GetPlayerState() == SDKError.Statue_PLAYING) {

				pc.SetPtz(cmd, 5);
				System.out.println("������̨���" + cmd);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				pc.SetPtz(MD_STOP, 0);
			}
			System.out.println("ֹͣ����");
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

					Thread.sleep(500);
					Message msg = new Message();
					msg.what = SHOW_STATE;
					msg.arg1 = pc.PlayCoreGetCameraPlayerState();
					if (pc.GetIsSnapVideo()) {
						msg.arg2 = 1;
					}
					Log.w("GetIsPPT", "GetIsPPT: " + pc.GetIsPPT());
					handler.sendMessage(msg);

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnPlay:
			pc.Play();

			break;
		case R.id.btnPause:
			new Thread() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					pc.Stop();
					super.run();
				}

			}.start();

			break;
		case R.id.btnSnap:// Ĭ��·�� //sdcard/snapShot
			// �ж�SDCard
			// .......
			// pc.SetAlbumPath(TempALBUM_PATH); ����ͼƬ����·��
			// pc.SetSnapPicture(SnapPicture, TempFilenamePrefix),��ͼ������ͼƬ��������
			// pc.SetSnapPicture(true);
			if (!pc.GetIsPPT()) {
				pc.StartPPTAudio();
			} else {
				pc.StopPPTAudio();
			}

			break;
		case R.id.btnVideo:// Ĭ��·�� //sdcard/snapShot
			// �ж�SDCard
			// .......
			// pc.SetVideoPath(TempVIDEO_PATH);
			if (pc.GetIsSnapVideo()) {
				pc.SetSnapVideo(false);
			} else {
				pc.SetSnapVideo(true);
			}

			break;
		default:
			break;
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
			break;
		// case R.id.imgLive:
		// if (action == MotionEvent.ACTION_DOWN) {
		// img.setScaleType(ScaleType.MATRIX);
		// deal= Imagedeal.getdeal(img);
		//
		// } else if (action == MotionEvent.ACTION_UP) {
		// img.setScaleType(ScaleType.FIT_XY);
		// }
		// deal.set(v, event);
		// break;
		//
		}
		return true;

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		isRun = false;
		if (pc != null) {
			pc.Stop();
		}

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// pc.Stop();
		isRun = false;

		if (pc != null) {
			pc.Stop();
		}

		super.onDestroy();
	}

	// public static String GetDescription(Context con, int state) {
	// String des = con.getString(R.string.error_code) + ":" + state;
	// switch (state) {
	// case SDKError.Statue_Ready:
	// des = con.getString(R.string.ready);
	// break;
	// case SDKError.Statue_PLAYING:
	// des = con.getString(R.string.playing);
	// break;
	// case SDKError.Statue_STOP:
	// des = con.getString(R.string.stop);
	// break;
	// case SDKError.Statue_ConnectingServer:
	// des = con.getString(R.string.connecting);
	// break;
	// case SDKError.Statue_ConnectingSucess:
	// des = con.getString(R.string.connect_success);
	// break;
	// case SDKError.Statue_ConnectFail:
	// des = con.getString(R.string.connect_fail);
	// break;
	// case SDKError.NET_LOGIN_ERROR_PASSWORD:
	// des = con.getString(R.string.passworderro);
	// break;
	// case SDKError.NET_LOGIN_ERROR_TIMEOUT:
	// des = con.getString(R.string.time_out);
	// break;
	// case SDKError.NET_LOGIN_ERROR_USER:
	// des = con.getString(R.string.usererro);
	// break;
	//
	// case SDKError.Unknow:
	// des = con.getString(R.string.unknown_error);
	// break;
	// case SDKError.NET_ERROR:
	// des = con.getString(R.string.net_error);
	// break;
	// case SDKError.NET_NODATA_ERROR:
	// des = con.getString(R.string.no_data);
	//
	// case SDKError.Exception_ERROR:
	// des = con.getString(R.string.exception_error);
	// break;
	// case SDKError.NosupportDevice_ERROR:
	// des = con.getString(R.string.unsupport_device);
	// break;
	// case SDKError.Beyondmaxchannels_ERROR:
	// des = con.getString(R.string.max_channel);
	// break;
	// }
	// return des;
	// }
	public String GetDescription(Context con, int state) {
		Log.i("GetDescription", "GetDescription:" + state);
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
		case 10:
			des = con.getString(R.string.buffering);
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
		case SDKError.NPC_D_MPI_MON_ERROR_REJECT_ACCESS:
			des = con.getString(R.string.NPC_D_MPI_MON_ERROR_REJECT_ACCESS);
			break;
		case -112:
			des = con.getString(R.string.NPC_D_MPI_MON_ERROR_CAMERA_OFFLINE);
			break;
		}
		des = des + "             " + pc.GetPlayFrameRate() + "fps";
		return des;

	}
}
