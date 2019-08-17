package lee.eventbus.annotation.mode;

/**
 * @author jv.lee
 * @date 2019-08-17
 * @description
 */
public enum ThreadMode {
    /**
     * 订阅、发布在同一线程。避免了线程切换，也是推荐的默认模式
     */
    POSTING,
    /**
     * 主线程（UI线程）中被调用，切勿耗时操作
     */
    MAIN,
    /**
     * 用于网络访问等耗时操作，事件总线已完成的异步订阅通知线程。并使用线程池有效的重用
     */
    ASYNC
}
