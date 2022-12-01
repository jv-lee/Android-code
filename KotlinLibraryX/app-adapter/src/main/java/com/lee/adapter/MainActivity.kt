package com.lee.adapter

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.adapter.adapter.ContentAdapter
import com.lee.adapter.databinding.*
import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.PageData
import com.lee.adapter.viewmodel.ContentViewModel
import com.lee.library.adapter.base.BaseViewAdapter
import com.lee.library.adapter.listener.LoadStatusListener
import com.lee.library.adapter.page.submitData
import com.lee.library.adapter.page.submitFailed
import com.lee.library.base.BaseVMActivity
import com.lee.library.extensions.toast
import com.lee.library.viewstate.LoadStatus
import com.lee.library.viewstate.observeState

class MainActivity : BaseVMActivity<ActivityMainBinding, ContentViewModel>(R.layout.activity_main) {

    private val headerOne by lazy {
        DataBindingUtil.inflate<LayoutHeaderOneBinding>(
            layoutInflater,
            R.layout.layout_header_one,
            binding.rvContainer,
            false
        )
    }
    private val headerTwo by lazy {
        DataBindingUtil.inflate<LayoutHeaderTwoBinding>(
            layoutInflater,
            R.layout.layout_header_two,
            binding.rvContainer,
            false
        )
    }
    private val footerOne by lazy {
        DataBindingUtil.inflate<LayoutFooterOneBinding>(
            layoutInflater,
            R.layout.layout_footer_one,
            binding.rvContainer,
            false
        )
    }
    private val footerTwo by lazy {
        DataBindingUtil.inflate<LayoutFooterTwoBinding>(
            layoutInflater,
            R.layout.layout_footer_two,
            binding.rvContainer,
            false
        )
    }

    private val mAdapter by lazy { ContentAdapter(this, arrayListOf()) }

    override fun bindView() {
        binding.rvContainer.layoutManager = LinearLayoutManager(this)
        binding.rvContainer.adapter = mAdapter.proxy

        mAdapter.setAutoLoadMoreListener {
            viewModel.loadData(LoadStatus.LOAD_MORE)
        }
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
                if (status == BaseViewAdapter.STATUS_ITEM_END) {
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
