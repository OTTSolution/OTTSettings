package com.xugaoxiang.ott.setting.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xugaoxiang.ott.setting.R;
import com.xugaoxiang.ott.setting.util.systemutils.DisplayDAgent;
import com.xugaoxiang.ott.setting.util.systemutils.DisplayModeInfo;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/9/22.
 */
public class OutputFragment extends Fragment {
    private static final String TAG="DisplayModeFragment";
    private final int FOCUS_LEFT_ID=R.id.btn_outputsetting_id;
    private DisplayDAgent mDisplayd=null;
    private RadioGroup mRadioGroup;
    ArrayList<DisplayModeInfo> mDisplayModeArray;
    DisplayModeInfo mDisplayModeCurrent;
    CompoundButton.OnCheckedChangeListener mDisplayModeSelectedListener =
            new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked || mDisplayd==null)
                        return;

                    RadioButton btn = (RadioButton)buttonView;

                    DisplayModeInfo mode_to_set = (DisplayModeInfo)btn.getTag();
                    try{
                        mDisplayd.setMode(mode_to_set);
                    }catch(RemoteException e){
                        Log.e(TAG, "set mode error");
                    }
                    Log.i(TAG, "=== set display mode ===");
                    mode_to_set.dump(TAG);

                    // re-query from hw for the current mode.
                    try{
                        mDisplayModeCurrent = mDisplayd.getMode();
                    }catch(RemoteException e){
                        Log.e(TAG, "requery current mode error");
                    }
                    Log.i(TAG, "=== current display mode ===");
                    mDisplayModeCurrent.dump(TAG);

                    for(int i=0;i<mRadioGroup.getChildCount();i++){
                        RadioButton child = (RadioButton)mRadioGroup.getChildAt(i);
                        if(child == btn)
                            continue;
                        child.setChecked(false);
                    }
                }
            };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_output,container,false);
        mRadioGroup = (RadioGroup)view.findViewById(R.id.display_mode_radiogroup);
        mDisplayd = new DisplayDAgent();
        updateDisplayMode();
        updateUI();
        return view;
    }

    private void updateDisplayMode(){
        int i;
        try{
            mDisplayModeArray = mDisplayd.getModePreferd();
        } catch(RemoteException e){
            Log.e(TAG, "fail to get display mode liste");
        }
        Log.i(TAG, "mode list:");
        for(i=0;i<mDisplayModeArray.size();i++)
            mDisplayModeArray.get(i).dump(TAG);

        try{
            mDisplayModeCurrent = mDisplayd.getMode();
        } catch(RemoteException e){
            Log.e(TAG, "fail to get display current mode");
        }
        Log.i(TAG, "current mode:");
        mDisplayModeCurrent.dump(TAG);
    }

    /*
     * setNextFocus: key navigation
     *
     * press UP/DOWN/RIGHT, keep in the same fragment
     * only when press LEFT, return to left panel.
     * implement this behavior by dealing with following attribute:
     *
     * android:nextFocusLeft
     * android:nextFocusRight
     * android:nextFocusUp
     * android:nextFocusDown
     *
     */
    private void setNextFocus(){
        if(mRadioGroup==null || mRadioGroup.getChildCount()==0)
            return;
        int i, total=mRadioGroup.getChildCount();

        for(i=0;i<total;i++){
            RadioButton b = (RadioButton)mRadioGroup.getChildAt(i);
            b.setNextFocusLeftId(FOCUS_LEFT_ID);

            // first button, move top -> self
            if(i==0)
                b.setNextFocusUpId(b.getId());

            // last button, move down -> self
            if(i==total-1)
                b.setNextFocusDownId(b.getId());
        }
    }
    private void updateUI(){
        int i;
        DisplayModeInfo modenull = new DisplayModeInfo(0);
        for(i=0;i<mDisplayModeArray.size();i++){
            DisplayModeInfo m = mDisplayModeArray.get(i);
            if(m.isEqual(modenull))
                continue;
            if(m.signature.equals("other"))
                continue;
            RadioButton btn=new RadioButton(getActivity());
            btn.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            btn.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)); //width, height
            btn.setBackgroundResource(R.drawable.radio_button_background_light);
            btn.setText(m.signature);
            btn.setTextColor(Color.WHITE);
            btn.setTextSize(20);
            btn.setPadding(20,20,0,0);//左,上,右,下
            btn.setTag(m);
            if(m.isEqual(mDisplayModeCurrent))
                btn.setChecked(true);
            btn.setOnCheckedChangeListener(mDisplayModeSelectedListener);

            mRadioGroup.addView(btn);
        }
        setNextFocus();
    }
}
