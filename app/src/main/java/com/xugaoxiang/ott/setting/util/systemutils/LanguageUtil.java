package com.xugaoxiang.ott.setting.util.systemutils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by Administrator on 2016/10/12.
 */
public class LanguageUtil {

    private Context context;

    public LanguageUtil(Context context) {
        this.context = context;
    }

    public void switchLanguage(String language){
        Resources resources=context.getResources();
        Configuration config=resources.getConfiguration();
        DisplayMetrics dm=resources.getDisplayMetrics();
        if(language.equals("en")){
            config.locale= Locale.ENGLISH;
        }
        if(language.equals("zh")){
            config.locale= Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(config,dm);

        //保存设置语言的类型
        PreferenceUtil.putString("language", language);

    }
}
