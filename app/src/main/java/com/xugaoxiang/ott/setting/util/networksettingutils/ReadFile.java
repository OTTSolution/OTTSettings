package com.xugaoxiang.ott.setting.util.networksettingutils;

import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2016/9/29.
 */
public class ReadFile {

    private static final String TAG = "NetSpeedFragment";
    private static final boolean DBG = true;
    public static boolean isStop = false;
    public static byte[] getFileFromUrl(String url, NetSpeedInfo netWorkSpeedInfo) {
        int currentByte = 0;
        int fileLength = 0;
        long startTime = 0;
        long intervalTime = 0;

        byte[] b = null;

        int bytecount = 0;
        URL urlx = null;
        URLConnection con = null;
        InputStream stream = null;
        try {
            Log.d(TAG,"URL:" + url);
            urlx = new URL(url);
            con = urlx.openConnection();
            con.setConnectTimeout(20000);
            con.setReadTimeout(20000);
            fileLength = con.getContentLength();//网络文件长度
            stream = con.getInputStream();
            netWorkSpeedInfo.totalBytes = fileLength;
            netWorkSpeedInfo.hadFinishedBytes=0;
            Log.i(TAG,"fileLength = " + fileLength);
            b = new byte[fileLength];
            startTime = System.currentTimeMillis();
            while (!isStop && (currentByte = stream.read()) != -1) {
                netWorkSpeedInfo.hadFinishedBytes++;
                intervalTime = System.currentTimeMillis() - startTime;
                if (intervalTime == 0) {
                    netWorkSpeedInfo.speed = 1000;
                } else {
                    netWorkSpeedInfo.speed = (netWorkSpeedInfo.hadFinishedBytes / intervalTime) * 1000;
                }
			/*	if (bytecount < fileLength) {
					b[bytecount++] = (byte) currentByte;
				}*/
            }
        } catch (Exception e) {
            Log.e(TAG,"exception :  = " + e.getMessage() );
        } finally {
            try {
                if(DBG)log("enter finally stream = " + stream);
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e) {
                Log.e(TAG,"exception :  =" +e.getMessage());
            }

        }
        return b;
    }
    private static void log(String s) {
        Log.d(TAG, s);
    }

}
