package com.xugaoxiang.ott.setting.util.networksettingutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Administrator on 2016/9/20.
 */
public class EthernetStatequery {


    //private EthernetDevInfo mEthInfo = mEthManager.getSavedEthConfig();
    public static String getIPAddress() {
        try {
            for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                NetworkInterface intf = en.nextElement();
                for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();){
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)){

                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "null";
    }
    /**
     * 获取本地IP地址
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMacAddress(){
        String macAddress= "";
        try {
            byte[] mac;
            NetworkInterface networkInterface= NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
            mac = networkInterface.getHardwareAddress();
            macAddress = byte2hex(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    public static  String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp;
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }
    public static String getDNS1(){
        Class<?> SystemProperties;
        try {
            SystemProperties = Class.forName("android.os.SystemProperties");
            Method method = SystemProperties.getMethod("get",  String.class );
            String servers = (String) method.invoke(SystemProperties,"net.dns1");
    //        Log.e("以太网DNS1",servers+"");
            if(!TextUtils.isEmpty(servers))
            return servers;
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getDNS2(){
        Class<?> SystemProperties;
        try {
            SystemProperties = Class.forName("android.os.SystemProperties");
            Method method = SystemProperties.getMethod("get",  String.class );
            String servers = (String) method.invoke(SystemProperties,"net.dns2");
     //       Log.e("以太网DNS2",servers+"");
            if(!TextUtils.isEmpty(servers))
            return servers;
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getRoute(){

        Class<?> SystemProperties;
        try {
            SystemProperties = Class.forName("android.os.SystemProperties");
            Method method = SystemProperties.getMethod("get",  String.class );
            String servers = (String) method.invoke(SystemProperties, "net.route");
    //        Log.e("以太网网关",servers+"");
            if(TextUtils.isEmpty(servers))
            return servers;
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getNetMask(Context context){

        Class<?> SystemProperties;
        try {
            SystemProperties = Class.forName("android.os.SystemProperties");
            Method method = SystemProperties.getMethod("get",  String.class );
            String servers = (String) method.invoke(SystemProperties,"dhcp." + "net" + ".route");
         //   Toast.makeText(context, servers+"以太网", Toast.LENGTH_LONG).show();
            if(TextUtils.isEmpty(servers))
                return servers;
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isEthernetConnected(ConnectivityManager connectivityManager){
        boolean isConnected = false;
        NetworkInfo.State mEthernetState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).getState();
        if(mEthernetState == NetworkInfo.State.CONNECTED)
            isConnected = true;
        return isConnected;
    }

    public static boolean netWorkConnected(ConnectivityManager connectivityManager){
        boolean isConnected = false;

        if(connectivityManager.getActiveNetworkInfo()!= null){
            isConnected = connectivityManager.getActiveNetworkInfo().isAvailable();
        }
        return isConnected;
    }
}
