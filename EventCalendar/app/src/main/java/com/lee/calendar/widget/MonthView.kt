package com.lee.calendar.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import com.lee.calendar.R
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.entity.DayStatus
import com.lee.calendar.ex.dp2px
import com.lee.calendar.ex.sp2px
import kotlin.math.abs

/**
 * @author jv.lee
 * @date 2020/11/12
 * @description
 */
class MonthView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val TAG = "jv.lee"

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var absSize = 0f

    //颜色属性
    private val updateBackgroundColor: Int
    private val updateStrokeColor: Int
    private val updateSelectedColor: Int
    private val overBackgroundColor: Int
    private val overStrokeColor: Int
    private val overSelectedColor: Int
    private val todayStrokeColor:Int
    private val todayBackgroundColor: Int
    private val dotColor: Int
    private val dotPairColor: Int
    private val goneTextColor: Int
    private val textColor: Int
    private val textPairColor: Int

    //size属性及显示字符
    private val strokeWidth: Float
    private val paddingSize: Float
    private val dotSize: Float
    private val textSize: Float

    private var startX = 0f
    private var startY = 0f
    private var mTouchSlop = 0

    private var dayWidth = 0f
    private var dayHeight = 0f

    private var totalRow = 6
    private val totalCol = 7
    private var mode: Int = MonthMode.MODE_MONTH

    private val dayList = ArrayList<DayEntity>()
    private var selectedIndex = 0

    init {
        //初始化系统拖动阈值
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        context.obtainStyledAttributes(attributeSet, R.styleable.MonthView).run {

            mode = getInt(R.styleable.MonthView_month_mode, MonthMode.MODE_MONTH)
            setMode(mode)

            updateBackgroundColor = getColor(
                R.styleable.MonthView_month_updateBackgroundColor,
                ContextCompat.getColor(context, android.R.color.holo_blue_light)
            )
            updateStrokeColor = getColor(
                R.styleable.MonthView_month_updateStrokeColor,
                ContextCompat.getColor(context, android.R.color.holo_blue_dark)
            )
            updateSelectedColor = getColor(
                R.styleable.MonthView_month_updateSelectedColor,
                ContextCompat.getColor(context, android.R.color.holo_blue_dark)
            )

            overBackgroundColor = getColor(
                R.styleable.MonthView_month_overBackgroundColor,
                ContextCompat.getColor(context, android.R.color.holo_red_light)
            )
            overStrokeColor = getColor(
                R.styleable.MonthView_month_overStrokeColor,
                ContextCompat.getColor(context, android.R.color.holo_red_dark)
            )
            overSelectedColor = getColor(
                R.styleable.MonthView_month_overSelectedColor,
                ContextCompat.getColor(context, android.R.color.holo_red_dark)
            )

            todayStrokeColor = getColor(
                R.styleable.MonthView_month_todayStrokeColor,
                ContextCompat.getColor(context, android.R.color.holo_orange_dark)
            )
            todayBackgroundColor = getColor(
                R.styleable.MonthView_month_todayBackgroundColor,
                ContextCompat.getColor(context, android.R.color.holo_orange_dark)
            )
            dotColor = getColor(
                R.styleable.MonthView_month_dotColor,
                ContextCompat.getColor(context, android.R.color.holo_orange_dark)
            )
            dotPairColor = getColor(
                R.styleable.MonthView_month_dotPairColor,
                ContextCompat.getColor(context, android.R.color.holo_orange_dark)
            )
            goneTextColor = getColor(
                R.styleable.MonthView_month_goneTextColor,
                ContextCompat.getColor(context, android.R.color.darker_gray)
            )
            textColor = getColor(
                R.styleable.MonthView_month_textColor,
                ContextCompat.getColor(context, android.R.color.black)
            )
            textPairColor = getColor(
                R.styleable.MonthView_month_textPairColor,
                ContextCompat.getColor(context, android.R.color.white)
            )

            strokeWidth = getDimension(
                R.styleable.MonthView_month_strokeWidth,
                dp2px(context, 1)
            )
            paddingSize = getDimension(
                R.styleable.MonthView_month_paddingSize,
                dp2px(context,6)
            )
            dotSize = getDimension(
                R.styleable.MonthView_month_dotSize,
                dp2px(context,5)
            )
            textSize = getDimension(
                R.styleable.MonthView_month_textSize,
                sp2px(context,15)
            )

            recycle()
        }

        mPaint.strokeWidth = strokeWidth
        mPaint.style = Paint.Style.FILL
        mPaint.typeface = Typeface.DEFAULT_BOLD
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    fun setMode(mode: Int) {
        totalRow = if (mode == MonthMode.MODE_MONTH) 6 else 1
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

        drawBackgroundMode(canvas, childSize, index)
        drawSelectedBackground(canvas, childSize, index)
        drawToDayBackground(canvas, childSize, index)
        drawDelayUpdateDot(canvas, childSize, index)
        drawNumberText(canvas, childSize, index)
    }

    private fun drawBackgroundMode(canvas: Canvas, childSize: ChildSize, index: Int) {
        val entity = indexToDayEntity(index)
        when (entity.backgroundStatus) {
            DayBackgroundStatus.STATUS_SINGLE -> drawSingle(canvas, childSize, index)
            DayBackgroundStatus.STATUS_START -> drawStart(canvas, childSize, index)
            DayBackgroundStatus.STATUS_CENTER -> drawCenter(canvas, childSize, index)
            DayBackgroundStatus.STATUS_END -> drawEnd(canvas, childSize, index)
        }
    }

    private fun drawSingle(canvas: Canvas, childSize: ChildSize, index: Int) {
        val entity = indexToDayEntity(index)
        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        canvas.drawCircle(
            childSize.cx,
            childSize.cy,
            (absSize / 2) - (strokeWidth + paddingSize),
            mPaint
        )

        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateStrokeColor else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        canvas.drawCircle(
            childSize.cx,
            childSize.cy,
            (absSize / 2) - (strokeWidth + paddingSize),
            mPaint
        )
    }

    private fun drawStart(canvas: Canvas, childSize: ChildSize, index: Int) {
        val entity = indexToDayEntity(index)
        canvas.save()
        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        val path1 = Path().apply {

            val topDimen = childSize.cy - ((absSize / 2) - (strokeWidth + paddingSize))
            val bottomDimen = childSize.cy + ((absSize / 2) - (strokeWidth + paddingSize))
            val startDimen = childSize.cx - ((absSize / 2) - (paddingSize)) + strokeWidth
            val rectF = RectF(startDimen, topDimen, childSize.width + dayWidth, bottomDimen)
            val radius = childSize.cx
            val radiusArray = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)

            val dimen = (strokeWidth / 2)
            canvas.clipRect(
                startDimen - dimen,
                topDimen - strokeWidth,
                childSize.width,
                bottomDimen + strokeWidth
            )
        }


        canvas.drawPath(path1, mPaint)

        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateStrokeColor else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        val path2 = Path().apply {

            val topDimen = childSize.cy - ((absSize / 2) - (strokeWidth + paddingSize))
            val bottomDimen = childSize.cy + ((absSize / 2) - (strokeWidth + paddingSize))
            val startDimen = childSize.cx - ((absSize / 2) - (paddingSize)) + strokeWidth
            val rectF = RectF(startDimen, topDimen, childSize.width + dayWidth, bottomDimen)
            val radius = childSize.cx
            val radiusArray = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }

        canvas.drawPath(path2, mPaint)
        canvas.restore()
    }

    private fun drawCenter(canvas: Canvas, childSize: ChildSize, index: Int) {
        val entity = indexToDayEntity(index)
        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth

        val topDimen = childSize.cy - ((absSize / 2) - (strokeWidth + paddingSize))
        val bottomDimen = childSize.cy + ((absSize / 2) - (strokeWidth + paddingSize))

        val path1 = Path().apply {
            val rectF =
                RectF(
                    childSize.left - strokeWidth,
                    topDimen,
                    childSize.right + strokeWidth,
                    bottomDimen
                )
            addRect(rectF, Path.Direction.CCW)
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateStrokeColor else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth

        canvas.drawLine(childSize.width - dayWidth, topDimen, childSize.width, topDimen, mPaint)
        canvas.drawLine(
            childSize.width - dayWidth,
            bottomDimen,
            childSize.width,
            bottomDimen,
            mPaint
        )
    }

    private fun drawEnd(canvas: Canvas, childSize: ChildSize, index: Int) {
        val entity = indexToDayEntity(index)
        canvas.save()

        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth

        val path1 = Path().apply {

            val topDimen = childSize.cy - ((absSize / 2) - (strokeWidth + paddingSize))
            val bottomDimen = childSize.cy + ((absSize / 2) - (strokeWidth + paddingSize))
            val endDimen = childSize.cx + ((absSize / 2) - paddingSize) - strokeWidth
            val rectF = RectF(0f, topDimen, endDimen, bottomDimen)
            val radius = childSize.cx
            val radiusArray = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)

            val dimen = (strokeWidth / 2)
            canvas.clipRect(
                childSize.width - dayWidth,
                topDimen - strokeWidth,
                endDimen + strokeWidth,
                bottomDimen + strokeWidth
            )
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateStrokeColor else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth

        val path2 = Path().apply {

            val topDimen = childSize.cy - ((absSize / 2) - (strokeWidth + paddingSize))
            val bottomDimen = childSize.cy + ((absSize / 2) - (strokeWidth + paddingSize))
            val endDimen = childSize.cx + ((absSize / 2) - paddingSize) - strokeWidth
            val rectF = RectF(0f, topDimen, endDimen, bottomDimen)
            val radius = childSize.cx
            val radiusArray = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }
        canvas.drawPath(path2, mPaint)
        canvas.restore()
    }

    private fun drawSelectedBackground(canvas: Canvas, childSize: ChildSize, index: Int) {
        val entity = indexToDayEntity(index)
        if (!entity.isSelected) return

        selectedIndex = index
        setSelectedBackgroundStyle(entity)
        canvas.drawCircle(
            childSize.cx,
            childSize.cy,
            (absSize / 2) - ((strokeWidth + (strokeWidth / 2)) + paddingSize),
            mPaint
        )
    }

    private fun drawToDayBackground(canvas: Canvas, childSize: ChildSize, index: Int) {
        val entity = indexToDayEntity(index)
        if (!entity.isToDay) return
        setTodayStyle()
        canvas.drawCircle(
            childSize.cx,
            childSize.cy,
            (absSize / 2) - ((strokeWidth + (strokeWidth / 2)) + paddingSize),
            mPaint
        )
    }

    private fun drawDelayUpdateDot(canvas: Canvas, childSize: ChildSize, index: Int) {
        val entity = indexToDayEntity(index)
        if (entity.dayStatus != DayStatus.DELAY_UPDATE_STATUS) return
        setDelayDotStyle(entity)

        val rect = Rect()
        val text = indexToDateText(index)
        mPaint.getTextBounds(text, 0, text.length, rect)

        val centerWidth = (childSize.cx + (strokeWidth / 2))
        val centerHeight = childSize.height - dotSize - (paddingSize * 2)
        canvas.drawCircle(centerWidth, centerHeight, (dotSize / 2), mPaint)
    }

    private fun drawNumberText(canvas: Canvas, childSize: ChildSize, index: Int) {
        val entity = indexToDayEntity(index)
        setNumberTextStyle(entity)

        val text = indexToDateText(index)
        val rect = Rect()
        mPaint.getTextBounds(text, 0, text.length, rect)

        val y = childSize.cy + (rect.height() / 2)

        if (text == "1") {
            canvas.drawText(text, childSize.cx - (rect.width() / 2) - (rect.width() / 2), y, mPaint)
        } else if (text == "11") {
            canvas.drawText(text, childSize.cx - (rect.width() / 1.5).toInt(), y, mPaint)
        } else if (text.startsWith("1")) {
            canvas.drawText(text, childSize.cx - (rect.width() / 1.8).toInt(), y, mPaint)
        } else {
            canvas.drawText(text, childSize.cx - (rect.width() / 2), y, mPaint)
        }
    }

    /**
     * 通过x坐标/y坐标计算出数据下标
     */
    private fun positionToIndex(rowIndex: Int, colIndex: Int): Int {
        val a = rowIndex * 7
        val b = colIndex
        return (a + b)
    }

    /**
     *  根据下标获取日期字符
     */
    private fun indexToDateText(index: Int): String {
        if (dayList.size > index) {
            return dayList[index].day.toString()
        }
        return ""
    }

    private fun indexToDayEntity(index: Int): DayEntity {
        return dayList[index]
    }

    private fun setSelectedBackgroundStyle(entity: DayEntity) {
        mPaint.color =
            if (entity.dayStatus == DayStatus.OVER_UPDATE_STATUS) overSelectedColor else updateSelectedColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth

    }

    private fun setTodayStyle() {
        mPaint.color = todayBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
    }

    private fun setDelayDotStyle(entity: DayEntity) {
        mPaint.style = Paint.Style.FILL
        mPaint.color = if (entity.isToDay) dotPairColor else dotColor
    }


    private fun setNumberTextStyle(entity: DayEntity) {
        mPaint.style = Paint.Style.FILL
        mPaint.color =
            if (entity.isToDay) textPairColor else if (!entity.isToMonth) goneTextColor else textColor
        mPaint.textSize = textSize
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
        val left = (width - dayWidth) + strokeWidth
        val top = (height - dayHeight)
        val right = width - strokeWidth
        val bottom = height - strokeWidth
        val cx = width - (dayWidth / 2)
        val cy = height - (dayHeight / 2)

        return ChildSize(width, height, left, top, right, bottom, absSize, cx, cy)
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

