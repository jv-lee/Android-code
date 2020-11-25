package com.lee.calendar.widget.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.annotation.IntDef
import com.lee.calendar.R
import com.lee.calendar.entity.DayEntity
import kotlin.math.abs

/**
 * @author jv.lee
 * @date 2020/11/12
 * @description
 */
class MonthView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val TAG = "jv.lee"

    private var absSize = 0f
    private var startX = 0f
    private var startY = 0f
    private var mTouchSlop = 0

    private var dayWidth = 0f
    private var dayHeight = 0f

    private var selectedIndex = 0
    private var totalRow = 6
    private val totalCol = 7
    private var mode: Int = MonthMode.MODE_MONTH

    private val dayList = ArrayList<DayEntity>()
    private var dayRender: IDayRender =
        DefaultDayRender(context, R.style.calendar_month_default)

    init {
        //初始化系统拖动阈值
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

        setLayerType(LAYER_TYPE_SOFTWARE, null)
        setMode(mode)
    }

    fun setMode(mode: Int) {
        totalRow = if (mode == MonthMode.MODE_MONTH) 6 else 1
    }

    fun setDayRender(dayRender: IDayRender) {
        this.dayRender = dayRender
    }

    fun bindData(data: ArrayList<DayEntity>) {
        dayList.clear()
        dayList.addAll(data)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        dayWidth = (w / totalCol).toFloat()
        dayHeight = (h / totalRow).toFloat()
        absSize = Math.min(dayWidth, dayHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (dayList.isEmpty()) return

        for (rowIndex in 0 until totalRow) {
            for (colIndex in 0 until totalCol) {
                drawDayView(canvas, rowIndex, colIndex)
            }
        }
    }

    /*
     * 触摸事件为了确定点击的位置日期
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = e.x
                startY = e.y
            }
            MotionEvent.ACTION_UP -> {
                val endY: Float = e.y
                val endX: Float = e.x
                val distanceX = abs(endX - startX)
                val distanceY = abs(endY - startY)
                if (distanceX < mTouchSlop && distanceY < mTouchSlop) {
                    val col = (startX / dayWidth).toInt()
                    val row = (startY / dayHeight).toInt()
                    val index = positionToIndex(row, col)
                    onItemClick(index)
                }
            }
        }
        return true
    }

    private fun drawDayView(canvas: Canvas, row: Int, col: Int) {
        val childSize = buildChildSize(row, col)
        val index = positionToIndex(row, col)
        val entity = indexToDayEntity(index)
        if (entity.isSelected) selectedIndex = index

        dayRender.initParams(childSize, canvas, entity, index)
        dayRender.draw()
    }

    /**
     * 通过x坐标/y坐标计算出数据下标
     */
    private fun positionToIndex(rowIndex: Int, colIndex: Int): Int {
        val a = rowIndex * 7
        val b = colIndex
        return (a + b)
    }

    private fun indexToDayEntity(index: Int): DayEntity {
        return dayList[index]
    }

    private fun onItemClick(index: Int) {
        if (selectedIndex == index) return //防止重复点击
        val dayEntity = dayList[index]
        if (dayEntity.isToMonth) {
            dayEntity.isSelected = true
            dayList[selectedIndex].isSelected = false
            mOnDaySelectedListener?.onDayClick(index, dayEntity)
            invalidate()
        }
    }

    data class ChildSize(
        val maxWidth: Float,
        val maxHeight: Float,
        val width: Float,
        val height: Float,
        val left: Float,
        val top: Float,
        val right: Float,
        val bottom: Float,
        val absSize: Float,
        val cx: Float,
        val cy: Float
    )

    private fun buildChildSize(rowIndex: Int, colIndex: Int): ChildSize {
        val rowWidth = dayWidth * colIndex
        val colHeight = dayHeight * rowIndex
        val width = dayWidth + rowWidth
        val height = dayHeight + colHeight
        val left = (width - dayWidth)
        val top = (height - dayHeight)
        val right = width
        val bottom = height
        val cx = width - (dayWidth / 2)
        val cy = height - (dayHeight / 2)

        return ChildSize(
            dayWidth,
            dayHeight,
            width,
            height,
            left,
            top,
            right,
            bottom,
            absSize,
            cx,
            cy
        )
    }

    private var mOnDaySelectedListener: OnDaySelectedListener? = null

    fun setOnDaySelectedListener(onDaySelectedListener: OnDaySelectedListener) {
        this.mOnDaySelectedListener = onDaySelectedListener
    }

    interface OnDaySelectedListener {
        fun onDayClick(position: Int, entity: DayEntity)
    }

    @IntDef(
        DayBackgroundStatus.STATUS_SINGLE,
        DayBackgroundStatus.STATUS_START,
        DayBackgroundStatus.STATUS_CENTER,
        DayBackgroundStatus.STATUS_END
    )
    @Retention(AnnotationRetention.SOURCE)
    @MustBeDocumented
    annotation class DayBackgroundStatus {
        companion object {
            const val STATUS_GONE = 0
            const val STATUS_SINGLE = 1
            const val STATUS_START = 2
            const val STATUS_CENTER = 3
            const val STATUS_END = 4

        }
    }

    @IntDef(
        MonthMode.MODE_MONTH,
        MonthMode.MODE_WEEK
    )
    @Retention(AnnotationRetention.SOURCE)
    @MustBeDocumented
    annotation class MonthMode {
        companion object {
            const val MODE_MONTH = 0
            const val MODE_WEEK = 1

        }
    }
}

