@file:Suppress("UNCHECKED_CAST", "unused")

package com.lee.library.tools

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * ViewBinding绑定工具类
 * 通过反射ViewBinding泛型获取ViewBinding实例，进行activity/fragment 基类初始化viewBinding视图
 * @see com.lee.library.base.BaseBindingActivity
 * @see com.lee.library.base.BaseBindingFragment
 * @see com.lee.library.base.BaseBindingDialogFragment
 * @see com.lee.library.base.BaseBindingSheetFragment
 * @see com.lee.library.base.BaseBindingNavigationFragment
 * @see com.lee.library.adapter.item.ViewBindingItem
 */
object ViewBindingTools {

    /**
     * 通过反射的方式，获取到对应的Binding类，并且初始化Activity binding视图绑定
     * @see [ComponentActivity.onCreate]
     */
    @JvmStatic
    fun <VB : ViewBinding> inflateWithGeneric(activity: ComponentActivity): VB {
        // 通过反射的方法拿到对应视图的binding类名和类
        val actualGenericTypeName = getActualGenericTypeName(activity.javaClass)
        val bindingClass = activity.classLoader.loadClass(actualGenericTypeName)
        val binding = bindingClass
            .getMethod("inflate", LayoutInflater::class.java)
            .invoke(null, activity.layoutInflater) as VB
        val invoke = bindingClass.getMethod("getRoot").invoke(binding) as View
        activity.setContentView(invoke)
        return binding
    }

    /**
     * 通过反射的方式，获取到对应的Binding类，binding视图绑定
     * @see [Fragment.onCreateView]
     * @see Adapter.itemView inflate binding
     */
    @JvmStatic
    fun <VB : ViewBinding> inflateWithGeneric(genericOwner: Any, layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): VB =
        withGenericBindingClass<VB>(genericOwner) { clazz ->
            clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
                .invoke(null, layoutInflater, parent, attachToParent) as VB
        }.withLifecycleOwner(genericOwner)

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

    private fun <VB : ViewBinding> VB.withLifecycleOwner(genericOwner: Any) = apply {
        if (this is ViewDataBinding && genericOwner is ComponentActivity) {
            lifecycleOwner = genericOwner
        } else if (this is ViewDataBinding && genericOwner is Fragment) {
            lifecycleOwner = genericOwner.viewLifecycleOwner
        }
    }

    private fun <VB : ViewBinding> withGenericBindingClass(genericOwner: Any, block: (Class<VB>) -> VB): VB {
        var genericSuperclass = genericOwner.javaClass.genericSuperclass
        var superclass = genericOwner.javaClass.superclass
        while (superclass != null) {
            if (genericSuperclass is ParameterizedType) {
                genericSuperclass.actualTypeArguments.forEach {
                    try {
                        return block.invoke(it as Class<VB>)
                    } catch (e: NoSuchMethodException) {
                    } catch (e: ClassCastException) {
                    } catch (e: InvocationTargetException) {
                        var tagException: Throwable? = e
                        while (tagException is InvocationTargetException) {
                            tagException = e.cause
                        }
                        throw tagException ?: IllegalArgumentException("ViewBinding generic was found, but creation failed.")
                    }
                }
            }
            genericSuperclass = superclass.genericSuperclass
            superclass = superclass.superclass
        }
        throw IllegalArgumentException("There is no generic of ViewBinding.")
    }

}
