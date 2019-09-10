package gionee.gnservice.app.view.fragment

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import com.lee.library.base.BaseDialogFragment
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.databinding.FragmentDialogNoviceBinding
import gionee.gnservice.app.view.activity.VideoDetailsActivity
import gionee.gnservice.app.vm.NoviceViewModel

/**
 * @author jv.lee
 * @date 2019/9/10.
 * @description
 */
class DialogNoviceFragment :
    BaseDialogFragment<FragmentDialogNoviceBinding, NoviceViewModel>(
        R.layout.fragment_dialog_novice,
        NoviceViewModel::class.java
    ) {

    override fun bindData(savedInstanceState: Bundle?) {
        viewModel.videoData.observe(this, Observer {
            binding.btnVideoStart.setOnClickListener { v ->
                if (it != null) {
                    startActivity(
                        Intent(activity, VideoDetailsActivity::class.java)
                            .putExtra(Constants.TITLE, it.title)
                            .putExtra(Constants.URL, it.shareLink)
                    )
                }
            }
        })
        viewModel.getVideoData()
    }

    override fun bindView() {
        binding.btnNewsStart.setOnClickListener { dismiss() }
        binding.btnVideoStart.setOnClickListener { dismiss() }
        binding.ivClose.setOnClickListener { dismiss() }
    }

}