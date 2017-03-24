package com.xugaoxiang.ott.setting.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xugaoxiang.ott.setting.R;

import com.xugaoxiang.ott.setting.util.systemutils.PreferenceUtil;

/**
 * Created by Administrator on 2016/10/14.
 */
public class UpsetFragment extends Fragment implements View.OnClickListener{

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private Button button;
    private TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_upset,container,false);

        editText1= (EditText) view.findViewById(R.id.update_ed1);
        editText5= (EditText) view.findViewById(R.id.update_ed5);
        editText2= (EditText) view.findViewById(R.id.update_ed2);
        editText3= (EditText) view.findViewById(R.id.update_ed3);
        editText4= (EditText) view.findViewById(R.id.update_ed4);
        button= (Button) view.findViewById(R.id.fragment_upset_bt);
        textView= (TextView) view.findViewById(R.id.upset_retvv);
        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view,0);
        initData();
        init();
        return view;
    }

    private void init() {
        editText1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction()==KeyEvent.ACTION_DOWN ){

                    String str =editText1.getText().toString();

                    if(!TextUtils.isEmpty(str)){

                        editText1.getText().delete(editText1.getSelectionStart()-1, editText1.getSelectionStart());
                    }

                    if(!TextUtils.isEmpty(str)){
                        return  true;
                    }

                }

                if(keyCode == KeyEvent.KEYCODE_DPAD_UP && !TextUtils.isEmpty(editText1.getText().toString())){

                    return  true;
                }
                return false;
            }
        });

        editText2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction()==KeyEvent.ACTION_DOWN){
                    String str =editText2.getText().toString();
                    if(!TextUtils.isEmpty(str)){
                        editText2.getText().delete(editText2.getSelectionStart()-1, editText2.getSelectionStart());
                    }
                    if(!TextUtils.isEmpty(str)){
                        return  true;
                    }
                }
                if(keyCode == KeyEvent.KEYCODE_DPAD_UP && !TextUtils.isEmpty(editText2.getText().toString())){

                    return  true;
                }
                return false;
            }
        });

        editText3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction()==KeyEvent.ACTION_DOWN){

                    String str =editText3.getText().toString();
                    if(!TextUtils.isEmpty(str)){
                        editText3.getText().delete(editText3.getSelectionStart()-1, editText3.getSelectionStart());
                    }
                    if(!TextUtils.isEmpty(str)){
                        return  true;
                    }
                }
                if(keyCode == KeyEvent.KEYCODE_DPAD_UP && !TextUtils.isEmpty(editText3.getText().toString())){

                    return  true;
                }
                return false;
            }
        });


        editText4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction()==KeyEvent.ACTION_DOWN){
                    String str =editText4.getText().toString();
                    if(!TextUtils.isEmpty(str)){
                        editText4.getText().delete(editText4.getSelectionStart()-1, editText4.getSelectionStart());
                    }
                    if(!TextUtils.isEmpty(str)){
                        return  true;
                    }
                }
                if(keyCode == KeyEvent.KEYCODE_DPAD_UP && !TextUtils.isEmpty(editText4.getText().toString())){

                    return  true;
                }
                return false;
            }
        });

        editText5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction()==KeyEvent.ACTION_DOWN){

                    String str =editText5.getText().toString();
                    if(!TextUtils.isEmpty(str) && editText5.getSelectionStart() != 0){
                        editText5.getText().delete(editText5.getSelectionStart()-1, editText5.getSelectionStart());
                    }
                    if(!TextUtils.isEmpty(str)){
                        return  true;
                    }
                }
                if(keyCode == KeyEvent.KEYCODE_DPAD_UP ){

                    return  false;
                }
                return false;
            }
        });
    }

    private void initData() {
        initAddress();
        initOnLis();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((editText1.getText().toString()).equals("")||(editText5.getText().toString()).equals("")||(editText2.getText().toString()).equals("")||(editText3.getText().toString()).equals("")||(editText4.getText().toString()).equals(""))
                {
                    Toast.makeText(getActivity(),R.string.set2,Toast.LENGTH_SHORT).show();
                   // Log.e("自定义的服务器地址","输入为空");
                }
                else {
                    String ser1 = editText1.getText().toString();
                    String ser2 = editText2.getText().toString();
                    String ser3 = editText3.getText().toString();
                    String ser4 = editText4.getText().toString();
                    String ser5 = editText5.getText().toString();
                    String server = "http://"+ser1+ "."+ser2 +"."+ser3+"."+ser4+":"+ser5+"/download/OTA/";
                    PreferenceUtil.putString("server_url", server);
                    initAddress();
                    Toast.makeText(getActivity(), R.string.set1,Toast.LENGTH_SHORT).show();
                    Log.e("自定义的服务器地址",PreferenceUtil.getString("server_url","未设置成功")+"");
                }
            }
        });
    }

    private void initOnLis(){
        editText1.setOnClickListener(this);
        editText2.setOnClickListener(this);
        editText3.setOnClickListener(this);
        editText4.setOnClickListener(this);
        editText5.setOnClickListener(this);
    }

    private void initAddress() {
        String s1="/download/OTA/";

        //要切割的字符串
        String s=PreferenceUtil.getString("server_url","http://10.10.10.200:8080"+s1);
        String sub ="";
        //调用方法
        sub = s.replaceAll(s1,"");//.replaceAll( ",122.jpg|122.jpg,","");
        textView.setText(sub);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.update_ed1:
                editText1.setText("");
                break;
            case R.id.update_ed2:
                editText2.setText("");
                break;
            case R.id.update_ed3:
                editText3.setText("");
                break;
            case R.id.update_ed4:
                editText4.setText("");
                break;
            case R.id.update_ed5:
                editText5.setText("");
                break;
        }
    }
}
