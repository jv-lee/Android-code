package com.lee.component.compiler.utils;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description
 */
public class Constants {
    /**
     * 注解处理器支持的注解类型
     */
    public static final String AROUTER_ANNOTATION_TYPES = "com.lee.component.annotation.ARouter";

    /**
     * 每个子模块的模块名
     */
    public static final String MODULE_NAME = "moduleName";

    /**
     * 用于存放APT生成的类文件
     */
    public static final String APT_PACKAGE = "packageNameForAPT";

    /**
     * 字符串String全类名
     */
    public static final String STRING = "java.lang.String";

    /**
     * Android Activity全类名
     */
    public static final String ACTIVITY = "android.app.Activity";

    /**
     * 包名前缀封装
     */
    public static final String BASE_PACKAGE = "com.lee.component.api";

    /**
     * 路由组Group加载接口
     */
    public static final String AROUTER_GROUP = BASE_PACKAGE + ".core.ARouterLoadGroup";

    /**
     * 路由组Group对应的详细Path加载接口
     */
    public static final String AROUTER_PATH = BASE_PACKAGE + ".core.ARouterLoadPath";

    /**
     * 路由组Group对应的详细Path，方法名
     */
    public static final String PATH_METHOD_NAME = "loadPath";

    /**
     * 路由Group对应的详细Path，参数名
     */
    public static final String PATH_PARAMETER_NAME = "pathMap";

    /**
     * APT生成的路由组Group对应的详细Path类文件名
     */
    public static final String PATH_FILE_NAME = "ARouter$$Path$$";

}
