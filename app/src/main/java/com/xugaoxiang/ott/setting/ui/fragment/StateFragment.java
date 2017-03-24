package com.xugaoxiang.ott.setting.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xugaoxiang.ott.setting.R;

import com.xugaoxiang.ott.setting.util.networksettingutils.EthernetStatequery;
import com.xugaoxiang.ott.setting.util.networksettingutils.Statequery;

/**
 * Created by Administrator on 2016/9/19.
 */
public class StateFragment extends android.support.v4.app.Fragment{

    private TextView textViewIp;
    private TextView textViewMac;
    private TextView textViewMask;
    private TextView textViewDns1;
    private TextView textViewFamMask;
    private TextView textViewDns2;
    private TextView textViewif;
    private ImageView imageViewif;
    private String networkes;
    private NetworkReceiver receiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_state, container, false);
        textViewIp= (TextView) view.findViewById(R.id.fragment_state_ip_ttv);
        textViewMac= (TextView) view.findViewById(R.id.fragment_state_mac_ttv);
        textViewMask= (TextView) view.findViewById(R.id.fragment_state_mask_ttv);
        textViewDns1= (TextView) view.findViewById(R.id.fragment_state_dns1_ttv);
        textViewFamMask= (TextView) view.findViewById(R.id.fragment_state_fammask_ttv);
        textViewDns2= (TextView) view.findViewById(R.id.fragment_state_dns2_ttv);
        textViewif= (TextView) view.findViewById(R.id.fragment_state_if_ttv);
        imageViewif= (ImageView) view.findViewById(R.id.fragment_state_state_iv);
        registerNetReceiver();
        isNetworkAvailable(getActivity());
        return view;
    }

    private void registerNetReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        getActivity().registerReceiver(receiver, filter);
    }
    //判断网络
    public  boolean isNetworkAvailable(Context context) {
        boolean flag =false;

        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.getActiveNetworkInfo()!= null)
        {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
     //  boolean isNetWork= manager.getActiveNetworkInfo().isAvailable();
        if(flag){
            textViewif.setText(R.string.networktv1);
            imageViewif.setImageResource(R.drawable.network1);
            NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            NetworkInfo.State ethernet = manager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).getState();
            if(wifi == NetworkInfo.State.CONNECTED){
                //   || wifi == NetworkInfo.State.CONNECTING
                Log.e("wifi","wifi连接");
               networkes="wifi";
               initWifiStateData(networkes);
            }
            if(ethernet == NetworkInfo.State.CONNECTED){
                Log.e("宽带","宽带连接");
                networkes="ethernet";
                initEthernetStateData(networkes);
            }
        }
        if(!flag){
            initNotworkFail();
        }
        return flag;
   }

    private void initWifiStateData(String string) {
        if(string.equals("wifi")) {
            textViewIp.setText(Statequery.getIPAddress(getActivity()));
            textViewMac.setText(Statequery.getMacAddress(getActivity()));
            textViewMask.setText(Statequery.getNetMaskAddress(getActivity()));
            textViewDns1.setText(Statequery.getDns1Address(getActivity()));
            textViewDns2.setText(Statequery.getDns2Address(getActivity()));
            textViewFamMask.setText(Statequery.getFamMaskAddress(getActivity()));
        }
    }

    private void initEthernetStateData(String string) {
        if(string.equals("ethernet")) {
            textViewIp.setText(EthernetStatequery.getIPAddress());
            textViewMac.setText(EthernetStatequery.getMacAddress());
            textViewMask.setText("255.255.255.0");
            textViewDns1.setText(EthernetStatequery.getDNS1());
            textViewDns2.setText(EthernetStatequery.getDNS2());
            textViewFamMask.setText("192.168.191.1");
        }
    }

    private void initNotworkFail(){
        imageViewif.setImageResource(R.drawable.network2);
        textViewif.setText(R.string.networktv2);
        textViewIp.setText(R.string.unknown);
        textViewMac.setText(R.string.unknown);
        textViewMask.setText(R.string.unknown);
        textViewDns1.setText(R.string.unknown);
        textViewDns2.setText(R.string.unknown);
        textViewFamMask.setText(R.string.unknown);
    }
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//
//        try {
//            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
//            childFragmentManager.setAccessible(true);
//            childFragmentManager.set(this, null);
//
//
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }

    class NetworkReceiver extends BroadcastReceiver {
        ConnectivityManager mConnectivityManager;

        NetworkInfo netInfo;

        @Override
        public void onReceive(Context context, Intent intent) {
//            setResultCode(1985);
//            setResultData("DOB");
        //    isNetworkAvailable(context);

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if(netInfo!= null && netInfo.isAvailable()) {
                    textViewif.setText(R.string.networktv1);
                    imageViewif.setImageResource(R.drawable.network1);
                    /////////////网络连接
                    String name = netInfo.getTypeName();
                    if(netInfo.getType()==ConnectivityManager.TYPE_WIFI){
                        /////WiFi网络
                        initWifiStateData("wifi");

                    }else if(netInfo.getType()==ConnectivityManager.TYPE_ETHERNET){
                        /////有线网络
                        initEthernetStateData("ethernet");
                    }
                }
                else {
                    ////////网络断开
                    initNotworkFail();
                }
            }

        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}
