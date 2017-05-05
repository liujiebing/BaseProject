package com.platform;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.droideek.util.DeviceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    public static final String ASSETS_PROPERTY = "seagoor.properties";

    public static String B_MANUFACTUER = "";

    public static String DEBUG = "none";
    public static String SIGNATURE_PRE = "";
    public static String APP_TYPE;
    public static String APP_VERSION;
    public static String PARTNER;
    public static String URL_PRE = "";

    public static String YINLIAN_MODE = "00";
    public static String URL_HEAD = "";
    public static String URL_TRACK = "";
    public static boolean IS_LIB = true;

    //-----分享-------
    public static String WEIXIN_APPID = "";
    public static String WEIXIN_APPKEY = "";
    //    public static String SINA_APPKEY = "";  //TODO 需要新浪时打开
//    public static String SINA_APPSecret = "";//TODO 需要新浪时打开
    public static String QQ_APPID = "";
    public static String QQ_APPKEY = "";
    public static String SHARE_DEFAULT_TARGET = "";



    // 应用是否在前台
    private static boolean mIsActive = false;

    public static Application context;
    private static GoToInterface mGoToInterface;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }




    private void initBuild() {
        B_MANUFACTUER = DeviceUtils.getBuildManufactuer();

    }


    private String getPropValue(Properties proper, String name) {
        if (proper.containsKey(name)) {
            return proper.get(name).toString();
        }
        return "";
    }

    protected void initPropertiesConfig() {

        APP_VERSION = DeviceUtils.getVersionName(this);
        InputStream inStream = null;
        try {
            inStream = context.getAssets().open(ASSETS_PROPERTY);
            Properties proper = new Properties();
            proper.load(inStream);
            DEBUG = getPropValue(proper, "DEBUG");
            SIGNATURE_PRE = getPropValue(proper, "SIGNATURE_PRE");
            YINLIAN_MODE = getPropValue(proper, "YINLIAN_MODE");
            URL_HEAD = getPropValue(proper, "URL_HEAD");
            URL_PRE = getPropValue(proper, "URL_PRE");
            URL_TRACK = getPropValue(proper, "URL_TRACK");
            IS_LIB = "true".equals(getPropValue(proper, "IS_LIB"));
            APP_TYPE = getPropValue(proper, "APP_TYPE");
            PARTNER = getPropValue(proper, "PARTNER");
            WEIXIN_APPID = getPropValue(proper, "WEIXIN_APPID");
            WEIXIN_APPKEY = getPropValue(proper, "WEIXIN_APPKEY");
            QQ_APPID = getPropValue(proper, "QQ_APPID");
            QQ_APPKEY = getPropValue(proper, "QQ_APPKEY");
            SHARE_DEFAULT_TARGET = getPropValue(proper, "SHARE_DEFAULT_TARGET");


        } catch (Exception e) {


        }finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 应用是否在前台
     *
     * @return
     */
    public static boolean isActive() {


        return mIsActive;
    }

    public static void setGoToInterface(GoToInterface goToInterface) {
        mGoToInterface = goToInterface;
    }

    public static GoToInterface getGoToInterface() {
        return mGoToInterface;
    }

    /**
     * 设置前台状态
     *
     * @param isActive
     */
    public static void setActive(boolean isActive) {


        mIsActive = isActive;
    }

    public static String getRealVersionName(Context app) {
        if (app != null) {
            try {
                ApplicationInfo info = app.getApplicationInfo();
                PackageManager pm = app.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(info.packageName, 0);
                return pi.versionName;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return APP_VERSION;
    }

    public static String getAppName(Context context) {
        PackageManager pm = context.getPackageManager();
        return context.getApplicationInfo().loadLabel(pm).toString();
    }

    /**
     * Created by Droideek on 2016/1/12.
     */
    public interface GoToInterface {
        /**
         * 登录. 提供token. sessionId标识这次回话的id
         *
         * @param from
         * @param sessionId
         */
        void login(Context from, String sessionId);

        /**
         * 注册. 注册成功后流程和登录相同
         *
         * @param from
         * @param sessionId
         */
        void register(Context from, String sessionId);

        /**
         * 退出登录
         *
         * @param from
         * @param sessionId
         */
        void logout(Context from, String sessionId);


        /**
         * 切换环境
         */
        void switchIp(Context from);

        void start(Context from);

    }

}