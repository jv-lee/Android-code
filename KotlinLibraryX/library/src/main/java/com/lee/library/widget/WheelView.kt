package com.lee.library.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.lee.library.R
import kotlin.math.roundToInt

/**
 * @author jv.lee
 * @date 2021/1/6
 * @description
 */
class WheelView : RecyclerView {

    companion object {
        const val DEFAULT_ITEM_HEIGHT = 40
    }

    private var mScrollY = 0F
    private var selectPosition = 0
    private var oldSelectPosition = 0
    private val linearSnapHelper = LinearSnapHelper()

    private var selectedTextColor: Int = 0
    private var unSelectedTextColor: Int = 0

    private var selectedTextSize: Float = 0F
    private var unSelectedTextSize: Float = 0F

    private val itemHeight = dp2px(DEFAULT_ITEM_HEIGHT)

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.WheelView)

        selectedTextColor =
            typeArray.getColor(R.styleable.WheelView_selected_text_color, Color.BLACK)
        unSelectedTextColor =
            typeArray.getColor(R.styleable.WheelView_unSelected_text_color, Color.GRAY)
        selectedTextSize =
            typeArray.getDimension(R.styleable.WheelView_selected_text_size, 20f)
        unSelectedTextSize =
            typeArray.getDimension(R.styleable.WheelView_unSelected_text_size, 16f)

        typeArray.recycle()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        oldSelectPosition = selectPosition

        mScrollY += dy
        selectPosition = (mScrollY / itemHeight).roundToInt()

        if (oldSelectPosition != selectPosition) {
            adapter?.notifyDataSetChanged()
        }
    }

    private inner class SelectAdapter<T>(val data: List<T>, val dataFormat: DataFormat<T>) :
        RecyclerView.Adapter<SelectAdapter<T>.SelectViewHolder>() {

        var mSelectedListener: SelectedListener<T>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
            return SelectViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_select, parent, false)
            )
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
            holder.bindView(data[position], position)
        }

        private inner class SelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(item: T, position: Int) {
                val textView = itemView.findViewById<TextView>(R.id.tv_text)
                textView.text = dataFormat.format(item)
                if (selectPosition == position) {
                    textView.textSize = selectedTextSize
                    textView.setTextColor(selectedTextColor)
                    mSelectedListener?.selected(item)
                } else {
                    textView.textSize = unSelectedTextSize
                    textView.setTextColor(unSelectedTextColor)
                }

            }
        }

    }

    private inner class PaddingDecoration : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            if (position == 0) outRect.top = itemHeight.toInt()

            val itemCount = parent.adapter?.itemCount ?: return
            if (position == itemCount - 1) outRect.bottom = itemHeight.toInt()
        }

    }

    internal fun dp2px(dpValue: Int): Float {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f)
    }

    interface DataFormat<T> {
        fun format(item: T): String
    }

    interface SelectedListener<T> {
        fun selected(item: T)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> bindData(
        data: List<T>,
        dataFormat: DataFormat<T>,
        selectedListener: SelectedListener<T>,
        startPosition: Int = 0
    ) {
        layoutManager = LinearLayoutManager(context)
        linearSnapHelper.attachToRecyclerView(this)
        adapter = SelectAdapter(data, dataFormat)
        addItemDecoration(PaddingDecoration())
        (adapter as? SelectAdapter<T>)?.mSelectedListener = selectedListener
        smoothScrollBy(0, (startPosition * itemHeight).toInt())
    }

    init {
        overScrollMode = View.OVER_SCROLL_NEVER
    }

}