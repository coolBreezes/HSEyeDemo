package com.example.umeyesdk.api;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.Player.web.response.ResponseCommon;
import com.Player.web.response.ResponseDevList;
import com.Player.web.response.ResponseGetServerList;
import com.Player.web.response.ResponseQueryAlarm;
import com.Player.web.response.ServerListInfo;
import com.Player.web.websocket.ClientCore;
import com.example.umeyesdk.utils.Constants;
import com.example.umeyesdk.utils.Show;

public class WebSdkApi {
	public static final String WebSdkApi_Error = "WebSdkApi_Error";

	/**
	 * ��½
	 * 
	 * @param pc
	 * @param userName
	 *            ����
	 * @param password
	 *            ����
	 */
	public static void loginServerAtUserId(final Context context,
			final ClientCore clientCore, String userName, String password,
			final Handler handler) {
		clientCore.loginServerAtUserId(context, userName, password,
				new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						ResponseCommon responseCommon = (ResponseCommon) msg.obj;
						if (responseCommon != null && responseCommon.h != null) {
							if (responseCommon.h.e == 200) {
								handler.sendEmptyMessage(Constants.LOGIN_OK);
							} else if (responseCommon.h.e == 406) {
								handler.sendEmptyMessage(Constants.LOGIN_USER_OR_PWD_ERROR);
							} else {
								Log.e(WebSdkApi_Error, "��¼ʧ��!code="
										+ responseCommon.h.e);
								handler.sendEmptyMessage(Constants.LOGIN_FAILED);
							}

						} else {
							Log.e(WebSdkApi_Error, "��¼ʧ��! error=" + msg.what);
							handler.sendEmptyMessage(Constants.LOGIN_FAILED);
						}
						super.handleMessage(msg);
					}

				});

	}

	/**
	 * ע���˻�
	 * 
	 * @param pc
	 * @param aUserId
	 *            �û�ID ����
	 * @param aPassword
	 *            ���� ����
	 * @param user_email
	 *            ���� ����
	 * @param nickName
	 *            �ǳ�
	 * @param user_phone
	 *            �ֻ�����
	 * @param user_id_card
	 *            �û����֤id
	 */
	public static void registeredUser(final Context context,
			final ClientCore clientCore, String aUserId, String aPassword,
			String user_email, String nickName, String user_phone,
			String user_id_card) {
		clientCore.registeredUser(aUserId, aPassword, user_email, nickName,
				user_phone, user_id_card, new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						ResponseCommon responseCommon = (ResponseCommon) msg.obj;
						if (responseCommon != null && responseCommon.h != null) {
							if (responseCommon.h.e == 200) {
								Show.toast(context, "ע��ɹ�");
							} else {
								Log.e(WebSdkApi_Error, "ע��ʧ��!code="
										+ responseCommon.h.e);
								Show.toast(context, "ע��ʧ��");
							}

						} else {
							Log.e(WebSdkApi_Error, "ע��ʧ��! error=" + msg.what);
							Show.toast(context, "ע��ʧ��");
						}
						super.handleMessage(msg);
					}

				});

	}

	/**
	 * ע��
	 * 
	 * @param pc
	 * @param disableAlarm
	 *            �Ƿ�ȡ����������
	 * @param handler
	 */
	public static void logoutServer(final Activity activity,
			final ClientCore clientCore, int disableAlarm) {
		clientCore.logoutServer(disableAlarm, new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				ResponseCommon responseCommon = (ResponseCommon) msg.obj;
				if (responseCommon != null && responseCommon.h != null) {
					if (responseCommon.h.e == 200) {
						activity.finish();
					} else {
						Log.e(WebSdkApi_Error, "ע��ʧ��!code="
								+ responseCommon.h.e);
						Show.toast(activity, "ע���˻�ʧ��");
					}

				} else {
					Log.e(WebSdkApi_Error, "ע��ʧ��! error=" + msg.what);
					Show.toast(activity, "ע���˻�ʧ��");
				}
				super.handleMessage(msg);
			}

		});

	}

	/**
	 * �޸�����
	 * 
	 * @param pc
	 * @param oldPassword
	 *            ������
	 * @param newPassword
	 *            ������
	 */
	public static void modifyUserPassword(final Context context,
			final ClientCore clientCore, String oldPassword, String newPassword) {
		clientCore.modifyUserPassword(oldPassword, newPassword, new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				ResponseCommon responseCommon = (ResponseCommon) msg.obj;
				if (responseCommon != null && responseCommon.h != null) {
					if (responseCommon.h.e == 200) {
						Show.toast(context, "�޸�����ɹ�");
					} else {
						Log.e(WebSdkApi_Error, "�޸�����ʧ��!code="
								+ responseCommon.h.e);
						Show.toast(context, "�޸�����ʧ��");
					}
				} else {
					Log.e(WebSdkApi_Error, "�޸�����ʧ��! error=" + msg.what);
					Show.toast(context, "�޸�����ʧ��");
				}
				super.handleMessage(msg);
			}

		});

	}

	/**
	 * �������������ʼ�
	 * 
	 * @param pc
	 * @param user_id
	 *            ��Ҫ����������û���
	 * @param language
	 *            ���ԣ�1��ʾӢ��(English)��2��ʾ��������(SimpChinese)�� 3��ʾ��������(TradChinese)
	 * 
	 */
	public static void resetUserPassword(final Context context,
			final ClientCore clientCore, String user_id, int language) {
		clientCore.resetUserPassword(user_id, language, new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				ResponseCommon responseCommon = (ResponseCommon) msg.obj;
				if (responseCommon != null && responseCommon.h != null) {
					if (responseCommon.h.e == 200) {
						Show.toast(context, "������������ɹ����Ժ�����գ�");
					} else {
						Log.e(WebSdkApi_Error, "������������ʧ��!code="
								+ responseCommon.h.e);
						Show.toast(context, "������������ʧ��");
					}
				} else {
					Log.e(WebSdkApi_Error, "������������ʧ��! error=" + msg.what);
					Show.toast(context, "������������ʧ��");
				}
				super.handleMessage(msg);
			}

		});

	}

	/**
	 * ��ȡ�豸�б�
	 * 
	 * @param pc
	 * @param parent_node_id
	 *            ���ڵ�ID
	 * @param page_index
	 *            ��ҳ���ܣ�ָ���ӵڼ�ҳ��ʼ���ǿ�ѡ�ģ�Ĭ�ϲ���ҳ��
	 * @param page_size
	 *            ��ҳ���ܣ�ÿҳ���صļ�¼�����ǿ�ѡ�ģ�Ĭ�ϲ���ҳ��
	 * @param handler
	 */
	public static void getNodeList(final Context context,
			final ClientCore clientCore, int parent_node_id, int page_index,
			int page_size, final Handler handler) {
		clientCore.getNodeList(parent_node_id, page_index, page_size,
				new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						ResponseDevList responseDevList = (ResponseDevList) msg.obj;
						if (responseDevList != null
								&& responseDevList.h != null) {
							if (responseDevList.h.e == 200) {
								handler.sendMessage(Message.obtain(handler,
										Constants.GET_DEVLIST_S,
										responseDevList));
							} else {
								Log.e(WebSdkApi_Error, "��ȡ�豸�б�ʧ��!code="
										+ responseDevList.h.e);
								handler.sendEmptyMessage(Constants.GET_DEVLIST_F);
							}
						} else {
							Log.e(WebSdkApi_Error, "��ȡ�豸�б�ʧ��! error="
									+ msg.what);
							handler.sendEmptyMessage(Constants.GET_DEVLIST_F);
						}
						super.handleMessage(msg);
					}

				});

	}

	/**
	 * ����豸�ڵ�
	 * 
	 * @param pc
	 * @param node_name
	 *            ����
	 * @param parent_node_id
	 *            ���ڵ�ID
	 * @param node_type
	 *            �ڵ����� : 0��ʾĿ¼�ڵ㡢1��ʾ�豸�ڵ㡢2��ʾ������ڵ㣻
	 * @param conn_mode
	 *            ��node_type��Ϊ0��ʱ���Ǳ���� ����ģʽ: 0��ʾֱ��ģʽ��1��ʾ��ý�������ģʽ��
	 *            2��ʾP2P��ģʽ��3��ʾ����ý��ģʽ��
	 * 
	 * @param vendor_id
	 * 
	 *            ����ID����node_type��Ϊ0��ʱ���Ǳ���ģ�ȡֵ��Χ��
	 *            1001��ʾ������ý��Э�飨bit����1002��ʾ������ý��Э�飨XML����
	 *            1003��ʾ������ý��Э�飨JSON����1004��ʾ������ý��Э�飨OWSP����
	 *            1005��ʾԭ������ý�������Э�飨HKSP����1006��ʾ�ļ������Э�飨FCAM����
	 *            1007��ʾ���Ƽ��ͨѶЭ�飨HMCP����1008��ʾ�����豸����Э�飨HDTP����
	 *            1009��ʾUMSP��1010��ʾEPMY��1011��ʾRTSP��
	 *            1012��ʾHTTP��1013��ʾSIP��1014��ʾRTMP��
	 *            2010��ʾ���ݺ�����2011��ʾ���ݺ�����ģʽ��2020��ʾ���ݴ󻪡�
	 *            2030��ʾ����������2040��ʾ���ڻƺӡ�2050��ʾ���ں��
	 *            2060��ʾ����������2070��ʾ��ɽ������2080��ʾ�ɶ�������
	 *            2090��ʾ�Ϻ��ά��2100��ʾ��������2110��ʾ�Ϻ�ͨ����
	 *            2120��ʾ�����ɳۡ�2130��ʾ����ͨ��2140��ʾ������á�
	 *            2150��ʾ�������ӡ�2160��ʾ���ݼѿɡ�2170��ʾ�����캲�� 2180��ʾ������ӡ�
	 * @param dev_umid
	 *            �豸umid
	 * @param dev_addr
	 *            �豸IP��������
	 * @param dev_port
	 *            �豸�˿�
	 * @param dev_user
	 *            �豸�û���
	 * @param dev_passwd
	 *            �豸����
	 * @param dev_ch_num
	 *            �豸ͨ���� dvr/nvrͨ����
	 * @param dev_ch_no
	 *            �豸ͨ���� node_typeΪ2ʱ��Ч�����dvr/nvrָ����ͨ����
	 * @param dev_stream_no
	 *            �豸�������� 0:��������1��������
	 * @param handler
	 */
	public static void addNodeInfo(final Context context,
			final ClientCore clientCore, String node_name, int parent_node_id,
			int node_type, int conn_mode, int vendor_id, String dev_umid,
			String dev_addr, int dev_port, String dev_user, String dev_passwd,
			int dev_ch_num, int dev_ch_no, int dev_stream_no,
			final Handler handler) {
		clientCore.addNodeInfo(node_name, parent_node_id, node_type, conn_mode,
				vendor_id, dev_umid, dev_addr, dev_port, dev_user, dev_passwd,
				dev_ch_num, dev_ch_no, dev_stream_no, new Handler() {

					@Override
					public void handleMessage(Message msg) {
						ResponseCommon responseCommon = (ResponseCommon) msg.obj;
						if (responseCommon != null && responseCommon.h != null) {
							if (responseCommon.h.e == 200) {
								handler.sendEmptyMessage(Constants.ADD_DEV_S);
							} else {
								Log.e(WebSdkApi_Error, "����豸ʧ��!code="
										+ responseCommon.h.e);
								handler.sendEmptyMessage(Constants.ADD_DEV_F);
							}
						} else {
							Log.e(WebSdkApi_Error, "����豸ʧ��! error=" + msg.what);
							handler.sendEmptyMessage(Constants.ADD_DEV_F);
						}
						super.handleMessage(msg);
					}

				});

	}

	/**
	 * ɾ���豸
	 * 
	 * @param pc
	 * @param node_id
	 *            �ڵ�ID
	 * @param handler
	 */
	public static void deleteNodeInfo(final Context context,
			final ClientCore clientCore, String node_id, int node_type,
			int id_type, final Handler handler) {
		clientCore.deleteNodeInfo(node_id, node_type, id_type, new Handler() {

			@Override
			public void handleMessage(Message msg) {
				ResponseCommon responseCommon = (ResponseCommon) msg.obj;
				if (responseCommon != null && responseCommon.h != null) {
					if (responseCommon.h.e == 200) {
						handler.sendEmptyMessage(Constants.DELETE_DEV_S);
					} else {
						Log.e(WebSdkApi_Error, " ɾ���豸�豸ʧ��!code="
								+ responseCommon.h.e);
						handler.sendEmptyMessage(Constants.DELETE_DEV_F);
					}
				} else {
					Log.e(WebSdkApi_Error, " ɾ���豸�豸ʧ��! error=" + msg.what);
					handler.sendEmptyMessage(Constants.DELETE_DEV_F);
				}
				super.handleMessage(msg);
			}

		});

	}

	/**
	 * �޸��豸
	 * 
	 * @param pc
	 * @param node_id
	 *            �ڵ�ID
	 * @param node_name
	 *            ����
	 * @param dev_umid
	 *            �豸umid
	 * @param dev_addr
	 *            �豸IP��ַ/����
	 * @param dev_port
	 *            �豸�˿�
	 * @param dev_user
	 *            �豸�û���
	 * @param dev_passwd
	 *            �豸����
	 * @param dev_ch_no
	 *            ������ڵ����Ч�� nvr/dvr��ͨ����
	 * @param dev_stream_no
	 *            ����
	 * @param handler
	 */
	public static void modifyNodeInfo(final Context context,
			final ClientCore clientCore, String node_id, String node_name,
			int node_type, int vendor_id, String dev_umid, String dev_addr,
			int dev_port, String dev_user, String dev_passwd, int dev_ch_no,
			int dev_stream_no, final Handler handler) {
		clientCore.modifyNodeInfo(node_id, node_name, node_type, 0, vendor_id,
				dev_umid, dev_addr, dev_port, dev_user, dev_passwd, dev_ch_no,
				dev_stream_no, new Handler() {

					@Override
					public void handleMessage(Message msg) {
						ResponseCommon responseCommon = (ResponseCommon) msg.obj;
						if (responseCommon != null && responseCommon.h != null) {
							if (responseCommon.h.e == 200) {
								handler.sendEmptyMessage(Constants.MODIFY_DEV_S);
							} else {
								Log.e(WebSdkApi_Error, " �޸��豸�豸ʧ��!code="
										+ responseCommon.h.e);
								handler.sendEmptyMessage(Constants.MODIFY_DEV_F);
							}
						} else {
							Log.e(WebSdkApi_Error, " �޸��豸�豸ʧ��! error="
									+ msg.what);
							handler.sendEmptyMessage(Constants.MODIFY_DEV_F);
						}
						super.handleMessage(msg);
					}

				});

	}

	/**
	 * ��ѯ����
	 * 
	 * @param pc
	 * @param handler
	 */
	public static void queryAlarmList(final Context context,
			final ClientCore clientCore) {
		clientCore.queryAlarmList(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				ResponseQueryAlarm responseQueryAlarm = (ResponseQueryAlarm) msg.obj;
				if (responseQueryAlarm != null && responseQueryAlarm.h != null) {
					if (responseQueryAlarm.h.e == 200) {
						Show.toast(context, "��ѯ�����ɹ�");
						if (responseQueryAlarm.b != null
								&& responseQueryAlarm.b.alarms != null) {
							for (int i = 0; i < responseQueryAlarm.b.alarms.length; i++) {
								Log.i("alarms", responseQueryAlarm.b.alarms[i]
										.toString());
							}
						} else {
							Log.e(WebSdkApi_Error, "��������");
							Show.toast(context, "��������");
						}

					} else {
						Log.e(WebSdkApi_Error, " ��ѯ����ʧ��!code="
								+ responseQueryAlarm.h.e);
						Show.toast(context, "��ѯ����ʧ��");
					}
				} else {
					Log.e(WebSdkApi_Error, " ��ѯ����ʧ��! error=" + msg.what);
					Show.toast(context, "��ѯ����ʧ��");
				}
				super.handleMessage(msg);
			}

		});

	}

	/**
	 * ���ݿͻ��˶��Ʊ�ʶ��ȡ�������б�
	 * 
	 * @param pc
	 */
	public static void getServerList(final Context context,
			final ClientCore clientCore) {
		clientCore.getServerList(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				final ResponseGetServerList responseGetServerList = (ResponseGetServerList) msg.obj;
				if (responseGetServerList != null
						&& responseGetServerList.h != null) {
					if (responseGetServerList.h.e == 200
							&& responseGetServerList.b.srvs != null) {
						ServerListInfo serverListInfos[] = responseGetServerList.b.srvs;
						for (int i = 0; i < serverListInfos.length; i++) {

							Log.d("ServerListInfo",
									"" + serverListInfos[i].toString());
						}

					} else {
						Log.e(WebSdkApi_Error, "��ȡ�������б�ʧ��! code="
								+ responseGetServerList.h.e);
					}
				} else {
					Log.e(WebSdkApi_Error, "��ȡ�������б�ʧ��! error=" + msg.what);
				}

				super.handleMessage(msg);
			}

		});
	}

	public static void modifyDevNum(ClientCore clientCore, String node_id,
			String dev_id, int dev_ch_num, final Handler handler) {
		clientCore.modifyDevNum(node_id, dev_id, dev_ch_num, new Handler() {

			@Override
			public void handleMessage(Message msg) {
				ResponseCommon responseCommon = (ResponseCommon) msg.obj;
				if (responseCommon != null && responseCommon.h != null) {
					if (responseCommon.h.e == 200) {
						handler.sendEmptyMessage(Constants.MODIFY_DEV_NUM_S);
					} else {
						Log.e(WebSdkApi_Error, " �޸��豸ͨ����ʧ��!code="
								+ responseCommon.h.e);
						handler.sendEmptyMessage(Constants.MODIFY_DEV_NUM_F);
					}
				} else {
					Log.e(WebSdkApi_Error, " �޸��豸ͨ����! error=" + msg.what);
					handler.sendEmptyMessage(Constants.MODIFY_DEV_NUM_F);
				}
				super.handleMessage(msg);
			}

		});

	}
}
