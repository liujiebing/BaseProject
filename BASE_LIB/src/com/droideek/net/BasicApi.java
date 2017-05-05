package com.droideek.net;

import com.google.gson.Gson;
import com.platform.data.Response;
import com.droideek.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Droideek on 2016/6/30.
 */
public class BasicApi {

    public static class ParamBuilder{
        private HashMap<String, String> p;

        public ParamBuilder() {
            p = ApiClient.getParams();
        }

        public ParamBuilder addParam(String key, String value){
            p.put(key, value);
            return this;
        }

        public ParamBuilder addParam(String key, List<?> list) {
            if(null != list  && list.size() >0){
                p.put(key, JsonUtil.toJSONString(list));
            }
            return this;
        }

        public ParamBuilder addParam(String key, Object obj) {
            p.put(key, JsonUtil.toJSONString(obj));
            return this;
        }

        public HashMap<String, String> build() {
            return p;
        }

    }



    public static void addStringParameter(JSONObject jsonData, String name, String value) {
        if (name == null || value == null)
            return;

        try {
            jsonData.put(name, value);
        } catch (JSONException e) {
        }
    }

    public static void addIntParameter(JSONObject jsonData, String name, int value) {
        if (name == null || value == -1)
            return;

        try {
            jsonData.put(name, value);
        } catch (JSONException e) {
        }
    }

    public static void addObjectParameter(JSONObject jsonData, String name, Object value) {
        if (name == null || value == null)
            return;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(value);

            jsonData.put(name, new JSONObject(json));
        } catch (Exception e) {
        }
    }

    public static <T> void addArrayObjectParameter(JSONObject jsonData, String name, List<T> value) {
        if (name == null || value == null)
            return;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(value);

            jsonData.put(name, new JSONArray(json));
        } catch (Exception e) {
        }
    }



    public static <D> void commit(Subscriber subscriber, final Observable<Response<D>> observable) {

        observable.subscribeOn(Schedulers.io())
                .flatMap(new Func1<Response<D>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Response<D> dResponse) {
                        if (Response.isHttpFailed(dResponse)) { //TODO 测试没网下拉的异常情况处理
                            return Observable.error(new ResponseFailedException(dResponse));
                        }
                        return Observable.just(dResponse);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static <D> void commit(Subscriber subscriber, final Observable<Response<D>> observable, Func1 mapFunc1) {
        observable.subscribeOn(Schedulers.io())
                .flatMap(new Func1<Response<D>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Response<D> dResponse) {
                        if (Response.isHttpFailed(dResponse)) {//TODO 测试没网下拉的异常情况处理
                            return Observable.error(new ResponseFailedException(dResponse));
                        }
                        return Observable.just(dResponse);
                    }
                })
                .map(mapFunc1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }



}
