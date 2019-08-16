package com.gionee.gnservice.common.http;

public interface IHttpResponse {
    byte[] getBytes() throws Exception;

    String getString();

}
