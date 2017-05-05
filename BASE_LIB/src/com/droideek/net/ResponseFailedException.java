package com.droideek.net;


import com.platform.data.Response;

/**
 * Created by Droideek on 2016/8/11.
 */
public class ResponseFailedException extends Throwable {
    public Response response;

    public <D> ResponseFailedException(Response<D> response){
        this.response = response;
    }

}
