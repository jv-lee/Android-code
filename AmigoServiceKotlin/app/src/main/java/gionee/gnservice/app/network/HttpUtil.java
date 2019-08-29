package gionee.gnservice.app.network;

import android.util.Log;
import com.gionee.gnservice.BuildConfig;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {

    private static final String TAG = "HttpUtil";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final int MAX_RETRY_COUNT = 3;

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(4, TimeUnit.SECONDS)
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) {
                    Request request = chain.request();

                    // try the request
                    Response response = null;

                    int tryCount = 0;
                    while (tryCount < MAX_RETRY_COUNT) {
                        try {

                            tryCount++;
                            if (BuildConfig.DEBUG) {
                                Log.i(TAG, "intercept: " + "req count:" + tryCount + ", " + request.url());
                            }

                            // retry the request
                            response = chain.proceed(request);
                            if (response.isSuccessful()) {
                                tryCount = MAX_RETRY_COUNT;
                            }

                        } catch (Exception e) {
                            Log.e(TAG, "intercept: ", e);
                        }
                    }

                    // otherwise just pass the original response on
                    return response;
                }
            })
            .build();

    private static final String SCHEME_HTTP = "http";

    String host;
    AsyncRespHandlerCallback successCallback, failureCallback, finishCallback;
    boolean isBuildErrorMessage = false;
    HttpUrl.Builder urlBuilder;
    Headers.Builder headerBuilder;

    String charset;


    private HttpUtil(Builder builder) {
        host = builder.host;

        urlBuilder = builder.urlBuilder;
        headerBuilder = builder.headerBuilder;

        successCallback = builder.successCallback;
        failureCallback = builder.failureCallback;
        finishCallback = builder.finishCallback;
        isBuildErrorMessage = builder.isBuildErrorMessage;

        charset = builder.charset;
    }


    public void post() {

        urlBuilder.host(host);
        RequestBody body = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .headers(headerBuilder.build())
                .post(body)
                .build();
        doRequest(request, true);
    }


    public void get() {
        doRequest(buildGetRequest(), true);
    }

    public String syncGet() {
        return getRespNoThrow(buildGetRequest());
    }


    private String getRespNoThrow(Request request) {
        try {
            return getResp(request);
        } catch (Exception e) {
            Log.e(TAG, "getRespNoThrow: ", e);
        }
        return null;
    }

    private String getResp(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        String s;
        if (charset != null) {
            byte[] b = response.body().bytes();
            s = new String(b, charset);
        } else {
            s = response.body().string();
        }

        if (BuildConfig.DEBUG) {
            String info = "header: " + headerBuilder.build().toString() + ", " + urlBuilder.build() + " success resp: " + s;
            Log.i(TAG, "getResp: " + info);
        }
        return s;
    }

    public Observable<String> getReqObservable() {
        return reqObservable(buildGetRequest());
    }

    private Request buildGetRequest() {
        urlBuilder.host(host);
        return new Request.Builder()
                .url(urlBuilder.build())
                .headers(headerBuilder.build())
                .build();
    }

    private Observable<String> reqObservable(final Request request) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> subscriber) {
                try {

                    Response response = client.newCall(request).execute();
                    String s;
                    if (charset != null) {
                        byte[] b = response.body().bytes();
                        s = new String(b, charset);
                    } else {
                        s = response.body().string();
                    }
                    subscriber.onNext(s);

                    if (BuildConfig.DEBUG) {
                        String info = "host: " + host + ", " + urlBuilder.build() + " success resp: " + s;
                        Log.i(TAG, "subscribe: " + info);
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                }
                subscriber.onComplete();
            }
        });
    }

    private void doRequest(Request request, final boolean isRetry) {
        reqObservable(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {
                        if (finishCallback != null) {
                            finishCallback.call();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "onError: ", e);
                            e.printStackTrace();
                        }
                        if (failureCallback != null) {
                            failureCallback.call("url:" + urlBuilder.build(), constructErrorMessage(e));
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        if (successCallback != null) {
                            successCallback.call(s);
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });
    }

    protected String constructErrorMessage(Throwable e) {
        if (isBuildErrorMessage) {
            StringBuilder sb = new StringBuilder();
            sb.append(e.getClass().getName()).append(":").append(e.getMessage());
            Throwable cause = e.getCause();
            int count = 0;
            while (cause != null && count < 3) {
                sb.append("---").append(cause.getClass().getName()).append(":").append(cause.getMessage());
                cause = cause.getCause();
                count++;
            }
            return sb.toString();
        }
        return "";
    }

    private String getHostInfo() {
        HttpUrl url = urlBuilder.build();
        return "header host: " + host + ", url: " + url.host() + url.encodedPath();
    }

    public static class Builder {
        String host;
        AsyncRespHandlerCallback successCallback;
        AsyncRespHandlerCallback failureCallback;
        AsyncRespHandlerCallback finishCallback;
        boolean isBuildErrorMessage = false;
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder().scheme(SCHEME_HTTP);
        Headers.Builder headerBuilder = new Headers.Builder();
        String charset;

        public Builder(String api, String host) {
            this.host = host;

            urlBuilder.addEncodedPathSegments(api);
        }

        public Builder(String url) {
            HttpUrl httpUrl = HttpUrl.parse(url);
            this.host = httpUrl.host();
            urlBuilder = httpUrl.newBuilder();
        }

        public Builder port(int port) {
            urlBuilder.port(port);
            return this;
        }

        public Builder successCallback(AsyncRespHandlerCallback callback) {
            successCallback = callback;
            return this;
        }

        public Builder failureCallback(AsyncRespHandlerCallback callback) {
            failureCallback = callback;
            return this;
        }

        public Builder failureCallbackWithError(AsyncRespHandlerCallback callback) {
            failureCallback = callback;
            isBuildErrorMessage = true;
            return this;
        }

        public Builder finishCallback(AsyncRespHandlerCallback callback) {
            finishCallback = callback;
            return this;
        }

        public Builder addParams(String key, String value) {
            urlBuilder.addQueryParameter(key, value);
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                headerBuilder.set(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public Builder charset(String charset) {
            this.charset = charset;
            return this;
        }

        public HttpUtil build() {
            return new HttpUtil(this);
        }
    }

    public static OkHttpClient getClient() {
        return client;
    }

}
