package com.droideek.util;

import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Droideek on 2016/1/11.
 */
public class UriUtil {

    public static Uri parse(String uriString) {
        Uri uri = null;
        if(!TextUtils.isEmpty(uriString)){
            uri = Uri.parse(uriString);
        }
        return uri;
    }

    public static String getQueryParam(Uri uri, String key) {
        if(null == uri) return "";
        String value = "";
        try {
            value = uri.getQueryParameter(key);
        } catch (Exception e) {
            //--
        }

        return value;
    }
}
