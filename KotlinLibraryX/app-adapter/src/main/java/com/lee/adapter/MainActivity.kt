package com.lee.adapter

import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.adapter.adapter.ContentAdapter
import com.lee.adapter.databinding.ActivityMainBinding
import com.lee.adapter.databinding.LayoutFooterOneBinding
import com.lee.adapter.databinding.LayoutFooterTwoBinding
import com.lee.adapter.databinding.LayoutHeaderOneBinding
import com.lee.adapter.databinding.LayoutHeaderTwoBinding
import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.PageData
import com.lee.adapter.viewmodel.ContentViewModel
import com.lee.library.adapter.base.AdapterStatus
import com.lee.library.adapter.base.BaseViewAdapter
import com.lee.library.adapter.listener.LoadStatusListener
import com.lee.library.adapter.extensions.submitData
import com.lee.library.adapter.extensions.submitFailed
import com.lee.library.base.BaseBindingActivity
import com.lee.library.extensions.toast
import com.lee.library.uistate.LoadStatus
import com.lee.library.uistate.observeState

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    private val viewModel by viewModels<ContentViewModel>()

    private val headerOne by lazy {
        DataBindingUtil.inflate<LayoutHeaderOneBinding>(
            layoutInflater,
            R.layout.layout_header_one,
            mBinding.rvContainer,
            false
        )
    }
    private val headerTwo by lazy {
        DataBindingUtil.inflate<LayoutHeaderTwoBinding>(
            layoutInflater,
            R.layout.layout_header_two,
            mBinding.rvContainer,
            false
        )
    }
    private val footerOne by lazy {
        DataBindingUtil.inflate<LayoutFooterOneBinding>(
            layoutInflater,
            R.layout.layout_footer_one,
            mBinding.rvContainer,
            false
        )
    }
    private val footerTwo by lazy {
        DataBindingUtil.inflate<LayoutFooterTwoBinding>(
            layoutInflater,
            R.layout.layout_footer_two,
            mBinding.rvContainer,
            false
        )
    }

    private val mAdapter by lazy { ContentAdapter(this) }

    override fun bindView() {
        mBinding.rvContainer.layoutManager = LinearLayoutManager(this)
        mBinding.rvContainer.adapter = mAdapter.getProxy()

        mAdapter.setAutoLoadMoreListener(object : BaseViewAdapter.AutoLoadMoreListener {
            override fun autoLoadMore() {
                viewModel.loadData(LoadStatus.LOAD_MORE)
            }
        })
        mAdapter.setLoadErrorListener(object : BaseViewAdapter.LoadErrorListener {
            override fun itemReload() {
                viewModel.loadData(LoadStatus.RELOAD)
            }

            override fun pageReload() {
                viewModel.loadData(LoadStatus.REFRESH)
            }
        })
        mAdapter.setLoadStatusListener(object : LoadStatusListener {
            override fun onChangeStatus(status: Int) {
                if (status == AdapterStatus.STATUS_ITEM_END) {
                    mAdapter.addFooter(footerTwo.root)
                }
            }
        })

        mAdapter.addHeader(headerOne.root)
        mAdapter.addHeader(headerTwo.root)
        mAdapter.addFooter(footerOne.root)
        mAdapter.initStatusView()
        mAdapter.pageLoading()
    }

    override fun bindData() {
        viewModel.dataLive.observeState<PageData<ContentData>>(this, success = {
            mAdapter.submitData(it)
        }, error = {
            toast(it.message)
            mAdapter.submitFailed()
        })

        viewModel.loadData(LoadStatus.INIT)
    }
}
