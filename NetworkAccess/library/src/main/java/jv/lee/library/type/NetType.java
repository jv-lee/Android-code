package jv.lee.library.type;

/**
 * @author jv.lee
 * @date 2019/9/23.
 * @description 网络类型
 */
public enum NetType {
    //有网络，无论网络类型
    AUTO,
    //WiFi网络
    WIFI,
    //流量: PC/笔记本/PAD上网 （霍尼韦尔）
    CMNET,
    //流量：移动网络
    CMWAP,
    //无网络
    NONE
}
