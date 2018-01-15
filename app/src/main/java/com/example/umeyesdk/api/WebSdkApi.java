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
	 * 登陆
	 * 
	 * @param pc
	 * @param userName
	 *            必填
	 * @param password
	 *            必填
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
								Log.e(WebSdkApi_Error, "登录失败!code="
										+ responseCommon.h.e);
								handler.sendEmptyMessage(Constants.LOGIN_FAILED);
							}

						} else {
							Log.e(WebSdkApi_Error, "登录失败! error=" + msg.what);
							handler.sendEmptyMessage(Constants.LOGIN_FAILED);
						}
						super.handleMessage(msg);
					}

				});

	}

	/**
	 * 注册账户
	 * 
	 * @param pc
	 * @param aUserId
	 *            用户ID 必填
	 * @param aPassword
	 *            密码 必填
	 * @param user_email
	 *            邮箱 必填
	 * @param nickName
	 *            昵称
	 * @param user_phone
	 *            手机号码
	 * @param user_id_card
	 *            用户身份证id
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
								Show.toast(context, "注册成功");
							} else {
								Log.e(WebSdkApi_Error, "注册失败!code="
										+ responseCommon.h.e);
								Show.toast(context, "注册失败");
							}

						} else {
							Log.e(WebSdkApi_Error, "注册失败! error=" + msg.what);
							Show.toast(context, "注册失败");
						}
						super.handleMessage(msg);
					}

				});

	}

	/**
	 * 注销
	 * 
	 * @param pc
	 * @param disableAlarm
	 *            是否取消报警推送
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
						Log.e(WebSdkApi_Error, "注销失败!code="
								+ responseCommon.h.e);
						Show.toast(activity, "注销账户失败");
					}

				} else {
					Log.e(WebSdkApi_Error, "注销失败! error=" + msg.what);
					Show.toast(activity, "注销账户失败");
				}
				super.handleMessage(msg);
			}

		});

	}

	/**
	 * 修改密码
	 * 
	 * @param pc
	 * @param oldPassword
	 *            旧密码
	 * @param newPassword
	 *            新密码
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
						Show.toast(context, "修改密码成功");
					} else {
						Log.e(WebSdkApi_Error, "修改密码失败!code="
								+ responseCommon.h.e);
						Show.toast(context, "修改密码失败");
					}
				} else {
					Log.e(WebSdkApi_Error, "修改密码失败! error=" + msg.what);
					Show.toast(context, "修改密码失败");
				}
				super.handleMessage(msg);
			}

		});

	}

	/**
	 * 发送重置密码邮件
	 * 
	 * @param pc
	 * @param user_id
	 *            需要重置密码的用户名
	 * @param language
	 *            语言：1表示英文(English)，2表示简体中文(SimpChinese)， 3表示繁体中文(TradChinese)
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
						Show.toast(context, "发送重置密码成功，稍后请查收！");
					} else {
						Log.e(WebSdkApi_Error, "发送重置密码失败!code="
								+ responseCommon.h.e);
						Show.toast(context, "发送重置密码失败");
					}
				} else {
					Log.e(WebSdkApi_Error, "发送重置密码失败! error=" + msg.what);
					Show.toast(context, "发送重置密码失败");
				}
				super.handleMessage(msg);
			}

		});

	}

	/**
	 * 获取设备列表
	 * 
	 * @param pc
	 * @param parent_node_id
	 *            父节点ID
	 * @param page_index
	 *            分页功能，指定从第几页开始，是可选的，默认不分页；
	 * @param page_size
	 *            分页功能，每页返回的记录数，是可选的，默认不分页；
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
								Log.e(WebSdkApi_Error, "获取设备列表失败!code="
										+ responseDevList.h.e);
								handler.sendEmptyMessage(Constants.GET_DEVLIST_F);
							}
						} else {
							Log.e(WebSdkApi_Error, "获取设备列表失败! error="
									+ msg.what);
							handler.sendEmptyMessage(Constants.GET_DEVLIST_F);
						}
						super.handleMessage(msg);
					}

				});

	}

	/**
	 * 添加设备节点
	 * 
	 * @param pc
	 * @param node_name
	 *            名称
	 * @param parent_node_id
	 *            父节点ID
	 * @param node_type
	 *            节点类型 : 0表示目录节点、1表示设备节点、2表示摄像机节点；
	 * @param conn_mode
	 *            当node_type不为0的时候是必须的 连接模式: 0表示直连模式、1表示流媒体服务器模式、
	 *            2表示P2P云模式、3表示云流媒体模式；
	 * 
	 * @param vendor_id
	 * 
	 *            厂商ID，当node_type不为0的时候是必须的，取值范围：
	 *            1001表示华科流媒体协议（bit）、1002表示华科流媒体协议（XML）、
	 *            1003表示华科流媒体协议（JSON）、1004表示华科流媒体协议（OWSP）、
	 *            1005表示原华科流媒体服务器协议（HKSP）、1006表示文件摄像机协议（FCAM）、
	 *            1007表示华科监控通讯协议（HMCP）、1008表示华科设备上连协议（HDTP）、
	 *            1009表示UMSP、1010表示EPMY、1011表示RTSP、
	 *            1012表示HTTP、1013表示SIP、1014表示RTMP、
	 *            2010表示杭州海康、2011表示杭州海康推模式、2020表示杭州大华、
	 *            2030表示深圳锐明、2040表示深圳黄河、2050表示深圳汉邦、
	 *            2060表示杭州雄迈、2070表示中山立堡、2080表示成都索贝、
	 *            2090表示上海皓维、2100表示金三立、2110表示上海通立、
	 *            2120表示深圳郎驰、2130表示网视通、2140表示广州奇幻、
	 *            2150表示安联锐视、2160表示广州佳可、2170表示深圳旗翰、 2180表示瀚晖威视。
	 * @param dev_umid
	 *            设备umid
	 * @param dev_addr
	 *            设备IP或者域名
	 * @param dev_port
	 *            设备端口
	 * @param dev_user
	 *            设备用户名
	 * @param dev_passwd
	 *            设备密码
	 * @param dev_ch_num
	 *            设备通道数 dvr/nvr通道数
	 * @param dev_ch_no
	 *            设备通道号 node_type为2时有效，添加dvr/nvr指定的通道号
	 * @param dev_stream_no
	 *            设备请求码流 0:主码流，1，子码流
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
								Log.e(WebSdkApi_Error, "添加设备失败!code="
										+ responseCommon.h.e);
								handler.sendEmptyMessage(Constants.ADD_DEV_F);
							}
						} else {
							Log.e(WebSdkApi_Error, "添加设备失败! error=" + msg.what);
							handler.sendEmptyMessage(Constants.ADD_DEV_F);
						}
						super.handleMessage(msg);
					}

				});

	}

	/**
	 * 删除设备
	 * 
	 * @param pc
	 * @param node_id
	 *            节点ID
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
						Log.e(WebSdkApi_Error, " 删除设备设备失败!code="
								+ responseCommon.h.e);
						handler.sendEmptyMessage(Constants.DELETE_DEV_F);
					}
				} else {
					Log.e(WebSdkApi_Error, " 删除设备设备失败! error=" + msg.what);
					handler.sendEmptyMessage(Constants.DELETE_DEV_F);
				}
				super.handleMessage(msg);
			}

		});

	}

	/**
	 * 修改设备
	 * 
	 * @param pc
	 * @param node_id
	 *            节点ID
	 * @param node_name
	 *            名称
	 * @param dev_umid
	 *            设备umid
	 * @param dev_addr
	 *            设备IP地址/域名
	 * @param dev_port
	 *            设备端口
	 * @param dev_user
	 *            设备用户名
	 * @param dev_passwd
	 *            设备密码
	 * @param dev_ch_no
	 *            摄像机节点才有效， nvr/dvr的通道号
	 * @param dev_stream_no
	 *            码流
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
								Log.e(WebSdkApi_Error, " 修改设备设备失败!code="
										+ responseCommon.h.e);
								handler.sendEmptyMessage(Constants.MODIFY_DEV_F);
							}
						} else {
							Log.e(WebSdkApi_Error, " 修改设备设备失败! error="
									+ msg.what);
							handler.sendEmptyMessage(Constants.MODIFY_DEV_F);
						}
						super.handleMessage(msg);
					}

				});

	}

	/**
	 * 查询报警
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
						Show.toast(context, "查询报警成功");
						if (responseQueryAlarm.b != null
								&& responseQueryAlarm.b.alarms != null) {
							for (int i = 0; i < responseQueryAlarm.b.alarms.length; i++) {
								Log.i("alarms", responseQueryAlarm.b.alarms[i]
										.toString());
							}
						} else {
							Log.e(WebSdkApi_Error, "其它错误");
							Show.toast(context, "其它错误");
						}

					} else {
						Log.e(WebSdkApi_Error, " 查询报警失败!code="
								+ responseQueryAlarm.h.e);
						Show.toast(context, "查询报警失败");
					}
				} else {
					Log.e(WebSdkApi_Error, " 查询报警失败! error=" + msg.what);
					Show.toast(context, "查询报警失败");
				}
				super.handleMessage(msg);
			}

		});

	}

	/**
	 * 根据客户端定制标识获取服务器列表
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
						Log.e(WebSdkApi_Error, "获取服务器列表失败! code="
								+ responseGetServerList.h.e);
					}
				} else {
					Log.e(WebSdkApi_Error, "获取服务器列表失败! error=" + msg.what);
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
						Log.e(WebSdkApi_Error, " 修改设备通道数失败!code="
								+ responseCommon.h.e);
						handler.sendEmptyMessage(Constants.MODIFY_DEV_NUM_F);
					}
				} else {
					Log.e(WebSdkApi_Error, " 修改设备通道数! error=" + msg.what);
					handler.sendEmptyMessage(Constants.MODIFY_DEV_NUM_F);
				}
				super.handleMessage(msg);
			}

		});

	}
}
