package com.gionee.gnservice.common.http;

/**
 * Created by caocong on 12/28/16.
 */
public interface IHttpHelper {
    IHttpResponse get(HttpParam param) throws Exception;

    IHttpResponse post(HttpParam param) throws Exception;
}
