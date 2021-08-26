package com.lee.adapter.binding

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.adapter.R
import com.lee.adapter.databinding.ActivityViewBindingBinding
import com.lee.adapter.databinding.LayoutFooterBinding
import com.lee.adapter.databinding.LayoutHeaderBinding
import com.lee.adapter.viewmodel.ContentViewModel
import com.lee.library.adapter.base.BaseViewAdapter
import com.lee.library.adapter.listener.LoadErrorListener
import com.lee.library.adapter.page.submitData
import com.lee.library.adapter.page.submitFailed
import com.lee.library.base.BaseActivity
import com.lee.library.extensions.binding
import com.lee.library.extensions.inflate
import com.lee.library.extensions.toast
import com.lee.library.mvvm.load.LoadStatus

class ViewBindingActivity : BaseActivity() {

    private val binding by binding(ActivityViewBindingBinding::inflate)
    private val viewModel by viewModels<ContentViewModel>()

    private val headerBinding by inflate {
        LayoutHeaderBinding.inflate(it, binding.rvContainer, false)
    }
    private val footerBinding by inflate {
        LayoutFooterBinding.inflate(it, binding.rvContainer, false)
    }

    private val mAdapter by lazy { BindingAdapter(this, arrayListOf()) }

    override fun bindView() {
        supportFragmentManager.beginTransaction().add(
            R.id.frame_container,
            BindingFragment()
        )
            .commit()

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
                mAdapter.addFooter(footerBinding.root)
            }
        }

        mAdapter.addHeader(headerBinding.root)
        mAdapter.addFooter(footerBinding.root)
        mAdapter.initStatusView()
        mAdapter.pageLoading()
    }

    override fun bindData() {
        viewModel.dataLiveData.observe(this, {
            mAdapter.submitData(it)
        }, {
            toast(it)
            mAdapter.submitFailed()
        })

        viewModel.loadData(LoadStatus.INIT)
    }
}