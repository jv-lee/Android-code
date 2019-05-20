package code.lee.code.library.listener;

/**
 * @author jv.lee
 * @date 2019/4/13
 */
public interface PermissionRequest {
    /**
     * 继续请求接口，用户拒绝一次后，给出Dialog提示后
     */
    void proceed();
}
