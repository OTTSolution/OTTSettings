package com.xugaoxiang.ott.setting.util.systemutils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2016/10/10.
 */
public class UninstallUtils {

    private  Context context;

    public UninstallUtils(Context context) {
        this.context = context;
    }

    //判断应用是否安装
    public  boolean checkApplication(String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {

            context.getPackageManager().getApplicationInfo(packageName,PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
