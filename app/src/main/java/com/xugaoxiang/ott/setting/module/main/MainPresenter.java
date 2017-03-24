package com.xugaoxiang.ott.setting.module.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.IpConfiguration;
import android.net.LinkAddress;
import android.net.NetworkUtils;
import android.net.StaticIpConfiguration;
import android.os.Handler;
import android.os.INetworkManagementService;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.xugaoxiang.ott.setting.R;
import com.xugaoxiang.ott.setting.util.CommonUtil;
import com.xugaoxiang.ott.setting.util.networksettingutils.EthernetStatequery;
import com.xugaoxiang.ott.setting.util.systemutils.PreferenceUtil;

import java.net.Inet4Address;

/**
 * Created by zero on 2017/1/10.
 */

public class MainPresenter {

    private IpConfiguration mIpConfiguration;
    private INetworkManagementService mNMService;
    private EthernetManager mEthernetManager;
    private ConnectivityManager mConnectivityManager;
    private Handler mHandler;
    private MainView mView;
    private Context mContext;

    private int times;


    public MainPresenter(Context context, MainView view, Handler handler) {
        this.mContext = context;
        mView = view;
        mHandler = handler;
        mNMService = INetworkManagementService.Stub.asInterface(ServiceManager.getService(context.NETWORKMANAGEMENT_SERVICE));
        mIpConfiguration = new IpConfiguration();
        mEthernetManager = (EthernetManager) context.getSystemService(Context.ETHERNET_SERVICE);
        mConnectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
    }
    public void dhcpConnect() {
        try {
            mHandler.removeCallbacks(mRunnable);
            mNMService.clearInterfaceAddresses("eth0");
            mIpConfiguration.setStaticIpConfiguration(null);
            mIpConfiguration.setIpAssignment(IpConfiguration.IpAssignment.DHCP);
            mEthernetManager.setConfiguration(mIpConfiguration);
            times = 0;
            mHandler.postDelayed(mRunnable, 3000);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void manuallyConnect() {
        try {
            mHandler.removeCallbacks(mRunnable);
            mNMService.clearInterfaceAddresses("eth0");
            Inet4Address ipAddress = (Inet4Address)NetworkUtils.numericToInetAddress(PreferenceUtil.getString("ip", "0.0.0.0"));
            Inet4Address gateway = (Inet4Address)NetworkUtils.numericToInetAddress(PreferenceUtil.getString("gateway", "0.0.0.0"));
            int networkPrefixLength = CommonUtil.netmaskToInt(PreferenceUtil.getString("netmask", "0.0.0.0"));
            Inet4Address dns1 = (Inet4Address)NetworkUtils.numericToInetAddress(PreferenceUtil.getString("dns1", "0.0.0.0"));
            Inet4Address dns2 = (Inet4Address)NetworkUtils.numericToInetAddress(PreferenceUtil.getString("dns2", "0.0.0.0"));
            StaticIpConfiguration staticIpConfiguration = new StaticIpConfiguration();
            if(ipAddress != null && networkPrefixLength != -1) staticIpConfiguration.ipAddress = new LinkAddress(ipAddress, networkPrefixLength);
            else mNMService.setInterfaceDown("eth0");
            if (gateway != null) staticIpConfiguration.gateway = gateway;
            if (dns1 != null) staticIpConfiguration.dnsServers.add(dns1);
            if (dns2 != null) staticIpConfiguration.dnsServers.add(dns2);
            mIpConfiguration.setStaticIpConfiguration(staticIpConfiguration);
            mIpConfiguration.setIpAssignment(IpConfiguration.IpAssignment.STATIC);
            mEthernetManager.setConfiguration(mIpConfiguration);
            times = 0;
            mHandler.postDelayed(mRunnable, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable mRunnable = new Runnable(){
        @Override
        public void run(){
            if(EthernetStatequery.isEthernetConnected(mConnectivityManager)){
                WirePresenter.changeText();
                mView.showToast(mContext.getResources().getString(R.string.ethernet_success_completetv));
            }
            else {
                times++;
                if(times <15)
                    mHandler.postDelayed(mRunnable, 1000);
                else{
                    WirePresenter.changeText();
                    mView.showToast(mContext.getResources().getString(R.string.ethernet_wifi_completetv));
                }
            }

        }
    };

//    private void change(){
//        wireView.changeText(string);
//    }
}
