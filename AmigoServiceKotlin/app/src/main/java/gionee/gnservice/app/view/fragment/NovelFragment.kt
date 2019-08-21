package gionee.gnservice.app.view.fragment

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import com.lee.library.base.BaseFragment
import com.lee.library.ioc.annotation.ContentView
import gionee.gnservice.app.R
import gionee.gnservice.app.databinding.FragmentNovelBinding

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description
 */
class NovelFragment : BaseFragment<FragmentNovelBinding, ViewModel>(R.layout.fragment_novel, null) {

    override fun bindData(savedInstanceState: Bundle?) {
    }

    override fun bindView() {
    }

    override fun lazyLoad() {
        super.lazyLoad()
//        frame_container.addView(BooksLayout(activity))
    }


}