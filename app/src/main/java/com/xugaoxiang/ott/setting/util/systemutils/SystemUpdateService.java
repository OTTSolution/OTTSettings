package com.xugaoxiang.ott.setting.util.systemutils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.android.ZXSettings.update.ISystemUpdateService;

import java.io.File;

/**
 * Created by Administrator on 2016/9/29.
 */
public class SystemUpdateService extends Service {

    static final String LOG_TAG = "SystemUpdateService";
    private int fileCnt = 0;

    public SystemUpdateService() {
    }

    private class SystemUpdateServiceImpl extends ISystemUpdateService.Stub{
        @Override
        public int getFileCnt(String path) throws RemoteException {
            Log.e(LOG_TAG, "path: " + path);

            File file = new File(path);

            if (file.exists()) {
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    if (files == null ) {
                        return 0;
                    }
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isFile()) {
                            fileCnt++;
                        }
                    }
                }
            }
            return fileCnt;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {

        return new SystemUpdateServiceImpl();
    }

    @Override
    public void onDestroy() {
        Log.e(LOG_TAG, "Release SystemUpdateService");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(LOG_TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
