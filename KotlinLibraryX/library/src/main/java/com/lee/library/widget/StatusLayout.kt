package com.lee.library.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.core.widget.ContentLoadingProgressBar
import com.lee.library.R

/**
 * @author jv.lee
 * @description 状态切换视图Layout
 */
class StatusLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    /**
     * 状态布局ResourceId
     */
    private var loadingId = 0
    private var errorId = 0
    private var networkId = 0
    private var emptyId = 0

    /**
     * 状态布局 视图View
     */
    private var loadingView: View? = null
    private var errorView: View? = null
    private var networkView: View? = null
    private var emptyView: View? = null
    private var errorRestart: View? = null
    private var networkRestart: View? = null
    private var loadingProgressBar: ContentLoadingProgressBar? = null

    /**
     * 重新加载回调接口
     * [OnReloadListener]
     */
    private var onReloadListener: OnReloadListener? = null
    var currentStatus = -1
        private set

    private fun readAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatusLayout)
        loadingId = typedArray.getResourceId(
            R.styleable.StatusLayout_layout_loading,
            R.layout.layout_status_loading
        )
        errorId = typedArray.getResourceId(
            R.styleable.StatusLayout_layout_error,
            R.layout.layout_status_error
        )
        networkId = typedArray.getResourceId(
            R.styleable.StatusLayout_layout_network,
            R.layout.layout_status_not_network
        )
        emptyId = typedArray.getResourceId(
            R.styleable.StatusLayout_layout_empty,
            R.layout.layout_status_empty
        )
        typedArray.recycle()
    }

    @SuppressLint("InflateParams")
    private fun initView() {
        loadingView = LayoutInflater.from(context).inflate(loadingId, null)
        errorView = LayoutInflater.from(context).inflate(errorId, null)
        networkView = LayoutInflater.from(context).inflate(networkId, null)
        emptyView = LayoutInflater.from(context).inflate(emptyId, null)
        loadingProgressBar = loadingView?.findViewById(R.id.progress)
        errorRestart = errorView?.findViewById(R.id.btn_restart)
        networkRestart = networkView?.findViewById(R.id.btn_restart)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(loadingView, layoutParams)
        addView(errorView, layoutParams)
        addView(networkView, layoutParams)
        addView(emptyView, layoutParams)
    }

    private fun initListener() {
        errorRestart?.setOnClickListener {
            onReloadListener?.onReload()
        }
        networkRestart?.setOnClickListener {
            onReloadListener?.onReload()
        }
    }

    /**
     * 隐藏所有布局
     */
    private fun initStatus() {
        for (i in 0 until childCount) {
            getChildAt(i).visibility = GONE
        }
    }

    /**
     * 显示数据展示
     */
    private fun showData() {
        for (i in 0 until childCount) {
            val childAt = getChildAt(i)
            if (childAt !== loadingView && childAt !== errorView && childAt !== networkView && childAt !== emptyView) {
                childAt.visibility = VISIBLE
            }
        }
    }

    /**
     * 设置显示状态
     *
     * @param status 状态码
     */
    fun setStatus(status: Int) {
        currentStatus = status
        initStatus()
        when (status) {
            STATUS_LOADING -> loadingView!!.visibility = VISIBLE
            STATUS_DATA_ERROR -> errorView!!.visibility = VISIBLE
            STATUS_NOT_NETWORK -> networkView!!.visibility = VISIBLE
            STATUS_EMPTY_DATA -> emptyView!!.visibility = VISIBLE
            STATUS_DATA -> showData()
            else -> {
            }
        }
    }

    /**
     * 通过延时设置loading状态
     */
    fun postLoading(postTime: Long = 200) {
        postDelayed({
            //处于默认状态设置loading状态
            if (currentStatus == -1) {
                setStatus(STATUS_LOADING)
            }
        }, postTime)
    }

    fun setLoadingProgressColor(color: Int) {
        loadingProgressBar?.indeterminateDrawable?.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
    }

    interface OnReloadListener {
        /**
         * 重新加载数据
         */
        fun onReload()
    }

    fun setOnReloadListener(onReloadListener: OnReloadListener) {
        this.onReloadListener = onReloadListener
    }

    companion object {
        /**
         * 状态：加载状态
         */
        const val STATUS_LOADING = 1

        /**
         * 状态：数据展示
         */
        const val STATUS_DATA = 2

        /**
         * 状态：错误数据
         */
        const val STATUS_DATA_ERROR = 3

        /**
         * 状态：无网络
         */
        const val STATUS_NOT_NETWORK = 4

        /**
         * 状态：空数据
         */
        const val STATUS_EMPTY_DATA = 5
    }

    init {
        readAttrs(context, attrs)
        initView()
        initListener()
        initStatus()
    }
}