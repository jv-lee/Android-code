package com.gionee.gnservice.base.webview;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.ResourceUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handle the file upload callbacks from WebView here
 */
public class UploadHandler {

    private static final String TAG = "UploadHandler";

    private static final String imageMimeType = "image/*";
    private static final String videoMimeType = "video/*";
    private static final String audioMimeType = "audio/*";
    private static final String mediaSourceKey = "capture";
    private static final String mediaSourceValueCamera = "camera";
    private static final String mediaSourceValueFileSystem = "filesystem";
    private static final String mediaSourceValueCamcorder = "camcorder";
    private static final String mediaSourceValueMicrophone = "microphone";
    public static final int FILE_SELECTED = 202;
    public static final int DOWNLOAD_TRACE_SELECT = 203;
    public static final int FROM_RESET_DEFAULT_SETTING = 204;
    public static final int FILE_SELECTED_ANDROIDL = 205;

    /*
     * The Object used to inform the WebView of the file to upload.
     */
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<String[]> mUploadFilePaths;
    private ValueCallback<Uri[]> mFilePathCallback;

    private String mCameraFilePath;
    private Uri mCameraImgUri;

    private boolean mHandled;
    private boolean mCaughtActivityNotFoundException;

    private Context mController;

    public UploadHandler(Context Context) {
        mController = Context;
    }

    String getFilePath() {
        return mCameraFilePath;
    }

    boolean handled() {
        return mHandled;
    }

    void onResult(int resultCode, Intent intent) {

        if (resultCode == Activity.RESULT_CANCELED && mCaughtActivityNotFoundException) {
            // Couldn't resolve an activity, we are going to try again so skip
            // this result.
            mCaughtActivityNotFoundException = false;
            return;
        }

        Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();

        // As we ask the camera to save the result of the user taking
        // a picture, the camera application does not return anything other
        // than RESULT_OK. So we need to check whether the file we expected
        // was written to disk in the in the case that we
        // did not get an intent returned but did get a RESULT_OK. If it was,
        // we assume that this result has came back from the camera.
        if (result == null && intent == null && resultCode == Activity.RESULT_OK) {
            File cameraFile = new File(mCameraFilePath);
            if (cameraFile.exists()) {
                result = Uri.fromFile(cameraFile);
                // Broadcast to the media scanner that we have a new photo
                // so it will be added into the gallery for the user.
                mController.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
            }
        }

        // try to get local file path from uri
        boolean hasGoodFilePath = false;
        String filePath = null;
        if (result != null) {
            String scheme = result.getScheme();
            if ("file".equals(scheme)) {
                filePath = result.getPath();
                hasGoodFilePath = filePath != null && !filePath.isEmpty();
            } else if ("content".equals(scheme)) {
                ContentResolver cr = mController.getContentResolver();
                String[] projection = {"_data"};
                Cursor c = cr.query(result, projection, null, null, null);
                try {
                    if (c != null && c.moveToFirst()) {
                        filePath = c.getString(0);
                        hasGoodFilePath = filePath != null && !filePath.isEmpty();
                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }

        // Add for carrier feature - prevent uploading DRM type files.
        String browserRes = mController.getResources().getString(ResourceUtil.getStringId(mController, "uc_webview_upload_handle_config_carrier_resource"));
        boolean isDRMFileType = false;
        if ("ct".equals(browserRes)
                && filePath != null
                && (filePath.endsWith(".fl") || filePath.endsWith(".dm") || filePath.endsWith(".dcf")
                || filePath.endsWith(".dr") || filePath.endsWith(".dd"))) {
            isDRMFileType = true;
            Toast.makeText(mController, ResourceUtil.getStringId(mController, "uc_webview_upload_handle_drm_file_unsupported"), Toast.LENGTH_LONG).show();
        }

        if (mUploadMessage != null) {
            if (!isDRMFileType) {
                mUploadMessage.onReceiveValue(result);
            } else {
                mUploadMessage.onReceiveValue(null);
            }
        }

        if (mUploadFilePaths != null) {
            if (hasGoodFilePath && !isDRMFileType) {
                Log.d(TAG, "upload file path:" + filePath);
                mUploadFilePaths.onReceiveValue(new String[]{filePath});
            } else {
                mUploadFilePaths.onReceiveValue(null);
            }
        }

        mHandled = true;
        mCaughtActivityNotFoundException = false;
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

        // According to the spec, media source can be 'filesystem' or 'camera'
        // or 'camcorder'
        // or 'microphone' and the default value should be 'filesystem'.
        String mediaSource = mediaSourceValueFileSystem;

        if (mUploadMessage != null) {
            // Already a file picker operation in progress.
            return;
        }

        mUploadMessage = uploadMsg;

        // Parse the accept type.
        String params[] = acceptType.split(";");
        String mimeType = params[0];

        if (capture.length() > 0) {
            mediaSource = capture;
        }

        if (capture.equals(mediaSourceValueFileSystem)) {
            // To maintain backwards compatibility with the previous
            // implementation
            // of the media capture API, if the value of the 'capture' attribute
            // is
            // "filesystem", we should examine the accept-type for a MIME type
            // that
            // may specify a different capture value.
            for (String p : params) {
                String[] keyValue = p.split("=");
                if (keyValue.length == 2) {
                    // Process key=value parameters.
                    if (mediaSourceKey.equals(keyValue[0])) {
                        mediaSource = keyValue[1];
                    }
                }
            }
        }

        // Ensure it is not still set from a previous upload.
        mCameraFilePath = null;

        if (mimeType.equals(imageMimeType)) {
            if (mediaSource.equals(mediaSourceValueCamera)) {
                // Specified 'image/*' and requested the camera, so go ahead and
                // launch the
                // camera directly.
                startActivity(createCameraIntent());
                return;
            } else {
                // Specified just 'image/*', capture=filesystem, or an invalid
                // capture parameter.
                // In all these cases we show a traditional picker filetered on
                // accept type
                // so launch an intent for both the Camera and image/* OPENABLE.
                Intent chooser = createChooserIntent(createCameraIntent());
                chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(imageMimeType));
                startActivity(chooser);
                return;
            }
        } else if (mimeType.equals(videoMimeType)) {
            if (mediaSource.equals(mediaSourceValueCamcorder)) {
                // Specified 'video/*' and requested the camcorder, so go ahead
                // and launch the
                // camcorder directly.
                startActivity(createCamcorderIntent());
                return;
            } else {
                // Specified just 'video/*', capture=filesystem or an invalid
                // capture parameter.
                // In all these cases we show an intent for the traditional file
                // picker, filtered
                // on accept type so launch an intent for both camcorder and
                // video/* OPENABLE.
                Intent chooser = createChooserIntent(createCamcorderIntent());
                chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(videoMimeType));
                startActivity(chooser);
                return;
            }
        } else if (mimeType.equals(audioMimeType)) {
            if (mediaSource.equals(mediaSourceValueMicrophone)) {
                // Specified 'audio/*' and requested microphone, so go ahead and
                // launch the sound
                // recorder.
                startActivity(createSoundRecorderIntent());
                return;
            } else {
                // Specified just 'audio/*', capture=filesystem of an invalid
                // capture parameter.
                // In all these cases so go ahead and launch an intent for both
                // the sound
                // recorder and audio/* OPENABLE.
                Intent chooser = createChooserIntent(createSoundRecorderIntent());
                chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(audioMimeType));
                startActivity(chooser);
                return;
            }
        }

        // No special handling based on the accept type was necessary, so
        // trigger the default
        // file upload chooser.
        startActivity(createDefaultOpenableIntent());
    }

    public void showFileChooser(ValueCallback<String[]> uploadFilePaths, String acceptTypes, boolean capture) {

        final String imageMimeType = "image/*";
        final String videoMimeType = "video/*";
        final String audioMimeType = "audio/*";

        if (mUploadFilePaths != null) {
            // Already a file picker operation in progress.
            return;
        }

        mUploadFilePaths = uploadFilePaths;

        // Parse the accept type.
        String params[] = acceptTypes.split(";");
        String mimeType = params[0];

        // Ensure it is not still set from a previous upload.
        mCameraFilePath = null;

        if (mimeType.equals(imageMimeType)) {
            if (capture) {
                // Specified 'image/*' and capture=true, so go ahead and launch
                // the
                // camera directly.
                startActivity(createCameraIntent());
                return;
            } else {
                // Specified just 'image/*', capture=false, or no capture value.
                // In all these cases we show a traditional picker filetered on
                // accept type
                // so launch an intent for both the Camera and image/* OPENABLE.
                Intent chooser = createChooserIntent(createCameraIntent());
                chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(imageMimeType));
                startActivity(chooser);
                return;
            }
        } else if (mimeType.equals(videoMimeType)) {
            if (capture) {
                // Specified 'video/*' and capture=true, so go ahead and launch
                // the
                // camcorder directly.
                startActivity(createCamcorderIntent());
                return;
            } else {
                // Specified just 'video/*', capture=false, or no capture value.
                // In all these cases we show an intent for the traditional file
                // picker, filtered
                // on accept type so launch an intent for both camcorder and
                // video/* OPENABLE.
                Intent chooser = createChooserIntent(createCamcorderIntent());
                chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(videoMimeType));
                startActivity(chooser);
                return;
            }
        } else if (mimeType.equals(audioMimeType)) {
            if (capture) {
                // Specified 'audio/*' and capture=true, so go ahead and launch
                // the sound
                // recorder.
                startActivity(createSoundRecorderIntent());
                return;
            } else {
                // Specified just 'audio/*', capture=false, or no capture value.
                // In all these cases so go ahead and launch an intent for both
                // the sound
                // recorder and audio/* OPENABLE.
                Intent chooser = createChooserIntent(createSoundRecorderIntent());
                chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(audioMimeType));
                startActivity(chooser);
                return;
            }
        }

        // No special handling based on the accept type was necessary, so
        // trigger the default
        // file upload chooser.
        startActivity(createDefaultOpenableIntent());
    }

    private void startActivity(Intent intent) {
        try {
            ((Activity) mController).startActivityForResult(intent, FILE_SELECTED);
        } catch (ActivityNotFoundException e) {
            // No installed app was able to handle the intent that
            // we sent, so fallback to the default file upload control.
            try {
                mCaughtActivityNotFoundException = true;
                ((Activity) mController).startActivityForResult(createDefaultOpenableIntent(), FILE_SELECTED);
            } catch (ActivityNotFoundException e2) {
                // Nothing can return us a file, so file upload is effectively
                // disabled.
                Toast.makeText(mController, ResourceUtil.getStringId(mController, "uc_webview_upload_handle_uploads_disabled"), Toast.LENGTH_LONG).show();
            }
        }
    }

    private Intent createDefaultOpenableIntent() {
        // Create and return a chooser with the default OPENABLE
        // actions including the camera, camcorder and sound
        // recorder where available.
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");

        Intent chooser = createChooserIntent(createCameraIntent(), createCamcorderIntent(),
                createSoundRecorderIntent());
        chooser.putExtra(Intent.EXTRA_INTENT, i);
        return chooser;
    }

    private Intent createChooserIntent(Intent... intents) {
        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
        chooser.putExtra(Intent.EXTRA_TITLE, mController.getResources().getString(ResourceUtil.getStringId(mController, "uc_webview_upload_handle_choose_upload")));
        return chooser;
    }

    private Intent createOpenableIntent(String type) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType(type);
        return i;
    }

    private Intent createCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File externalDataDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File cameraDataDir = new File(externalDataDir.getAbsolutePath() + File.separator + "browser-photos");
        if (cameraDataDir.mkdirs()) {
            Log.d(TAG, "createCameraIntent cameraDataDir.mkdirs()");
        }
        mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + System.currentTimeMillis()
                + ".jpg";
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
        return cameraIntent;
    }

    private Intent createCamcorderIntent() {
        return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    }

    private Intent createSoundRecorderIntent() {
        return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
    }

    /*********************************
     * android L
     **********************************/

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                     WebChromeClient.FileChooserParams fileChooserParams) {

        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
        }
        mFilePathCallback = filePathCallback;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mController.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCameraFilePath);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "Unable to create Image File", ex);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraFilePath = "file:" + photoFile.getAbsolutePath();
                LogUtil.d("save picture path" + photoFile.getAbsolutePath());
                mCameraImgUri = Uri.fromFile(photoFile);
                LogUtil.d("image url is: " + mCameraImgUri);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImgUri);
            } else {
                takePictureIntent = null;
            }
        }

        String mimeType = "*/*";
        String[] acceptTypes = fileChooserParams.getAcceptTypes();
        if (acceptTypes != null && acceptTypes.length > 0) {
            mimeType = acceptTypes[0];
        }

        mimeType = getMimeType(mimeType);

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType(mimeType);

        Intent[] intentArray;
        if (takePictureIntent != null) {
            intentArray = new Intent[]{takePictureIntent};
        } else {
            intentArray = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE,
                mController.getResources().getString(ResourceUtil.getStringId(mController, "uc_webview_upload_handle_choose_upload")));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        ((Activity) mController).startActivityForResult(chooserIntent, FILE_SELECTED_ANDROIDL);

        return true;

    }

    private String getMimeType(final String mimeType) {
        if (TextUtils.isEmpty(mimeType)) {
            return "*/*";
        }

        if (mimeType.contains("jpeg")) {
            return imageMimeType;
        }

        return mimeType;
    }

    /**
     * More info this method can be found at http://developer.android.com/training/camera/photobasics.html
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        return imageFile;
    }

    public void onResultForAndroidL(int resultCode, Intent intent) {

        Uri[] results = null;

        // Check that the response is a good one
        if (resultCode == Activity.RESULT_OK) {
            if (intent == null) {
                // If there is not data, then we may have taken a photo
                LogUtil.d("img url path is：" + mCameraImgUri);
                if (mCameraImgUri != null) {
                    mController.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, mCameraImgUri));
                    //results = new Uri[]{Uri.parse(UploadContentProvider.URI_PREFIX + mCameraImgUri.getPath())};
                    results = new Uri[]{mCameraImgUri};
                }
            } else {
                String dataString = intent.getDataString();
                LogUtil.d("onResultForAndroidL dataString = " + dataString);
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }

        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        }
    }

    ;

}
