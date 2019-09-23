package jv.lee.library.listener;

import jv.lee.library.type.NetType;

/**
 * @author jv.lee
 * @date 2019/9/23.
 * @description
 */
public interface NetChangeObserver {

    /**
     * 成功,当前网络类型
     */
    void onConnect(NetType type);

    /**
     * 失败
     */
    void onDisConnect();
}
