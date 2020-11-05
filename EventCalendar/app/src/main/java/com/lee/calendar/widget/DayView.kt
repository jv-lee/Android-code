package com.lee.calendar.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
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
    private var mLeft = 0f
    private var mTop = 0f
    private var mRight = 0f
    private var mBottom = 0f
    private var absSize = 0f

    //状态属性
    private var dayStatus: Int
    private var isSelect: Boolean
    private var isToday: Boolean
    private var isDelayUpdate: Boolean
    private var isUpdate:Boolean
    private var isGone:Boolean

    //颜色属性
    private val updateBackgroundColor: Int
    private val updateStrokeColor: Int
    private val updateSelectedColor: Int
    private val overBackgroundColor: Int
    private val overStrokeColor: Int
    private val overSelectedColor: Int
    private val todayBackgroundColor:Int
    private val dotColor: Int
    private val goneTextColor: Int
    private val defaultTextColor: Int
    private val todayTextColor:Int

    //size属性及显示字符
    private val strokeWidth: Float
    private val paddingSize: Float
    private val dotSize: Float
    private val textSize: Float
    private var text: String

    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.DayView).run {
            dayStatus = getInt(R.styleable.DayView_day_status, DayBackgroundStatus.STATUS_GONE)
            isSelect = getBoolean(R.styleable.DayView_day_isSelect, false)
            isToday = getBoolean(R.styleable.DayView_day_isToday, false)
            isUpdate = getBoolean(R.styleable.DayView_day_isUpdate,true)
            isDelayUpdate = getBoolean(R.styleable.DayView_day_isDelayUpdate, false)
            isGone = getBoolean(R.styleable.DayView_day_isGone,false)

            updateBackgroundColor = getColor(R.styleable.DayView_day_updateBackgroundColor,ContextCompat.getColor(context,android.R.color.holo_blue_light))
            updateStrokeColor = getColor(R.styleable.DayView_day_updateStrokeColor,ContextCompat.getColor(context,android.R.color.holo_blue_dark))
            updateSelectedColor = getColor(R.styleable.DayView_day_updateSelectedColor,ContextCompat.getColor(context,android.R.color.holo_blue_dark))

            overBackgroundColor = getColor(R.styleable.DayView_day_overBackgroundColor,ContextCompat.getColor(context,android.R.color.holo_red_light))
            overStrokeColor = getColor(R.styleable.DayView_day_overStrokeColor,ContextCompat.getColor(context,android.R.color.holo_red_dark))
            overSelectedColor = getColor(R.styleable.DayView_day_overSelectedColor,ContextCompat.getColor(context,android.R.color.holo_red_dark))

            todayBackgroundColor = getColor(R.styleable.DayView_day_todayBackgroundColor,ContextCompat.getColor(context,android.R.color.holo_orange_dark))
            dotColor = getColor(R.styleable.DayView_day_dotColor,ContextCompat.getColor(context,android.R.color.holo_orange_dark))
            goneTextColor = getColor(R.styleable.DayView_day_goneTextColor,ContextCompat.getColor(context,android.R.color.darker_gray))
            defaultTextColor = getColor(R.styleable.DayView_day_defaultTextColor,ContextCompat.getColor(context,android.R.color.black))
            todayTextColor = getColor(R.styleable.DayView_day_todayTextColor,ContextCompat.getColor(context,android.R.color.white))

            strokeWidth = getDimension(R.styleable.DayView_day_strokeWidth,SizeUtil.dp2px(context,1F).toFloat())
            paddingSize = getDimension(R.styleable.DayView_day_paddingSize,SizeUtil.dp2px(context,6F).toFloat())
            dotSize = getDimension(R.styleable.DayView_day_dotSize,SizeUtil.dp2px(context,5F).toFloat())
            textSize = getDimension(R.styleable.DayView_day_textSize,SizeUtil.sp2px(context,15F).toFloat())
            text = getString(R.styleable.DayView_day_text)?:""

            recycle()
        }

        mPaint.strokeWidth = strokeWidth
        mPaint.style = Paint.Style.FILL
        mPaint.typeface = Typeface.DEFAULT_BOLD
        setLayerType(LAYER_TYPE_SOFTWARE, null)
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

        drawBackgroundMode(canvas)
        drawSelectedBackground(canvas)
        drawToDayBackground(canvas)
        drawDelayUpdateDot(canvas)
        drawNumberText(canvas)
    }

    private fun drawNumberText(canvas: Canvas) {
        val rect = Rect()
        mPaint.style = Paint.Style.FILL
        mPaint.color = if(isToday) todayTextColor else if(isGone) goneTextColor else defaultTextColor
        mPaint.textSize = textSize
        mPaint.getTextBounds(text, 0, text.length, rect)
        when (text) {
            "1" -> {
                canvas.drawText(text, (width / 2).toFloat() - (rect.width() / 2) - (rect.width() / 2), (height / 2).toFloat() + (rect.height() / 2), mPaint)
            }
            "11" -> {
                canvas.drawText(text, (width / 2).toFloat() - (rect.width() / 1.5).toInt() , (height / 2).toFloat() + (rect.height() / 2), mPaint)
            }
            else -> {
                canvas.drawText(text, (width / 2).toFloat() - (rect.width() / 2), (height / 2).toFloat() + (rect.height() / 2), mPaint)
            }
        }
    }

    private fun drawDelayUpdateDot(canvas: Canvas) {
        if(!isDelayUpdate)return
        val rect = Rect()
        mPaint.style = Paint.Style.FILL
        mPaint.color = dotColor
        mPaint.getTextBounds(text, 0, text.length, rect)
        val centerWidth = (width / 2).toFloat() + (strokeWidth / 2)
        val dimen = (mHeight - (((absSize / 2) - ( paddingSize)) * 2)) / 2
        val centerHeight = mHeight - dimen - dotSize
        canvas.drawCircle(centerWidth, centerHeight, (dotSize / 2), mPaint)
    }

    private fun drawToDayBackground(canvas: Canvas) {
        if (!isToday) return
        mPaint.color = todayBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        canvas.drawCircle((mWidth / 2).toFloat(), (mHeight / 2).toFloat(), (absSize / 2) - (strokeWidth + paddingSize),mPaint)
    }

    private fun drawSelectedBackground(canvas: Canvas) {
        if (!isSelect) return
        mPaint.color = if(isUpdate) updateSelectedColor else overSelectedColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        canvas.drawCircle((mWidth / 2).toFloat(), (mHeight / 2).toFloat(), (absSize / 2) - ((strokeWidth + (strokeWidth /2)) + paddingSize),mPaint)
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
        mPaint.color = if(isUpdate)updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        canvas.drawCircle((mWidth / 2).toFloat(), (mHeight / 2).toFloat(), (absSize / 2) - (strokeWidth + paddingSize), mPaint)

        mPaint.color = if(isUpdate)updateStrokeColor else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        canvas.drawCircle((width / 2).toFloat(), (mHeight / 2).toFloat(), (absSize / 2) - (strokeWidth + paddingSize), mPaint)
    }

    private fun drawStart(canvas: Canvas) {
        canvas.save()
        canvas.translate(strokeWidth, 0f)

        mPaint.color = if(isUpdate)updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        val path1 = Path().apply {
            val dimen = (mHeight - (((absSize / 2) - ( paddingSize)) * 2)) / 2
            val startDimen = (mWidth / 2) - ((absSize / 2) - (strokeWidth + paddingSize))
            val rectF = RectF(startDimen, dimen + strokeWidth, width.toFloat(), mBottom - dimen)
            val radius = (mWidth / 2).toFloat()
            val radiusArray = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color = if(isUpdate)updateStrokeColor else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        val path2 = Path().apply {
            val dimen = (mHeight - (((absSize / 2) - ( paddingSize)) * 2)) / 2
            val startDimen = (mWidth / 2) - ((absSize / 2) - (strokeWidth + paddingSize))
            val rectF = RectF(startDimen, dimen + strokeWidth, width.toFloat(), mBottom - dimen)
            val radius = (mWidth / 2).toFloat()
            val radiusArray = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }
        canvas.drawPath(path2, mPaint)
        canvas.restore()
    }

    private fun drawCenter(canvas: Canvas) {
        mPaint.color = if(isUpdate)updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth

        val path1 = Path().apply {
            val dimen = (mHeight - (((absSize / 2) - ( paddingSize)) * 2)) / 2
            val rectF = RectF(0f, dimen + strokeWidth, width.toFloat(), mBottom - dimen)
            addRect(rectF, Path.Direction.CCW)
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color = if(isUpdate)updateStrokeColor else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        val dimen = (mHeight - (((absSize / 2) - ( paddingSize)) * 2)) / 2
        canvas.drawLine(0f, dimen + strokeWidth, mWidth.toFloat(), dimen + strokeWidth, mPaint)
        canvas.drawLine(0f, mBottom - dimen, mWidth.toFloat(), mBottom - dimen, mPaint)
    }

    private fun drawEnd(canvas: Canvas) {
        canvas.save()
        canvas.translate(-strokeWidth, 0f)

        mPaint.color = if(isUpdate)updateBackgroundColor else overBackgroundColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth

        val path1 = Path().apply {
            val dimen = (mHeight - (((absSize / 2) - ( paddingSize)) * 2)) / 2
            val endDimen = (mWidth /2 ) + ((absSize / 2) - (strokeWidth + paddingSize))
            val rectF = RectF(0f, dimen + strokeWidth, endDimen, mBottom - dimen)
            val radius = (mWidth / 2).toFloat()
            val radiusArray = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }
        canvas.drawPath(path1, mPaint)

        mPaint.color = if(isUpdate)updateStrokeColor else overStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth

        val path2 = Path().apply {
            val dimen = (mHeight - (((absSize / 2) - ( paddingSize)) * 2)) / 2
            val endDimen = (mWidth /2 ) + ((absSize / 2) - (strokeWidth + paddingSize))
            val rectF = RectF(0f, dimen + strokeWidth, endDimen, mBottom - dimen)
            val radius = (mWidth / 2).toFloat()
            val radiusArray = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
            addRoundRect(rectF, radiusArray, Path.Direction.CCW)
        }
        canvas.drawPath(path2, mPaint)
        canvas.restore()
    }

    fun updateDataStatus(@DayBackgroundStatus backgroundStatus: Int?,isSelect:Boolean?,isToday:Boolean?,isDelayUpdate:Boolean?,isUpdate:Boolean?,isGone:Boolean?,text:String?) {
        backgroundStatus?.let { this.dayStatus = it }
        isSelect?.let { this.isSelect = it }
        isToday?.let { this.isToday = it }
        isDelayUpdate?.let { this.isDelayUpdate = it }
        isUpdate?.let { this.isUpdate = it }
        isGone?.let { this.isGone = it }
        text?.let { this.text = it }
        postInvalidate()
    }

    fun updateBackgroundStatus(@DayBackgroundStatus status: Int) {
        this.dayStatus = status
        postInvalidate()
    }

    fun updateSelect(isSelect: Boolean) {
        this.isSelect = isSelect
        postInvalidate()
    }

    fun updateIsToday(isToday: Boolean) {
        this.isToday = isToday
        postInvalidate()
    }

    fun updateIsDelayUpdate(isDelayUpdate: Boolean) {
        this.isDelayUpdate = isDelayUpdate
        postInvalidate()
    }

    fun updateIsUpdate(isUpdate: Boolean) {
        this.isUpdate = isUpdate
        postInvalidate()
    }

    fun updateIsGone(isGone: Boolean) {
        this.isGone = isGone
        postInvalidate()
    }

    fun setText(text: String) {
        this.text = text
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