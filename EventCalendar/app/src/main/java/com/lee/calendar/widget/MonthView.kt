package com.lee.calendar.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * @author jv.lee
 * @date 2020/11/12
 * @description
 */
class MonthView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var dayWidth = 0f
    private var dayHeight = 0f

    private val totalRow = 6
    private val totalCol = 7

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.color = Color.parseColor("#000000")
        mPaint.strokeWidth = 1f
        mPaint.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        dayWidth = (w / totalCol).toFloat()
        dayHeight = (h / totalRow).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (rowIndex in 0 until totalRow) {
            for (colIndex in 0 until totalCol) {
                Log.i("jv.lee", "onDraw: $rowIndex - $colIndex")
                drawDayView(canvas, rowIndex, colIndex)
            }
        }
    }

    private fun drawDayView(canvas: Canvas, row: Int, col: Int) {
        val rowWidth = dayWidth * col
        val colHeight = dayHeight * row
        canvas.drawRect(0f +rowWidth,0f + colHeight,dayWidth + rowWidth,dayHeight + colHeight,mPaint)
    }

}