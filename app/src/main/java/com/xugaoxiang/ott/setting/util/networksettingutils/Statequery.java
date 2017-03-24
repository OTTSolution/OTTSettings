package com.xugaoxiang.ott.setting.util.networksettingutils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Administrator on 2016/9/20.
 */
public class Statequery {

    public static WifiManager my_wifiManager;
    public static WifiInfo wifiInfo;
    public static DhcpInfo dhcpInfo;

    public static String getIPAddress(Context context) {
        my_wifiManager = ((WifiManager)context.getSystemService(Context.WIFI_SERVICE));
        dhcpInfo = my_wifiManager.getDhcpInfo();
        String ipaddress= intToIp(dhcpInfo.ipAddress);
       // Log.e("IP地址",ipaddress+"");
        return ipaddress;
    }
    public static String getMacAddress(Context context) {
        my_wifiManager = ((WifiManager)context.getSystemService(Context.WIFI_SERVICE));
        wifiInfo = my_wifiManager.getConnectionInfo();
        String macaddress=wifiInfo.getMacAddress();
      //  Log.e("mac地址",macaddress+"");
        return macaddress;
    }
    public static String getNetMaskAddress(Context context) {
        my_wifiManager = ((WifiManager)context.getSystemService(Context.WIFI_SERVICE));
        dhcpInfo = my_wifiManager.getDhcpInfo();
        String maskaddress = intToIp(dhcpInfo.netmask);
     //   Log.e("网络掩码地址",maskaddress+"");
        return maskaddress;
    }

    public static String getDns1Address(Context context) {
        my_wifiManager = ((WifiManager)context.getSystemService(Context.WIFI_SERVICE));
        dhcpInfo = my_wifiManager.getDhcpInfo();
        String dns1address = intToIp(dhcpInfo.dns1);
     //   Log.e("DNS服务器地址",dns1address+"");
        return dns1address;
    }
    public static String getDns2Address(Context context) {
        my_wifiManager = ((WifiManager)context.getSystemService(Context.WIFI_SERVICE));
        dhcpInfo = my_wifiManager.getDhcpInfo();
        String dns2address = intToIp(dhcpInfo.dns2);
    //    Log.e("备用DNS服务器地址",dns2address+"");
        return dns2address;
    }
    public static String getFamMaskAddress(Context context) {
        my_wifiManager = ((WifiManager)context.getSystemService(Context.WIFI_SERVICE));
        dhcpInfo = my_wifiManager.getDhcpInfo();
        String fammaskaddress = intToIp(dhcpInfo.gateway);
   //     Log.e("家庭网关地址",fammaskaddress+"");
        return fammaskaddress;
    }
    public static String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }
}
