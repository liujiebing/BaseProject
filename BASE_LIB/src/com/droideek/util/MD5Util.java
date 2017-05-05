package com.droideek.util;

import java.security.MessageDigest;

/**
 * Created by Droideek on 2016/3/30.
 */
public class MD5Util {
    public static String md5(String data) {
        try {
            byte defaultBytes[] = data.getBytes();
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuffer hexString = new StringBuffer();
            for (byte aMessageDigest : messageDigest) {
                int val = 0xff & aMessageDigest;
                if (val < 16)
                    hexString.append("0");
                hexString.append(Integer.toHexString(val));
            }
            return hexString.toString();
        } catch (Exception e) {
            //LogUtil.e(TAG, "md5 e[" + e + "]");
        }
        return null;
    }
}