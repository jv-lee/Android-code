package com.lee.calendar.widget.calendar

import android.content.Context
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import com.lee.calendar.entity.DayStatus

/**
 * @author jv.lee
 * @date 2020/11/25
 * @description
 */
class SimpleDayRender(context: Context, defStyleRes: Int) : DefaultDayRender(context, defStyleRes) {

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
        mPaint.color = if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        canvas.drawCircle(
            childSize.cx,
            childSize.cy,
            (childSize.absSize / 2) - (strokeWidth + paddingSize),
            mPaint
        )

        mPaint.color = if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateStrokeColor else overStrokeColor
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
        mPaint.color = if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        val path1 = Path().apply {

            val topDimen = childSize.cy - ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val bottomDimen = childSize.cy + ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val startDimen = childSize.cx - ((childSize.absSize / 2) - (paddingSize)) + strokeWidth
            val rectF = RectF(startDimen, topDimen, childSize.width + childSize.maxWidth, bottomDimen)
            val radius = childSize.cx
            val radiusArray = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)

            val dimen = (strokeWidth / 2)
            canvas.clipRect(startDimen - dimen, topDimen - strokeWidth, childSize.width, bottomDimen + strokeWidth)
        }


        canvas.drawPath(path1, mPaint)

        mPaint.color = if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateStrokeColor else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        val path2 = Path().apply {

            val topDimen = childSize.cy - ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val bottomDimen = childSize.cy + ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val startDimen = childSize.cx - ((childSize.absSize / 2) - (paddingSize)) + strokeWidth
            val rectF = RectF(startDimen, topDimen, childSize.width + childSize.maxWidth, bottomDimen)
            val radius = childSize.cx
            val radiusArray = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }

        canvas.drawPath(path2, mPaint)
        canvas.restore()
    }

    private fun drawCenter() {
        mPaint.color = if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth

        val topDimen = childSize.cy - ((childSize.absSize / 2) - (strokeWidth + paddingSize))
        val bottomDimen = childSize.cy + ((childSize.absSize / 2) - (strokeWidth + paddingSize))

        val path1 = Path().apply {
            val rectF =
                RectF(childSize.left, topDimen, childSize.right, bottomDimen)
            addRect(rectF, Path.Direction.CCW)
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color = if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateStrokeColor else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth

        canvas.drawLine(childSize.width - childSize.maxWidth, topDimen, childSize.width, topDimen, mPaint)
        canvas.drawLine(childSize.width - childSize.maxWidth, bottomDimen, childSize.width, bottomDimen, mPaint)
    }

    private fun drawEnd() {
        canvas.save()

        mPaint.color = if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth

        val path1 = Path().apply {

            val topDimen = childSize.cy - ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val bottomDimen = childSize.cy + ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val endDimen = childSize.cx + ((childSize.absSize / 2) - paddingSize) - strokeWidth
            val rectF = RectF(0f, topDimen, endDimen, bottomDimen)
            val radius = childSize.cx
            val radiusArray = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)

            val dimen = (strokeWidth / 2)
            canvas.clipRect(childSize.width - childSize.maxWidth, topDimen - strokeWidth, endDimen + strokeWidth, bottomDimen + strokeWidth)
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color = if (entity.dayStatus == DayStatus.UPDATE_STATUS) updateStrokeColor else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth

        val path2 = Path().apply {

            val topDimen = childSize.cy - ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val bottomDimen = childSize.cy + ((childSize.absSize / 2) - (strokeWidth + paddingSize))
            val endDimen = childSize.cx + ((childSize.absSize / 2) - paddingSize) - strokeWidth
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

        mPaint.color = if (entity.dayStatus == DayStatus.OVER_UPDATE_STATUS) overSelectedColor else updateSelectedColor
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
            (childSize.absSize / 2) - (strokeWidth + paddingSize),
            mPaint
        )

        mPaint.color = todayStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = (strokeWidth + (strokeWidth*0.5)).toFloat()
        canvas.drawCircle(
            childSize.cx,
            childSize.cy,
            (childSize.absSize / 2) - (strokeWidth + paddingSize) - (strokeWidth + (strokeWidth*0.5)).toFloat(),
            mPaint
        )
    }

    private fun drawDelayUpdateDot() {
        if (entity.dayStatus != DayStatus.DELAY_UPDATE_STATUS) return
        mPaint.style = Paint.Style.FILL
        mPaint.color = if (entity.isSelected) dotPairColor else dotColor

        val rect = Rect()
        val text = entity.day.toString()
        mPaint.getTextBounds(text, 0, text.length, rect)

        val centerWidth = (childSize.cx + (strokeWidth / 2))
        val centerHeight = childSize.height - dotSize - (paddingSize * 2) - strokeWidth
        canvas.drawCircle(centerWidth, centerHeight, (dotSize / 2), mPaint)
    }

    private fun drawNumberText() {
        mPaint.style = Paint.Style.FILL
        mPaint.color = if (entity.isSelected) textPairColor else if (!entity.isToMonth) goneTextColor else textColor
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