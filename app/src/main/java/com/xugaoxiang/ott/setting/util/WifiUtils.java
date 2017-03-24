package com.xugaoxiang.ott.setting.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * Created by zero on 2016/10/21.
 */

public class WifiUtils {

    private static  WifiUtils wifiUtils = null;
    public WifiManager wifiManager;

    public static WifiUtils getInstance(Context context) {
        if(wifiUtils == null) {
            wifiUtils = new WifiUtils(context);
        }
        return wifiUtils;
    }
    private WifiUtils(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public void openWifi() {
        if(!wifiManager.isWifiEnabled()){ //当前wifi不可用
            wifiManager.setWifiEnabled(true);
        }
    }

    public void closeWifi() {
        if(wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public boolean scanWifi() {
        return wifiManager.startScan();
    }

    public void disconnectNetwork(int networkId) {
        wifiManager.disableNetwork(networkId);
        boolean isSuccess = wifiManager.disconnect();
        Log.i("mumu", "断开网络" + isSuccess);
    }

    public void forgetNetwork(int networkId) {
        wifiManager.disableNetwork(networkId);
        wifiManager.removeNetwork(networkId);
        wifiManager.saveConfiguration();
    }

    /**添加指定网络**/
    public void addNetwork(WifiConfiguration wifiConfig) {
        wifiManager.disconnect();
        wifiManager.disableNetwork(getCurrentNetworkId());
        Log.i("mumu", "之前的netId：" + wifiConfig.networkId);
        int wifiId = wifiManager.addNetwork(wifiConfig);
        Log.i("mumu", "addNetwork之后的netId：" + wifiId);
        boolean is = wifiManager.enableNetwork(wifiId, true);
        Log.i("mumu", "enable" + is);
        boolean isSuccess = wifiManager.saveConfiguration();
        Log.i("mumu", "保存" + isSuccess);
    }

    public void addNet(WifiConfiguration wifiConfig,Context context) {
        wifiManager.disconnect();
        wifiManager.disableNetwork(getCurrentNetworkId());
        Log.i("mumu", "之前的netId：" + wifiConfig.networkId);
        int wifiId = wifiManager.addNetwork(wifiConfig);
        Log.i("mumu", "addNetwork之后的netId：" + wifiId);
        boolean is = wifiManager.enableNetwork(wifiId, true);
       // Toast.makeText(context,"wifi连接结果"+wifiId+"",Toast.LENGTH_SHORT).show();
        Log.i("mumu", "enable" + is);
        boolean isSuccess = wifiManager.saveConfiguration();
        Log.i("mumu", "保存" + isSuccess);
    }

    public void connectNetworkById(int oldId, int newId) {
        wifiManager.disconnect();
        wifiManager.disableNetwork(oldId);
        wifiManager.enableNetwork(newId, true);
    }


    /**
     * 创建一个wifi信息
     * @param ssid 名称
     * @param password 密码
     * @param passwordType 有3个参数，1是无密码，2是简单密码，3是wap加密
     * @return
     */
    public WifiConfiguration
    createWifiInfo(String ssid, String password, int passwordType) {
        //配置网络信息类
        WifiConfiguration wifiConfig = new WifiConfiguration();
        //设置配置网络属性
        wifiConfig.allowedAuthAlgorithms.clear();
        wifiConfig.allowedGroupCiphers.clear();
        wifiConfig.allowedKeyManagement.clear();
        wifiConfig.allowedPairwiseCiphers.clear();
        wifiConfig.allowedProtocols.clear();

        if (!ssid.startsWith("\""))
            ssid = "\"" + ssid + "\"";
        wifiConfig.SSID = ssid;
        WifiConfiguration localConfig = isExsits(ssid);
        if(localConfig != null)
            wifiManager.removeNetwork(localConfig.networkId);
        if(passwordType == 1)//没有密码
        {
            wifiConfig.wepKeys[0] = "\""+ "\"";
            //wifiConfig.wepKeys[0] = "";
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConfig.wepTxKeyIndex = 0;
        }
        else if(passwordType == 2)//简单密码
        {
            wifiConfig.wepKeys[0] = ("\"" + password + "\"");
            wifiConfig.hiddenSSID = true;
            wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConfig.wepTxKeyIndex = 0;
        }
        else//wap加密
        {
            wifiConfig.preSharedKey = ("\"" + password + "\"");
            wifiConfig.hiddenSSID = true;
            wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfig.status = WifiConfiguration.Status.ENABLED;
        }
        return wifiConfig;
    }

    /**
     * 是否存在网络信息
     * @param ssid  热点名称
     * @return
     */
    private WifiConfiguration isExsits(String ssid) {
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        for (int i = 0; i<configuredNetworks.size(); i++) {
            WifiConfiguration config = configuredNetworks.get(i);
            if (TextUtils.equals(ssid, config.SSID))
                return config;
        }
        return null;
    }

    public WifiInfo getCurrentWifiInfo() {
        return wifiManager.getConnectionInfo();
    }

    public List<WifiConfiguration> getConfiguredNetworks() {
        return wifiManager.getConfiguredNetworks();
    }

    public String getCurrentNetworkSSID() {
        return wifiManager.getConnectionInfo().getSSID();
    }

    public int getCurrentNetworkId() {
        return wifiManager.getConnectionInfo().getNetworkId();
    }

    public String getCurrentNetworkIP() {
        int ip = wifiManager.getConnectionInfo().getIpAddress();
        String address = "" + (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
        return address;
    }

    public boolean isWifiOpened() {
        return wifiManager.isWifiEnabled();
    }

    public List<ScanResult> getScanResult() {
        return wifiManager.getScanResults();
    }


}
