package com.platform.data;

import java.io.Serializable;


/**
 * Created by Droideek on 2016/6/28.
 */

//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Response<T> implements Serializable{

    public String code;

    public String msg;

    public T data;

    public boolean isSuccess() {
        return "0".equals(code);
    }

    public Response(){}

    public Response(T data) {
        this.data = data;
    }

    public static boolean isHttpFailed(Response response) {
        return null == response || !response.isSuccess();
    }

    public static boolean isSuccessWithData(Response response) {
        return ((null != response) && (response.isSuccess()) && (null != response.data));
    }


}
