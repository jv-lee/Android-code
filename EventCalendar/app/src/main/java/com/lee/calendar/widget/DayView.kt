package com.lee.calendar.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IntDef
import com.lee.calendar.R
import com.lee.calendar.utils.SizeUtil


/**
 * @author jv.lee
 * @date 2020/10/29
 * @description
 */
class DayView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mWidth = 0
    private var mHeight: Int = 0
    private val strokeWidth: Float
    private val paddingSize: Float
    private val textSize:Float
    private var dayStatus: Int = DayBackgroundStatus.STATUS_GONE
    private var mLeft = 0f
    private var mTop = 0f
    private var mRight = 0f
    private var mBottom = 0f
    private var absSize = 0f

    init {
        paddingSize = SizeUtil.dp2px(context, 6F).toFloat()
        strokeWidth = SizeUtil.dp2px(context, 1F).toFloat()
        textSize = SizeUtil.sp2px(context,15F).toFloat()

        mPaint.strokeWidth = strokeWidth
        mPaint.style = Paint.Style.FILL
        mPaint.textSize = textSize
        mPaint.typeface = Typeface.DEFAULT_BOLD
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        context.obtainStyledAttributes(attributeSet, R.styleable.DayView).run {
            dayStatus = getInt(R.styleable.DayView_day_status, 0)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        mLeft = strokeWidth
        mTop = strokeWidth
        mRight = mWidth - strokeWidth
        mBottom = mHeight - strokeWidth
        absSize = Math.min(mWidth, mHeight).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        drawNumberText(canvas)
        drawSelectedBackground(canvas)
        drawToDayBackground(canvas)
        drawDot(canvas)
        drawBackgroundMode(canvas)
    }

    private fun drawNumberText(canvas: Canvas) {
        val text = "12"
        val rect = Rect()
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.parseColor("#000000")
        mPaint.getTextBounds(text, 0, text.length, rect)
        canvas.drawText(
            text,
            (width / 2).toFloat() - (rect.width() / 2),
            (height / 2).toFloat() + (rect.height() / 2),
            mPaint
        )
    }

    private fun drawDot(canvas: Canvas) {

    }

    private fun drawToDayBackground(canvas: Canvas) {

    }

    private fun drawSelectedBackground(canvas: Canvas) {

    }

    private fun drawBackgroundMode(canvas: Canvas) {
        when (dayStatus) {
            DayBackgroundStatus.STATUS_SINGLE -> drawSingle(canvas)
            DayBackgroundStatus.STATUS_START -> drawStart(canvas)
            DayBackgroundStatus.STATUS_CENTER -> drawCenter(canvas)
            DayBackgroundStatus.STATUS_END -> drawEnd(canvas)
        }
    }

    private fun drawSingle(canvas: Canvas) {
        mPaint.color = Color.parseColor("#0A2477FA")
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        canvas.drawCircle(
            (mWidth / 2).toFloat(),
            (mHeight / 2).toFloat(),
            (absSize / 2) - (strokeWidth + paddingSize),
            mPaint
        )

        mPaint.color = Color.parseColor("#992477FA")
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        canvas.drawCircle(
            (width / 2).toFloat(),
            (mHeight / 2).toFloat(),
            (absSize / 2) - (strokeWidth + paddingSize),
            mPaint
        )
    }

    private fun drawStart(canvas: Canvas) {
        canvas.translate(strokeWidth, 0f)

        mPaint.color = Color.parseColor("#0A2477FA")
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        val path1 = Path().apply {
            val rectF =
                RectF(mLeft, mTop + paddingSize, width.toFloat(), mBottom - paddingSize)
            val radius = (mWidth / 2).toFloat()
            val radiusArray = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color = Color.parseColor("#992477FA")
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        val path2 = Path().apply {
            val rectF =
                RectF(mLeft, mTop + paddingSize, width.toFloat(), mBottom - paddingSize)
            val radius = (mWidth / 2).toFloat()
            val radiusArray = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }
        canvas.drawPath(path2, mPaint)
    }

    private fun drawCenter(canvas: Canvas) {
        mPaint.color = Color.parseColor("#0A2477FA")
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth

        val path1 = Path().apply {
            val rectF = RectF(0f, 0f + paddingSize, width.toFloat(), height.toFloat() - paddingSize)
            addRect(rectF, Path.Direction.CCW)
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color = Color.parseColor("#992477FA")
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        canvas.drawLine(0f, mTop + paddingSize, mWidth.toFloat(), mTop + paddingSize, mPaint)
        canvas.drawLine(0f, mBottom - paddingSize, mWidth.toFloat(), mBottom - paddingSize, mPaint)
    }

    private fun drawEnd(canvas: Canvas) {
        canvas.translate(-strokeWidth, 0f)

        mPaint.color = Color.parseColor("#0A2477FA")
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth

        val path1 = Path().apply {
            val rectF = RectF(0f, mTop + paddingSize, mRight, mBottom - paddingSize)
            val radius = (mWidth / 2).toFloat()
            val radiusArray = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color = Color.parseColor("#992477FA")
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth

        val path2 = Path().apply {
            val rectF = RectF(0f, mTop + paddingSize, mRight, mBottom - paddingSize)
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