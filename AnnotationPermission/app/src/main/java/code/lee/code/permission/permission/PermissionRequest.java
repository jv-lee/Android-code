package code.lee.code.permission.permission;

/**
 * @author jv.lee
 * @date 2019/4/14
 */
public interface PermissionRequest {

    /**
     * 请求权限成功
     */
    void onSuccess();

    /**
     * 请求权限失败
     * @param permission 权限字符
     */
    void onFiled(String permission);

}
