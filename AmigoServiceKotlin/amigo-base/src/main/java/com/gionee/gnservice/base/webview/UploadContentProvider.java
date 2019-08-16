package com.gionee.gnservice.base.webview;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.gionee.gnservice.utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;

public class UploadContentProvider extends ContentProvider {

    private static final String TAG = "UploadContentProvider";

    public static final String URI_PREFIX =
            "content://com.gionee.gnservice.base.webview.UploadContentProvider";

    @Override
    public AssetFileDescriptor openAssetFile(Uri uri, String mode)
            throws FileNotFoundException {
        String path = uri.getPath();
        File file = new File(path);
        LogUtil.d(TAG, "file path: " + path);
        if (file.exists()) {
            LogUtil.d(TAG, "file path exist: " + path);
        }
        ParcelFileDescriptor parcel = ParcelFileDescriptor.open(file,
                ParcelFileDescriptor.MODE_READ_WRITE);
        return new AssetFileDescriptor(parcel, 0, -1);
    }

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

}
