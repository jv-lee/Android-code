package com.jv.qrcode;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;

import com.google.zxing.Result;
import com.jv.qrcode.camera.CameraManager;

public interface CaptureCallback {
    
    Rect getCropRect();
    
    Handler getHandler();

    CameraManager getCameraManager();

    void handleDecode(Result result, Bundle bundle);

    void setResult(int resultCode, Intent intent);

    void finish();
}
