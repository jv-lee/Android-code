package com.lee.adapter

import androidx.lifecycle.Observer
import com.lee.adapter.databinding.ActivityFlowBinding
import com.lee.adapter.viewmodel.FlowViewModel
import com.lee.library.base.BaseVMActivity
import com.lee.library.extensions.toast
import com.lee.library.mvvm.load.LoadStatus
import com.lee.library.utils.LogUtil

class FlowActivity : BaseVMActivity<ActivityFlowBinding, FlowViewModel>(R.layout.activity_flow) {

    override fun bindView() {

    }

    override fun bindData() {
        viewModel.run {
            dataLiveData.observe(this@FlowActivity, {
                toast(it.toString())
            }, {
                toast(it)
            })

//            getData(LoadStatus.INIT)
//            getCacheOrNetworkData1()

            pageLiveData.observe(this@FlowActivity, {
                LogUtil.i(it)
            }, {
                LogUtil.e(it)
            })
            getFlowData(LoadStatus.INIT)
            getFlowData(LoadStatus.LOAD_MORE)
        }
    }
}