package com.example.umeyesdk.entity;

public class DevIpInfoRet {
	/**
	 * NPC_D_DPS_JSON_FUNCID_DEV_IP=0x03 �豸������������
	 */
	public int Operation;
	/**
	 * �������ͣ�0:APP���豸��ȡ��Ϣ��1��APP��������Ϣ�����豸������ (�������ͣ�int)
	 */
	public int Request_Type;
	/**
	 * ִ�н����-1����֧�ָĹ��ܣ�0��ʧ�ܣ�1���ɹ� (�������ͣ�int)
	 */
	public int Result;
	/**
	 * ��������JSON����
	 */
	public DevIpInfo Value;

	public DevIpInfoRet() {
		super();
	}

	@Override
	public String toString() {
		return "DevCodeInfoRet [Operation=" + Operation + ", Request_Type="
				+ Request_Type + ", Result=" + Result + ", Value=" + Value
				+ "]";
	}

}
