package com.lee.adapter

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.adapter.adapter.ContentAdapter
import com.lee.adapter.databinding.*
import com.lee.adapter.viewmodel.ContentViewModel
import com.lee.library.adapter.listener.LoadErrorListener
import com.lee.library.base.BaseActivity
import com.lee.library.mvvm.live.LoadStatus

class MainActivity : BaseActivity<ActivityMainBinding, ContentViewModel>(R.layout.activity_main) {

    private val headerOne by lazy { DataBindingUtil.inflate<LayoutHeaderOneBinding>(layoutInflater,R.layout.layout_header_one,binding.rvContainer,false) }
    private val headerTwo by lazy { DataBindingUtil.inflate<LayoutHeaderTwoBinding>(layoutInflater,R.layout.layout_header_two,binding.rvContainer,false) }
    private val footerOne by lazy { DataBindingUtil.inflate<LayoutFooterOneBinding>(layoutInflater,R.layout.layout_footer_one,binding.rvContainer,false) }
    private val footerTwo by lazy { DataBindingUtil.inflate<LayoutFooterTwoBinding>(layoutInflater,R.layout.layout_footer_two,binding.rvContainer,false) }

    private val mAdapter by lazy { ContentAdapter(this, arrayListOf()) }

    override fun bindView() {
        binding.rvContainer.layoutManager = LinearLayoutManager(this)
        binding.rvContainer.adapter = mAdapter.proxy

        mAdapter.setAutoLoadMoreListener {
            viewModel.loadData(LoadStatus.LOAD_MORE)
        }
        mAdapter.setLoadErrorListener(object:LoadErrorListener{
            override fun itemReload() {
                viewModel.loadData(LoadStatus.RELOAD)
            }

            override fun pageReload() {
                viewModel.loadData(LoadStatus.REFRESH)
            }

        })

        mAdapter.addHeader(headerOne.root)
        mAdapter.addHeader(headerTwo.root)
        mAdapter.addFooter(footerOne.root)
        mAdapter.addFooter(footerTwo.root)
        mAdapter.initStatusView()
        mAdapter.pageLoading()
    }

    override fun bindData() {
        viewModel.dataLiveData.observe(this, Observer {
            mAdapter.submitData(it)
        })

        viewModel.dataLiveData.failedEvent.observe(this, Observer {
            mAdapter.submitFailed()
        })

        viewModel.loadData(LoadStatus.INIT)
    }

}
