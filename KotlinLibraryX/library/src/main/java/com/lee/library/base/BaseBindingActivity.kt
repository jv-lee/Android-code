package com.lee.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


abstract class BaseBindingActivity<BindingClass : ViewBinding> : BaseActivity() {

    //私有化的Binding类，类型即为实际使用的Binding类型
    private var innerViewBinding: BindingClass? = null

    // 通过lazy的方式，避免在创建是初始化发生错误。因为实际上官方模板的用法，binding需要再onCreate之后初始化 LayoutInflater
    // 那么这里通过lazy的方式，后续子类不再需要手动初始化
    protected val mBinding: BindingClass by lazy { innerViewBinding!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView()
        super.onCreate(savedInstanceState)
    }


    /**
     * 通过反射的方式，获取到对应的Binding类，并且初始化
     */
    private fun setContentView() {
        // 通过反射的方法拿到对应视图的binding类名和类
        val actualGenericTypeName = getActualGenericTypeName(this.javaClass)
        val bindingClass = classLoader.loadClass(actualGenericTypeName)
        innerViewBinding = bindingClass
            .getMethod("inflate", LayoutInflater::class.java)
            .invoke(null, layoutInflater) as BindingClass
        val invoke = bindingClass.getMethod("getRoot").invoke(mBinding) as View
        setContentView(invoke)
    }

    /**
     * 通过反射的方式获取泛型的实际类型
     */
    private fun getActualGenericTypeName(clazz: Class<*>): String {
        val genericSuperclass = clazz.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val actualTypeArguments = genericSuperclass.actualTypeArguments
            if (actualTypeArguments.isNotEmpty()) {
                val actualTypeArgument = actualTypeArguments[0]
                return actualTypeArgument.typeName
            }
        }
        return ""
    }

}