package com.xugaoxiang.ott.setting.util.systemutils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.xugaoxiang.ott.setting.Myapplication;

/**
 * Created by Administrator on 2016/10/12.
 */
public class PreferenceUtil {

    private static SharedPreferences mSharedPreferences = null;

    public static void init(){
        if (null == mSharedPreferences) mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(Myapplication.getContext());
    }

    public static void removeValue(String key){
        mSharedPreferences.edit().remove(key).commit();
    }

    public static void clearAll(){
        mSharedPreferences.edit().clear().commit();
    }

    public static void putString(String key, String value){
        mSharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(String key, String faillValue){
        return mSharedPreferences.getString(key, faillValue);
    }

    public static void putInt(String key, int value){
        mSharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getInt(String key, int failValue){
        return mSharedPreferences.getInt(key, failValue);
    }

    public static void putLong(String key, long value){
        mSharedPreferences.edit().putLong(key, value).commit();
    }

    public static long getLong(String key, long failValue) {
        return mSharedPreferences.getLong(key, failValue);
    }

    public static void putBoolean(String key, boolean value){
        mSharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static Boolean getBoolean(String key, boolean failValue){
        return mSharedPreferences.getBoolean(key, failValue);
    }
}
