package com.lee.library.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.lee.library.tools.ViewBindingTools

/**
 * 通过反射实现binding注入 baseActivity
 * @author jv.lee
 * @date 2024/6/6
 */
abstract class BaseBindingActivity<VB : ViewBinding> : BaseActivity() {

    //私有化的Binding类，类型即为实际使用的Binding类型
    private var _binding: VB? = null

    // 通过lazy的方式，避免在创建是初始化发生错误。
    // 因为实际上官方模板的用法，binding需要再onCreate之后初始化 LayoutInflater
    // 那么这里通过lazy的方式，后续子类不再需要手动初始化
    protected val mBinding: VB by lazy { _binding!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ViewBindingTools.inflateWithGeneric(this)
        super.onCreate(savedInstanceState)
    }

}