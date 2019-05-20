package code.lee.code.library.listener;

import android.support.annotation.NonNull;

/**
 * @author jv.lee
 * @date 2019/4/13
 */
public interface RequestPermission<T> {
    /**
     * 请求权限组
     * @param target
     * @param permissions
     */
    void requestPermission(T target, String[] permissions);

    /**
     * 授权结果返回
     * @param target
     * @param requestCode
     * @param grantResults
     */
    void onRequestPermissionsResult(T target, int requestCode, @NonNull int[] grantResults);
}
