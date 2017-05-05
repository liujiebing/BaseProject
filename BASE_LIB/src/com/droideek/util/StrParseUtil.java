package com.droideek.util;

import android.text.TextUtils;

/**
 * Created by Droideek on 2015/12/28.
 */
public class StrParseUtil {
    public static float parseFloat(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int parseInt(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static double parseDouble(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static long parseLong(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean parseBoolean(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        try {
            return Boolean.parseBoolean(string);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean equal(String compare, String obj) {
        if (TextUtils.isEmpty(compare)) {
            return false;
        }

        return compare.equals(obj);
    }

    public static boolean isNumeric(String str) {
        if(TextUtils.isEmpty(str)) return false;
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    public static String getUTF8String(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String mStr = str;

        String encode = getEncoding(str);

        if (!"UTF-8".equals(encode)) {
            try {
                mStr= new String(str.getBytes(encode), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return mStr;

    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }
}
