package com.simple.vm

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.lee.library.extensions.arguments
import com.lee.library.extensions.binding
import com.lee.library.extensions.viewModelByFactory
import com.lee.library.utils.LogUtil
import com.simple.vm.constants.USER_ID_KEY
import com.simple.vm.databinding.ActivityViewModelBinding
import com.simple.vm.viewmodel.HandleViewModel
import com.simple.vm.viewmodel.InjectViewModel
import com.simple.vm.viewmodel.ParamsViewModel
import com.simple.vm.viewmodel.SimpleViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * @author jv.lee
 * @date 2021/8/19
 */
@AndroidEntryPoint //使用hilt 为ViewModel注入savedStateHandler
class ViewModelActivity : AppCompatActivity() {

    //通过viewBinding扩展函数构建view
    private val binding by binding(ActivityViewModelBinding::inflate)

    //通过arguments扩展函数 获取intent传递参数
    private val userID by arguments<String>(USER_ID_KEY)

    //获取hilt注入savedStateHandler的ViewModel
    private val injectViewModel by viewModels<InjectViewModel>()

    //获取普通viewModel
    private val simpleViewModel by viewModels<SimpleViewModel>()

    //获取有参构造ViewModel
    private val paramsViewModel by viewModels<ParamsViewModel> {
        ParamsViewModel.CreateFactory(userID)
    }

    //获取intent透传参数ViewModel
    private val handleViewModel by viewModels<HandleViewModel> {
        SavedStateViewModelFactory(application, this, intent.extras)
    }

    //自定义扩展函数 简化模版代码生成 intent透传ViewModel
    private val simpleHandleViewModel by viewModelByFactory<HandleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectViewModel.textLiveData.observe(this) {
            LogUtil.i("activity:$it")
        }
        handleViewModel.textLiveData.observe(this) {
            LogUtil.i("activity:$it")
        }
        simpleHandleViewModel.textLiveData.observe(this) {
            LogUtil.i("activity:$it")
        }

        supportFragmentManager.beginTransaction()
            .add(binding.frameContainer.id, ViewModelFragment.newInstance(userID))
            .commit()
    }


    private fun simple() {
        val injectViewModel = ViewModelProvider(this)[InjectViewModel::class.java]
        val simpleViewModel = ViewModelProvider(this)[SimpleViewModel::class.java]
        val paramsViewModel =
            ViewModelProvider(this, ParamsViewModel.CreateFactory(""))[ParamsViewModel::class.java]
        val handleViewModel = ViewModelProvider(
            this,
            SavedStateViewModelFactory(application, this, intent.extras)
        )[HandleViewModel::class.java]
    }

}