package com.lee.library.widget.banner

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lee.library.R
import com.lee.library.extensions.dp2px
import com.lee.library.extensions.setMargin
import com.lee.library.widget.banner.BannerView.BannerMode.Companion.MODE_CLIP
import com.lee.library.widget.banner.BannerView.BannerMode.Companion.MODE_CLIP_SCALE
import com.lee.library.widget.banner.BannerView.BannerMode.Companion.MODE_DEFAULT
import com.lee.library.widget.banner.transformer.ClipTransformer
import java.util.*

/**
 * @author jv.lee
 * @data 2021/9/23
 * @description 使用ViewPager2实现的BannerView
 */
class BannerView : RelativeLayout {

    //记录初始化启动状态
    private var isLoop = false

    //首次初始化状态
    private var isInit = false

    //是否支持自动轮播
    private var isAutoPlay: Boolean = false

    //数据保存下标值
    private var saveIndex = -1

    //页面翻页延时
    private var delayTime: Long = 0

    //页面切换动画时长
    private var moveDuration: Long = 5

    //无限翻页最大倍数 count * filed
    private val mLooperCountFactor = 500

    //banner适配器
    private lateinit var mAdapter: BannerAdapter<*>

    //banner容器
    private var mViewPager = ViewPager2(context)

    //indicator容器
    private val mIndicatorContainer = LinearLayout(context)

    //indicatorView数据集合
    private val mIndicators = ArrayList<ImageView>()

    //indicator配置参数
    private var indicatorGravity: Int = 0
    private var indicatorPadding: Float = 0F
    private var indicatorChildPadding: Float = 0F

    //clip距离参数
    private var clipMargin: Float = 0F

    //mIndicatorRes[0] 为为选中，mIndicatorRes[1]为选中
    private val mIndicatorRes =
        intArrayOf(R.drawable.shape_indicator_normal, R.drawable.shape_indicator_selected)

    @IntDef(MODE_DEFAULT, MODE_CLIP, MODE_CLIP_SCALE)
    annotation class BannerMode {
        companion object {
            const val MODE_DEFAULT = 0x0
            const val MODE_CLIP = 0x1
            const val MODE_CLIP_SCALE = 0X2
        }
    }

    private var bannerMode = MODE_DEFAULT

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        initAttributes(attributeSet)
        initViewPager()
        initIndicator()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                startLoop()
            }
            else -> {
                stopLoop()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        //当前可以自动轮播或构建初始化未完成不做处理以免重复发起轮播任务
        if (!isAutoPlay || !isInit) return

        if (visibility == VISIBLE) {
            //当前未轮播状态重新发起轮播任务
            if (!isLoop) {
                startLoop()
            }
        } else {
            //当前在轮播状态，移除轮播状态
            if (isLoop) isLoop = false
            stopLoop()
        }
    }

    private fun initAttributes(attributeSet: AttributeSet?) {
        context.obtainStyledAttributes(attributeSet, R.styleable.BannerView).run {
            isAutoPlay = getBoolean(R.styleable.BannerView_autoPlay, true)
            delayTime = getInteger(R.styleable.BannerView_delayTime, 3000).toLong()
            moveDuration = getInteger(R.styleable.BannerView_moveDuration, 500).toLong()
            clipMargin = getDimension(R.styleable.BannerView_clipMargin, context.dp2px(30))
            indicatorPadding = getDimension(R.styleable.BannerView_indicatorPadding, 10F)
            indicatorChildPadding = getDimension(R.styleable.BannerView_indicatorChildPadding, 10F)
            indicatorGravity = getInt(
                R.styleable.BannerView_indicatorGravity,
                Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            )
            mIndicatorRes[0] = getResourceId(
                R.styleable.BannerView_indicatorNormalDrawable,
                R.drawable.shape_indicator_normal
            )
            mIndicatorRes[1] = getResourceId(
                R.styleable.BannerView_indicatorSelectedDrawable,
                R.drawable.shape_indicator_selected
            )
            bannerMode = getInt(R.styleable.BannerView_bannerMode, MODE_DEFAULT)
            recycle()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViewPager() {
        mViewPager.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        mViewPager.isSaveEnabled = true
        mViewPager.isSaveFromParentEnabled = true
        mViewPager.offscreenPageLimit = 3
        setBannerClipMode(bannerMode == MODE_CLIP || bannerMode == MODE_CLIP_SCALE)

        addView(mViewPager)
    }

    private fun initIndicator() {
        mIndicatorContainer.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
                setLayoutGravity(it)
            }
        mIndicatorContainer.setPadding(indicatorPadding.toInt())
        mIndicatorContainer.orientation = LinearLayout.HORIZONTAL

        addView(mIndicatorContainer)
    }

    private fun hasGravity(gravity: Int): Boolean {
        return indicatorGravity and gravity == gravity
    }

    private fun setLayoutGravity(layoutParams: LayoutParams) {
        if (hasGravity(Gravity.LEFT) || hasGravity(Gravity.START)) {
            layoutParams.addRule(ALIGN_PARENT_LEFT)
        }
        if (hasGravity(Gravity.TOP)) {
            layoutParams.addRule(ALIGN_PARENT_TOP)
        }
        if (hasGravity(Gravity.RIGHT) || hasGravity(Gravity.END)) {
            layoutParams.addRule(ALIGN_PARENT_RIGHT)
        }
        if (hasGravity(Gravity.BOTTOM)) {
            layoutParams.addRule(ALIGN_PARENT_BOTTOM)
        }
        if (hasGravity(Gravity.CENTER_HORIZONTAL)) {
            layoutParams.addRule(CENTER_HORIZONTAL)
        }
        if (hasGravity(Gravity.CENTER_VERTICAL)) {
            layoutParams.addRule(CENTER_VERTICAL)
        }
        if (hasGravity(Gravity.CENTER)) {
            layoutParams.addRule(CENTER_IN_PARENT)
        }
    }

    private fun buildIndicatorView() {
        mIndicatorContainer.removeAllViews()
        mIndicators.clear()
        for (index in mAdapter.data.indices) {
            val imageView = ImageView(context)
            imageView.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            imageView.setPadding(indicatorChildPadding.toInt())

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

    private fun setBannerClipMode(enable: Boolean) {
        clipChildren = !enable
        mViewPager.clipChildren = !enable
        if (enable) {
            if (bannerMode == MODE_CLIP_SCALE) {
                mViewPager.setPageTransformer(ClipTransformer())
            }
            mViewPager.setMargin(left = clipMargin.toInt(), right = clipMargin.toInt())
        } else {
            mViewPager.setPageTransformer(null)
            mViewPager.setMargin(0, 0, 0, 0)
        }
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
            if (!isAutoPlay) return

            //开启轮播状态
            isLoop = true

            //设置当前轮训下标
            var itemIndex = mViewPager.currentItem
            if (itemIndex == mAdapter.itemCount - 1) {
                val startIndex = mAdapter.getStartSelectItem()
                mViewPager.setCurrentItem(startIndex, false)
            } else {
                ++itemIndex
                mViewPager.moveToItem(itemIndex, moveDuration)
            }
            postDelayed(this, delayTime)
        }
    }

    private fun startLoop() {
        if (isAutoPlay) {
            removeCallbacks(mLoopRunnable)
            postDelayed(mLoopRunnable, delayTime)
        }
    }

    private fun stopLoop() {
        removeCallbacks(mLoopRunnable)
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

    override fun onSaveInstanceState(): Parcelable {
        val parcelable = super.onSaveInstanceState()
        val saveState = SaveState(parcelable)
        saveState.currentItem = mViewPager.currentItem
        return saveState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val saveState = state as SaveState
        super.onRestoreInstanceState(saveState.superState)
        saveIndex = saveState.currentItem
        mViewPager.setCurrentItem(saveState.currentItem, false)
    }

    class SaveState : BaseSavedState {
        var currentItem: Int = 0

        constructor(source: Parcel?) : super(source)
        constructor(source: Parcelable?) : super(source)

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeInt(currentItem)
        }

        @JvmField
        val create = object : Parcelable.Creator<SaveState> {
            override fun createFromParcel(source: Parcel?): SaveState {
                return SaveState(source)
            }

            override fun newArray(size: Int): Array<SaveState> {
                return newArray(size)
            }

        }
    }

    /**
     * 绑定数据及View构建器
     * @param data banner数据源
     * @param createHolder bannerView构建样式
     */
    fun <T> bindDataCreate(data: List<T>, createHolder: CreateHolder<T>) {
        //相同数据重复构建过滤
        if (isInit && mAdapter.data == data) return
        isInit = false

        mAdapter = BannerAdapter(data, createHolder)

        post {
            if (bannerMode == MODE_CLIP && data.size < 3) {
                setBannerClipMode(false)
            }
            mViewPager.adapter = mAdapter
            mViewPager.setCurrentItem(getStartIndex(), false)
            mViewPager.registerOnPageChangeCallback(mPagerChange)
            buildIndicatorView()

            isInit = true
            startLoop()
        }
    }

    /**
     * 查看当前启动状态
     */
    fun isLoop() = isLoop

    /**
     * 设置是否支持自动播放
     * @param enable
     */
    fun setAutoPlay(enable: Boolean) {
        isAutoPlay = enable
    }

    /**
     * 设置轮播延时间隔
     * @param duration
     */
    fun setDelayTime(duration: Long) {
        delayTime = duration
    }

    /**
     * 设置切换banner时动画时长
     * @param duration
     */
    fun setMoveDuration(duration: Long) {
        moveDuration = duration
    }

    /**
     * 设置indicator指示器位置
     * @param gravity 位置参数 Gravity.*
     */
    fun setIndicatorGravity(gravity: Int) {
        indicatorGravity = gravity
    }

    /**
     * 设置indicator容器padding值
     * @param dimension
     */
    fun setIndicatorPadding(dimension: Float) {
        indicatorPadding = dimension
    }

    /**
     * 设置indicator指示灯子viewPadding间距
     * @param dimension
     */
    fun setIndicatorChildPadding(dimension: Float) {
        indicatorChildPadding = dimension
    }

    /**
     * 设置指示器资源样式
     * @param normalRes 未选中指示器样式
     * @param selectedRes 选中指示器样式
     */
    fun setIndicatorDrawable(@DrawableRes normalRes: Int, @DrawableRes selectedRes: Int) {
        mIndicatorRes[0] = normalRes
        mIndicatorRes[1] = selectedRes
    }

    /**
     * BannerView处于RecyclerView头部无法回调view状态监听
     * RecyclerView内部dispatch状态通知只更新自身 不更新子view
     * 手动在fragment中调用该方法恢复状态
     */
    fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(BannerView::class.java.simpleName, mViewPager.currentItem)
    }

    /**
     * * 手动在fragment中调用该方法恢复状态
     */
    fun onViewStateRestored(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            val currentIndex = getInt(BannerView::class.java.simpleName, -1)
            saveIndex = currentIndex
            mViewPager.setCurrentItem(currentIndex, false)
        }
    }

}