package com.netease.core.network;

import com.netease.core.RequestClient;
import com.netease.core.network.callback.IError;
import com.netease.core.network.callback.IFailure;
import com.netease.core.network.callback.IRequest;
import com.netease.core.network.callback.ISuccess;

import java.io.File;
import java.util.HashMap;

import okhttp3.RequestBody;

/**
 * @author jv.lee
 * @date 2019/10/22.
 * @description
 */
public class RequestClientBuilder {
    private HashMap<String, Object> param;
    private String url;
    private IError iError;
    private IFailure iFailure;
    private IRequest iRequest;
    private ISuccess iSuccess;
    private RequestBody requestBody;

    private File file;
    private String downloadDir;
    private String extension;
    private String fileName;

    public RequestClientBuilder param(HashMap<String, Object> param) {
        this.param = param;
        return this;
    }

    public RequestClientBuilder url(String url) {
        this.url = url;
        return this;
    }

    public RequestClientBuilder error(IError iError) {
        this.iError = iError;
        return this;
    }

    public RequestClientBuilder failure(IFailure iFailure) {
        this.iFailure = iFailure;
        return this;
    }

    public RequestClientBuilder success(ISuccess iSuccess) {
        this.iSuccess = iSuccess;
        return this;
    }

    public RequestClientBuilder request(IRequest iRequest) {
        this.iRequest = iRequest;
        return this;
    }

    public RequestClientBuilder requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public RequestClientBuilder requestBody(File file) {
        this.file = file;
        return this;
    }

    public RequestClientBuilder downloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
        return this;
    }

    public RequestClientBuilder extension(String extension) {
        this.extension = extension;
        return this;
    }

    public RequestClientBuilder fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public RequestClient build(){
        return new RequestClient(param, url, iError, iFailure, iRequest, iSuccess, requestBody, file, downloadDir, extension, fileName);
    }

}
