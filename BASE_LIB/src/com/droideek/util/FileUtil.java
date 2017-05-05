package com.droideek.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Properties;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by Droideek on 2015/12/27.
 */
public class FileUtil {
    private static final String TAG = "FileUtil";

    /**
     * 检查文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File f = new File(filePath);
        return f.exists() && f.isFile();
    }

    /**
     * 判断手机是否存在sd卡,并且有读写权限
     *
     * @return
     */
    public static boolean isExistSdcard() {
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        } catch (IncompatibleClassChangeError e) { // (sh)it happens too (Issue #989)
            externalStorageState = "";
        }
        if (MEDIA_MOUNTED.equals(externalStorageState)) {
            return true;
        }
        return false;
    }





    /**
     * 获取Assets目录下xxx.properties中的属性
     *
     * @param key
     * @return
     */
    public static String getAssetsProperties(Context context, String key, String propertyFile) {
        String result = "";
        try {
            InputStream inStream = context.getAssets().open(propertyFile);
            Properties proper = new Properties();
            proper.load(inStream);
            if (proper.containsKey(key)) {
                result = proper.get(key).toString();
            }
            inStream.close();
        } catch (Exception e) {


        }
        return result;
    }



    /**
     * 将android uri如content://media/external/image/media/102这样格式转换 成File类型
     * @param uri
     * @return
     */
    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }

        return data;
    }


    /**
     * 创建文件夹
     *
     * @param dirPath
     * @return
     */
    public static boolean makeFolders(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }
        File f = new File(dirPath);
        return (f.exists() && f.isDirectory()) || f.mkdirs();
    }


    /**
     * 读取list
     *
     * @param filePath
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> readerList(String filePath) {
        try {
            return (List<T>) readFileOIS(filePath);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 读取文件缓存
     *
     * @param filePath  文件路径
     * @return    Object vector map以及实现序列化的对象
     */
    public static Object readFileOIS(String filePath) {
        try {
            return readFileOIS(new File(filePath));
        } catch (Exception e) {
            return null;
        }
    }

    public static Object readFileOIS(File file) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object obj = null;
        if (file != null && file.exists()) {
            try {
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                obj = ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ois != null)
                        ois.close();
                    if (fis != null)
                        fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }


    public static <T> boolean writeList(String filePath, List<T> list, boolean append) {
        try {
            return writeFileOOS(filePath, list, append);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 写 object 类型数据到文件缓存
     *
     * @param filePath       文件路径（需确保目录存在)
     * @param o 			vector map以及实现序列化的对象
     * @param append
     */
    public static boolean writeFileOOS(String filePath, Object o, boolean append) {
        try {
            return writeFileOOS(new File(filePath), o, append);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean writeFileOOS(File file, Object o, boolean append) {
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        if (file != null && createFile(file) && o != null) {
            try {
                fos = new FileOutputStream(file, append);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(o);
                oos.flush();
            } catch (IOException e) {
                return false;
            } finally {
                try {
                    if (oos != null)
                        oos.close();
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 创建一个文件 ,如果文件已经存在则不创建
     * @param file
     * @return
     */
    public static boolean createFile(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists() && file.isFile()) {
            return true;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 创建一个新的文件 ,如果已经存在则删除文件
     * @param file
     * @return
     */
    public static boolean makeNewFile(File file) {
        if(null == file) return false;
        if (file.exists() && file.isFile()) {
            try {
                file.delete();
            } catch (Exception e) {
                return false;
            }
        }

        try {
            return file.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * 删除指定目录、文件   以及里面的文件
     *
     * @param dirPath
     * @return
     */
    public static boolean deleteFile(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }
        File file = new File(dirPath);
        try {
            if (!file.exists()) return true;

            if (file.isFile()) return file.delete();

            if (!file.isDirectory()) return false;
            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                } else if (f.isDirectory()) {
                    deleteFile(f.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            return false;
        }
        return file.delete();
    }




    /**
     *
     * 获取缓存路径(优先放在SD，如果不能放SD，就用手机的CACHE"
     * @param context
     * @return  返回带/的文件夹名
     */
    public static String getSDFirstCacheDir(Context context) {
        String path = StorageUtils.getCacheDirectory(context).getAbsolutePath();
        if(TextUtils.isEmpty(path)) return "";
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        return path;
    }



    /**
     * 获取缓存路径
     * @param context
     * @param fileName
     * @return
     */
    public static String getCachePath(Context context, String fileName){
        StringBuilder sb = new StringBuilder(); //处理缓存
        sb.append(context.getCacheDir().getAbsolutePath());
        sb.append(File.separator);
        sb.append(fileName);

        return sb.toString();
    }

}
