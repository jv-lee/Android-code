package com.lee.app.view.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lee.app.R
import com.lee.app.databinding.FragmentMainBinding
import com.lee.app.view.adapter.SimpleAdapter
import com.lee.app.viewmodel.MainFragmentViewModel
import com.lee.library.base.BaseFragment

/**
 * @author jv.lee
 * @date 2019/8/16.
 * @description
 */
class MainFragment : BaseFragment<FragmentMainBinding, MainFragmentViewModel>(
    R.layout.fragment_main,
    MainFragmentViewModel::class.java
) {

    private var adapter: SimpleAdapter? = null
    private var page = 0

    override fun bindData(savedInstanceState: Bundle?) {
        adapter = SimpleAdapter(context!!, ArrayList())
        adapter?.openLoadMore()

        binding.rvContainer.layoutManager = LinearLayoutManager(activity)
        binding.rvContainer.adapter = adapter?.proxy

        viewModel.data.observe(this, Observer {
            if (it?.get(0) == 0) {
                adapter?.updateData(it)
            } else {
                adapter?.addData(it)
            }

            if (binding.refresh.isRefreshing) {
                binding.refresh.isRefreshing = false
            }

            adapter?.loadMoreCompleted()
        })
    }

    override fun bindView() {
        binding.refresh.setOnRefreshListener {
            page = 0
            viewModel.getData(page)
        }

        adapter?.setAutoLoadMoreListener {
            page += 10
            viewModel.getData(page)
        }

        viewModel.getData(page)
    }

}