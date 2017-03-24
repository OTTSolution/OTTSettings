package com.xugaoxiang.ott.setting.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.xugaoxiang.ott.setting.R;
import com.xugaoxiang.ott.setting.ui.adapters.ScanResultsAdapter;
import com.xugaoxiang.ott.setting.util.MessageWindow;
import com.xugaoxiang.ott.setting.util.WifiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 无线设置
 */
public class WifiFragment extends Fragment {

    private Context mContext;
    private Switch wifiSwitch;
    private ListView listView;
    private ScanResultsAdapter adapter;

    private List<ScanResult> mScanResult;
    private boolean isWifiOn;
    private WifiUtils wifiUtils;

    private LayoutInflater layoutInflater;
    private View dialogView;
    private EditText ed;
    private CheckBox checkBox;
    private String isShow;

    private View viewBreak;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    List<ScanResult> scanResult = wifiUtils.getScanResult();
                    if (isWifiOn && scanResult.size() < 1)
                        handler.sendEmptyMessageDelayed(1, 1000);
                    else if (isWifiOn)
                        showWifiList(scanResult);
                    else if (!isWifiOn)
                        handler.removeMessages(1);
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container,false);
        wifiSwitch = (Switch) view.findViewById(R.id.wifi_switch);
        listView = (ListView) view.findViewById(R.id.listview);
        wifiUtils = WifiUtils.getInstance(mContext);
        isWifiOn = wifiUtils.isWifiOpened();
        layoutInflater=LayoutInflater.from(mContext);
//        dialogView=layoutInflater.inflate(R.layout.wifi_dialog,null);
//        ed= (EditText) dialogView.findViewById(R.id.password);
//        checkBox= (CheckBox) dialogView.findViewById(R.id.show_password);
        initListView();
        initSwitch();
        return view;
    }

    private void initListView() {
        mScanResult = new ArrayList<>();
        adapter = new ScanResultsAdapter(mContext, mScanResult, wifiUtils);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ScanResult clickItem = mScanResult.get(position);
                final String clickedSSID = ("\"" + clickItem.SSID + "\"");
                final WifiInfo currentWifiInfo = wifiUtils.getCurrentWifiInfo();//获得当前连接的wifi信息
                String currentSSID = currentWifiInfo.getSSID();//这里加引号了
                String pwdType = clickItem.capabilities;
                if (pwdType.contains("WPA") || pwdType.contains("wpa")
                        ||pwdType.contains("WEP") ||pwdType.contains("wep")) {
                    if (TextUtils.equals(clickedSSID, currentSSID)) {
                        showCurrentWifiDialog(clickItem, currentWifiInfo);
                    } else {
                        List<WifiConfiguration> configuredNetworks = wifiUtils.getConfiguredNetworks();
                        for (int i = 0; i < configuredNetworks.size(); i++) {
                            final WifiConfiguration wifiConfig = configuredNetworks.get(i);
                            if (TextUtils.equals(clickedSSID, wifiConfig.SSID)) {//这里加引号了 wifiConfig.SSID
                                showConfiguredWifiDialog(clickItem, wifiConfig);
                                return;
                            }
                        }
                        showAddWifiDialog(clickItem);
                    }
                }
                else {
                    if (TextUtils.equals(clickedSSID, currentSSID)) {
                        showCurrentWifiDialog(clickItem, currentWifiInfo);
                    } else {
                        List<WifiConfiguration> configuredNetworks = wifiUtils.getConfiguredNetworks();
                        for (int i = 0; i < configuredNetworks.size(); i++) {
                            final WifiConfiguration wifiConfig = configuredNetworks.get(i);
                            if (TextUtils.equals(clickedSSID, wifiConfig.SSID)) {//这里加引号了 wifiConfig.SSID
                                showConfiguredWifiDialog(clickItem, wifiConfig);
                                return;
                            }
                        }
                        showNoPasswordDialog(clickItem);
                    }

                    //showNoPasswordDialog(clickItem);
                }
            }
        });
    }
    //连接过的wifidialog
    private void showConfiguredWifiDialog(ScanResult clickItem, final WifiConfiguration wifiConfig) {
        viewBreak = layoutInflater.inflate(R.layout.wifi_dialog_all,null);
        TextView text = (TextView) viewBreak.findViewById(R.id.wifi_dialog_all_tv);
        text.setWidth(MessageWindow.getWid(mContext)/3);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.wifi_dialog);
        builder.setTitle(clickItem.SSID)
                .setView(viewBreak)
                .setPositiveButton(R.string.wifi_connect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        wifiUtils.connectNetworkById(wifiUtils.getCurrentNetworkId(), wifiConfig.networkId);
                        adapter.notifyDataSetChanged();
                        new Thread() {
                            @Override
                            public void run() {
                                String strIp;
                                do {
                                    strIp = wifiUtils.getCurrentNetworkIP();
                                }
                                while (TextUtils.isEmpty(strIp) || TextUtils.equals(strIp, "0.0.0.0"));
                                listView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }.start();

                    }
                })
                .setNegativeButton(R.string.wifi_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton(R.string.wifi_forget, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wifiUtils.forgetNetwork(wifiConfig.networkId);
                        dialog.dismiss();
                        upInfo();
                    }
                }).show();
    }

    //断开的dialog
    private void showCurrentWifiDialog(ScanResult clickItem, final WifiInfo currentWifiInfo) {
        viewBreak = layoutInflater.inflate(R.layout.wifi_dialog_all,null);
        TextView text = (TextView) viewBreak.findViewById(R.id.wifi_dialog_all_tv);
        text.setWidth(MessageWindow.getWid(mContext)/3);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.wifi_dialog);
        builder.setTitle(clickItem.SSID)
                .setView(viewBreak)
                .setPositiveButton(R.string.wifi_disconnect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wifiUtils.disconnectNetwork(currentWifiInfo.getNetworkId());
                        dialog.dismiss();
                        // adapter.notifyDataSetChanged();
                        upInfo();
                    }
                })
                .setNegativeButton(R.string.wifi_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton(R.string.wifi_forget, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wifiUtils.forgetNetwork(currentWifiInfo.getNetworkId());
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                }).show();
    }

    /**
     * 添加一个陌生的wifi
     * @param clickItem
     */
    private void showAddWifiDialog(final ScanResult clickItem) {

        dialogView=layoutInflater.inflate(R.layout.wifi_dialog,null);
        final EditText edd= (EditText) dialogView.findViewById(R.id.password);
        CheckBox  check= (CheckBox) dialogView.findViewById(R.id.show_password);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    isShow = "true";
                    edd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else {
                    isShow = "false";
                    edd.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
//        final EditText etPassword = new EditText(mContext);
//        etPassword.setPadding(40, 0, 40, 0);
//        //etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        etPassword.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.wifi_dialog);
        builder.setTitle(clickItem.SSID)
                .setInverseBackgroundForced(false)
                .setMessage(R.string.wifi_password)
                .setView(dialogView)
                .setPositiveButton(R.string.wifi_connect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String password = edd.getText().toString();
                        if (password.length() < 8)
                            return;
                        String capabilities = clickItem.capabilities;
                        if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
                            WifiConfiguration wifiInfo = wifiUtils.createWifiInfo(clickItem.SSID, password, 3);
                            wifiUtils.addNetwork(wifiInfo);
                            // wifiUtils.addNet(wifiInfo,mContext);
                            upInfo();
                        } else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
                            WifiConfiguration wifiInfo = wifiUtils.createWifiInfo(clickItem.SSID, password, 2);
                            wifiUtils.addNetwork(wifiInfo);
                            upInfo();
                        }
                    }
                })
                .setNegativeButton(R.string.wifi_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 没有密码直接进入的wifi
     * @param clickItem
     */
    private void showNoPasswordDialog(final ScanResult clickItem) {
        viewBreak = layoutInflater.inflate(R.layout.wifi_dialog_all,null);
        TextView text = (TextView) viewBreak.findViewById(R.id.wifi_dialog_all_tv);
        text.setWidth(MessageWindow.getWid(mContext)/3);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.wifi_dialog);
        builder.setTitle(clickItem.SSID)
                .setView(viewBreak)
                .setPositiveButton(R.string.wifi_connect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WifiConfiguration wifiInfo = wifiUtils.createWifiInfo(clickItem.SSID, "", 1);
                        wifiUtils.addNetwork(wifiInfo);
                        upInfo();
                    }
                })
                .setNegativeButton(R.string.wifi_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        upInfo();
                    }
                }).show();
    }

    private void upInfo(){
        adapter.notifyDataSetChanged();
        new Thread(){
            @Override
            public void run() {
                String strIp;
                do {
                    strIp = wifiUtils.getCurrentNetworkIP();
                }
                while (TextUtils.isEmpty(strIp) || TextUtils.equals(strIp, "0.0.0.0"));
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }

    private void initSwitch() {
        if (isWifiOn)
        {
            wifiSwitch.setChecked(true);
            showWifiList(wifiUtils.getScanResult());
        }
        else
            wifiSwitch.setChecked(false);
        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    isWifiOn = true;
                    wifiUtils.openWifi();
                    handler.sendEmptyMessageDelayed(1, 2000);
                }
                else
                {
                    isWifiOn = false;
                    handler.removeMessages(1);
                    wifiUtils.closeWifi();
                    showWifiList(null);
                }
            }
        });
    }

    private void showWifiList(List<ScanResult> scanResultList) {
        List<ScanResult> filterList = filterScanResult(scanResultList);
        mScanResult.clear();
        if (filterList != null)
            mScanResult.addAll(filterList);
        adapter.notifyDataSetChanged();
        upInfo();
    }

    public List<ScanResult> filterScanResult(List<ScanResult> scanResultList) {
        List<ScanResult> filterList = new ArrayList<>();
        if (scanResultList != null && scanResultList.size() >0) {
            //按照信号强度排序
            Collections.sort(scanResultList, new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult lhs, ScanResult rhs) {
                    if (lhs.level <= rhs.level)
                        return 1;
                    else
                        return -1;
                }
            });
            for(ScanResult result : scanResultList){
                if (TextUtils.isEmpty(result.SSID) || result.capabilities.contains("[IBSS]"))
                    continue;
                boolean isSame = false;
                for(ScanResult item : filterList){
                    if(TextUtils.equals(item.SSID, result.SSID) && TextUtils.equals(item.capabilities, result.capabilities)){
                        isSame = true;
                        break;
                    }
                }
                if(!isSame)
                    filterList.add(result);
            }
        }
        return filterList;
    }

}
