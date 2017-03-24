package com.xugaoxiang.ott.setting.util.networksettingutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xugaoxiang.ott.setting.module.main.MainActivity;
import com.xugaoxiang.ott.setting.R;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * Created by Administrator on 2016/9/23.
 */

@SuppressLint("NewApi")
public class ShowWifiInfoDialog extends android.support.v4.app.DialogFragment{

    public ShowWifiInfoDialog() {
        // TODO Auto-generated constructor stub
    };

    FragmentActivity mActivity;

    public interface IRemoveWifi{

        void onRemoveClick(int networkId);
    }

    IRemoveWifi mIRemoveWifi = null;

    WifiInfo mConnectedInfo;

    String encrypt;

    public static ShowWifiInfoDialog newInstance(IRemoveWifi mIRemoveWifi , WifiInfo mConnectedInfo ,String encrypt){

        ShowWifiInfoDialog mFragment = new ShowWifiInfoDialog();

        mFragment.mConnectedInfo = mConnectedInfo;

        mFragment.encrypt = encrypt;

        mFragment.mIRemoveWifi = mIRemoveWifi;

        return mFragment;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        try {

            mActivity = (FragmentActivity) activity;
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.wifi_show_connected_wifi_page, null);

        TextView mStateTv = (TextView) view.findViewById(R.id.state_tv);

        String state = "";

        int Ip = mConnectedInfo.getIpAddress() ;

        Log.d(MainActivity.class.getSimpleName(), "ip = " + Ip);

        String strIp = "" + (Ip & 0xFF) + "." + ((Ip >> 8) & 0xFF) + "." + ((Ip >> 16) & 0xFF) + "." + ((Ip >> 24) & 0xFF);

        if(mConnectedInfo.getSSID() != null && mConnectedInfo.getBSSID() != null && !strIp.equals("0.0.0.0")){

            mStateTv.setText("已连接");
        }else{
            mStateTv.setText("正在连接...");
        }

        TextView mSafetyTv = (TextView) view.findViewById(R.id.safety_tv);

        mSafetyTv.setText(encrypt);

        TextView mLevelTv = (TextView) view.findViewById(R.id.level_tv);

        mLevelTv.setText(mConnectedInfo.getRssi() + "");

        TextView mSpeedTv = (TextView) view.findViewById(R.id.speed_tv);

        mSpeedTv.setText(mConnectedInfo.getLinkSpeed() + " Mbps");

        TextView mIpTv = (TextView) view.findViewById(R.id.ip_tv);

        mIpTv.setText(WifiUtil.long2ip(mConnectedInfo.getIpAddress()));

        TextView mApMacTv = (TextView) view.findViewById(R.id.ap_mac_tv);

        mApMacTv.setText(mConnectedInfo.getBSSID());

        TextView mNetMacTv = (TextView) view.findViewById(R.id.netcard_mac_tv);

        TextView mNetInterfaceTv = (TextView) view.findViewById(R.id.netcard_interface_tv);
        try {

            Field mField = mConnectedInfo.getClass().getDeclaredField("mIpAddress");
            mField.setAccessible(true);
            InetAddress mInetAddr = (InetAddress) mField.get(mConnectedInfo);

//	        for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
//	            Log.d(ShowWifiInfoDialog.class.getSimpleName(), networkInterface.toString());
//	        }

            NetworkInterface mInterface = NetworkInterface.getByInetAddress(mInetAddr);

            byte[] mac = mInterface.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for(int i = 0; i<mac.length;i++){

                sb.append(String.format("%02X%s", mac[i],(i<mac.length-1)?":":""));
            }
            mNetMacTv.setText(sb.toString());

            mNetInterfaceTv.setText(mInterface.getDisplayName() + "/"+mInterface.getName());

        }catch (SocketException e){

            e.printStackTrace();
        }catch(NoSuchFieldException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        TextView mMaskTv = (TextView) view.findViewById(R.id.mask_tv);

        DhcpInfo mDhcpInfo = WifiUtil.getDhcpInfo(mActivity);

        mMaskTv.setText(WifiUtil.long2ip(mDhcpInfo.netmask));

        TextView mGateWayTv = (TextView) view.findViewById(R.id.maskway_tv);

        mGateWayTv.setText(WifiUtil.long2ip(mDhcpInfo.gateway));

        TextView mDns1Tv = (TextView) view.findViewById(R.id.dns1_tv);

        mDns1Tv.setText(WifiUtil.long2ip(mDhcpInfo.dns1));

        TextView mDns2Tv = (TextView) view.findViewById(R.id.dns2_tv);

        mDns2Tv.setText(WifiUtil.long2ip(mDhcpInfo.dns2));

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());

        mBuilder.setView(view)
                .setTitle(mConnectedInfo.getSSID())
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setPositiveButton("清除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(mIRemoveWifi != null){
                            mIRemoveWifi.onRemoveClick(mConnectedInfo.getNetworkId());
                        }
                    }
                });

        return mBuilder.create();
    }

    public static void show(FragmentActivity mActivity,IRemoveWifi mIRemoveWifi ,WifiInfo mWifiInfo ,String encrypt){

        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();

        Fragment mBefore = mActivity.getSupportFragmentManager().findFragmentByTag(ShowWifiInfoDialog.class.getSimpleName());

        if(mBefore != null){

            ((android.support.v4.app.DialogFragment)mBefore).dismiss();

            ft.remove(mBefore);
        }
        ft.addToBackStack(null);

        android.support.v4.app.DialogFragment mNow =  ShowWifiInfoDialog.newInstance(mIRemoveWifi , mWifiInfo , encrypt);

        mNow.show(ft, ShowWifiInfoDialog.class.getSimpleName());
    }



}
