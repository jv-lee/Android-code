package com.lee.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lee.ui.utils.moveToItem

/**
 * @author jv.lee
 * @data 2021/9/23
 * @description
 */
class BannerView : FrameLayout {

    private val mHandler = Handler(Looper.getMainLooper())

    private var isStart = true
    private var isAutoPlay = false

    private var delayTime = 3000L
    private var moveDuration = 500L
    private val mLooperCountFactor = 500

    private lateinit var mAdapter: BannerAdapter<*>

    private var mViewPager: ViewPager2 = ViewPager2(context).also {
        it.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    constructor(context: Context) : super(context, null, 0)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )

    init {
        initViewPager()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViewPager() {
        addView(mViewPager)

        mViewPager.setOnTouchListener { v, event ->
            isAutoPlay = event.action == MotionEvent.ACTION_UP
            false
        }
    }

    fun <T> bindDataCreate(data: List<T>, createHolder: CreateHolder<T>) {
        BannerAdapter(data, createHolder).also {
            mAdapter = it
            mViewPager.adapter = it
            mViewPager.setCurrentItem(it.getStartSelectItem(), false)
            mViewPager.registerOnPageChangeCallback(mPagerChange)
        }

        isStart = false
        start()
    }

    fun start() {
        if (isStart) return
        isStart = true
        isAutoPlay = true
        mHandler.removeCallbacks(mLoopRunnable)
        mHandler.postDelayed(mLoopRunnable, delayTime)
    }

    fun pause() {
        isStart = false
        isAutoPlay = false
        mHandler.removeCallbacks(mLoopRunnable)
    }

    fun destroy() {
        mViewPager.unregisterOnPageChangeCallback(mPagerChange)
    }

    private val mPagerChange = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val index = position % mAdapter.data.size
        }
    }

    /**
     * banner轮询任务
     */
    private val mLoopRunnable = object : Runnable {
        override fun run() {
            if (isAutoPlay) {
                var itemIndex = mViewPager.currentItem
                if (itemIndex == mAdapter.itemCount - 1) {
                    mViewPager.setCurrentItem(mAdapter.getStartSelectItem(), false)
                } else {
                    mViewPager.moveToItem(++itemIndex, moveDuration)
                }
            }
            mHandler.postDelayed(this, delayTime)
        }
    }

    /**
     * banner视图适配器
     */
    inner class BannerAdapter<T>(val data: List<T>, private val create: CreateHolder<T>) :
        RecyclerView.Adapter<BannerAdapter<T>.BannerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
            return BannerViewHolder(create.createView(parent.context))
        }

        override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
            //获取真实的下标
            val realPosition = position % data.size
            val itemData = data[realPosition]
            create.onBind(holder, realPosition, itemData)

            holder.itemView.setOnClickListener {
                create.onItemClick(realPosition, itemData)
            }
        }

        override fun getItemCount(): Int {
            return mLooperCountFactor * 3
        }

        fun getStartSelectItem(): Int {
            // 我们设置当前选中的位置为Integer.MAX_VALUE / 2,这样开始就能往左滑动
            // 但是要保证这个值与getRealPosition 的 余数为0，因为要从第一页开始显示
            var currentItem: Int = data.size * mLooperCountFactor / 2
            if (currentItem % data.size == 0) {
                return currentItem
            }
            // 直到找到从0开始的位置
            while (currentItem % data.size != 0) {
                currentItem++
            }
            return currentItem
        }


        inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }


    interface CreateHolder<T> {
        /**
         * 创建View
         *
         * @param context
         * @return
         */
        fun createView(context: Context): View

        /**
         * 绑定数据
         *
         * @param holder
         * @param position
         * @param item
         */
        fun onBind(holder: BannerAdapter<T>.BannerViewHolder, position: Int, item: T)

        /**
         * item点击事件
         * @param position
         * @param item
         */
        fun onItemClick(position: Int, item: T) {}
    }

}