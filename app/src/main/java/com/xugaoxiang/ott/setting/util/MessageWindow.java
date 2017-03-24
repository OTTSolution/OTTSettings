package com.xugaoxiang.ott.setting.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/3/2 0002.
 */
public class MessageWindow {
    public static int getWid(Context context){
        WindowManager windowManager = (WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        int width = display.getWidth();
        // height = display.getHeight();
        return width;
    }

    public static int getHei(Context context){
        WindowManager windowManager = (WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        int height = display.getHeight();

        return height;
    }
}
