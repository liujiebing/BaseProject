package com.platform.helper;

import android.content.Context;
import android.text.TextUtils;

import com.droideek.util.FileUtil;
import com.droideek.util.StorageUtils;
import com.platform.BaseApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.droideek.util.FileUtil.deleteFile;
import static com.droideek.util.FileUtil.getAssetsProperties;
import static com.droideek.util.FileUtil.getSDFirstCacheDir;
import static com.droideek.util.FileUtil.makeFolders;

/**
 * Created by Droideek on 2017/1/23.
 */

public class FileHelper {
    /**
     * assets 文件下properties
     */
    public static final String ASSETS_PROPERTY = BaseApplication.ASSETS_PROPERTY;
    public static final String XG_TEMP_FOLDER = "XGTEMP";
    public static final String SD_FOLDER = "XG_SEAGOOR";
    public static final String CACHE_MAIN = "mc";
    public static final String CACHE_INPUT = "xgInput";
    public static final String CACHE_SEARCH = "XGSearchHis";
    public static final String CACHE_GROUP_SEARCH = "XGGroupSearchHis";

    /**
     * 获取 项目 SD缓存目录，末尾带/;
     *
     * @return 如：/SD_FOLDER/
     */
    public static String getTempFolder() {
        String folder = getSDFirstCacheDir(BaseApplication.context) + XG_TEMP_FOLDER;

        if (makeFolders(folder)) {

            return folder + File.separator;
        } else {
            return "";
        }
    }

    /**
     * 初始化 properties中的属性
     *
     * @param key
     * @return
     */
    public static String getPropertiesValue(String key) {
        String result = getSDcardProperties(getTempFolder() + ASSETS_PROPERTY, key);
        return result.equals("") ? getAssetsProperties(BaseApplication.context, key, ASSETS_PROPERTY) : result;
    }

    /**
     * 读取本地SD卡文件下  XXX.properties中的属性key 的value
     *
     * @param filePath
     * @param key
     * @return
     */
    private static String getSDcardProperties(String filePath, String key) {
        String result = "";
        if (FileUtil.isExistSdcard() && FileUtil.isFileExist(filePath)) {
            try {
                InputStream inStream = new FileInputStream(filePath);
                Properties proper = new Properties();
                proper.load(inStream);
                if (proper.containsKey(key)) {
                    result = proper.get(key).toString();
                }
                inStream.close();
            } catch (Exception e) {

            }
        }
        return result;
    }

    public static File writeHtmlToFile(Context context, String content) {
        if (TextUtils.isEmpty(content))
            return null;

        File file = new File(getTempFolder(), "index.html");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return file;
    }

    /**
     * 获取 项目 SD缓存目录，末尾带/;
     *
     * @return 如：/SD_FOLDER/
     */
    public static String getSDFolder() {
        try {
            return StorageUtils.getOwnCacheDirectory(BaseApplication.context, SD_FOLDER).getCanonicalPath();
        } catch (IOException e) {
            // e.printStackTrace();
            return getTempFolder();
        }
    }

    /**
     *
     * @param context ：上下文
     * @return：返回缓存路径，可能为null
     */
    public static String getImgCacheDirectory(Context context) {
        return getSDFolder();
    }

    public static void clearFileCache() {
        deleteFile(getImgCacheDirectory(BaseApplication.context));
        deleteFile(getTempFolder());
    }

}
