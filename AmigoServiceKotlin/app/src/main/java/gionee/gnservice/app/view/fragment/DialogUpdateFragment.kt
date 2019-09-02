package gionee.gnservice.app.view.fragment


import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v4.app.Fragment
import com.lee.library.base.BaseDialogFragment
import gionee.gnservice.app.R
import gionee.gnservice.app.databinding.FragmentDialogUpdateBinding
import gionee.gnservice.app.view.activity.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class DialogUpdateFragment :
    BaseDialogFragment<FragmentDialogUpdateBinding, ViewModel>(R.layout.fragment_dialog_update, null) {
    override fun bindData(savedInstanceState: Bundle?) {
        isCancelable = false
        dialog.setOnCancelListener {  }
    }

    override fun bindView() {
        binding.btnBack.setOnClickListener {
            //显示磁铁及红包雨
            (activity as? MainActivity)?.initMagnet()
            (activity as? MainActivity)?.initRedPackage()
            dismiss()
        }
    }

}
