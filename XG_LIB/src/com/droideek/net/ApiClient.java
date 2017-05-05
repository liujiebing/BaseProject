package com.droideek.net;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.platform.BaseApplication;
import com.droideek.util.LogUtil;
import com.droideek.util.MD5Util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Droideek on 2016/6/28.
 */
public class ApiClient {
    public static final String XG_HOST = "m.seagoor.com";
    public static final String TAG = "[OKHTTP]";
    public static final String CUR_PAGE = "curpage";
    public static final String SHOP_MOBILE = "shopmobile";
    public static final String TOKEN = "token";

    //public static boolean DEBUG = true;
    static final int MAX_STALE = 30 * 24 * 60 * 60; //

    //--------请求公共参数 ---------------
    public static final String regcode = "250";
    public static final String provcode = "264";
    public static final String oem = "AM";

    public static String screenwidth = "720";
    public static String token = "";
    public static String screenheight = "1280";
    public static String scheme = "https";
    public static String nettype;
    public static String osversion;
    public static String apptype;
    public static String partner;
    public static String appversion;

    public static String COMMON_URL;

    public static HashMap<String, String> basicParam;

    static {

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        screenwidth = "" + metrics.widthPixels;
        screenheight = "" + metrics.heightPixels;

        osversion = String.valueOf(Build.VERSION.SDK_INT);
        nettype = com.droideek.util.NetUtil.getCurrentNetType(BaseApplication.context);
        apptype = BaseApplication.APP_TYPE;
        partner = BaseApplication.PARTNER;
        appversion = BaseApplication.APP_VERSION;
        COMMON_URL = BaseApplication.URL_PRE;

        basicParam = getParams();

    }

    static HttpUrl.Builder addCommonParams(HttpUrl.Builder builder) {
        Set set = basicParam.keySet();
        Iterator<String> it = set.iterator();

        while (it.hasNext()) {
            String key = it.next();
            builder.addQueryParameter(key, basicParam.get(key));
        }

        return builder;
    }


    public static HashMap<String, String> addParams(String key, String Value) {
        HashMap<String, String> basicParam = getParams();
        basicParam.put(key, Value);
        return basicParam;
    }

    /**
     * 配置 请求公共参数
     * @return
     */
    public static HashMap<String, String> getParams() {
        HashMap<String, String> basicParam = new HashMap<>();

        basicParam.put("screenwidth", screenwidth);
        basicParam.put("screenheight", screenheight);

        basicParam.put("oem", oem);
        basicParam.put("osversion", osversion);

        basicParam.put("apptype", apptype);
        basicParam.put("nettype", nettype);
        basicParam.put("regcode", regcode);
        basicParam.put("provcode", provcode);
        basicParam.put("partner", partner);
        basicParam.put("appversion", appversion);
        basicParam.put("scheme", scheme);

        addSignature(basicParam);
        return basicParam;
    }


    static Context mContext;
    static Cache cache;

    public static <S> S create(final Class<S> service) {
        mContext = BaseApplication.context;


        //---- 缓存拦截器-----
        File cacheFile = new File(mContext.getCacheDir(), "HttpCache");
        cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().cache(cache);

        httpClient.connectTimeout(12, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS);

//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//
//                // 创建缓存路径
//                if (NetUtil.checkNetwork(mContext) == NetUtil.NO_NETWORK) {
//                    request = request.newBuilder()
//                            .cacheControl(CacheControl.FORCE_CACHE)
//                            .build();
//                    if (DEBUG) {
//                        Log.d(TAG, "== no network");
//                    }
//                }
//
//
//                okhttp3.Response response = chain.proceed(request);
//
////                String requestUrl = response.request().url().uri().getPath(); //TODO TEST 接口拦截器
////                if(!TextUtils.isEmpty(requestUrl)){
////                    if(requestUrl.contains("checknew")) {
////                        if (Looper.myLooper() == null) {
////                            Looper.prepare();
////                        }
////                        if (DEBUG) {
////                            Log.d(TAG,"== 拦截> 现在请求的是 版本更新接口");
////                        }
////                      //  createObservable("现在请求的是登录接口");
////                    }
////                }
//                //缓存响应
//                if (NetUtil.checkNetwork(mContext) != NetUtil.NO_NETWORK) {
//                    //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
//                    String cacheControl = request.cacheControl().toString();
//                    if (DEBUG) Log.d(TAG, "== 拦截> cacheControl=====" + cacheControl);
//
//                    return response.newBuilder()
//                            .header("Cache-Control", cacheControl)
//                            //http1.0的旧东西，优先级比Cache-Control低
//                            .removeHeader("Pragma")
//                            .build();
//                } else {
//                    return response.newBuilder()
//                            .header("Cache-Control", String.format("public, only-if-cached, max-stale=%d", MAX_STALE))
//                            .removeHeader("Pragma")
//                            .build();
//                }
//            }
//        });


        //OkHttpClient.Builder httpClient = new OkHttpClient.Builder(); //不用缓存时打开
        //----参数拦截器-----
        Interceptor paramInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = null;
                if ("GET".equals(original.method())) {
                    url = addCommonParams(originalHttpUrl.newBuilder()).build(); //设置通用参数
                } else {
                    url = originalHttpUrl.newBuilder().build();
                }

                Request.Builder requestBuilder = original.newBuilder() //ADD HEADER
                        .url(url);

                if (LogUtil.isDebug) { //TODO DELETE TEST， 【配合格式化JSON】
                    Log.d(TAG, "== 请求：" + url);
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        httpClient.addInterceptor(paramInterceptor);

        if (LogUtil.isDebug) { //TODO DELETE TEST
            //----日志拦截器-----
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(
                    new HttpLoggingInterceptor.Logger() { //TODO 如果需要格式化JSON打开这个。
                        @Override
                        public void log(String message) {
                            try {
                                LogUtil.json(message);
                            } catch (Exception e) {
                                LogUtil.d(TAG, message);
                            }

                        }
                    }
            );
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient.addInterceptor(logging);  // <-- this is the important line!
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(COMMON_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(service);
    }

    public static void addSignature(HashMap<String, String> map) {
        String time = "" + System.currentTimeMillis();
        map.put("signature", getSignatureStr(time));
        map.put("curtime", time);
    }

    public static String getSignatureStr(String curTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(apptype);
        sb.append(curTime);
        sb.append(BaseApplication.SIGNATURE_PRE);


        return MD5Util.md5(sb.toString());
    }


}
