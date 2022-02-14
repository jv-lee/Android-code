package com.lee.api.flow

import androidx.activity.viewModels
import com.lee.api.databinding.ActivityFlowBinding
import com.lee.library.base.BaseActivity
import com.lee.library.extensions.binding
import com.lee.library.extensions.launchAndRepeatWithViewLifecycle
import com.lee.library.mvvm.ui.stateCollect
import com.lee.library.utils.LogUtil

/**
 * @author jv.lee
 * @date 2021/10/25
 * @description
 */
class FlowActivity : BaseActivity() {

    private val binding by binding(ActivityFlowBinding::inflate)
    private val viewModel by viewModels<FlowViewModel>()

    override fun bindView() {
        binding.button.setOnClickListener {
            viewModel.updateType("reload.")
        }
    }

    override fun bindData() {
        launchAndRepeatWithViewLifecycle {
            viewModel.contentFlow.stateCollect<String>(success = {
                LogUtil.i("success -> $it")
            }, error = {
                LogUtil.i("error -> $it")
            }, loading = {
                LogUtil.i("loading state.")
            }, default = {
                LogUtil.i("default state.")
            })
        }
    }
}