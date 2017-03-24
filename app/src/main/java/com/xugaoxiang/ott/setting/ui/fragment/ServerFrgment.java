package com.xugaoxiang.ott.setting.ui.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xugaoxiang.ott.setting.R;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/10/14.
 */
public class ServerFrgment extends Fragment implements View.OnClickListener{

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

        View view=inflater.inflate(R.layout.fragment_server,container,false);
        editText1= (EditText) view.findViewById(R.id.server_ed1);
        editText5= (EditText) view.findViewById(R.id.server_ed5);
        editText2= (EditText) view.findViewById(R.id.server_ed2);
        editText3= (EditText) view.findViewById(R.id.server_ed3);
        editText4= (EditText) view.findViewById(R.id.server_ed4);
        button= (Button) view.findViewById(R.id.fragment_server_bt);
        textView= (TextView) view.findViewById(R.id.server_retvv);
        initData();
        initList();
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
    private void initList() {
        editText1.setOnClickListener(this);
        editText2.setOnClickListener(this);
        editText3.setOnClickListener(this);
        editText4.setOnClickListener(this);
        editText5.setOnClickListener(this);
    }

    private void initData() {

        isFile(Environment.getExternalStorageDirectory() + File.separator + "MyApp" + File.separator + "AppServer");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((editText1.getText().toString()).equals("")||(editText5.getText().toString()).equals("")||(editText2.getText().toString()).equals("")||(editText3.getText().toString()).equals("")||(editText4.getText().toString()).equals(""))
                {
                    Toast.makeText(getActivity(),R.string.set2,Toast.LENGTH_SHORT).show();
                   Log.e("","自定义的服务器地址输入为空");
                }
                else {
                    String ser1 = editText1.getText().toString();
                    String ser2 = editText2.getText().toString();
                    String ser3 = editText3.getText().toString();
                    String ser4 = editText4.getText().toString();
                    String ser5 = editText5.getText().toString();
                    String server = "http://"+ser1+ "."+ser2 +"."+ser3+"."+ser4+":"+ser5;
                    //isFile();
                    saveStringToFile(Environment.getExternalStorageDirectory() + File.separator + "MyApp" + File.separator + "AppServer",server);
                    initAddress();
                    Toast.makeText(getActivity(),R.string.set1,Toast.LENGTH_SHORT).show();
                    // readFileContentStr(Environment.getExternalStorageDirectory() + File.separator + "MyApp" + File.separator + "AppServer");
                  //  Log.e("自定义的服务器地址",PreferenceUtil.getString("server_url","未设置成功")+"");
                }
            }
        });
    }

    private void initAddress() {
       String string=readFileContentStr(Environment.getExternalStorageDirectory() + File.separator + "MyApp" + File.separator + "AppServer");
        if(!TextUtils.isEmpty(string)){
            textView.setText(string);
        }
        if(TextUtils.isEmpty(string)){
            textView.setText("");
        }

    }




    public void isFile(String string){

        File file = new File(string);
       if(!file.exists()) {
           saveStringToFile(Environment.getExternalStorageDirectory() + File.separator + "MyApp" + File.separator + "AppServer","http://10.10.10.200:8080");
           //textView.setText("http://10.10.10.200:8080");
           initAddress();
           Log.e("","首次服务地址");
       }
        else {
            initAddress();
       }
    }
    /**
     * 保存字符串到文件
     * @param
     * @param
     * @return
     */
    public static boolean saveStringToFile(String path, String str) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream os = new FileOutputStream(file);
            os.write(str.getBytes());
            os.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String readFileContentStr(String fullFilename)
    {
        String readOutStr = null;

        try {

            DataInputStream dis = new DataInputStream(new FileInputStream(fullFilename));
            try {
                long len = new File(fullFilename).length();
                if (len > Integer.MAX_VALUE) throw new IOException("File "+fullFilename+" too large, was "+len+" bytes.");
                byte[] bytes = new byte[(int) len];
                dis.readFully(bytes);
                readOutStr = new String(bytes, "UTF-8");
            } finally {
                dis.close();
            }

      //      Log.d("readFileContentStr", "Successfully to read out string from file "+ fullFilename);
        } catch (IOException e) {
            readOutStr = null;

            //e.printStackTrace();
       //     Log.d("readFileContentStr", "Fail to read out string from file "+ fullFilename);
        }
      //  Log.e("111服务器",readOutStr+"");
        return readOutStr;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.server_ed1:
                editText1.setText("");
                break;
            case R.id.server_ed2:
                editText2.setText("");
                break;
            case R.id.server_ed3:
                editText3.setText("");
                break;
            case R.id.server_ed4:
                editText4.setText("");
                break;
            case R.id.server_ed5:
                editText5.setText("");
                break;
        }
    }
}
