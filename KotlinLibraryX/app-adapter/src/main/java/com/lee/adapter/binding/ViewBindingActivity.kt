package com.lee.adapter.binding

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.adapter.R
import com.lee.adapter.databinding.ActivityViewBindingBinding
import com.lee.adapter.databinding.LayoutFooterBinding
import com.lee.adapter.databinding.LayoutHeaderBinding
import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.PageData
import com.lee.adapter.viewmodel.ContentViewModel
import com.lee.library.adapter.base.AdapterStatus
import com.lee.library.adapter.base.BaseViewAdapter
import com.lee.library.adapter.listener.LoadStatusListener
import com.lee.library.adapter.extensions.submitData
import com.lee.library.adapter.extensions.submitFailed
import com.lee.library.base.BaseBindingActivity
import com.lee.library.extensions.inflate
import com.lee.library.extensions.toast
import com.lee.library.uistate.LoadStatus
import com.lee.library.uistate.observeState

class ViewBindingActivity : BaseBindingActivity<ActivityViewBindingBinding>() {

    private val viewModel by viewModels<ContentViewModel>()

    private val headerBinding by inflate {
        LayoutHeaderBinding.inflate(it, mBinding.rvContainer, false)
    }
    private val footerBinding by inflate {
        LayoutFooterBinding.inflate(it, mBinding.rvContainer, false)
    }

    private val mAdapter by lazy { BindingAdapter(this) }

    override fun bindView() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frame_container, BindingFragment())
            .commit()

        mBinding.rvContainer.layoutManager = LinearLayoutManager(this)

        mAdapter.bindRecyclerView(mBinding.rvContainer)
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
                    mAdapter.addFooter(footerBinding.root)
                }
            }
        })

        mAdapter.addHeader(headerBinding.root)
        mAdapter.addFooter(footerBinding.root)
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