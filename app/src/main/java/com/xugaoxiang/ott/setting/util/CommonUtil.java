package com.xugaoxiang.ott.setting.util;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zero on 2016/10/19.
 */

public class CommonUtil {

    private static Toast toast;

    public static void showToast(Context context, String content) {
        if (toast == null)
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        else
            toast.setText(content);
        toast.show();
    }

    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    /********************************************/
    public static int netmaskToInt(String netmask) {
        Pattern pattern = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
        Matcher matcher = pattern.matcher(netmask);
        if (matcher.matches()) {
            int number = 0;
            for (int i = 1; i <= 4; ++i) {
                int n = (checkRange(Integer.parseInt(matcher.group(i)), 0, 255));
                while((n & 128)==128){
                    number = number + 1;
                    n = n << 1;
                }
            }
            return number;
        }
        else
            throw new IllegalArgumentException("Could not parse [" + netmask + "]");
    }

    private static int checkRange(int value, int begin, int end) {
        if (value >= begin && value <= end)
            return value;
        throw new IllegalArgumentException("Value out of range: [" + value + "]");
    }
}
