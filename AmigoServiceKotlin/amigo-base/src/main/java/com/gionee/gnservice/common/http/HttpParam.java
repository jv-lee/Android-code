package com.gionee.gnservice.common.http;

import com.gionee.gnservice.utils.PreconditionsUtil;

import java.util.Map;

/**
 * Created by caocong on 12/26/16.
 */
public class HttpParam {
    protected String mUrl;
    protected Object mTag;
    protected Map<String, String> mHeaders;
    protected Map<String, String> mParams;
    protected int mId;
    //post的params是否需要是json格式
    protected boolean isPostParamJson;

    public HttpParam(Builder builder) {
        this.mUrl = PreconditionsUtil.checkNotNull(builder.mUrl, "url can not be null");
        this.mTag = builder.mTag;
        this.mHeaders = builder.mHeaders;
        this.mParams = builder.mParams;
        this.mId = builder.mId;
        this.isPostParamJson = builder.isPostParamJson;
    }

    public String getUrl() {
        return mUrl;
    }

    public Object getTag() {
        return mTag;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    public int getId() {
        return mId;
    }

    public boolean isPostParamJson() {
        return isPostParamJson;
    }

    public static class Builder {
        String mUrl;
        Object mTag;
        Map<String, String> mHeaders;
        Map<String, String> mParams;
        int mId;
        boolean isPostParamJson = true;

        public Builder() {

        }

        public Builder setUrl(String mUrl) {
            PreconditionsUtil.checkNotNull(mUrl);
            this.mUrl = mUrl;
            return this;
        }

        public Builder setTag(Object mTag) {
            this.mTag = mTag;
            return this;
        }

        public Builder setHeaders(Map<String, String> mHeaders) {
            this.mHeaders = mHeaders;
            return this;
        }

        public Builder setParams(Map<String, String> mParams) {
            this.mParams = mParams;
            return this;
        }

        public Builder setId(int mId) {
            this.mId = mId;
            return this;
        }

        public void setIsPostParmasJson(boolean isPostParamJson) {
            this.isPostParamJson = isPostParamJson;
        }

        public HttpParam build() {
            return new HttpParam(this);
        }
    }

    @Override
    public String toString() {
        return "HttpParam{" +
                "mUrl='" + mUrl + '\'' +
                ", mTag=" + mTag +
                ", mHeaders=" + mHeaders +
                ", mParams=" + mParams +
                ", mId=" + mId +
                '}';
    }
}
