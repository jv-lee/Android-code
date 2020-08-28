package com.lee.code.draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * @author jv.lee
 * @date 2020/8/28
 * @description 五子棋盘 实现xml布局构造方法
 */
class Chessboard constructor(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val mPaint: Paint = Paint()
    private var endX = 0
    private var endY = 0
    private val lineCount = 10
    private val lineSize = 1
    private val factor = PiecesFactor()

    init {
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = lineSize.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        endX = measuredWidth
        endY = measuredHeight
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            drawChessboardBackground(canvas)
            drawChessboardLine(canvas)
            val chessPieces = factor.getChessPieces(PiecesType.WHITE_PIECES)
            chessPieces?.downPieces(canvas,mPaint,Point(35,35))
        }
    }

    /**
     * 绘制棋盘背景
     */
    private fun drawChessboardBackground(canvas: Canvas) {
        mPaint.color = Color.parseColor("#ababab")
        canvas.drawRect(0F, 0F, endX.toFloat(), endY.toFloat(), mPaint)
    }

    /**
     * 绘制棋盘线
     */
    private fun drawChessboardLine(canvas: Canvas) {
        mPaint.color = Color.parseColor("#000000")
        val width = endX / lineCount
        val height = endY / lineCount
        for (index in 0..lineCount) {
            canvas.drawLine((width.toFloat() * index), 0F, (width.toFloat() * index), endY.toFloat(), mPaint)
            canvas.drawLine(0F, (height.toFloat() * index), endX.toFloat(), (height.toFloat() * index), mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action!!) {
            MotionEvent.ACTION_UP -> {
                val chessPieces = factor.getChessPieces(PiecesType.WHITE_PIECES)

            }
        }
        return super.onTouchEvent(event)
    }


}