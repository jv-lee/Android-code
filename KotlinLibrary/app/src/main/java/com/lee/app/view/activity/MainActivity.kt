package com.lee.app.view.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lee.app.R
import com.lee.library.base.BaseActivity
import com.lee.app.databinding.ActivityMainBinding
import com.lee.app.view.adapter.SimpleAdapter
import com.lee.app.view.fragment.MainFragment
import com.lee.app.view.fragment.MyBottomSheetFragment
import com.lee.app.viewmodel.MainViewModel
import com.lee.library.widget.refresh.header.DefaultHeader
import java.util.*


class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main, MainViewModel::class.java) {

    private var id: Int = 1999

    private var adapter: SimpleAdapter? = null
    private var page = 0

    override fun bindData(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel

        viewModel.apply {
            userInfo.observe(this@MainActivity, Observer {
                user.set(it)
            })
        }
        viewModel.login(id++)


        adapter = SimpleAdapter(this, ArrayList())

        binding.rvContainer.layoutManager = LinearLayoutManager(this)
        binding.rvContainer.adapter = adapter

    }

    override fun bindView() {
        binding.setClick {
            when (it.id) {
                R.id.button -> {
                    supportFragmentManager.beginTransaction().add(R.id.frame_container, MainFragment()).commit()
                }
                R.id.button2 -> {
//                    MyBottomSheetFragment().show(supportFragmentManager, "dialog")
                    viewModel.login(id++)
                }
            }

        }

        binding.refresh.setBootView(binding.container, binding.rvContainer, DefaultHeader(this), null)
        binding.refresh.setRefreshCallBack {
            binding.refresh.postDelayed({
                adapter?.updateData(initData())
                adapter?.notifyDataSetChanged()
                binding.refresh.setRefreshCompleted()
            }, 500)
        }

    }

    fun initData(): List<Int> {
        val asList = Arrays.asList(1, 2, 3, 4, 5)
        return asList
    }

}



