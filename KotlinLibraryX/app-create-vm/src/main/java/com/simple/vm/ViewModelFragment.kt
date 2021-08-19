package com.simple.vm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import com.lee.library.extensions.arguments
import com.lee.library.extensions.binding
import com.lee.library.extensions.viewModelByFactory
import com.lee.library.utils.LogUtil
import com.simple.vm.constants.USER_ID_KEY
import com.simple.vm.databinding.FragmentViewModelBinding
import com.simple.vm.viewmodel.HandleViewModel
import com.simple.vm.viewmodel.ParamsViewModel
import com.simple.vm.viewmodel.SimpleViewModel

/**
 * @author jv.lee
 * @data 2021/8/19
 * @description
 */
class ViewModelFragment : Fragment(R.layout.fragment_view_model) {

    companion object {
        fun newInstance(userID: String): Fragment {
            return ViewModelFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID_KEY, userID)
                }
            }
        }
    }

    //通过viewBinding扩展函数构建view
    private val binding by binding(FragmentViewModelBinding::bind)

    //通过arguments扩展函数 获取intent传递参数
    private val userID by arguments<String>(USER_ID_KEY)

    //获取普通viewModel
    private val simpleViewModel by viewModels<SimpleViewModel>()

    //获取有参构造ViewModel
    private val paramsViewModel by viewModels<ParamsViewModel> {
        ParamsViewModel.CreateFactory(userID)
    }

    //获取intent透传参数ViewModel
    private val handleViewModel by viewModels<HandleViewModel> {
        SavedStateViewModelFactory(requireActivity().application, this, arguments)
    }

    //自定义扩展函数 简化模版代码生成 intent透传ViewModel
    private val simpleHandleViewModel by viewModelByFactory<HandleViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewModel.textLiveData.observe(viewLifecycleOwner, {
            LogUtil.i("fragment:$it")
        })
        simpleHandleViewModel.textLiveData.observe(viewLifecycleOwner, {
            LogUtil.i("fragment:$it")
        })

        binding.tvText.text = userID
    }
}