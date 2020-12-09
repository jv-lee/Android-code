package com.lee.api.fragment

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import com.lee.api.R
import com.lee.api.databinding.FragmentMainBinding
import com.lee.library.base.BaseFragment
import com.lee.library.mvvm.base.BaseViewModel

class StartFragment : BaseFragment<FragmentMainBinding, BaseViewModel>(R.layout.fragment_main) {

    private val forResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        toast(it?.data?.getStringExtra("value") ?: "")
    }

    override fun bindView() {
        binding.btnActivityResult.setOnClickListener {
            forResult.launch(Intent(requireContext(), ResultFragmentActivity::class.java))
        }
    }

    override fun bindData() {

    }


}