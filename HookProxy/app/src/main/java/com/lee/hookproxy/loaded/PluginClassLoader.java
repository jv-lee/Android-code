package com.lee.hookproxy.loaded;

import dalvik.system.DexClassLoader;

/**
 * @author jv.lee
 * @date 2019-08-11
 * @description 专门加载插件中的class使用的类加载器
 */
public class PluginClassLoader extends DexClassLoader {

    public PluginClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
    }

}
