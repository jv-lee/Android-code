package com.lee.api

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import com.lee.api.databinding.FragmentMainBinding
import com.lee.library.base.BaseFragment
import com.lee.library.mvvm.base.BaseViewModel

class MainFragment : BaseFragment<FragmentMainBinding, BaseViewModel>(R.layout.fragment_main) {

    private val forResult by lazy {
        prepareCall(
            ActivityResultContracts.StartActivityForResult(),
            requireActivity().activityResultRegistry
        ) {
            toast(it?.data?.getStringExtra("value") ?: "")
        }
    }

    override fun bindView() {
        binding.btnActivityResult.setOnClickListener {
            forResult.launch(Intent(requireContext(), ResultActivity::class.java))
        }
    }

    override fun bindData() {

    }


}