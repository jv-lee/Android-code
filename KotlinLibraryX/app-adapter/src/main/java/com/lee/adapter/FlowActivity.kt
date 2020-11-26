package com.lee.adapter

import androidx.lifecycle.Observer
import com.lee.adapter.databinding.ActivityFlowBinding
import com.lee.adapter.viewmodel.FlowViewModel
import com.lee.library.base.BaseActivity
import com.lee.library.mvvm.load.LoadStatus

class FlowActivity : BaseActivity<ActivityFlowBinding, FlowViewModel>(R.layout.activity_flow) {

    override fun bindView() {

    }

    override fun bindData() {
        viewModel.run {
            dataLiveData.observe(this@FlowActivity, Observer {
                toast(it.toString())
            }, Observer {
                toast(it)
            })

            getData(LoadStatus.INIT)
        }
    }
}