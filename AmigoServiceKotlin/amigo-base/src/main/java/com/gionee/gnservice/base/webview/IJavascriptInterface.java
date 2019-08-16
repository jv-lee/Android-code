package com.gionee.gnservice.base.webview;

/**
 * Created by caocong on 4/14/17.
 */

public interface IJavascriptInterface {

    String JS_INTERFACE_NAME = "AndroidClient";

    void setActionBar(String title, boolean displayHomeAsUpEnabled);

    void startAndroidActivity(String json);

    boolean isAndroidActivityExist(String json);

}
