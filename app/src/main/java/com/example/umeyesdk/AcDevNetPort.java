package com.example.umeyesdk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.stream.NewAllStreamParser;

public class AcDevNetPort extends Activity {
	String umid = "xmksit5p4dfu";// //xmumh9ckm0mu //umkss83g7brx
	TextView textshow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		textshow = new TextView(this);
		setContentView(textshow);
		setTitle("�˿�ӳ��");
		textshow.setText("���Ժ�..\n");
		// String umid = getIntent().getStringExtra("umid");
		textshow.setText("����UMID��" + umid + "\n���Ժ�..\n");
		String configPath = "//data//data//" + this.getPackageName() + "//"
				+ "Config";
		NewAllStreamParser.sSaveModeDirName = configPath;

		new Thread() {

			@Override
			public void run() {
				int handle = NewAllStreamParser.DNPCreatePortServer(
						"app.umeye.cn", 8300, "sdktest", "sdktest");// //cloud.hzmr-tech.com
				if (handle == 0) {
					postMainThread("����ӳ�����ʧ��");
					return;
				} else
					postMainThread("����ӳ�����ɹ�");
				int state = NewAllStreamParser.DNPCheckSrvConnState(handle);
				int checkTimes = 0;
				try {
					while (state != 2) { // 2 Ϊ ������
						if (checkTimes >= 30) {
							postMainThread("���ӳ�ʱ����");
							return;
						}
						Thread.sleep(1000);
						checkTimes++;
						state = NewAllStreamParser.DNPCheckSrvConnState(handle);
					}
					postMainThread("���ӳɹ�");
					// int port = NewAllStreamParser.DNPAddPort(handle, umid);
					// if (port == 0) {
					// postMainThread("�����˿�ʧ��:");
					// } else
					// postMainThread("�����˿ڳɹ��� " + port);
					int port1 = NewAllStreamParser.DNPAddPortByChNo(handle,
							umid, 0);
					if (port1 == 0) {
						postMainThread("�����˿�ʧ��:" + port1);
					} else
						postMainThread("�����˿ڳɹ��� " + port1);
					/**
					 * ͨ�Ŵ���
					 */
					NewAllStreamParser.DNPDelPort(handle, port1);
					postMainThread("ɾ���˿�");
					NewAllStreamParser.DNPDestroyPortServer(handle);
					postMainThread("����ӳ�����");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.run();
			}
		}.start();

	}

	public void postMainThread(final String info) {
		textshow.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				textshow.append(info + "\n");
				Log.d("postMainThread", "info:" + info);
			}
		});
	}

}
