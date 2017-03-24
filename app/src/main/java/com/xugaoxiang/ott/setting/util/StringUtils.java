package com.xugaoxiang.ott.setting.util;

/**
 * Created by zero on 2017/1/9.
 */

public class StringUtils {

    public static boolean isIpAddress(String address) {
        int start = 0;
        int end = address.indexOf('.');
        int numBlocks = 0;
        while (start < address.length()) {
            if (end == -1) {
                end = address.length();
            }

            try {
                int block = Integer.parseInt(address.substring(start, end));
                if ((block > 255) || (block < 0)) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
            numBlocks++;
            start = end + 1;
            end = address.indexOf('.', start);
        }
        return numBlocks == 4;
    }
}
