package gionee.gnservice.app.view.fragment


import android.arch.lifecycle.Observer
import android.os.Bundle
import com.lee.library.base.BaseDialogFragment
import gionee.gnservice.app.BuildConfig
import gionee.gnservice.app.R
import gionee.gnservice.app.databinding.FragmentDialogUpdateBinding
import gionee.gnservice.app.view.activity.MainActivity
import gionee.gnservice.app.vm.DialogUpdateViewModel

/**
 * @author jv.lee
 * @date 2019/9/2.
 * @description 更新提示弹窗
 */
class DialogUpdateFragment :
    BaseDialogFragment<FragmentDialogUpdateBinding, DialogUpdateViewModel>(
        R.layout.fragment_dialog_update,
        DialogUpdateViewModel::class.java
    ) {

    override fun bindData(savedInstanceState: Bundle?) {
        isCancelable = false

        viewModel.versionInfo.observe(this, Observer {
            for (item in it!!.list) {
                binding.tvContent.append(item + "\n")
            }
        })

        viewModel.getVersionInfo()
    }

    override fun bindView() {
        binding.tvTitle.text =
            String.format(resources.getString(R.string.dialog_update_title, BuildConfig.VERSION_NAME))
        binding.btnBack.setOnClickListener {
            //显示红包雨
            (activity as? MainActivity)?.initRedPackage()
            dismiss()
        }
    }

}
