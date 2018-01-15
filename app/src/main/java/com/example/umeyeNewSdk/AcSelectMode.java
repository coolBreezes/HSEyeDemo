package com.example.umeyeNewSdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.Player.Source.LogLisenter;
import com.Player.Source.LogOut;
import com.Player.web.response.DevItemInfo;
import com.Player.web.response.ResponseCommon;
import com.Player.web.response.ResponseDevList;
import com.Player.web.websocket.ClientCore;
import com.example.umeyesdk.AppMain;
import com.example.umeyesdk.MainActivity;
import com.example.umeyesdk.R;
import com.example.umeyesdk.api.WebSdkApi;
import com.example.umeyesdk.entity.PlayNode;
import com.example.umeyesdk.utils.Constants;
import com.example.umeyesdk.utils.Show;
import com.example.umeyesdk.utils.Utility;
import com.getui.demo.AlarmUtils;

import java.util.ArrayList;
import java.util.List;

public class AcSelectMode extends Activity {
    public ClientCore clientCore;
    AppMain appMain;
    private Context context;
    public List<PlayNode> nodeList = new ArrayList<PlayNode>();
    private EditText editServer;
    private EditText editUser;
    private EditText editPassword;
    /**
     * ��Handler������UI
     */
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.LOGIN:

                    break;

                case Constants.LOGIN_OK:
                    Show.toast(AcSelectMode.this, getString(R.string.login_succeed));
                    String[] server = clientCore.getCurrentServer();

                    Log.d("getCurrentServer", getString(R.string.auth_server) + server[1]);
                    // �����û���������
                    // Start user alarm push
                    if (!TextUtils.isEmpty(AlarmUtils.GETUI_CID)) {
                        clientCore.setUserPush(1, Utility.isZh(context) ? 2 : 1,
                                AlarmUtils.GETUI_CID, 1, 0, new Handler() {

                                    @Override
                                    public void handleMessage(Message msg) {
                                        ResponseCommon responseCommon = (ResponseCommon) msg.obj;
                                        if (responseCommon != null
                                                && responseCommon.h != null
                                                && responseCommon.h.e == 200) {
                                            Log.i("setUserPush", getString(R.string.set_push_succeed));
                                        } else {
                                            Log.i("setUserPush", getString(R.string.set_push_fail));
                                        }
                                    }
                                });
                    }

                    // clientCore.isDevListCache = true;
                    // �����豸�б�
                    WebSdkApi.getNodeList(AcSelectMode.this, clientCore, 0, 0, 0,
                            this);
                    break;
                case Constants.LOGIN_USER_OR_PWD_ERROR:
                    Show.toast(AcSelectMode.this, getString(R.string.NPC_D_MPI_MON_ERROR_USER_PWD_ERROR));
                    break;
                case Constants.GET_DEVLIST_F:
                    Show.toast(AcSelectMode.this, getString(R.string.get_dev_fail));
                    break;
                case Constants.GET_DEVLIST_S:
                    nodeList.clear();
                    ResponseDevList responseDevList = (ResponseDevList) msg.obj;
                    List<DevItemInfo> items = responseDevList.b.nodes;
                    for (int i = 0; i < items.size(); i++) {
                        DevItemInfo devItemInfo = items.get(i);
                        if (devItemInfo != null) {
                            PlayNode node = PlayNode.ChangeData(devItemInfo);
                            nodeList.add(node);
                        }
                    }
                    appMain.setNodeList(nodeList);
                    Show.toast(AcSelectMode.this, getString(R.string.get_dev_succeed));
                    startActivity(new Intent(AcSelectMode.this, MainActivity.class));
                    finish();
                    break;
                case Constants.LOGIN_FAILED:

                    Show.toast(AcSelectMode.this, getString(R.string.login_fail));
                    break;
                case Constants.LUNCH_FAILED:

                    break;
                case Constants.GET_DEVLIST_OK_NO_DATA:
                    Show.toast(context, R.string.login_succeed_no_data);
                    break;

                default:
                    break;
            }

        }
    };
    private TextView textLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        setContentView(R.layout.ac_select_mod);
        context = this;
        clientCore = ClientCore.getInstance();
        appMain = (AppMain) getApplication();
        editUser = (EditText) findViewById(R.id.et_user);
        editServer = (EditText) findViewById(R.id.et_server);
        editPassword = (EditText) findViewById(R.id.et_password);
        editUser.setText(Constants.Login_user);
        editPassword.setText(Constants.Login_password);
        editServer.setText(Constants.server);
        textLog = (TextView) findViewById(R.id.log_text);
        LogOut.logLisenter = new LogLisenter() {

            @Override
            public void OnLogLisenter(final int logColor, final String tag,
                                      final String message) {
                // TODO Auto-generated method stub
                textLog.post(new Runnable() {

                    @Override
                    public void run() {
                        SpannableStringBuilder spantag = new SpannableStringBuilder(
                                "<--" + tag + "-->" + "\n" + message + "\n");
                        // TODO Auto-generated method stub
                        spantag.setSpan(new ForegroundColorSpan(logColor), 0,
                                spantag.length(),
                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        textLog.append(spantag);

                    }
                });
            }
        };
        super.onCreate(savedInstanceState);
    }

    /**
     * �����־��Clear the log��
     *
     * @param v
     */
    public void clearLog(View v) {
        textLog.setText("");
    }

    /**
     * �޸ķ�������Modify the server��
     *
     * @param v
     */
    public void modifyServer(View v) {
        String server = editServer.getText().toString();
        if (TextUtils.isEmpty(server)) {
            return;
        }
        if (server.equals(Constants.server)) {
            return;
        }
        Constants.server = server;
        ClientCore clientCore = ClientCore.getInstance();
        clientCore.RelaseClient();
        clientCore.setCurrentBestServer(this, "", 0);
        int language = Utility.isZh(this) ? 2 : 1;
        clientCore.setupHost(this, Constants.server, 0, Utility.getImsi(this),
                language, Constants.custom_flag,
                String.valueOf(Utility.GetVersionCode(this)), "");
        clientCore.getCurrentBestServer(this, new Handler());
        SharedPreferences sp = getSharedPreferences("server",
                Context.MODE_PRIVATE);
        Editor ed = sp.edit();
        ed.putString("server", server);
        ed.commit();

    }

    /**
     * ��¼ģʽ
     *
     * @param v
     */
    public void onClick(View v) {

        // TODO Auto-generated method stub
        String userName = editUser.getText().toString();
        String password = editPassword.getText().toString();
        if (TextUtils.isEmpty(userName)) {

            Show.toast(this, R.string.enter_user_name);
            return;
        }
        if (TextUtils.isEmpty(password)) {

            Show.toast(this, R.string.enter_password);
            return;
        }
        Constants.Login_user = userName;
        Constants.Login_password = password;

        // ���õ�¼ģʽ ��Set the login mode��
        clientCore.setLocalList(false);
        WebSdkApi.loginServerAtUserId(AcSelectMode.this, clientCore,
                Constants.Login_user, Constants.Login_password, handler);

    }

    /**
     * ���½ģʽ
     *
     * @param v
     */
    public void onClick1(View v) {
        // �������½ģʽ
        clientCore.setLocalList(true);
        WebSdkApi.loginServerAtUserId(AcSelectMode.this, clientCore, "", "",
                handler);
    }

    /**
     * umidֱ��ģʽ
     *
     * @param v
     */
    public void onClick2(View v) {
        // umidֱ��ģʽ ������Ҫ�����豸�б�
        clientCore.setLocalList(true);
        WebSdkApi.loginServerAtUserId(AcSelectMode.this, clientCore, "", "",
                new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        // TODO Auto-generated method stub

                        clientCore.setUserPush(1,
                                Utility.isZh(context) ? 2 : 1,
                                AlarmUtils.GETUI_CID, 1, 0, new Handler() {

                                    @Override
                                    public void handleMessage(Message msg) {
                                        ResponseCommon responseCommon = (ResponseCommon) msg.obj;
                                        if (responseCommon != null
                                                && responseCommon.h != null
                                                && responseCommon.h.e == 200) {
                                            Log.i("setUserPush", getString(R.string.set_push_succeed));
                                        } else {
                                            Log.i("setUserPush", getString(R.string.set_push_fail));
                                        }
                                    }
                                });
                        startActivity(new Intent(AcSelectMode.this,
                                PlayActivity.class));
                        finish();
                        super.handleMessage(msg);
                    }
                });

    }

}
