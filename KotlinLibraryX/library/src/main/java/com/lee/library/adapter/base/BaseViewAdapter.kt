@file:Suppress("UNCHECKED_CAST", "NotifyDataSetChanged")

package com.lee.library.adapter.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.IntDef
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.lee.library.adapter.core.ProxyAdapter
import com.lee.library.adapter.listener.DefaultLoadResource
import com.lee.library.adapter.listener.LoadResource
import com.lee.library.adapter.listener.LoadStatusListener
import com.lee.library.adapter.manager.ViewItemManager
import com.lee.library.adapter.manager.ViewLoadManager

/**
 * 封装的RecyclerViewAdapter
 *
 * @author jv.lee
 * @date 2019/3/29
 */
open class BaseViewAdapter<T>(private val context: Context) :
    RecyclerView.Adapter<BaseViewHolder>() {

    private var lastClickTime = 0
    private var loadMoreNum = 5 // 自动加载更多预备值
    private var hasLoadMore = false // 是否开启加载更多
    private var currentPageStatus = AdapterStatus.UNKNOWN // 当前页面状态
    private var currentItemStatus = AdapterStatus.UNKNOWN // 当前item状态

    private val itemClickTimeSpan = 1000 // item点击间隔
    private val mData = arrayListOf<T>() // 数据源
    private val childClickIds = arrayListOf<Int>() // item子view点击id数组
    private val itemStyle = ViewItemManager<T>() // item样式管理器

    private var proxyAdapter: ProxyAdapter? = null // 列表头尾view代理适配器
    private var recyclerView: RecyclerView? = null // 绑定recyclerView

    /**
     * 页面加载布局
     */
    private var pageLayout: View? = null
    private var pageLoadingView: View? = null
    private var pageErrorView: View? = null
    private var pageEmptyView: View? = null
    private var pageNetworkView: View? = null

    /**
     * item加载布局
     */
    private var itemLayout: View? = null
    private var loadMoreView: View? = null
    private var loadEndView: View? = null
    private var loadErrorView: View? = null

    /**
     * 条目点击事件监听
     */
    private var mOnItemClickListener: OnItemClickListener<T>? = null

    /**
     * 条目长按事件监听
     */
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null

    /**
     * 条目子view点击事件
     */
    private var mOnItemChildChange: OnItemChildView<T>? = null

    /**
     * 自动加载更多
     * autoLoadMore
     */
    private var mAutoLoadMoreListener: AutoLoadMoreListener? = null

    /**
     * 状态布局及加载更多布局资源di接口动态设置
     */
    private var mLoadResource: LoadResource? = null

    /**
     * 错误状态重试接口
     */
    private var mLoadErrorListener: LoadErrorListener? = null

    /**
     * 所有状态监听
     */
    private var mLoadStatusListener: LoadStatusListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        // 根据布局的类型 创建不同的ViewHolder
        val item = itemStyle.getViewItem(viewType)
            ?: throw RuntimeException("itemStyle.getViewItem is null.")
        val view = item.getItemViewAny(parent.context, parent) as? View
            ?: throw RuntimeException("itemStyle.getItemViewAny is null.")
        val viewHolder = BaseViewHolder(view)

        if (item.openClick()) {
            // itemView点击事件设置监听
            setListener(viewHolder, item.openShake())
            // childView点击事件设置监听
            setChildListener(viewHolder, item.openShake())
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        convert(holder, mData[position])
        callEnd(position)
    }

    override fun onViewRecycled(holder: BaseViewHolder) {
        super.onViewRecycled(holder)
        viewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    /**
     * 根据position获取当前Item的布局类型
     *
     * @param position 下标
     * @return 返回下标类型
     */
    override fun getItemViewType(position: Int): Int {
        // 如果有多样式，需要判断
        return if (hasMultiStyle()) {
            itemStyle.getItemViewType(mData[position], position)
        } else super.getItemViewType(position)
    }

    /**
     * 自动加载数据
     *
     * @param position 当前下标
     */
    private fun callEnd(position: Int) {
        if (!hasLoadMore || mAutoLoadMoreListener == null) return

        val currentPosition = itemCount - position
        // 回调加载更多 关闭开关
        if (currentPosition == loadMoreNum) {
            updateStatus(AdapterStatus.STATUS_ITEM_MORE)
            hasLoadMore = false
            // 防止更新过快导致 RecyclerView 还处于锁定状态，直接更新数据
            val value = ValueAnimator.ofInt(0, 1)
            value.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mAutoLoadMoreListener?.autoLoadMore()
                }
            })
            value.duration = 50
            value.start()
        }
    }

    /**
     * 更新适配器当前加载状态
     * @param status [AdapterStatus]
     */
    private fun updateStatus(@AdapterStatus status: Int) {
        if (pageLayout == null || itemLayout == null) {
            return
        }
        (pageLayout as? ViewGroup)?.children?.forEach {
            it.visibility = View.GONE
        }
        (itemLayout as? ViewGroup)?.children?.forEach {
            it.visibility = View.GONE
        }
        when (status) {
            AdapterStatus.STATUS_PAGE_LOADING -> {
                currentPageStatus = status
                clearData()
                pageLoadingView?.visibility = View.VISIBLE
                notifyDataSetChanged()
            }

            AdapterStatus.STATUS_PAGE_EMPTY -> {
                currentPageStatus = status
                clearData()
                pageEmptyView?.visibility = View.VISIBLE
                notifyDataSetChanged()
            }

            AdapterStatus.STATUS_PAGE_NETWORK -> {
                currentItemStatus = status
                clearData()
                pageNetworkView?.visibility = View.VISIBLE
                notifyDataSetChanged()
            }

            AdapterStatus.STATUS_PAGE_ERROR -> {
                currentPageStatus = status
                clearData()
                pageErrorView?.visibility = View.VISIBLE
                notifyDataSetChanged()
            }

            AdapterStatus.STATUS_PAGE_COMPLETED -> {
                currentPageStatus = status
                pageLayout?.run(this::removeFooter)
            }

            AdapterStatus.STATUS_ITEM_MORE -> {
                currentItemStatus = status
                loadMoreView?.visibility = View.VISIBLE
            }

            AdapterStatus.STATUS_ITEM_END -> {
                currentItemStatus = status
                loadEndView?.visibility = View.VISIBLE
                if (!isPageCompleted()) {
                    pageLayout?.run(this::removeFooter)
                }
            }

            AdapterStatus.STATUS_ITEM_ERROR -> {
                currentItemStatus = status
                loadErrorView?.visibility = View.VISIBLE
            }

            else -> {}
        }
        mLoadStatusListener?.onChangeStatus(status)
    }

    /**
     * 判断是否有多样式布局
     *
     * @return boolean
     */
    private fun hasMultiStyle(): Boolean {
        return itemStyle.getItemViewStylesCount() > 0
    }

    /**
     * itemView 绘制方法
     *
     * @param holder
     * @param entity
     */
    private fun convert(holder: BaseViewHolder, entity: T) {
        itemStyle.convert(holder, entity, holder.layoutPosition)
    }

    /**
     * itemView 回收资源回收
     *
     * @param holder
     */
    private fun viewRecycled(holder: BaseViewHolder) {
        itemStyle.viewRecycled(holder)
    }

    /**
     * 设置错误回调逻辑
     */
    private fun bindLoadErrorListener() {
        mLoadResource?.apply {
            pageLayout?.findViewById<View>(pageReloadId())?.setOnClickListener {
                updateStatus(AdapterStatus.STATUS_PAGE_LOADING)
                mLoadErrorListener?.pageReload()
            }

            itemLayout?.findViewById<View>(itemReloadId())?.setOnClickListener {
                updateStatus(AdapterStatus.STATUS_ITEM_MORE)
                mLoadErrorListener?.itemReload()
            }
        }
    }

    /**
     * @param viewHolder 缓存view
     * @return 获取当前数据下标
     */
    private fun getPosition(viewHolder: BaseViewHolder): Int {
        return viewHolder.layoutPosition - (proxyAdapter?.getHeaderCount() ?: 0)
    }

    /**
     * 设置子点击监听事件
     *
     * @param viewHolder view复用器
     */
    protected fun setChildListener(viewHolder: BaseViewHolder, shake: Boolean) {
        mOnItemChildChange?.run {
            for (childClickId in childClickIds) {
                val view = viewHolder.itemView.findViewById<View>(childClickId) ?: continue
                view.setOnClickListener {
                    if (shake) {
                        val timeSpan = System.currentTimeMillis() - lastClickTime
                        if (timeSpan < itemClickTimeSpan) {
                            return@setOnClickListener
                        }
                        lastClickTime = System.currentTimeMillis().toInt()
                    }
                    val position = getPosition(viewHolder)
                    if (position >= 0) {
                        onItemChildClick(view, mData[position], position)
                    }
                }
            }
        }
    }

    /**
     * 设置点击监听事件
     *
     * @param viewHolder view复用器
     */
    protected fun setListener(viewHolder: BaseViewHolder, shake: Boolean) {
        // 阻塞事件
        mOnItemClickListener?.run {
            viewHolder.itemView.setOnClickListener { v: View ->
                val position = getPosition(viewHolder)
                if (shake) {
                    val timeSpan = System.currentTimeMillis() - lastClickTime
                    if (timeSpan < itemClickTimeSpan) {
                        return@setOnClickListener
                    }
                    lastClickTime = System.currentTimeMillis().toInt()
                }
                if (position >= 0) {
                    onItemClick(v, mData[position], position)
                }
            }
        }
        mOnItemLongClickListener?.run {
            viewHolder.itemView.setOnLongClickListener { v: View ->
                val position = getPosition(viewHolder)
                if (position >= 0) {
                    return@setOnLongClickListener onItemLongClick(v, mData[position], position)
                }
                true
            }
        }
    }

    /**
     * 使用底部加载状态时，适配器需要获取代理实例
     * 创建Adapter后最终通过getProxyAdapter 后设置到RecyclerView中
     */
    private fun getProxy(): ProxyAdapter {
        recyclerView
            ?: throw RuntimeException("BaseViewAdapter.recyclerView is null, use Adapter.bindRecyclerView()")

        if (proxyAdapter == null) {
            proxyAdapter =
                ProxyAdapter(recyclerView!!, this as RecyclerView.Adapter<RecyclerView.ViewHolder>)
        }
        return proxyAdapter as ProxyAdapter
    }

    /**
     * 适配器绑定至RecyclerView
     */
    fun bindRecyclerView(
        recyclerView: RecyclerView,
        loadResource: LoadResource = DefaultLoadResource(),
        loadStateEnable: Boolean = true
    ) {
        this.recyclerView = recyclerView
        this.recyclerView?.adapter = getProxy()
        if (loadStateEnable) {
            setLoadResource(loadResource)
            initStatusView()
            pageLoading()
        }
    }

    /**
     * 获取当前绑定的RecyclerView
     */
    fun findRecyclerView(): RecyclerView? {
        return this.recyclerView
    }

    /**
     * 添加头部view
     *
     * @param view
     */
    fun addHeader(view: View) {
        getProxy().addHeaderView(view)
    }

    /**
     * 添加底部view
     *
     * @param view
     */
    fun addFooter(view: View) {
        getProxy().addFooterView(view)
    }

    /**
     * 添加底部view 每一次设置到底部view组的顶部
     *
     * @param view
     */
    fun addFooterAtTop(view: View) {
        getProxy().addFooterViewAtTop(view)
    }

    /**
     * 删除头部view
     *
     * @param view
     */
    fun removeHeader(view: View) {
        getProxy().removeHeaderView(view)
    }

    /**
     * 删除底部view
     *
     * @param view
     */
    fun removeFooter(view: View) {
        getProxy().removeFooterView(view)
    }

    /**
     * 添加数据源
     * @param data 数据集合
     */
    fun addData(data: List<T>) {
        mData.addAll(data)
    }

    /**
     * 添加数据
     * @param data 单个数据add
     */
    fun addData(data: T) {
        mData.add(data)
    }

    /**
     * 清空数据源
     */
    fun clearData() {
        mData.clear()
    }

    /**
     * 更新数据源
     * @param data 数据集合
     */
    fun updateData(data: List<T>) {
        mData.clear()
        mData.addAll(data)
    }

    /**
     * 获取数据源
     */
    fun getData() = mData

    /**
     * 根据下标获取当前下标数据实体
     */
    fun getItemByPosition(position: Int) = mData[position]

    /**
     * 添加多类型样式方法
     *
     * @param item 样式类型
     */
    fun addItemStyles(item: BaseViewItem<T>?) {
        itemStyle.addStyles(item)
    }

    /**
     * 获取样式管理器 @see [ViewItemManager]
     */
    fun getItemStyles() = itemStyle

    /**
     * 初始化列表状态view
     * pageLayout（页面加载中、页面加载失败、页面空数据）
     * itemLayout （加载更多、加载错误、加载到未页）
     * @param isInit 是否是首次初始化状态
     */
    fun initStatusView(isInit: Boolean = true) {
        // 开启loadMore模式
        hasLoadMore = true
        currentPageStatus = AdapterStatus.UNKNOWN

        // 初始化LoadResource
        mLoadResource ?: kotlin.run {
            mLoadResource = ViewLoadManager.getInstance().getLoadResource()
        }

        // 设置pageLayout and itemLayout
        mLoadResource?.apply {
            // pageLayout
            pageLayout ?: kotlin.run {
                pageLayout = LayoutInflater.from(context)
                    .inflate(pageLayoutId(), FrameLayout(context), false)
            }
            pageLayout?.run {
                pageLoadingView = findViewById(pageLoadingId())
                pageEmptyView = findViewById(pageEmptyId())
                pageErrorView = findViewById(pageErrorId())
                pageNetworkView = findViewById(pageNetworkId())
                addFooter(this)
            }

            // itemLayout
            itemLayout ?: kotlin.run {
                itemLayout = LayoutInflater.from(context)
                    .inflate(itemLayoutId(), FrameLayout(context), false)
            }
            itemLayout?.run {
                loadMoreView = findViewById(itemLoadMoreId())
                loadEndView = findViewById(itemLoadEndId())
                loadErrorView = findViewById(itemLoadErrorId())
                addFooter(this)
            }
        }

        if (isInit) {
            updateStatus(AdapterStatus.STATUS_INIT)
        }
    }

    /**
     * 重置适配器加载状态至初始化状态
     */
    fun reInitStatusView() {
        pageLayout?.run(this@BaseViewAdapter::removeFooter)
        itemLayout?.run(this@BaseViewAdapter::removeFooter)
        initStatusView(isInit = false)
        bindLoadErrorListener()

        // item状态已更改同步page、item状态更新
        if (currentItemStatus != -1) {
            updateStatus(currentPageStatus)
            updateStatus(currentItemStatus)
            // item状态未更改，同步page状态更新
        } else {
            updateStatus(currentPageStatus)
        }
    }

    /**
     * 开启加载更多状态
     */
    fun openLoadMore() {
        hasLoadMore = true
        updateStatus(AdapterStatus.STATUS_INIT)
    }

    /**
     * 设置页面loading状态
     */
    fun pageLoading() {
        if (isPageCompleted()) {
            reInitStatusView()
        }
        updateStatus(AdapterStatus.STATUS_PAGE_LOADING)
    }

    /**
     * 设置页面空数据状态
     */
    fun pageEmpty() {
        if (isPageCompleted()) {
            reInitStatusView()
        }
        updateStatus(AdapterStatus.STATUS_PAGE_EMPTY)
    }

    /**
     * 设置页面错误状态
     */
    fun pageError() {
        if (isPageCompleted()) {
            reInitStatusView()
        }
        updateStatus(AdapterStatus.STATUS_PAGE_ERROR)
    }

    fun pageNetwork() {
        if (isPageCompleted()) {
            reInitStatusView()
        }
        updateStatus(AdapterStatus.STATUS_PAGE_NETWORK)
    }

    /**
     * 设置页面数据加载完毕状态进入item状态
     */
    fun pageCompleted() {
        updateStatus(AdapterStatus.STATUS_PAGE_COMPLETED)
    }

    /**
     * 加载更多完成
     */
    fun loadMoreCompleted() {
        hasLoadMore = true
        notifyDataSetChanged()
    }

    /**
     * 设置未页状态
     */
    fun loadMoreEnd() {
        // 添加底部布局
        if (proxyAdapter == null) {
            getProxy()
        }
        updateStatus(AdapterStatus.STATUS_ITEM_END)
        hasLoadMore = false
        notifyDataSetChanged()
    }

    /**
     * 加载更多完成
     */
    fun loadMoreCompleted(count: Int) {
        hasLoadMore = true
        val startIndex = mData.size - count
        notifyItemRangeInserted(startIndex, count)
    }

    /**
     * 设置未页状态
     */
    fun loadMoreEnd(count: Int) {
        // 添加底部布局
        if (proxyAdapter == null) {
            getProxy()
        }
        updateStatus(AdapterStatus.STATUS_ITEM_END)
        hasLoadMore = false
        val startIndex = mData.size - count
        notifyItemRangeInserted(startIndex, count)
    }

    /**
     * 设置item错误
     */
    fun loadFailed() {
        updateStatus(AdapterStatus.STATUS_ITEM_ERROR)
    }

    fun isPageCompleted(): Boolean {
        return currentPageStatus == AdapterStatus.STATUS_PAGE_COMPLETED
    }

    fun isPageError(): Boolean {
        return currentPageStatus == AdapterStatus.STATUS_PAGE_ERROR
    }

    fun isPageEmpty(): Boolean {
        return currentPageStatus == AdapterStatus.STATUS_PAGE_EMPTY
    }

    fun isPageLoading(): Boolean {
        return currentPageStatus == AdapterStatus.STATUS_PAGE_LOADING
    }

    fun isItemEnd(): Boolean {
        return currentItemStatus == AdapterStatus.STATUS_ITEM_END
    }

    fun isItemError(): Boolean {
        return currentItemStatus == AdapterStatus.STATUS_ITEM_ERROR
    }

    fun isItemMore(): Boolean {
        return currentItemStatus == AdapterStatus.STATUS_ITEM_MORE
    }

    /**
     * 设置加载更多最低阈值
     *
     * @param num num = 5 则为 20-5 = 滑动到15项的时候加载
     */
    fun setLoadMoreNum(num: Int) {
        loadMoreNum = num
    }

    /**
     * 条目点击监听接口
     */
    interface OnItemClickListener<T> {
        /**
         * 点击事件监听
         *
         * @param view     item
         * @param entity   数据
         * @param position 下标
         */
        fun onItemClick(view: View, entity: T, position: Int)
    }

    /**
     * 返回所有子view
     *
     * @param <T>
     */
    interface OnItemChildView<T> {
        /**
         * item子view点击事件
         *
         * @param view     控件
         * @param entity   数据
         * @param position 下标
         */
        fun onItemChildClick(view: View, entity: T, position: Int)
    }

    /**
     * 条目长按监听接口
     */
    interface OnItemLongClickListener<T> {
        /**
         * 长按事件监听
         *
         * @param view     item
         * @param entity   数据
         * @param position 下标
         * @return 返回消费事件
         */
        fun onItemLongClick(view: View, entity: T, position: Int): Boolean
    }

    /**
     * 自动加载更多监听接口
     */
    interface AutoLoadMoreListener {
        /**
         * 回调需要加载更多
         */
        fun autoLoadMore()
    }

    interface LoadErrorListener {
        /**
         * 初始页面错误状态重试 （适用于刷新页面失败）
         */
        fun pageReload()

        /**
         * 列表数据错误状态重试 ( 适用于分页失败 )
         */
        fun itemReload()
    }

    /**
     * 添加item点击监听接口
     *
     * @param onItemClickListener item点击监听接口
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>?) {
        mOnItemClickListener = onItemClickListener
    }

    /**
     * 设置item长按点击事件
     *
     * @param onItemLongClickListener item长按点击事件接口
     */
    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener<T>?) {
        mOnItemLongClickListener = onItemLongClickListener
    }

    /**
     * 设置item子view点击id组
     *
     */
    fun addItemChildIds(vararg childClickIds: Int) {
        this.childClickIds.clear()
        this.childClickIds.addAll(childClickIds.toList())
    }

    /**
     * 设置item子view点击事件
     *
     * @param onItemChildView item子view点击事件监听接口
     */
    fun setOnItemChildClickListener(onItemChildView: OnItemChildView<T>?) {
        this.mOnItemChildChange = onItemChildView
    }

    /**
     * 设置item子view点击事件
     *
     * @param onItemChildView item子view点击事件监听接口
     * @param childClickIds   注册子view id
     */
    fun setOnItemChildClickListener(
        onItemChildView: OnItemChildView<T>,
        vararg childClickIds: Int
    ) {
        this.childClickIds.clear()
        this.childClickIds.addAll(childClickIds.toList())
        this.mOnItemChildChange = onItemChildView
    }

    /**
     * 设置自动加载更多监听
     *
     * @param autoLoadMoreListener 自动加载更多监听接口
     */
    fun setAutoLoadMoreListener(autoLoadMoreListener: AutoLoadMoreListener?) {
        this.mAutoLoadMoreListener = autoLoadMoreListener
    }

    /**
     * 设置自定义 page状态资源布局接口
     *
     * @param loadResource page状态资源布局接口
     */
    fun setLoadResource(loadResource: LoadResource) {
        this.mLoadResource = loadResource
    }

    /**
     * 设置错误重试接口
     *
     * @param loadErrorListener 错误重试接口
     */
    fun setLoadErrorListener(loadErrorListener: LoadErrorListener?) {
        mLoadErrorListener = loadErrorListener
        bindLoadErrorListener()
    }

    /**
     * 是遏制加载状态监听接口
     *
     * @param loadStatusListener 加载状态接口
     */
    fun setLoadStatusListener(loadStatusListener: LoadStatusListener) {
        mLoadStatusListener = loadStatusListener
    }
}

/**
 * 适配器加载状态
 */
@IntDef(
    AdapterStatus.UNKNOWN,
    AdapterStatus.STATUS_INIT,
    AdapterStatus.STATUS_PAGE_LOADING,
    AdapterStatus.STATUS_PAGE_EMPTY,
    AdapterStatus.STATUS_PAGE_ERROR,
    AdapterStatus.STATUS_PAGE_NETWORK,
    AdapterStatus.STATUS_PAGE_COMPLETED,
    AdapterStatus.STATUS_ITEM_MORE,
    AdapterStatus.STATUS_ITEM_END,
    AdapterStatus.STATUS_ITEM_ERROR
)
@Retention(AnnotationRetention.SOURCE)
annotation class AdapterStatus {
    companion object {
        const val UNKNOWN = -1
        const val STATUS_INIT = 0
        const val STATUS_PAGE_LOADING = 1
        const val STATUS_PAGE_EMPTY = 2
        const val STATUS_PAGE_ERROR = 3
        const val STATUS_PAGE_NETWORK = 4
        const val STATUS_PAGE_COMPLETED = 5
        const val STATUS_ITEM_MORE = 6
        const val STATUS_ITEM_END = 7
        const val STATUS_ITEM_ERROR = 8
    }
}