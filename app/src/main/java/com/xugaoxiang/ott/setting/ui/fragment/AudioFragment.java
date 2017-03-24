package com.xugaoxiang.ott.setting.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xugaoxiang.ott.setting.R;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/9/22.
 */
public class AudioFragment extends android.support.v4.app.Fragment implements RadioGroup.OnCheckedChangeListener,
        Button.OnClickListener{
    private static final String TAG = "NotificationSettings";
    private View rootView;
    private Context context;
    private RadioGroup hdmi_dolby;
    private RadioGroup spdif_dolby;
    private RadioButton hdmi_dolby_enable_btn;
    private RadioButton hdmi_dolby_disable_btn;
    private RadioButton spdif_dolby_enable_btn;
    private RadioButton spdif_dolby_disable_btn;
    private Button setVolumeBtn;
    private boolean dolby_capable = false;
    private boolean dolby_enable = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_audio,container,false);
        initView();
        return rootView;
    }
    private void initView() {

        hdmi_dolby = (RadioGroup) rootView.findViewById(R.id.hdmi_dolby_group);
        spdif_dolby = (RadioGroup) rootView.findViewById(R.id.spdif_dolby_group);
        hdmi_dolby_enable_btn = (RadioButton) rootView.findViewById(R.id.btn_pass_through_hdmi);
        hdmi_dolby_disable_btn = (RadioButton) rootView.findViewById(R.id.btn_rampcm_hdmi);
        spdif_dolby_enable_btn = (RadioButton) rootView.findViewById(R.id.btn_pass_through_spdif);
        spdif_dolby_disable_btn = (RadioButton) rootView.findViewById(R.id.btn_rampcm_spdif);

        dolby_capable = isDolbyCapable();
        if (dolby_capable) {
            hdmi_dolby.setOnCheckedChangeListener(this);
            spdif_dolby.setOnCheckedChangeListener(this);

            if (dolby_enable = isDolbyEnable()) {
                hdmi_dolby_enable_btn.setChecked(true);
                spdif_dolby_enable_btn.setChecked(true);
            } else {
                hdmi_dolby_disable_btn.setChecked(true);
                spdif_dolby_disable_btn.setChecked(true);
            }
        } else {
            hdmi_dolby_disable_btn.setChecked(true);
            spdif_dolby_disable_btn.setChecked(true);
            hdmi_dolby_disable_btn.setClickable(false);
            spdif_dolby_disable_btn.setClickable(false);
            hdmi_dolby_enable_btn.setClickable(false);
            spdif_dolby_enable_btn.setClickable(false);

            spdif_dolby_disable_btn.setTextColor(Color.GRAY);
            spdif_dolby_enable_btn.setTextColor(Color.GRAY);
            hdmi_dolby_disable_btn.setTextColor(Color.GRAY);
            hdmi_dolby_enable_btn.setTextColor(Color.GRAY);

            DolbyDisableListener dolbyDisableHandler = new DolbyDisableListener();
            spdif_dolby_enable_btn.setOnClickListener(dolbyDisableHandler);
            hdmi_dolby_enable_btn.setOnClickListener(dolbyDisableHandler);

            Log.i(TAG, "Dolby is NOT capable !");
        }

        setVolumeBtn = (Button) rootView.findViewById(R.id.media_volume_set_btn);
        setVolumeBtn.setOnClickListener(this);
    }

    private boolean isDolbyEnable() {
        String utcDate=null;
        try {

            Class<?> mClassType = null;
            if (mClassType == null) {
                mClassType = Class.forName("android.os.SystemProperties");
                Method get = mClassType.getDeclaredMethod("get", String.class, String.class);
                utcDate = (String) get.invoke(mClassType, "persist.nuplayer.force.bypass", "NOT_FOUND");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dolby_capable) {
            boolean isresult=utcDate.equals("true");
            return isresult;

            //return SystemProperties.get("persist.nuplayer.force.bypass", "NOT_FOUND").equals("true");
        }
        return false;
    }

    private boolean isDolbyCapable() {
        String utcDate=null;
        try {

            Class<?> mClassType = null;
            if (mClassType == null) {
                mClassType = Class.forName("android.os.SystemProperties");
                Method get = mClassType.getDeclaredMethod("get", String.class, String.class);
                utcDate = (String) get.invoke(mClassType, "persist.nuplayer.force.bypass", "NOT_FOUND");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isresult=utcDate.equals("NOT_FOUND");
        return !isresult;
        //return !(SystemProperties.get("persist.nuplayer.force.bypass", "NOT_FOUND").equals("NOT_FOUND"));
    }

    private void set_dolby(boolean val) {



        if (dolby_capable && (val != dolby_enable)) {

            if(val){
                try {

                    Class<?> mClassType = null;
                    if (mClassType == null) {
                        mClassType = Class.forName("android.os.SystemProperties");
                        Method set=mClassType.getDeclaredMethod("set", String.class, String.class);
                        set.invoke(mClassType, "persist.nuplayer.force.bypass", "true");
                        dolby_enable = val;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else {
                try {

                    Class<?> mClassType = null;
                    if (mClassType == null) {
                        mClassType = Class.forName("android.os.SystemProperties");
                        Method set=mClassType.getDeclaredMethod("set", String.class, String.class);
                        set.invoke(mClassType, "persist.nuplayer.force.bypass", "false");
                        dolby_enable = val;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            // SystemProperties.set("persist.nuplayer.force.bypass", val == true ? "true" : "false");

            // Log.i(TAG, "set Dolby " + (val==true? "enabled":"disabled"));
        }
        else {
            if ( ! dolby_capable) Log.i(TAG, "Dolby is NOT enabled !");
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if ( ! isDolbyCapable()) return;

        switch(checkedId) {
            case R.id.btn_pass_through_hdmi:
            case R.id.btn_pass_through_spdif:
                set_dolby(true);
                hdmi_dolby_enable_btn.setChecked(true);
                spdif_dolby_enable_btn.setChecked(true);
                break;
            default:
                set_dolby(false);
                hdmi_dolby_disable_btn.setChecked(true);
                spdif_dolby_disable_btn.setChecked(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
       // if ((Button)v != setVolumeBtn) return;
        Toast.makeText(getActivity(),R.string.set1,Toast.LENGTH_SHORT).show();
      //  new VolumeSettingDialog().show(getActivity().getFragmentManager(), "volume_dialog");
    }

    private class DolbyDisableListener implements Button.OnClickListener {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_pass_through_spdif:
                case R.id.btn_pass_through_hdmi:
                    hdmi_dolby_disable_btn.setChecked(true);
                    spdif_dolby_disable_btn.setChecked(true);
                    Toast.makeText(context, "dolby is not available now !", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
