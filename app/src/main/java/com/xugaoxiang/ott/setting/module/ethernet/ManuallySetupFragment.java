package com.xugaoxiang.ott.setting.module.ethernet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xugaoxiang.ott.setting.R;
import com.xugaoxiang.ott.setting.module.main.MainActivity;
import com.xugaoxiang.ott.setting.util.StringUtils;
import com.xugaoxiang.ott.setting.util.systemutils.PreferenceUtil;

/**
 * 有线网络手动设置界面
 */
public class ManuallySetupFragment extends Fragment implements View.OnClickListener, View.OnKeyListener {

    private Context mContext;
    private EditText etIpaddress1;
    private EditText etIpaddress2;
    private EditText etIpaddress3;
    private EditText etIpaddress4;
    private EditText etNetmask1;
    private EditText etNetmask2;
    private EditText etNetmask3;
    private EditText etNetmask4;
    private EditText etGatewawy1;
    private EditText etGatewawy2;
    private EditText etGatewawy3;
    private EditText etGatewawy4;
    private EditText etDns1;
    private EditText etDns2;
    private EditText etDns3;
    private EditText etDns4;
    private EditText etDns21;
    private EditText etDns22;
    private EditText etDns23;
    private EditText etDns24;
    private Button btnBack;
    private Button btnFinish;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_manually_setup, container, false);
        initView(contentView);
        initListener();
        initDate();
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        etIpaddress1.setFocusable(true);
        etIpaddress1.setFocusableInTouchMode(true);
        etIpaddress1.requestFocus();
    }

    private void initView(View contentView) {
        etIpaddress1 = (EditText) contentView.findViewById(R.id.et_ipaddress_1);
        etIpaddress2 = (EditText) contentView.findViewById(R.id.et_ipaddress_2);
        etIpaddress3 = (EditText) contentView.findViewById(R.id.et_ipaddress_3);
        etIpaddress4 = (EditText) contentView.findViewById(R.id.et_ipaddress_4);
        etNetmask1 = (EditText) contentView.findViewById(R.id.et_netmask_1);
        etNetmask2 = (EditText) contentView.findViewById(R.id.et_netmask_2);
        etNetmask3 = (EditText) contentView.findViewById(R.id.et_netmask_3);
        etNetmask4 = (EditText) contentView.findViewById(R.id.et_netmask_4);
        etGatewawy1 = (EditText) contentView.findViewById(R.id.et_gateway_1);
        etGatewawy2 = (EditText) contentView.findViewById(R.id.et_gateway_2);
        etGatewawy3 = (EditText) contentView.findViewById(R.id.et_gateway_3);
        etGatewawy4 = (EditText) contentView.findViewById(R.id.et_gateway_4);
        etDns1 = (EditText) contentView.findViewById(R.id.et_dns_1);
        etDns2 = (EditText) contentView.findViewById(R.id.et_dns_2);
        etDns3 = (EditText) contentView.findViewById(R.id.et_dns_3);
        etDns4 = (EditText) contentView.findViewById(R.id.et_dns_4);
        etDns21 = (EditText) contentView.findViewById(R.id.et_dns2_1);
        etDns22 = (EditText) contentView.findViewById(R.id.et_dns2_2);
        etDns23 = (EditText) contentView.findViewById(R.id.et_dns2_3);
        etDns24 = (EditText) contentView.findViewById(R.id.et_dns2_4);
        btnBack = (Button) contentView.findViewById(R.id.btn_back);
        btnFinish = (Button) contentView.findViewById(R.id.btn_finish);
    }

    private void initListener() {
        etIpaddress1.setOnKeyListener(this);
        etIpaddress2.setOnKeyListener(this);
        etIpaddress3.setOnKeyListener(this);
        etIpaddress4.setOnKeyListener(this);
        etNetmask1.setOnKeyListener(this);
        etNetmask2.setOnKeyListener(this);
        etNetmask3.setOnKeyListener(this);
        etNetmask4.setOnKeyListener(this);
        etGatewawy1.setOnKeyListener(this);
        etGatewawy2.setOnKeyListener(this);
        etGatewawy3.setOnKeyListener(this);
        etGatewawy4.setOnKeyListener(this);
        etDns1.setOnKeyListener(this);
        etDns2.setOnKeyListener(this);
        etDns3.setOnKeyListener(this);
        etDns4.setOnKeyListener(this);
        etDns21.setOnKeyListener(this);
        etDns22.setOnKeyListener(this);
        etDns23.setOnKeyListener(this);
        etDns24.setOnKeyListener(this);
        btnBack.setOnKeyListener(this);
        btnFinish.setOnKeyListener(this);
        btnBack.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
    }

    private void initDate() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                backToWireFragment();
                break;
            case R.id.btn_finish:
                connectToNetwork();
                break;
        }
    }

    private void backToWireFragment() {
        WireFragment fragment = new WireFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFocus", true);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "wirefragment").commit();
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (view == null) {

        //    Toast.makeText(mContext, "手动设置返回键", Toast.LENGTH_LONG).show();
            return false;
        }
        boolean handled = false;
        switch (keyCode) {
            case KeyEvent.KEYCODE_POUND:
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if(view instanceof EditText){
                        Integer index = ((EditText)view).getSelectionStart();
                        Editable editable = ((EditText)view).getText();
                        if(index > 0) editable.delete(index-1,index);
                        handled=true;
                    }
                }
                break;
            case KeyEvent.KEYCODE_BACK:
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN ){
                    backToWireFragment();
                    handled=true;
                }
                break;
        }
        return handled;
    }

    private void connectToNetwork() {
        String ip = etIpaddress1.getText().toString() + "." + etIpaddress2.getText().toString() + "." + etIpaddress3.getText().toString() + "." + etIpaddress4.getText().toString();
        String netmask = etNetmask1.getText().toString() + "." + etNetmask2.getText().toString() + "." + etNetmask3.getText().toString() + "." + etNetmask4.getText().toString();
        String gateway = etGatewawy1.getText().toString() + "." + etGatewawy2.getText().toString() + "." + etGatewawy3.getText().toString() + "." + etGatewawy4.getText().toString();
        String dns1 = etDns1.getText().toString() + "." + etDns2.getText().toString() + "." + etDns3.getText().toString() + "." + etDns4.getText().toString();
        String dns2 = etDns21.getText().toString() + "." + etDns22.getText().toString() + "." + etDns23.getText().toString() + "." + etDns24.getText().toString();
        if(!StringUtils.isIpAddress(ip)){
            Toast.makeText(mContext, R.string.wire_ip_error, Toast.LENGTH_LONG).show();
            return;
        }
        if(!StringUtils.isIpAddress(netmask)){
            Toast.makeText(mContext, R.string.wire_mask_error, Toast.LENGTH_LONG).show();
            return;
        }
        if(!StringUtils.isIpAddress(gateway)){
            Toast.makeText(mContext, R.string.wire_gateway_error, Toast.LENGTH_LONG).show();
            return;
        }
        if(!StringUtils.isIpAddress(dns1)){
            Toast.makeText(mContext, R.string.wire_dns1_error, Toast.LENGTH_LONG).show();
            return;
        }
        if(!StringUtils.isIpAddress(dns2)){
            Toast.makeText(mContext, R.string.wire_dns2_error, Toast.LENGTH_LONG).show();
            return;
        }
        PreferenceUtil.init();
        PreferenceUtil.putString("ip", ip);
        PreferenceUtil.putString("netmask", netmask);
        PreferenceUtil.putString("gateway", gateway);
        PreferenceUtil.putString("dns1", dns1);
        PreferenceUtil.putString("dns2", dns2);
        MainActivity activity = (MainActivity) getActivity();
        activity.getPresenter().manuallyConnect();
    }

}
