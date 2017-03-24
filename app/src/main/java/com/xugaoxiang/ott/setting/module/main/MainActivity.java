package com.xugaoxiang.ott.setting.module.main;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.FragmentActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.xugaoxiang.ott.setting.R;
import com.xugaoxiang.ott.setting.module.ethernet.WireFragment;
import com.xugaoxiang.ott.setting.ui.fragment.AudioFragment;
import com.xugaoxiang.ott.setting.ui.fragment.LanguageFragment;
import com.xugaoxiang.ott.setting.ui.fragment.NetspeedFragment;
import com.xugaoxiang.ott.setting.ui.fragment.OutputFragment;
import com.xugaoxiang.ott.setting.ui.fragment.RecoverFragment;
import com.xugaoxiang.ott.setting.ui.fragment.ServerFrgment;
import com.xugaoxiang.ott.setting.ui.fragment.StateFragment;
import com.xugaoxiang.ott.setting.ui.fragment.UpdateFragment;
import com.xugaoxiang.ott.setting.ui.fragment.UpsetFragment;
import com.xugaoxiang.ott.setting.ui.fragment.WifiFragment;
import com.xugaoxiang.ott.setting.util.systemutils.LanguageUtil;
import com.xugaoxiang.ott.setting.util.systemutils.PreferenceUtil;


public class MainActivity extends FragmentActivity implements View.OnFocusChangeListener, MainView{

    private Button btnNetworkState;
    private Button btnWireless;
    private Button btnWired;
    private Button btnSpeedTest;
    private Button btnAudio;
    private Button btnLanguage;
    private Button btnOut;
    private Button btnRecover;
    private Button btnUpdate;
    private Button btnUpdateUrl;
    private Button btnServiceUrl;
    private ButtonType currentType;

    private Context mContext;
    private Handler mHandler = new Handler();
    private MainPresenter mPresenter;
    private boolean focusable = true;

    private enum ButtonType {
        NETWORK_STATE, WIRELESS, WIRED, SPEED_TEST,OUT_PUT,
        AUDIO, LANGUAGE, RECOVER, UPDATE, UPDATE_URL, SERVICE_URL
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        PreferenceUtil.init();
        LanguageUtil languageUtil=new LanguageUtil(this);
        languageUtil.switchLanguage(PreferenceUtil.getString("language", "zh"));
        setContentView(R.layout.activity_main);
        initView();
        setListener();
        initVariable();
    }

    private void initView() {
        btnNetworkState = (Button)findViewById(R.id.btn_netstate_id);
        btnWireless = (Button) findViewById(R.id.btn_wifisetting_id);
        btnWired = (Button)findViewById(R.id.btn_wiresetting_id);
        btnAudio = (Button)findViewById(R.id.btn_audiosetting_id);
        btnLanguage = (Button)findViewById(R.id.btn_languagesetting_id);
        btnOut= (Button)findViewById(R.id.btn_outputsetting_id);
        btnRecover =(Button)findViewById(R.id.btn_recoversetting_id);
        btnSpeedTest = (Button) findViewById(R.id.btn_netspeedsetting_id);
        btnUpdate = (Button) findViewById(R.id.btn_update_id);
        btnUpdateUrl = (Button) findViewById(R.id.btn_upset_id);
        btnServiceUrl = (Button) findViewById(R.id.btn_server_id);
    }

    private void setListener() {
        btnNetworkState.setOnFocusChangeListener(this);
        btnWireless.setOnFocusChangeListener(this);
        btnWired.setOnFocusChangeListener(this);
        btnAudio.setOnFocusChangeListener(this);
        btnLanguage.setOnFocusChangeListener(this);
        btnOut.setOnFocusChangeListener(this);
        btnRecover.setOnFocusChangeListener(this);
        btnSpeedTest.setOnFocusChangeListener(this);
        btnUpdate.setOnFocusChangeListener(this);
        btnUpdateUrl.setOnFocusChangeListener(this);
        btnServiceUrl.setOnFocusChangeListener(this);
    }

    private void initVariable() {
        mContext = this;
        mPresenter = new MainPresenter(mContext, this, mHandler);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus&&focusable)
            switch (v.getId()){
                case R.id.btn_netstate_id:
                    if (currentType != ButtonType.NETWORK_STATE) {
                        currentType = ButtonType.NETWORK_STATE;

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StateFragment()).commitAllowingStateLoss();
                    }
                    break;
                case R.id.btn_wifisetting_id:
                    if (currentType != ButtonType.WIRELESS) {
                        currentType = ButtonType.WIRELESS;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WifiFragment()).commitAllowingStateLoss();
                    }
                    break;
                case R.id.btn_wiresetting_id:
                    if (currentType != ButtonType.WIRED) {
                        currentType = ButtonType.WIRED;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WireFragment(), "wirefragment").commitAllowingStateLoss();
                    }
                    break;
                case R.id.btn_netspeedsetting_id:
                    if (currentType != ButtonType.SPEED_TEST) {
                        currentType = ButtonType.SPEED_TEST;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NetspeedFragment()).commitAllowingStateLoss();
                    }
                    break;
                case R.id.btn_audiosetting_id:
                    if (currentType != ButtonType.AUDIO) {
                        currentType = ButtonType.AUDIO;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AudioFragment()).commitAllowingStateLoss();
                    }
                    break;
                case R.id.btn_languagesetting_id:
                    if (currentType != ButtonType.LANGUAGE) {
                        currentType = ButtonType.LANGUAGE;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LanguageFragment()).commitAllowingStateLoss();
                    }
                    break;
                case R.id.btn_outputsetting_id:
                    if (currentType != ButtonType.OUT_PUT) {
                        currentType = ButtonType.OUT_PUT;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OutputFragment()).commitAllowingStateLoss();
                    }
                    break;
                case R.id.btn_recoversetting_id:
                    if (currentType != ButtonType.RECOVER) {
                        currentType = ButtonType.RECOVER;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecoverFragment()).commitAllowingStateLoss();
                    }
                    break;

                case R.id.btn_update_id:
                    if (currentType != ButtonType.UPDATE) {
                        currentType = ButtonType.UPDATE;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UpdateFragment()).commitAllowingStateLoss();
                    }
                    break;
                case R.id.btn_upset_id:
                    if (currentType != ButtonType.UPDATE_URL) {
                        currentType = ButtonType.UPDATE_URL;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UpsetFragment()).commitAllowingStateLoss();
                    }
                    break;
                case R.id.btn_server_id:
                    if (currentType != ButtonType.SERVICE_URL) {
                        currentType = ButtonType.SERVICE_URL;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ServerFrgment()).commitAllowingStateLoss();
                    }
                    break;
            }
    }

    public MainPresenter getPresenter() {
        return mPresenter;
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(mContext,msg, Toast.LENGTH_LONG).show();
    }
}
