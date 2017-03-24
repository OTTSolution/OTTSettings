package com.xugaoxiang.ott.setting.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/10/14.
 */
public class UpdateUtils {

    private static String TAG = "UpdateFragment";

    /**
     * 检查当前是否有网络连接
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo().isAvailable();
    }

    /**
     * 比较日期
     * @param date1
     * @param date2
     * @return
     */
    public static int compareDate(String date1, String date2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime())
                return 1;
            else if (dt1.getTime() < dt2.getTime())
                return -1;
            else
                return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 世界统一时间转为当前时区的时间
     * @param utcTime
     * @param utcTimeFormater
     * @param localTimeFormater
     * @return
     */
    public static String utcToLocal(String utcTime, String utcTimeFormater, String localTimeFormater) {
        SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimeFormater);
        SimpleDateFormat localFormater = new SimpleDateFormat(localTimeFormater);
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        localFormater.setTimeZone(TimeZone.getDefault());
        Date gpsUTCDate = null;
        try {
            gpsUTCDate =utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }

    public static String getHeadOfBSPVersion(String filename) {
        String rawKernelVersion;
        try {
            rawKernelVersion = readLine(filename);
        } catch (Exception e) {
            Log.i(TAG, "IO Exception when getting kernel version for Device Info screen");
            return "Unavailable";
        }
        final String PROC_VERSION_REGEX =
                "Linux version (\\S+) " + /* group 1: "3.0.31-g6fb96c9" */
                        "\\((\\S+?)\\) " +        /* group 2: "x@y.com" (kernel builder) */
                        "(?:\\(gcc.+? \\)) " +    /* ignore: GCC version information */
                        "(#\\d+) " +              /* group 3: "#1" */
                        "(?:.*?)?" +              /* ignore: optional SMP, PREEMPT, and any CONFIG_FLAGS */
                        "((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)"; /* group 4: "Thu Jun 28 11:02:39 PDT 2012" */
        Matcher m = Pattern.compile(PROC_VERSION_REGEX).matcher(rawKernelVersion);
        if (!m.matches()) {
            Log.i(TAG, "Regex did not match on /proc/version: " + rawKernelVersion);
            return "Unavailable";
        } else if (m.groupCount() < 4) {
            Log.i(TAG, "Regex match on /proc/version only returned " + m.groupCount() + " groups");
            return "Unavailable";
        }
        int index = m.group(1).indexOf("-") + 1;
        return m.group(1).substring(index, index + 2);
    }

    private static String readLine(String filename) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(filename), 256);
        String str = reader.readLine();
        reader.close();
        return str;
    }

    public static String formatKernelVersion(String filename) {
        String rawKernelVersion;
        try {
            rawKernelVersion = readLine(filename);
        } catch (Exception e) {
            Log.i(TAG, "IO Exception when getting kernel version for Device Info screen");
            return "Unavailable";
        }
        final String PROC_VERSION_REGEX =
                "Linux version (\\S+) " + /* group 1: "3.0.31-g6fb96c9" */
                        "\\((\\S+?)\\) " +        /* group 2: "x@y.com" (kernel builder) */
                        "(?:\\(gcc.+? \\)) " +    /* ignore: GCC version information */
                        "(#\\d+) " +              /* group 3: "#1" */
                        "(?:.*?)?" +              /* ignore: optional SMP, PREEMPT, and any CONFIG_FLAGS */
                        "((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)"; /* group 4: "Thu Jun 28 11:02:39 PDT 2012" */

        Matcher m = Pattern.compile(PROC_VERSION_REGEX).matcher(rawKernelVersion);
        if (!m.matches()) {
            Log.e(TAG, "Regex did not match on /proc/version: " + rawKernelVersion);
            return "Unavailable";
        } else if (m.groupCount() < 4) {
            Log.e(TAG, "Regex match on /proc/version only returned " + m.groupCount() + " groups");
            return "Unavailable";
        }
        return m.group(1);
    }
}
