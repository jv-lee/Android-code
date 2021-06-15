package com.lee.adapter

import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.adapter.binding.BindingAdapter
import com.lee.adapter.databinding.*
import com.lee.adapter.viewmodel.ContentViewModel
import com.lee.library.adapter.base.BaseViewAdapter
import com.lee.library.adapter.listener.LoadErrorListener
import com.lee.library.adapter.page.submitData
import com.lee.library.adapter.page.submitFailed
import com.lee.library.base.BaseActivity
import com.lee.library.extensions.binding
import com.lee.library.mvvm.load.LoadStatus

class ViewBindingActivity : BaseActivity() {

    val binding by binding(ActivityViewBindingBinding::inflate)
    val viewModel by viewModels<ContentViewModel>()

    private val headerOne by lazy {
        DataBindingUtil.inflate<LayoutHeaderOneBinding>(
            layoutInflater, R.layout.layout_header_one, binding.rvContainer, false
        )
    }
    private val headerTwo by lazy {
        DataBindingUtil.inflate<LayoutHeaderTwoBinding>(
            layoutInflater, R.layout.layout_header_two, binding.rvContainer, false
        )
    }
    private val footerOne by lazy {
        DataBindingUtil.inflate<LayoutFooterOneBinding>(
            layoutInflater, R.layout.layout_footer_one, binding.rvContainer, false
        )
    }
    private val footerTwo by lazy {
        DataBindingUtil.inflate<LayoutFooterTwoBinding>(
            layoutInflater, R.layout.layout_footer_two, binding.rvContainer, false
        )
    }

    private val mAdapter by lazy { BindingAdapter(this, arrayListOf()) }

    override fun bindView() {
        supportFragmentManager.beginTransaction().add(R.id.frame_container,BindingFragment()).commit()

        binding.rvContainer.layoutManager = LinearLayoutManager(this)
        binding.rvContainer.adapter = mAdapter.proxy

        mAdapter.setAutoLoadMoreListener {
            viewModel.loadData(LoadStatus.LOAD_MORE)
        }
        mAdapter.setLoadErrorListener(object : LoadErrorListener {
            override fun itemReload() {
                viewModel.loadData(LoadStatus.RELOAD)
            }

            override fun pageReload() {
                viewModel.loadData(LoadStatus.REFRESH)
            }

        })
        mAdapter.setLoadStatusListener {
            if (it == BaseViewAdapter.STATUS_ITEM_END) {
                mAdapter.addFooter(footerTwo.root)
            }
        }

        mAdapter.addHeader(headerOne.root)
        mAdapter.addHeader(headerTwo.root)
        mAdapter.addFooter(footerOne.root)
        mAdapter.initStatusView()
        mAdapter.pageLoading()
    }

    override fun bindData() {
        viewModel.dataLiveData.observe(this, Observer {
            mAdapter.submitData(it)
        }, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            mAdapter.submitFailed()
        })

        viewModel.loadData(LoadStatus.INIT)
    }
}