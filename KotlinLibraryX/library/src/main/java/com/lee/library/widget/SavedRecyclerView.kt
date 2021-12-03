package com.lee.library.widget

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

/**
 * @author jv.lee
 * @date 2021/9/24
 * @description 可保存恢复子View状态的RecyclerView
 *
 * RecyclerView内部重写了ViewGroup的  dispatchSaveInstanceState/dispatchRestoreInstanceState方法
 * 内部直接调用
 * dispatchSaveInstanceState.dispatchFreezeSelfOnly(container);
 * dispatchRestoreInstanceState.dispatchThawSelfOnly
 * 这种情况RecyclerView只会保存恢复自身状态 不会处理子View状态保存恢复
 */
class SavedRecyclerView : RecyclerView {

    //存储需要被记录状态的view类型 根据recycler adapter 不同item类型，需要保存的存储类型
    private val stateTypes = HashSet<Class<*>>()

    constructor(context: Context) : super(context, null, 0)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>?) {
        super.dispatchSaveInstanceState(container)
        children.forEach { view ->
            if (stateTypes.contains(view.javaClass)) {
                view.saveHierarchyState(container)
            }
        }
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>?) {
        super.dispatchRestoreInstanceState(container)
        post {
            children.forEach { view ->
                if (stateTypes.contains(view.javaClass)) {
                    view.restoreHierarchyState(container)
                }
            }
        }
    }

    /**
     * 添加需要记录状态的view类型
     */
    fun addSaveStateViewType(type: Class<*>) {
        stateTypes.add(type)
    }

}