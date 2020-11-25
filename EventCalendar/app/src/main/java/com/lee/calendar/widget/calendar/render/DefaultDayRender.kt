package com.lee.calendar.widget.calendar.render

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import androidx.core.content.ContextCompat
import com.lee.calendar.R
import com.lee.calendar.widget.calendar.entity.DayEntity
import com.lee.calendar.widget.calendar.entity.DayStatus
import com.lee.calendar.ex.dp2px
import com.lee.calendar.ex.sp2px
import com.lee.calendar.widget.calendar.MonthView

/**
 * @author jv.lee
 * @date 2020/11/25
 * @description
 */
open class DefaultDayRender(context: Context, defStyleRes: Int) :
    IDayRender(context, R.styleable.MonthView, defStyleRes) {

    protected val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    //颜色属性
    protected var updateBackgroundColor: Int = 0
    protected var updateStrokeColor: Int = 0
    protected var updateSelectedColor: Int = 0
    protected var overBackgroundColor: Int = 0
    protected var overStrokeColor: Int = 0
    protected var overSelectedColor: Int = 0
    protected var todayStrokeColor: Int = 0
    protected var todayBackgroundColor: Int = 0
    protected var dotColor: Int = 0
    protected var dotPairColor: Int = 0
    protected var goneTextColor: Int = 0
    protected var textColor: Int = 0
    protected var textPairColor: Int = 0

    //size属性及显示字符
    protected var strokeWidth: Float = 0F
    protected var paddingSize: Float = 0F
    protected var dotSize: Float = 0F
    protected var textSize: Float = 0F

    protected lateinit var childSize: MonthView.ChildSize
    protected lateinit var canvas: Canvas
    protected lateinit var entity: DayEntity
    protected var index: Int = 0

    override fun initAttrs(typedArray: TypedArray) {
        typedArray.run {
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
                context.dp2px(1)
            )
            paddingSize = getDimension(
                R.styleable.MonthView_month_paddingSize,
                context.dp2px(6)
            )
            dotSize = getDimension(
                R.styleable.MonthView_month_dotSize,
                context.dp2px(5)
            )
            textSize = getDimension(
                R.styleable.MonthView_month_textSize,
                context.sp2px(15)
            )
        }
    }

    override fun initParams(
        childSize: MonthView.ChildSize,
        canvas: Canvas,
        entity: DayEntity,
        index: Int
    ) {
        this.childSize = childSize
        this.canvas = canvas
        this.entity = entity
        this.index = index
    }

    override fun draw() {
        drawBackgroundMode()
        drawSelectedBackground()
        drawToDayBackground()
        drawDelayUpdateDot()
        drawNumberText()
    }

    private fun drawBackgroundMode() {
        when (entity.backgroundStatus) {
            MonthView.DayBackgroundStatus.STATUS_SINGLE -> drawSingle()
            MonthView.DayBackgroundStatus.STATUS_START -> drawStart()
            MonthView.DayBackgroundStatus.STATUS_CENTER -> drawCenter()
            MonthView.DayBackgroundStatus.STATUS_END -> drawEnd()
        }
    }

    private fun drawSingle() {
        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateBackgroundColor
            else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        canvas.drawCircle(
            childSize.cx,
            childSize.cy,
            (childSize.absSize / 2) - (strokeWidth + paddingSize),
            mPaint
        )

        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateStrokeColor
            else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        canvas.drawCircle(
            childSize.cx,
            childSize.cy,
            (childSize.absSize / 2) - (strokeWidth + paddingSize),
            mPaint
        )
    }

    private fun drawStart() {
        canvas.save()
        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateBackgroundColor
            else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        val path1 = Path().apply {

            val topDimen =
                childSize.cy - ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val bottomDimen =
                childSize.cy + ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val startDimen =
                childSize.cx - ((childSize.absSize / 2) - (paddingSize)) + strokeWidth
            val rectF =
                RectF(startDimen, topDimen, childSize.width + childSize.maxWidth, bottomDimen)
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

            val topDimen =
                childSize.cy - ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val bottomDimen =
                childSize.cy + ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val startDimen =
                childSize.cx - ((childSize.absSize / 2) - (paddingSize)) + strokeWidth
            val rectF =
                RectF(startDimen, topDimen, childSize.width + childSize.maxWidth, bottomDimen)
            val radius = childSize.cx
            val radiusArray = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }

        canvas.drawPath(path2, mPaint)
        canvas.restore()
    }

    private fun drawCenter() {
        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth

        val topDimen =
            childSize.cy - ((childSize.absSize / 2) - (strokeWidth + paddingSize))
        val bottomDimen =
            childSize.cy + ((childSize.absSize / 2) - (strokeWidth + paddingSize))

        val path1 = Path().apply {
            val rectF =
                RectF(
                    childSize.left,
                    topDimen,
                    childSize.right,
                    bottomDimen
                )
            addRect(rectF, Path.Direction.CCW)
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateStrokeColor else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth

        canvas.drawLine(
            childSize.width - childSize.maxWidth,
            topDimen,
            childSize.width,
            topDimen,
            mPaint
        )
        canvas.drawLine(
            childSize.width - childSize.maxWidth,
            bottomDimen,
            childSize.width,
            bottomDimen,
            mPaint
        )
    }

    private fun drawEnd() {
        canvas.save()

        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth

        val path1 = Path().apply {

            val topDimen =
                childSize.cy - ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val bottomDimen =
                childSize.cy + ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val endDimen =
                childSize.cx + ((childSize.absSize / 2) - paddingSize) - strokeWidth
            val rectF = RectF(0f, topDimen, endDimen, bottomDimen)
            val radius = childSize.cx
            val radiusArray = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)

            val dimen = (strokeWidth / 2)
            canvas.clipRect(
                childSize.width - childSize.maxWidth,
                topDimen - strokeWidth,
                endDimen + strokeWidth,
                bottomDimen + strokeWidth
            )
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color =
            if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateStrokeColor
            else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth

        val path2 = Path().apply {

            val topDimen =
                childSize.cy - ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val bottomDimen =
                childSize.cy + ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val endDimen =
                childSize.cx + ((childSize.absSize / 2) - paddingSize) - strokeWidth
            val rectF = RectF(0f, topDimen, endDimen, bottomDimen)
            val radius = childSize.cx
            val radiusArray = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }
        canvas.drawPath(path2, mPaint)
        canvas.restore()
    }

    private fun drawSelectedBackground() {
        if (!entity.isSelected) return

        mPaint.color =
            if (entity.dayStatus == DayStatus.OVER_UPDATE_STATUS) overSelectedColor
            else updateSelectedColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth

        canvas.drawCircle(
            childSize.cx,
            childSize.cy,
            (childSize.absSize / 2) - ((strokeWidth + (strokeWidth / 2)) + paddingSize),
            mPaint
        )
    }

    private fun drawToDayBackground() {
        if (!entity.isToDay) return
        mPaint.color = todayBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        canvas.drawCircle(
            childSize.cx,
            childSize.cy,
            (childSize.absSize / 2) - ((strokeWidth + (strokeWidth / 2)) + paddingSize),
            mPaint
        )
    }

    private fun drawDelayUpdateDot() {
        if (entity.dayStatus != DayStatus.DELAY_UPDATE_STATUS) return
        mPaint.style = Paint.Style.FILL
        mPaint.color = if (entity.isToDay) dotPairColor else dotColor

        val rect = Rect()
        val text = entity.day.toString()
        mPaint.getTextBounds(text, 0, text.length, rect)

        val centerWidth = (childSize.cx + (strokeWidth / 2))
        val centerHeight =
            childSize.height - dotSize - (paddingSize * 2)
        canvas.drawCircle(centerWidth, centerHeight, (dotSize / 2), mPaint)
    }

    private fun drawNumberText() {
        mPaint.style = Paint.Style.FILL
        mPaint.color =
            if (entity.isToDay) textPairColor else if (!entity.isToMonth) goneTextColor else textColor
        mPaint.textSize = textSize

        val text = entity.day.toString()
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

}