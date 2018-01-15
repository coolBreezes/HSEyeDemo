package com.example.umeyesdk.utils;

import java.nio.ByteBuffer;

import android.os.Message;
import android.util.Log;
import android.view.View;

import com.Player.Core.PlayerCore;
import com.Player.Source.TSourceFrame;
import com.video.VideoFrameInfor;
import com.video.h264.DecodeDisplay;
import com.video.h264.DisplayHandler;
import com.video.h264.H264DecodeInterface;
import com.video.h264.LysH264Decode;

public class MyVideoDecodeThread extends Thread {
	DecodeDisplay decodeDisplay;
	PlayerCore playercore;
	private H264DecodeInterface myH264Decode = null;
	private int FrameRate = 0;// ��̬֡��
	DisplayHandler displayHandler;
	int lastvideoencode = 0;// ��һ�ε���Ƶ�����ʽ�������һ����Ҫ����
	/**
	 * �Ƿ��ڴ����
	 */
	boolean RgbHaveOutOfMemory = false;
	int RgbOutOfMemoryIndex = 0;
	private int LastDecodeVideoWidth = 352;// 176;// 352;
	private int LastDecodeVideoHeight = 288;// 144; // 288;

	public MyVideoDecodeThread(DecodeDisplay decodeDisplay) {
		this.playercore = decodeDisplay.playercore;
		this.decodeDisplay = decodeDisplay;
		this.displayHandler = decodeDisplay.displayHandler;
	}

	@Override
	public void run() {

		Log.d("VideoThreadDecode", "VideoThreadDecode run");
		VideoDecode();
		FrameRate = 0;
		RgbOutOfMemoryIndex = 0;
		RgbHaveOutOfMemory = false;
		decodeDisplay.pYuvBuffer = null;
		if (myH264Decode != null) {
			synchronized (myH264Decode) {
				if (myH264Decode != null) {
					myH264Decode.destroy();
					myH264Decode = null;
				}

			}
		}
		Log.d("VideoThreadDecode", "VideoThreadDecode exit");
	}

	synchronized void VideoDecode() {
		decodeDisplay.CurrentPlayTime = 0;
		int decodeindex = 0;
		long decodetimeaverage = 0;
		long videoStarTime = 0;
		// long AllFrameRate = 0;
		long videoDecodeStarTime = 0;

		int onSecondFrame = 0;
		long onSecondTimeMiles = 0;
		playercore.isHaveVideodata = false;
		while (playercore.ThreadisTrue) {
			try {
				decodeDisplay.isSnap();
				if (playercore.IsPausing)// ��ͣ
				{
					Thread.sleep(20);
					continue;
				}

				TSourceFrame mFrame = null;
				int leftvideo = playercore.GetVideoFrameLeft();
				if (leftvideo > 120 && playercore.GetPlayModel() == 0)// ��֡����
				{
					while (leftvideo > 0) {
						leftvideo = playercore.GetVideoFrameLeft();
						mFrame = playercore.GetNextVideoFrame();
						if (mFrame == null)
							break;
						if (mFrame.Framekind == 1)// �ȵ�����I֡ʱ����˳���֡
							break;
						decodeDisplay.isRecord(mFrame);
						if (!playercore.ThreadisTrue)
							return;
					}
				} else {
					mFrame = playercore.GetNextVideoFrame();// �¼���
				}

				if (mFrame == null) {
					Thread.sleep(10);
					continue;
				}
				videoStarTime = System.currentTimeMillis();
				decodeindex++;
				decodetimeaverage += System.currentTimeMillis() - videoStarTime;

				if (mFrame.iFrameFlag == 2 || mFrame.iFrameFlag == 1) {// �򲥷���������ֹͣ
					decodeDisplay.CurrentPlayTime = playercore
							.GetFileAllTime_Int();
					Log.d("total and current", "GetFileAllTime_Int:"
							+ decodeDisplay.CurrentPlayTime + ",�򲥷���������ֹͣ");
					continue;
				}
				if (mFrame.iData[0] == -1 && mFrame.iData[1] == -40
						&& mFrame.iData[2] == -1 && mFrame.iData[3] == -32
						&& mFrame.iData[6] == 0x4A && mFrame.iData[7] == 0x46
						&& mFrame.iData[8] == 0x49 && mFrame.iData[9] == 0x46)// MJPG���Ӱ���
				{
					mFrame.EncodeType = 3;
				}

				if (playercore.GetPlayModel() == 2) { // i֡ģʽ
					if (mFrame.Framekind != 1) {
						decodeDisplay.isRecord(mFrame);
						continue;
					}
				}
				decodeDisplay.dataCount += (mFrame.iLen);
				ByteBuffer pInBuffer = ByteBuffer.wrap(mFrame.iData, 0,
						mFrame.iLen);

				playercore.PowerLeft = mFrame.nParam2;// ���ڶ�����˵���豸ʣ�����
				playercore.DeviceVersionNo = mFrame.nParam1;

				pInBuffer.position(0);
				if (displayHandler.pRGBBuffer != null)
					displayHandler.pRGBBuffer.position(0);

				int DecodeLength = 0;
				VideoFrameInfor tmpFrameInfor = null;
				{
					if (myH264Decode != null) {
						if (mFrame.EncodeType != lastvideoencode)// �����ʽ���л�
						{
							synchronized (myH264Decode) {
								if (myH264Decode != null)
									myH264Decode.destroy();
								myH264Decode = null;
								System.gc();
								myH264Decode = new LysH264Decode();
								myH264Decode.initEx(mFrame.EncodeType);
							}

						}
					}
					lastvideoencode = mFrame.EncodeType;
					if (mFrame.EncodeType == 4)// H265
					{
						if (myH264Decode == null) {
							myH264Decode = new LysH264Decode();
							int ret = myH264Decode.initEx(4);
							if (ret == -1) {
								return;
							}
						}
					} else if (mFrame.EncodeType == 3 || mFrame.EncodeType == 2)// MJPG
					{
						if (myH264Decode == null) {
							myH264Decode = new LysH264Decode();
							int ret = myH264Decode.initEx(3);
							if (ret == -1) {
								return;
							}
						}
					} else if (mFrame.EncodeType == 1)// MPEG4
					{
						if (myH264Decode == null) {
							myH264Decode = new LysH264Decode();
							int ret = myH264Decode.initEx(1);
							if (ret == -1) {
								return;
							}
						}
					} else// h264
					{
						if (myH264Decode == null) {
							myH264Decode = new LysH264Decode();
							if (playercore.GetOpenLog())
								Log.d("OpenLog",
										"L264Decode_InitExEx new LysH264Decode()");
							int ret = myH264Decode.initEx(0);//
							if (playercore.GetOpenLog())
								Log.d("OpenLog",
										" L264Decode_InitExEx L264Decode_InitExEx finish");
							if (ret == -1) {
								return;
							}
						}
					}

					if (myH264Decode != null) {
						int ret = 0;
						long iSpan01 = 0;
						long iSpan02 = 0;
						long iSpan03 = 0;
						long iSpan04 = 0;
						synchronized (myH264Decode)// ͬ������������ ��Ҫ�Ǹ��ͷŵ�ʱ��ͬ��
						{
							if (playercore.GetOpenLog())
								iSpan01 = System.currentTimeMillis();
							ret = myH264Decode.DecodeOneFrameEx(pInBuffer,
									mFrame.iLen);
							if (playercore.GetOpenLog())
								iSpan02 = System.currentTimeMillis();
							if (ret > 0) {
								if (videoDecodeStarTime == 0) {
									videoDecodeStarTime = System
											.currentTimeMillis();
								}
								int tmpwidht = myH264Decode.GetPictureWidth();
								int tmpheight = myH264Decode.GetPictureHeight();
								// ����RGBbuffer
								if (!createRGBBuffer(tmpwidht, tmpheight)) {
									continue;
								}
								if (playercore.GetOpenLog())
									iSpan03 = System.currentTimeMillis();
								if (playercore.DisplayMode == 1) {
									if (decodeDisplay.pYuvBuffer != null) {
										{
											tmpFrameInfor = myH264Decode
													.GetYuv(decodeDisplay.pYuvBuffer);
										}
									}
								} else {
									tmpFrameInfor = myH264Decode.Yuv2Rgb(
											displayHandler.pRGBBuffer,
											playercore.FMT_RGB);
								}
								if (playercore.GetOpenLog()) {
									iSpan04 = System.currentTimeMillis();
									long decost = (iSpan02 - iSpan01);
									long yuvcost = (iSpan04 - iSpan03);
									long allcost = decost + yuvcost;
									Log.w("DisplayThread",
											"Decode cost:"
													+ decost
													+ ", Yuv2Rgb cost:"
													+ yuvcost
													+ ", all cost:"
													+ allcost
													+ ", VideoLeft:"
													+ playercore
															.GetVideoFrameLeft()
													+ ", �������ͣ�"
													+ mFrame.EncodeType
													+ ",input length:"
													+ mFrame.iLen);

								}
							} else {
								Log.w("DisplayThread", "Decode fail....");//
							}
						}
						decodeDisplay.isRecord(mFrame);
						if (playercore.DisplayMode == 0) {
							if (tmpFrameInfor != null) {
								decodeDisplay.VideoWidth = tmpFrameInfor.VideoWidth;
								decodeDisplay.VideoHeight = tmpFrameInfor.VideoHeight;
								DecodeLength = tmpFrameInfor.DecodeLength;
							} else {
								decodeDisplay.VideoWidth = 0;
								decodeDisplay.VideoHeight = 0;
								DecodeLength = 0;
							}
						}
					}
				}
				// �������ɹ����ѽ��������ͼƬ��ʾ����
				if (decodeDisplay.mImageView.getVisibility() != View.VISIBLE)// ���������ImageView���ؾͲ���ͼ
				{
					Thread.sleep(20);
					Log.e("mImageView",
							"mImageView.getVisibility() == View.GONE");
					continue;
				}

				if (mFrame.dwPlayPos > 0)// ¼��ط�
					decodeDisplay.CurrentPlayTime = mFrame.dwPlayPos;// ����Ϊ��λ*1000);
				else
					decodeDisplay.CurrentPlayTime = mFrame.iPTS;// ����Ϊ��λ*1000);
				if (mFrame.iPTS != 0) {
					decodeDisplay.CurrentTime = mFrame.iPTS;
				}
				// Log.d("total and current", "current is:" + CurrentPlayTime
				// + ",֡��ʶ��" + mFrame.iFrameFlag);

				if (playercore.DisplayMode == 1)
					DecodeLength = 1;
				if (DecodeLength > 0) {
					if (playercore.DisplayMode == 0 || mFrame.EncodeType == 3)// JPG��ʽ�Ļ���Ҫ����һ��
					{
						if (decodeDisplay.mImageView != null)// ImageView��ʽ��ͼ
						{
							if (decodeDisplay.mImageView.getVisibility() != View.GONE) // ���صľͲ���ͼ
							{
								if (playercore.GetOpenLog())
									Log.w("������ͼ�¼�", "");
								Message msg = displayHandler.obtainMessage(1);
								msg.arg1 = decodeDisplay.VideoWidth;
								msg.arg2 = decodeDisplay.VideoHeight;
								displayHandler.sendMessage(msg);// DisplayHandler.obtainMessage());
							}
						}
					}

					int leftvideoframe = playercore.GetVideoFrameLeft();
					if (decodeindex < 1)
						Log.w("Decode", "decodeindex:" + decodeindex
								+ "cost time:" + decodetimeaverage
								/ decodeindex + " " + decodeDisplay.VideoWidth
								+ " X " + decodeDisplay.VideoHeight + "FPS:"
								+ playercore.FrameRate + "left:"
								+ leftvideoframe);
					else {
						if (playercore.GetOpenLog())
							Log.w("Decode", "decodeindex:" + decodeindex
									+ "cost time:" + decodetimeaverage
									/ decodeindex + " "
									+ decodeDisplay.VideoWidth + " X "
									+ decodeDisplay.VideoHeight + "FPS:"
									+ playercore.FrameRate + "left:"
									+ leftvideoframe);
					}

					// ֱ�����߼��
					{
						if (playercore.GetPlayModel() == 0
								&& playercore.ServerType != 100)// ʵʱģʽ
						{
							if (playercore.GetOpenLog())
								Log.w("Decode", "ʵʱģʽ");
							playercore.FrameRate = (int) ((1000 * decodeindex) / (System
									.currentTimeMillis() - videoDecodeStarTime));
						} else if (playercore.GetPlayModel() == 1)// ����ģʽ
						{
							if (FrameRate == 0) {
								FrameRate = 15;
							}
							if (playercore.PtzControling) {
								if (playercore.GetOpenLog())
									Log.w("Decode", "��̨����ģʽ");
								Thread.sleep(10);
								playercore.FrameRate = (int) ((1000 * decodeindex) / (System
										.currentTimeMillis() - videoDecodeStarTime));
							} else {
								int m_dwShouldSpanMS = 0;
								if (leftvideoframe > FrameRate * 1.5) {
									FrameRate += 1;
									if (FrameRate > 25)
										FrameRate = 25;
								} else if (leftvideoframe < FrameRate * 1.5) {
									FrameRate -= 1;
									if (FrameRate < 3)
										FrameRate = 3;
								}
								m_dwShouldSpanMS = GetFrameTime(FrameRate);
								int iSpan = (int) (System.currentTimeMillis() - videoStarTime);
								int iShoud = m_dwShouldSpanMS - iSpan;

								if (FrameRate < 4)
									FrameRate = 4;
								if (iShoud <= 0)
									iShoud = 0;
								// Log.d("Decode_PlayModel", "����ģʽ,ʣ��֡:"
								// + leftvideoframe + ",ƫ��ʱ��:" + iShoud
								// + ",��̬֡�ʣ�" + FrameRate);
								Thread.sleep(iShoud);
							}
						} else if (playercore.GetPlayModel() == 2)// I֡ģʽ
						{
							if (playercore.GetOpenLog())
								Log.w("Decode", "ʡ��ģʽ");
							Thread.sleep(10);
							playercore.FrameRate = (int) ((1000 * decodeindex) / (System
									.currentTimeMillis() - videoDecodeStarTime));
						} else if (playercore.ServerType == 100
								&& playercore.ControlMp4PlaySpeed != 0) {
							int iSpan = (int) (System.currentTimeMillis() - videoStarTime);
							int times = 1000 / playercore.ControlMp4PlaySpeed
									- iSpan;
							Log.w("Decode", "¼����ʱ��" + times);
							Thread.sleep(times);

						}
					}

					onSecondFrame++;
					if (onSecondTimeMiles == 0) {
						onSecondTimeMiles = System.currentTimeMillis();
					} else {
						if (System.currentTimeMillis() - onSecondTimeMiles > 1000) {
							onSecondTimeMiles = System.currentTimeMillis();
							decodeDisplay.DisplayFrameRate = onSecondFrame;
							// Log.i("DisplayFrameRate","DisplayFrameRate:"+DisplayFrameRate);
							onSecondFrame = 0;
						}
					}
					// ���ý���
					if (!playercore.isHaveVideodata) {
						playercore.setProgress(100);
					}
					playercore.isHaveVideodata = true;// �������ȷ����Ƶ����
				} else {
					Log.w("Decode statu", "Decode fail... mFrame.iLen is:"
							+ mFrame.iLen + ",�������ͣ�" + mFrame.EncodeType);
				}
				mFrame = null;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		displayHandler.sendMessage(displayHandler.obtainMessage(0));
	}

	public int GetFrameTime(int FrameRate) {
		int iRet;
		iRet = (1000 / FrameRate);
		return iRet;
	}

	public boolean createRGBBuffer(int tmpwidht, int tmpheight) {
		if (displayHandler.pRGBBuffer == null) {
			if (RgbHaveOutOfMemory) {
				RgbOutOfMemoryIndex++;
				if (RgbOutOfMemoryIndex < 10) {
					Log.e("RgbOutOfMemoryIndex", "RgbOutOfMemoryIndex is "
							+ RgbOutOfMemoryIndex);
					return false;
				}
			}
			try {
				LastDecodeVideoWidth = myH264Decode.GetPictureWidth();
				LastDecodeVideoHeight = myH264Decode.GetPictureHeight();

				if (playercore.DisplayMode == 1)// ֻ֧��565��RGB
				{
					if (decodeDisplay.pYuvBuffer == null)
						decodeDisplay.pYuvBuffer = ByteBuffer
								.allocate((tmpwidht * tmpheight * 3) >> 1);
					displayHandler.pRGBBuffer = ByteBuffer
							.allocate((tmpwidht + 10) * tmpheight << 1);
				} else {
					if (playercore.FMT_RGB == H264DecodeInterface.FMT_RGBA32) {
						displayHandler.pRGBBuffer = ByteBuffer
								.allocate((tmpwidht + 10) * tmpheight << 2);
					} else {
						displayHandler.pRGBBuffer = ByteBuffer
								.allocate((tmpwidht + 10) * tmpheight << 1);
					}
				}
			} catch (OutOfMemoryError e) {
				Log.e("OutOfMemoryError",
						"pRGBBuffer OutOfMemoryError.........");
				RgbHaveOutOfMemory = true;
				try {
					decodeDisplay.StopPlayerCore();
					decodeDisplay.RealeaseGC();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
				return false;
			}
			RgbOutOfMemoryIndex = 0;
			RgbHaveOutOfMemory = false;
		} else if (LastDecodeVideoWidth != tmpwidht
				|| LastDecodeVideoHeight != tmpheight) {
			try {
				if (LastDecodeVideoWidth * LastDecodeVideoHeight < tmpwidht
						* tmpheight) {
					synchronized (displayHandler.pRGBBuffer) {
						displayHandler.pRGBBuffer = null;
						decodeDisplay.pYuvBuffer = null;
						System.gc();
						if (playercore.DisplayMode == 1)// ֻ֧��565��RGB
						{
							decodeDisplay.pYuvBuffer = ByteBuffer
									.allocate((tmpwidht * tmpheight * 3) >> 1);
							displayHandler.pRGBBuffer = ByteBuffer
									.allocate((tmpwidht + 10) * tmpheight << 1);
						} else {
							if (playercore.FMT_RGB == H264DecodeInterface.FMT_RGBA32) {
								displayHandler.pRGBBuffer = ByteBuffer
										.allocate((tmpwidht + 10) * tmpheight << 2);
							} else {
								displayHandler.pRGBBuffer = ByteBuffer
										.allocate((tmpwidht + 10) * tmpheight << 1);
							}
						}
					}
				}
			} catch (OutOfMemoryError e) {
				Log.e("OutOfMemoryError",
						"pRGBBuffer OutOfMemoryError.........");
				try {
					decodeDisplay.RealeaseGC();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			// *****************hyl add
			// 20141024*************************
			LastDecodeVideoWidth = tmpwidht;
			LastDecodeVideoHeight = tmpheight;
			if (myH264Decode != null)// �ߴ�ı������ý�����
			{
				synchronized (myH264Decode) {
					if (myH264Decode != null)
						myH264Decode.destroy();
					myH264Decode = null;
					System.gc();
					myH264Decode = new LysH264Decode();
					myH264Decode.initEx(lastvideoencode);
				}
			}
			return false;
			// ******************************************
		}
		return true;
	}
}
