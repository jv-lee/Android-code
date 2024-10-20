package com.lee.library.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.lee.library.R
import com.lee.library.uistate.PageState

/**
 * 状态切换视图Layout
 * @author jv.lee
 */
class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    /**
     * 状态布局 视图View
     */
    private lateinit var mLoadingView: View
    private lateinit var mEmptyView: View
    private lateinit var mErrorView: View
    private lateinit var mNetworkView: View
    private lateinit var mErrorRestart: View
    private lateinit var mNetworkRestart: View

    private lateinit var loadingText: String
    private lateinit var emptyText: String
    private lateinit var errorText: String
    private lateinit var networkText: String

    private var emptyImageRes: Int = -1
    private var errorImageRes: Int = -1
    private var networkImageRes: Int = -1

    /**
     * 重新加载回调接口
     */
    private var onReloadListener: OnReloadListener? = null

    @PageState
    var currentState = PageState.LOADING
        private set

    init {
        readAttrs(context, attrs)
        initView()
    }

    private fun readAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StateLayout)

        val inflaterView = fun(index: Int, defValue: Int): View {
            val layoutId = typedArray.getResourceId(index, defValue)
            val view = LayoutInflater.from(context).inflate(layoutId, null)
            addView(view)
            return view
        }

        mLoadingView =
            inflaterView(R.styleable.StateLayout_layout_loading, R.layout.layout_status_loading)
        mErrorView =
            inflaterView(R.styleable.StateLayout_layout_error, R.layout.layout_status_error)
        mEmptyView =
            inflaterView(R.styleable.StateLayout_layout_empty, R.layout.layout_status_empty)
        mNetworkView =
            inflaterView(R.styleable.StateLayout_layout_network, R.layout.layout_status_not_network)

        loadingText = typedArray.getString(R.styleable.StateLayout_layout_loading_text) ?: ""
        emptyText = typedArray.getString(R.styleable.StateLayout_layout_empty_text) ?: ""
        errorText = typedArray.getString(R.styleable.StateLayout_layout_error_text) ?: ""
        networkText = typedArray.getString(R.styleable.StateLayout_layout_network_text) ?: ""

//        emptyImageRes = typedArray.getResourceId(
//            R.styleable.StateLayout_layout_empty_image, R.drawable.ic_page_state_empty
//        )
//        errorImageRes = typedArray.getResourceId(
//            R.styleable.StateLayout_layout_empty_image, R.drawable.ic_page_state_network
//        )
//        networkImageRes = typedArray.getResourceId(
//            R.styleable.StateLayout_layout_empty_image, R.drawable.ic_page_state_network
//        )

        currentState = typedArray.getInt(R.styleable.StateLayout_layout_state, PageState.LOADING)

        typedArray.recycle()
    }

    @SuppressLint("InflateParams")
    private fun initView() {
        mErrorRestart = mErrorView.findViewById(R.id.btn_restart)
        mNetworkRestart = mNetworkView.findViewById(R.id.btn_restart)

//        mLoadingView.findViewById<TextView>(R.id.tv_loading_text).text = loadingText
//        mEmptyView.findViewById<TextView>(R.id.tv_empty_text).text = emptyText
//        mErrorView.findViewById<TextView>(R.id.tv_error_text).text = errorText
//        mNetworkView.findViewById<TextView>(R.id.tv_network_text).text = networkText
//
//        mEmptyView.findViewById<ImageView>(R.id.iv_empty_image).setImageResource(emptyImageRes)
//        mErrorView.findViewById<ImageView>(R.id.iv_error_image).setImageResource(errorImageRes)
//        mNetworkView.findViewById<ImageView>(R.id.iv_network_image).setImageResource(networkImageRes)

        mErrorRestart.setOnClickListener { onReloadListener?.onReload() }
        mNetworkRestart.setOnClickListener { onReloadListener?.onReload() }

        post {
            setState(currentState)
        }
    }

    /**
     * 隐藏所有布局
     */
    private fun hideStateLayout() {
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            childView.visibility = GONE
        }
    }

    /**
     * 显示数据展示
     */
    private fun showDataLayout() {
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView == mLoadingView ||
                childView == mEmptyView ||
                childView == mNetworkView ||
                childView == mErrorView) {
                continue
            }
            childView.visibility = VISIBLE
        }
    }

    /**
     * 设置显示状态
     *
     * @param status 状态码
     */
    fun setState(@PageState status: Int) {
        currentState = status
        hideStateLayout()

        when (status) {
            PageState.LOADING -> mLoadingView.visibility = VISIBLE
            PageState.ERROR -> mErrorView.visibility = VISIBLE
            PageState.EMPTY -> mEmptyView.visibility = VISIBLE
            PageState.NETWORK -> mNetworkView.visibility = VISIBLE
            PageState.DATA -> showDataLayout()
        }
    }

    /**
     * 通过延时设置loading状态
     *
     * @param postTime 延迟时间
     */
    fun postLoading(postTime: Long = 200) {
        postDelayed({
            // 处于默认状态设置loading状态
            if (currentState == -1) {
                setState(PageState.LOADING)
            }
        }, postTime)
    }

    /**
     * 重试按钮接口
     */
    interface OnReloadListener {
        /**
         * 点击重试按钮
         */
        fun onReload()
    }

    fun setOnReloadListener(onReloadListener: OnReloadListener) {
        this.onReloadListener = onReloadListener
    }
}