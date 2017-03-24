package com.xugaoxiang.ott.setting.util.systemutils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
public class ApkTool {

    static  String TAG = "ApkTool";

    public static List<String> scanLocalInstallAppList(PackageManager packageManager) {
        List<String> myAppInfos = new ArrayList<String>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                continue;
                 }
                String packname=packageInfo.packageName;
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                myAppInfos.add(packname);
            }
        }catch (Exception e){
            Log.e(TAG,"===============获取应用包信息失败");
        }
        return myAppInfos;
    }

}
