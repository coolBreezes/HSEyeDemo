package com.example.umeyesdk.utils;

public class Constants {
	/**
	 * ������
	 */
//	public static String server = "v0.api.umeye.com";// api.qvcloud.net
	public static String server = "api.qvcloud.net";// api.qvcloud.net
													// v0.api.umeye.com
													// v4.api.umeye.com
													// 121.40.110.250

	public static final int port = 8300;// �˿�
	/**
	 * �ͻ��˱�ʶ ���ɷ��������ɣ��������͹��ܣ� ���̨�󶨵��������͵�PUSH_APPID��PUSH_APPKEY��PUSH_APPSECRET��
	 */
	public static final String custom_flag = "1000000053";
	/**
	 * ��¼ģʽ Ĭ���û���
	 */
	public static String Login_user = "lin";

	/**
	 * ��¼ģʽ Ĭ������
	 */

	public static String Login_password = "123456";

	/**
	 * umidֱ��ģʽ Ĭ���豸UMID
	 */
	public static String UMID = "asksbdkmb72u";// wjks02kywj2u xmksit5p4dfu
												// umks922mj9e2
												// //ea9b6c781fb3ba2a
												// //umksguyuvxrj umksca0vafs9

	/**
	 * umidֱ��ģʽ Ĭ���豸��
	 */
	public static String user = "admin";
	/**
	 * umidֱ��ģʽ Ĭ���豸����
	 */
	public static String password = "Rabbit1298";// �豸����

	/**
	 * umidֱ��ģʽ��ǰѡ���豸ͨ����
	 */
	public static int iChNo = 0;//
	/**
	 * Ĭ�Ͻ�ͼ·��
	 */
	public static String UserImageDir = "sdcard/snapshot";
	/**
	 * Ĭ��¼��·��
	 */
	public static String UserVideoDir = "sdcard/videorecord";

	public final static int LOGIN = 1;// ��½
	public final static int LOGIN_OK = 2;// ��½�ɹ�
	public final static int LUNCH_FAILED = -1;// �����ͻ���ʧ��
	public final static int LOGIN_USER_OR_PWD_ERROR = -2;// �û�������������½ʧ��
	public final static int LOGIN_FAILED = -3;// ������½ʧ��
	public final static int MODIFY_PASSWORD_S = 3;// �޸�����ɹ�
	public final static int MODIFY_PASSWORD_F = -4;// �޸�����ʧ��
	public final static int REGIST_S = 5;// ע��ɹ�
	public final static int REGIST_F = -5;// ע��ʧ��
	public final static int LOGOUT_S = 6;// ע���ɹ�
	public final static int LOGOUT_F = -6;// ע��ʧ��
	public final static int GET_DEVLIST_F = 0;// ��ȡ�б�ʧ��
	public final static int GET_DEVLIST_S = 7;// ��ȡ�б�ɹ�
	public final static int GET_DEVLIST_OK_NO_DATA = 8;// ��ȡ�б�ɹ�,û�豸����
	public final static int RESET_PASSWORD_S = 9;// ������������ɹ�
	public final static int RESET_PASSWORD_F = -9;// ������������ʧ��
	public final static int ADD_DEV_S = 10;// ����豸�ɹ�
	public final static int ADD_DEV_F = -10;// ����豸ʧ��
	public final static int DELETE_DEV_S = 11;// ɾ���豸�ɹ�
	public final static int DELETE_DEV_F = -11;// ɾ���豸ʧ��
	public final static int MODIFY_DEV_S = 12;// �޸��豸�ɹ�
	public final static int MODIFY_DEV_F = -12;// �޸��豸ʧ��
	public final static int QUERY_ALARM_S = 13;// ��ѯ�����ɹ�
	public final static int QUERY_ALARM_F = -13;// ��ѯ����ʧ��
	public final static int MODIFY_DEV_NUM_S = 14;// �޸��豸�ɹ�
	public final static int MODIFY_DEV_NUM_F = -14;// �޸��豸ʧ��
	// public static final byte NEW_DATA = 0x11;
	public static final byte DELETE_FAILED = 0x12;
	public static final byte DELETE_SUCCEED = 0x13;
	public static final byte ADD_FAILED = 0x14;
	public static final byte ADD_SUCCEED = 0x15;
	public static final byte SET_ALARM_S = 0x16;
	public static final byte SET_ALARM_F = 0x17;
	public static final byte CANCEL_ALARM_S = 0x18;
	public static final byte CANCEL_ALARM_F = 0x19;
}
