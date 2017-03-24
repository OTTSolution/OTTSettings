package com.xugaoxiang.ott.setting.ui.adapters;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xugaoxiang.ott.setting.R;
import com.xugaoxiang.ott.setting.util.WifiUtils;
import com.xugaoxiang.ott.setting.util.networksettingutils.ViewHolder;

import java.util.List;

/**
 * Created by zero on 2016/10/24.
 */

public class ScanResultsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<ScanResult> scanResultList;
    private WifiUtils wifiUtils;

    public ScanResultsAdapter(Context context, List<ScanResult> results, WifiUtils wifiUtils) {
        inflater = LayoutInflater.from(context);
        scanResultList = results;
        this.wifiUtils = wifiUtils;
    }

    @Override
    public int getCount() {
        return scanResultList.size();
    }

    @Override
    public Object getItem(int position) {
        return scanResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.wifi_scan_result_item, null);
        ImageView ivSignal= ViewHolder.getView(convertView,R.id.iv_signal_strength);
        TextView tvName = ViewHolder.getView(convertView, R.id.tv_ssid);
        TextView tvStatus = ViewHolder.getView(convertView, R.id.tv_wifi_status);
        ScanResult info = (ScanResult) getItem(position);
        if(!TextUtils.isEmpty(info.SSID))
            tvName.setText(info.SSID);
        String capabilities = info.capabilities.trim();
        tvStatus.setText(capabilities);
        int level=info.level;
        if (TextUtils.isEmpty(capabilities) || TextUtils.equals(capabilities, "[ESS]"))
        {
            if (-50 <= level)
                ivSignal.setImageResource(R.drawable.wifi_1_4);
            else if (-70 <= level && level < -50)
                ivSignal.setImageResource(R.drawable.wifi_1_3);
            else if (level < -70)
                ivSignal.setImageResource(R.drawable.wifi_1_2);
        }
        else
        {
            if (-50 <= level)
                ivSignal.setImageResource(R.drawable.wifi_04);
            else if (-70 <= level && level < -50)
                ivSignal.setImageResource(R.drawable.wifi_03);
            else if (level < -70)
                ivSignal.setImageResource(R.drawable.wifi_02);
        }
        if (TextUtils.equals(wifiUtils.getCurrentNetworkSSID().replace("\"", ""), info.SSID))
        {
            String address = wifiUtils.getCurrentNetworkIP();
            if (TextUtils.equals(address, "0.0.0.0") || TextUtils.isEmpty(address))
                tvStatus.setText(R.string.wifi_connectde1);
            else
                tvStatus.setText(R.string.wifi_connectde2);
        }
        else
            tvStatus.setText("");
        return convertView;
    }
}
