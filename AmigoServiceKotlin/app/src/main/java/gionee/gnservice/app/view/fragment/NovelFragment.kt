package gionee.gnservice.app.view.fragment

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import com.android.droi.books.BooksLayout
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

    private val booksLayout: BooksLayout? = null

    override fun bindData(savedInstanceState: Bundle?) {
    }

    override fun bindView() {
    }

    override fun lazyLoad() {
        super.lazyLoad()
        binding.frameContainer.addView(BooksLayout(activity))
    }


}