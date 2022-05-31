package com.lee.ui.fragment

import androidx.core.view.postDelayed
import com.lee.library.base.BaseFragment
import com.lee.library.extensions.binding
import com.lee.library.widget.skeleton.Skeleton
import com.lee.ui.R
import com.lee.ui.databinding.FragmentShimmerBinding

/**
 *
 * @author jv.lee
 * @date 2021/10/29
 */
class ShimmerFragment : BaseFragment(R.layout.fragment_shimmer) {

    private val binding by binding(FragmentShimmerBinding::bind)

    private lateinit var skeleton: Skeleton

    override fun bindView() {
        skeleton = Skeleton.Builder(binding.root)
            .load(R.layout.layout_shimmer_skeleton)
            .bind()

        binding.root.postDelayed(3000) {
            skeleton.unBind()
        }
    }

    override fun bindData() {

    }
}