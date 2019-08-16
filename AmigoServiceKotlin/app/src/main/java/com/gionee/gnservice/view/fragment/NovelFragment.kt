package com.gionee.gnservice.view.fragment

import android.os.Bundle
import com.android.droi.books.BooksLayout
import com.gionee.gnservice.app.R
import com.lee.library.base.BaseFragment
import com.lee.library.ioc.annotation.ContentView
import kotlinx.android.synthetic.main.fragment_novel.*

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description
 */
@ContentView(R.layout.fragment_novel)
class NovelFragment : BaseFragment() {

    override fun bindData(savedInstanceState: Bundle?) {
    }

    override fun bindView() {
    }

    override fun lazyLoad() {
        super.lazyLoad()
        frame_container.addView(BooksLayout(activity))
    }


}