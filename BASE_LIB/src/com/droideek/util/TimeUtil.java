package com.droideek.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Droideek on 2015/12/28.
 */
public class TimeUtil {

    public static final String TIME_FM_DEFAULT ="yyyy-MM-dd HH:mm";
    public static final String TIME_UNIT_S="s";
    public static final String TIME_UNIT_MS="ms";

    /**
     * @param time
     * @param formatStr  yyyy-MM-dd HH:mm:ss
     * @param unit 传入当前time的单位: s or ms
     * @return 如果传入time是空, 返回 ""
     */
    public static String getTime(String time, String formatStr, String unit) {
        SimpleDateFormat format = new SimpleDateFormat(((TextUtils.isEmpty(formatStr)) ? TIME_FM_DEFAULT : formatStr), Locale.getDefault());
        String str = "";
        if (!TextUtils.isEmpty(time)) {
            Date date = new Date(StrParseUtil.parseLong(time) * (TIME_UNIT_S.equalsIgnoreCase(unit) ? 1000:1));     //如果是s,这里转成ms
            str = format.format(date);
        }
        return str;
    }

    /**
     * @param time
     * @param unit 传入当前time的单位: s or ms
     * @return 如果传入time是空, 返回 ""
     */
    public static String getTime(String time, String unit) {
        return getTime(time, TIME_FM_DEFAULT, unit);
    }

    public static String getTime(String time) {
        return getTime(time, TIME_FM_DEFAULT, TIME_UNIT_MS);
    }

    public static final String getTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return format.format(date);
    }

}
