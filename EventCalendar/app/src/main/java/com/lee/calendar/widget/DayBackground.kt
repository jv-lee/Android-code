package com.lee.calendar.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IntDef
import com.lee.calendar.R


/**
 * @author jv.lee
 * @date 2020/10/29
 * @description
 */
class DayBackground constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mWidth = 0
    private var mHeight: Int = 0
    private var strokeWidth: Float = 2f
    private var dayStatus: Int = DayBackgroundStatus.STATUS_GONE

    init {
        mPaint.strokeWidth = strokeWidth
        mPaint.style = Paint.Style.FILL
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        context.obtainStyledAttributes(attributeSet, R.styleable.DayBackground).run {
            dayStatus = getInt(R.styleable.DayBackground_day_status, 0)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        drawMode(canvas)
    }

    private fun drawMode(canvas: Canvas) {
        when (dayStatus) {
            DayBackgroundStatus.STATUS_SINGLE -> drawSingle(canvas)
            DayBackgroundStatus.STATUS_START -> drawStart(canvas)
            DayBackgroundStatus.STATUS_CENTER -> drawCenter(canvas)
            DayBackgroundStatus.STATUS_END -> drawEnd(canvas)
        }
    }

    private fun drawSingle(canvas: Canvas) {
        mPaint.color = Color.parseColor("#0A2477FA")
        canvas.drawCircle(
            (width / 2).toFloat(),
            (mHeight / 2).toFloat(),
            (mWidth / 2).toFloat(),
            mPaint
        )

        mPaint.color = Color.parseColor("#992477FA")
        mPaint.style = Paint.Style.STROKE
        canvas.drawCircle(
            (width / 2).toFloat(),
            (mHeight / 2).toFloat(),
            (mWidth / 2).toFloat() - strokeWidth,
            mPaint
        )
    }

    private fun drawStart(canvas: Canvas) {
        canvas.translate(strokeWidth, 0f)

        mPaint.color = Color.parseColor("#0A2477FA")
        val path1 = Path().apply {
            val rectF =
                RectF(strokeWidth, strokeWidth, width.toFloat(), height.toFloat() - strokeWidth)
            val radius = (mWidth / 2).toFloat()
            val radiusArray = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color = Color.parseColor("#992477FA")
        mPaint.style = Paint.Style.STROKE
        val path2 = Path().apply {
            val rectF =
                RectF(strokeWidth, strokeWidth, width.toFloat(), height.toFloat() - strokeWidth)
            val radius = (mWidth / 2).toFloat()
            val radiusArray = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }
        canvas.drawPath(path2, mPaint)
    }

    private fun drawCenter(canvas: Canvas) {
        mPaint.color = Color.parseColor("#0A2477FA")
        val path1 = Path().apply {
            val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
            addRect(rectF, Path.Direction.CCW)
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color = Color.parseColor("#992477FA")
        mPaint.style = Paint.Style.STROKE
        canvas.drawLine(0f, strokeWidth, mWidth.toFloat(), strokeWidth, mPaint)
        canvas.drawLine(0f, mHeight - strokeWidth, mWidth.toFloat(), mHeight - strokeWidth, mPaint)
    }

    private fun drawEnd(canvas: Canvas) {
        canvas.translate(-strokeWidth, 0f)

        mPaint.color = Color.parseColor("#0A2477FA")
        val path1 = Path().apply {
            val rectF = RectF(
                0f,
                strokeWidth,
                width.toFloat() - strokeWidth,
                height.toFloat() - strokeWidth
            )
            val radius = (mWidth / 2).toFloat()
            val radiusArray = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color = Color.parseColor("#992477FA")
        mPaint.style = Paint.Style.STROKE
        val path2 = Path().apply {
            val rectF = RectF(
                0f,
                strokeWidth,
                width.toFloat() - strokeWidth,
                height.toFloat() - strokeWidth
            )
            val radius = (mWidth / 2).toFloat()
            val radiusArray = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }
        canvas.drawPath(path2, mPaint)
    }

    fun updateStatus(@DayBackgroundStatus status: Int) {
        dayStatus = status
        postInvalidate()
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

}