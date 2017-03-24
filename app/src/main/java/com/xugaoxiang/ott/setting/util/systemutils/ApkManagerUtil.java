package com.xugaoxiang.ott.setting.util.systemutils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/10/10.
 */
public class ApkManagerUtil {

    public static void openApp(Context context, String packageName){
        try{
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        }catch(Exception e){
            Toast.makeText(context, "没有安装", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 正常安装app
     * @param context
     * @param filePath
     */
    public static void installApp(Context context, String filePath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 正常卸载app
     * @param context
     * @param packageName
     */
//    public static void uninstallApp(Context context, String packageName) {
//        Uri uri = Uri.parse("package:" + packageName);
//        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
//        context.startActivity(intent);
//    }

    /**
     * 静默安装
     * @param context
     * @param filePath
     * @param packageName
     */
//    public static void installAppSlient(Context context, String filePath, String packageName) {
//        PackageManager pm = context.getPackageManager();
//        IPackageInstallObserver observer = new MyPakcageInstallObserver();
//        Uri fileUri = Uri.fromFile(new File(filePath));
//        int installFlags = 0;
//        try {
//            Class<?> b = Class.forName("android.content.pm.PackageManager");
//            Field[] fields = b.getDeclaredFields();
//            int install_replace_existing;
//            for(int i=0;i <fields.length;i++){
//                //获取数组中对应的属性
//                Field field=fields[i];
//                String fieldName=field.getName();
//                if(fieldName.equals("INSTALL_REPLACE_EXISTING")){
//                    install_replace_existing = (Integer) field.get(pm);
//                    if(isAppInstalled(context, packageName))
//                        installFlags |= install_replace_existing;
//                }
//            }
//            Method getInstallMethod = pm.getClass().getDeclaredMethod("installPackage", Uri.class, IPackageInstallObserver.class, int.class, String.class);
//            getInstallMethod.invoke(pm, fileUri, observer, installFlags, packageName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 静默卸载
     * @param context
     * @param packageName
     */
    public static void uninstallAppSlient(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();

        IPackageDeleteObserver observer = new MyPackageDeleteObserver();
        try {
            Class<?> b = Class.forName("android.content.pm.PackageManager");
            Field[] fields = b.getDeclaredFields();
            int dont_delete_data = 0;
            for(int i=0;i <fields.length;i++){
                //获取数组中对应的属性
                Field field=fields[i];
                String fieldName=field.getName();
                if(fieldName.equals("DONT_DELETE_DATA")){
                    dont_delete_data = (Integer) field.get(pm);
                }
            }
            Method getDeleteInfo = pm.getClass().getDeclaredMethod("deletePackage",String.class,IPackageDeleteObserver.class,int.class);
            getDeleteInfo.invoke(pm, packageName, observer, dont_delete_data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    /*静默安装回调*/
//    static class MyPakcageInstallObserver extends IPackageInstallObserver.Stub{
//
//        @Override
//        public void packageInstalled(String packageName, int returnCode) {
//        }
//    }

    /* 静默卸载回调 */
    static class MyPackageDeleteObserver extends IPackageDeleteObserver.Stub {

        @Override
        public void packageDeleted(String packageName, int returnCode) {

        }
    }

    /**
     * 判断app是否安装
     * @param context
     * @param packagename
     * @return
     */
//    private static boolean isAppInstalled(Context context, String packagename) {
//        try {
//            context.getPackageManager().getPackageInfo(packagename, PackageManager.GET_UNINSTALLED_PACKAGES);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            return false;
//        }
//    }

}
