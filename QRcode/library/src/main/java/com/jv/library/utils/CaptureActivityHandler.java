package com.jv.library.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.zxing.Result;
import com.jv.library.CaptureCallback;
import com.jv.library.camera.CameraManager;
import com.jv.library.decode.DecodeThread;

/**
 * This class handles all the messaging which comprises the state machine for
 * capture.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public class CaptureActivityHandler extends Handler {

    private final CaptureCallback activity;
    private final DecodeThread decodeThread;
    private final CameraManager cameraManager;
    private State state;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public CaptureActivityHandler(CaptureCallback activity, CameraManager cameraManager, int decodeMode) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity, decodeMode);
        decodeThread.start();
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == Constants.RESTART_PREVIEW) {
            restartPreviewAndDecode();
        } else if (message.what == Constants.DECODE_SUCCEEDED) {
            state = State.SUCCESS;
            Bundle bundle = message.getData();
            activity.handleDecode((Result) message.obj, bundle);
        } else if (message.what == Constants.DECODE_FAILED) {
            // We're decoding as fast as possible, so when one DECODE fails,
            // start another.
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), Constants.DECODE);
        } else if (message.what == Constants.RETURN_SCAN_RESULT) {
            activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
            activity.finish();
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), Constants.QUIT);
        quit.sendToTarget();
        try {
            // Wait at most half a second; should be enough time, and onPause()
            // will timeout quickly
            decodeThread.join(500L);
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(Constants.DECODE_SUCCEEDED);
        removeMessages(Constants.DECODE_FAILED);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), Constants.DECODE);
        }
    }

}
