package com.lee.library.widget.banner

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lee.library.R
import java.util.*

/**
 * @author jv.lee
 * @data 2021/9/23
 * @description
 */
class BannerView : RelativeLayout {

    private val mHandler = Handler(Looper.getMainLooper())

    private var saveIndex = -1

    private var isStart = true
    private var isAutoPlay = false

    private var delayTime = 3000L
    private var moveDuration = 500L
    private val mLooperCountFactor = 500

    private lateinit var mAdapter: BannerAdapter<*>

    private var mViewPager: ViewPager2 = ViewPager2(context).also {
        it.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    /**
     * indicator容器
     */
    private var indicatorPadding = 10
    private val mIndicatorContainer = LinearLayout(context)
    private val mIndicators = ArrayList<ImageView>()

    /**
     * mIndicatorRes[0] 为为选中，mIndicatorRes[1]为选中
     */
    private val mIndicatorRes =
        intArrayOf(R.drawable.shape_indicator_normal, R.drawable.shape_indicator_selected)

    constructor(context: Context) : super(context, null, 0)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )

    init {
        initViewPager()
        initIndicator()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViewPager() {
        addView(mViewPager)

        mViewPager.setOnTouchListener { v, event ->
            isAutoPlay = event.action == MotionEvent.ACTION_UP
            true
        }
    }

    private fun initIndicator() {
        mIndicatorContainer.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
                it.addRule(CENTER_HORIZONTAL)
                it.addRule(ALIGN_PARENT_BOTTOM)
                it.setMargins(0, 0, 0, indicatorPadding)
            }
        mIndicatorContainer.orientation = LinearLayout.HORIZONTAL

        addView(mIndicatorContainer)
    }

    private fun buildIndicatorView() {
        mIndicatorContainer.removeAllViews()
        mIndicators.clear()
        for (index in mAdapter.data.indices) {
            val imageView = ImageView(context)
            imageView.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

            imageView.setPadding(
                indicatorPadding,
                indicatorPadding,
                indicatorPadding,
                indicatorPadding
            )

            if (index == getRealIndex(mViewPager.currentItem)) {
                imageView.setImageResource(mIndicatorRes[1])
            } else {
                imageView.setImageResource(mIndicatorRes[0])
            }

            mIndicatorContainer.addView(imageView)
            mIndicators.add(imageView)
        }
        mIndicatorContainer.requestLayout()
    }

    private fun ViewPager2.moveToItem(
        itemIndex: Int,
        duration: Long = 500,
        interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
    ) {
        val pxToDrag: Int = width * (itemIndex - currentItem)
        val animator = ValueAnimator.ofInt(0, pxToDrag)
        var previousValue = 0
        animator.addUpdateListener { valueAnimator ->
            val currentValue = valueAnimator.animatedValue as Int
            val currentPxToDrag = (currentValue - previousValue).toFloat()
            fakeDragBy(-currentPxToDrag)
            previousValue = currentValue
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                beginFakeDrag()
            }

            override fun onAnimationEnd(animation: Animator?) {
                endFakeDrag()
            }
        })
        animator.interpolator = interpolator
        animator.duration = duration
        animator.start()
    }

    fun <T> bindDataCreate(data: List<T>, createHolder: CreateHolder<T>) {
        BannerAdapter(data, createHolder).also {
            mAdapter = it
            mViewPager.adapter = it
            mViewPager.setCurrentItem(getStartIndex(), false)
            mViewPager.registerOnPageChangeCallback(mPagerChange)
            buildIndicatorView()
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

    private fun getRealIndex(position: Int): Int {
        return position % mAdapter.data.size
    }

    private fun getStartIndex(): Int {
        val index: Int
        if (saveIndex == -1) {
            index = mAdapter.getStartSelectItem()
        } else {
            index = saveIndex
            saveIndex = -1
        }
        return index
    }

    private val mPagerChange = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val index = getRealIndex(position)

            // 切换indicator
            for (i in mIndicators.indices) {
                if (i == index) {
                    mIndicators[i].setImageResource(mIndicatorRes[1])
                } else {
                    mIndicators[i].setImageResource(mIndicatorRes[0])
                }
            }
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
            val realPosition = getRealIndex(position)
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
            val realIndex = getRealIndex(currentItem)
            if (realIndex == 0) {
                return currentItem
            }
            // 直到找到从0开始的位置
            while (realIndex != 0) {
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

    /**
     * banner处于ViewGroup 如RecyclerView的header内 不会调用子view的onSaveInstanceState / onRestoreInstanceState 方法
     * 需手动保存恢复view状态
     */
    fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(BannerView::class.java.simpleName, mViewPager.currentItem)
    }

    fun onViewStateRestored(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            val currentItem = it.getInt(BannerView::class.java.simpleName, -1)
            saveIndex = currentItem
            mViewPager.setCurrentItem(currentItem, false)
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val parcelable = super.onSaveInstanceState()
        val currentItemSaveState = CurrentItemSaveState(parcelable)
        currentItemSaveState.currentItem = mViewPager.currentItem
        return currentItemSaveState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state?.run {
            val currentItemSaveState = state as CurrentItemSaveState
            super.onRestoreInstanceState(currentItemSaveState.superState)
            saveIndex = currentItemSaveState.currentItem
            mViewPager.setCurrentItem(currentItemSaveState.currentItem, false)
        }
    }

    class CurrentItemSaveState : BaseSavedState {
        var currentItem: Int = 0

        constructor(source: Parcel?) : super(source)
        constructor(source: Parcelable?) : super(source)

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeInt(currentItem)
        }

        @JvmField
        val create = object : Parcelable.Creator<CurrentItemSaveState> {
            override fun createFromParcel(source: Parcel?): CurrentItemSaveState {
                return CurrentItemSaveState(source)
            }

            override fun newArray(size: Int): Array<CurrentItemSaveState> {
                return newArray(size)
            }

        }
    }

}