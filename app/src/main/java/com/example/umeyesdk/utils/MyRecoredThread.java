package com.example.umeyesdk.utils;

import java.nio.ByteBuffer;

import com.Player.Core.PlayerCore;
import com.audio.g711adec;
import com.audio.junjiadpcmdec;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class MyRecoredThread extends Thread{
	private PlayerCore playercore;
	private junjiadpcmdec junjiadpcm_dec = null;
	private g711adec g711a_dec = null;
	private ByteBuffer pG711aBuffer = ByteBuffer.allocate(80 * 10 * 36);
	public MyRecoredThread(PlayerCore playercore) {
		super();
		this.playercore = playercore;
	}
	@Override
	public synchronized void run() {
		Log.d("auDecoder", "run");
		ThreadRecordAudio();
		/*
		 * try { while(true)//֪��¼���߳̽��� { Thread.sleep(20); } }catch
		 * (Exception e) { e.printStackTrace(); }
		 */
	}
	@SuppressWarnings("deprecation")
	public void ThreadRecordAudio()// ¼��ʱ��һ��Ҫ���϶�������ȫ������
	{
		AudioRecord recorder = null;
		try {
			// ���¼����������С
			int bufferSize = AudioRecord.getMinBufferSize(
					playercore.RecordSamplingRate,
					AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT);

			Log.e("", "¼����������С" + bufferSize);

			// ���¼��������
			recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
					playercore.RecordSamplingRate,
					AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize * 10);

			recorder.startRecording();// ��ʼ¼��

			byte[] readBuffer = new byte[640 * 5];// ¼��������
			int length = 0;
			int G711asize = 0;
			playercore.PPTisover = false;
			while (playercore.IsPPTaudio && playercore.ThreadisTrue) {

				if (playercore.audioppttype == PlayerCore.AUDIOPPT_G711A) {
					length = recorder.read(readBuffer, 0,
							playercore.RecordVocSize);// 1024);//640*2);//
														// ��mic��ȡ��Ƶ����
					if (length > 0 && length % 2 == 0) {
						Log.w("¼��", "¼������ �����ǣ�" + length);
						// Log.d("����¼��", "¼������ �����ǣ�"+length);
						ByteBuffer pInBuffer = ByteBuffer.wrap(readBuffer, 0,
								length);
						pInBuffer.position(0);
						if (g711a_dec == null) { // ����
							g711a_dec = new g711adec();
						}
						G711asize = g711a_dec.EncodeOneFrame(pInBuffer,
								pG711aBuffer);
						if (playercore.GetOpenLog())
							Log.w("¼��", "ѹ�����¼������ �����ǣ�" + G711asize);
						playercore.SendPPTAudio(pG711aBuffer, G711asize, 1);
					}
				} else if (playercore.audioppttype == PlayerCore.AUDIOPPT_JUNJIADPCM)// ����
																						// ADPCM
				{
					length = recorder.read(readBuffer, 0,
							playercore.RecordVocSize);// ��mic��ȡ��Ƶ����
					if (length > 0 && length % 2 == 0) {
						ByteBuffer pInBuffer = ByteBuffer.wrap(readBuffer, 0,
								length);
						pInBuffer.position(0);
						if (junjiadpcm_dec == null) { // ����
							junjiadpcm_dec = new junjiadpcmdec();
						}
						synchronized (junjiadpcm_dec) {
							G711asize = junjiadpcm_dec.EncodeOneFrame(
									pInBuffer, pG711aBuffer);
						}
						if (playercore.GetOpenLog())
							Log.w("¼��", "ѹ�����¼������ �����ǣ�" + G711asize);
						playercore.SendPPTAudio(pG711aBuffer, G711asize, 1);
					}
				} else {
					length = recorder.read(readBuffer, 0,
							playercore.RecordVocSize);// 1024);//640*2);//
														// ��mic��ȡ��Ƶ����
					if (length > 0 && length % 2 == 0) {
						ByteBuffer pInBuffer = ByteBuffer.wrap(readBuffer, 0,
								length);
						pInBuffer.position(0);
						if (playercore.GetOpenLog())
							Log.w("¼��", "ѹ�����¼������ �����ǣ�" + length);
						// FileOutputStream outputStream = new FileOutputStream(
						// file, true);
						// //outputStream.write(pInBuffer.array());
						// outputStream.write(pInBuffer.array(), 0, length);
						// outputStream.close();
						playercore.SendPPTAudio(pInBuffer, length, 0);
					}
				}

				Thread.sleep(10);
			}

			recorder.stop();
			recorder.release();
			recorder = null;
			playercore.PPTisover = true;
			playercore.IsPPTaudio = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
