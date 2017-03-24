package com.xugaoxiang.ott.setting.ui.fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xugaoxiang.ott.setting.R;
import com.xugaoxiang.ott.setting.util.networksettingutils.NetSpeedInfo;
import com.xugaoxiang.ott.setting.util.networksettingutils.ReadFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class NetspeedFragment extends android.support.v4.app.Fragment implements View.OnClickListener{


    private static final String TAG = "NetSpeedFragment";
    private static final boolean DBG = true;
    private String url = "http://gdown.baidu.com/data/wisegame/6f9153d4a8d1f7d8/QQ.apk";
   // private String url = "http://202.158.177.67:8080/testbin";
    private View mView=null;
    byte[] imageData = null;
    String type;
    private Button b;
    private NetSpeedInfo mNetSpeedInfo = null;
    //private NetworkActivity mNetworkActivity=null;
    private Threadget mThreadget = null;
    private Threadcal mThreadcal = null;
    private final int UPDATE_SPEED = 1;
    private final int UPDATE_DNOE = 0;
    private ImageView imageView;
    private long begin = 0;
    private Button startButton;
    private TextView connectionType, nowSpeed, avageSpeed;
    long tem = 0;
    long falg = 0;
    long numberTotal = 0;
    List<Long> list = new ArrayList<Long>();
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable(){
        @Override
        public void run(){
            mThreadget.setStop(true);
            mThreadcal.setStop(true);
            startButton.setEnabled(true);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_netspeed, container, false);
        //mNetworkActivity = (NetworkActivity)getActivity();
        //imageView = (ImageView)mView.findViewById(R.id.iv_needle);

        connectionType = (TextView) mView.findViewById(R.id.netspeed_connecttype_id);
        nowSpeed = (TextView) mView.findViewById(R.id.netspeed_speed_id);
        avageSpeed = (TextView) mView.findViewById(R.id.netspeed_avgspeed_id);
        startButton = (Button)mView.findViewById(R.id.start_button);
        initView();
        mNetSpeedInfo = new NetSpeedInfo();
        return mView;

    }

    @Override
    public void onDestroy(){
        if(mThreadget != null)
            mThreadget.setStop(true);
        if(mThreadcal != null)
            mThreadcal.setStop(true);
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }
    private void initView(){

        startButton.setOnClickListener(this);
        isNetworkAvailable(getActivity());
        String netype=type;
        if(TextUtils.equals(netype,"ethernet")){
            connectionType.setText(R.string.net_ethernet);
            startButton.setEnabled(true);
        }else if(TextUtils.equals(netype,"wifi")){
            connectionType.setText(R.string.net_wifi);
            startButton.setEnabled(true);
        }else if(TextUtils.equals(netype,"no")){
            connectionType.setText(R.string.net_nonetype);
            startButton.setEnabled(true);
        }


    }
    @Override
    public void onClick(View v){
        if(DBG)log("v = "+v );
        switch(v.getId()){
            case R.id.start_button:
                Boolean boo = isNetworkAvailable(getActivity());
                if(!boo){
                    Toast.makeText(getActivity(),R.string.netspeed_no,Toast.LENGTH_SHORT).show();
                }
                startButton.setEnabled(false);
                list.clear();
                tem = 0;
                falg = 0;
                numberTotal = 0;
                mNetSpeedInfo.hadFinishedBytes = 0;
                mThreadget =  new Threadget();
                mThreadcal = new Threadcal();
                mThreadget.start();
                mThreadcal.start();
                mHandler.postDelayed(mRunnable, 30000);
                break;
            default:
                break;
        }
    }
    protected void startAnimation(double d) {
        AnimationSet animationSet = new AnimationSet(true);

        int end = getDuShu(d);

        Log.i(TAG, "begin:" + begin + "***end:" + end);
        RotateAnimation rotateAnimation = new RotateAnimation(begin, end, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
        rotateAnimation.setDuration(1000);
        animationSet.addAnimation(rotateAnimation);
        imageView.startAnimation(animationSet);
        begin = end;
    }

    public int getDuShu(double number) {
        double a = 0;
        if (number >= 0 && number <= 512) {
            a = number / 128 * 15;
        } else if (number > 521 && number <= 1024) {
            a = number / 256 * 15 + 30;
        } else if (number > 1024 && number <= 10 * 1024) {
            a = number / 512 * 5 + 80;
        } else {
            a = 180;
        }
        return (int) a;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int value = msg.what;
            switch (value) {
                case UPDATE_SPEED:
                    tem = mNetSpeedInfo.speed / 1024;
                    list.add(tem);
                    for (Long numberLong : list) {
                        numberTotal += numberLong;
                    }
                    falg = numberTotal / list.size();
                    numberTotal = 0;
                    if(DBG)log("now speed = " +tem + "kB/s");
                    if(DBG)log("avg spped = " +falg + "kB/s");
                    nowSpeed.setText(tem+" kB/s");
                    avageSpeed.setText(falg+" kB/s");
                    // startAnimation(Double.parseDouble(tem+""));
                    break;
                case UPDATE_DNOE:
                    tem = mNetSpeedInfo.speed / 1024;
                    list.add(tem);
                    Log.i("a", "tem****" + tem);
                    for (Long numberLong : list) {
                        numberTotal += numberLong;
                    }
                    falg = numberTotal / list.size();
                    numberTotal = 0;
                    if(DBG)log("update done now speed = " +tem + "kB/s");
                    if(DBG)log("update done avg spped = " +falg + "kB/s");
                    nowSpeed.setText(tem+" kB/s");
                    avageSpeed.setText(falg+" kB/s");
                    startButton.setEnabled(true);
                    mHandler.removeCallbacks(mRunnable);
                    // startAnimation(Double.parseDouble(tem+""));
                    break;
                default:
                    break;
            }
        }
    };
    private static void log(String s) {
        Log.d(TAG, s);
    }
    public class Threadget extends Thread{
        @Override
        public void run(){
            if(DBG)log("###### start #######");
            ReadFile.isStop = false;
            imageData = ReadFile.getFileFromUrl(url, mNetSpeedInfo);
        }
        public void setStop(boolean stop){
            ReadFile.isStop = stop;
        }
    }
    public class Threadcal extends Thread{
        private volatile boolean isStop = false;
        @Override
        public void run(){
            isStop = false;
            while (!isStop && mNetSpeedInfo.hadFinishedBytes < mNetSpeedInfo.totalBytes) {
                try {
                    sleep(1000);
                    if(DBG)log("mNetSpeedInfo.hadFinishedBytes = " + mNetSpeedInfo.hadFinishedBytes);
                    if(DBG)log("mNetSpeedInfo.totalBytes = " + mNetSpeedInfo.totalBytes);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(mNetSpeedInfo.hadFinishedBytes >0)
                    handler.sendEmptyMessage(UPDATE_SPEED);
            }
            if (mNetSpeedInfo.hadFinishedBytes == mNetSpeedInfo.totalBytes) {
                handler.sendEmptyMessage(UPDATE_DNOE);
                mNetSpeedInfo.hadFinishedBytes = 0;
            }

        }
        public void setStop(boolean stop){
            this.isStop = stop;
        }
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
        if(flag){

            //textViewif.setText("网络连接正常");
            NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            NetworkInfo.State ethernet = manager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).getState();
            if(wifi == NetworkInfo.State.CONNECTED){
                //   || wifi == NetworkInfo.State.CONNECTING
               // Log.e("wifi","wifi连接");
                type="wifi";
                //initWifiStateData();
            }
            if(ethernet == NetworkInfo.State.CONNECTED){
                //Log.e("宽带","宽带连接");
                //initEthernetStateData();
                type="enthernet";
            }
        }
        if(!flag){
            //textViewif.setText("网络连接异常");
            type="no";


        }
        return flag;
    }
}
