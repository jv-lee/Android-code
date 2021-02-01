package com.lee.wordstatistics.input

import android.content.Context
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dreame.reader.common.ui.widgets.edittext.CharLengthInputFilter
import com.dreame.reader.common.ui.widgets.edittext.LengthWatcher
import com.lee.wordstatistics.input.edit.WordLengthBySpaceInputFilter
import com.lee.wordstatistics.R
import com.lee.wordstatistics.WordCounter
import com.lee.wordstatistics.input.edit.BreakInputFilter
import com.lee.wordstatistics.input.edit.WordCounterInputFilter
import com.lee.wordstatistics.input.edit.WordCounterWatcher

/**
 * @Description 限制长度输入框
 * @Author zouzhihao
 * @Date 2020/5/9 5:12 PM
 * @Version 1.0
 * 同步的
 */
@Suppress("NAME_SHADOWING")
class LimitInputView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        const val DEFAULT_LIMIT_SIZE = 30
        const val DEFAULT_MIN_SIZE = 1
    }

    enum class ComputerType(override val value: Int) : HasValue<Int> {
        NORMAL(1), WORD(2), WORD_NEW(3);

        companion object : EnumConverter<Int, ComputerType>(buildValueMap())
    }

    var maxLength = DEFAULT_LIMIT_SIZE
    var minLength = DEFAULT_MIN_SIZE

    private var computerType: ComputerType = ComputerType.NORMAL
    private var watcher: LengthWatcher? = null
    private val editText: EditText
    private val limitView: TextView

    private var hintString: String = ""
    private var filterBreak: Boolean = false

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    init {
        View.inflate(getContext(), R.layout.layout_limit_input_view, this)
        editText = findViewById(R.id.edit_text)
        limitView = findViewById(R.id.tv_limit)

        getContext()?.obtainStyledAttributes(attrs, R.styleable.LimitInputView)
            ?.run {
                maxLength = getInt(R.styleable.LimitInputView_max_length, DEFAULT_LIMIT_SIZE)
                maxLength = if (maxLength == 0) DEFAULT_LIMIT_SIZE else maxLength
                minLength = getInt(R.styleable.LimitInputView_min_length, DEFAULT_MIN_SIZE)
                minLength = if (minLength > maxLength) maxLength else minLength

                computerType = ComputerType.fromValue(
                    getInt(
                        R.styleable.LimitInputView_computer_type,
                        ComputerType.NORMAL.value
                    ), ComputerType.NORMAL
                )
                hintString = getString(R.styleable.LimitInputView_hint) ?: ""
                filterBreak = getBoolean(R.styleable.LimitInputView_filter_break, false)

                recycle()
            }

        initView()
    }

    @Suppress("ControlFlowWithEmptyBody")
    fun initView() {
        editText.hint = hintString
        val filters = arrayListOf<InputFilter>()

        if (filterBreak) {
            filters.add(BreakInputFilter())
        }

        val watcher = object : LengthWatcher {
            override fun onChanged(inputLength: Int, minLength: Int, maxLength: Int) {
                val remainLength = maxLength - inputLength
                if (inputLength == 0) {
                    limitView.visibility = View.GONE
                } else {
                    limitView.visibility = View.VISIBLE
                    limitView.text = getLengthString(inputLength)
                    limitView.setTextColor(getShowTipColor(remainLength))
                }

                watcher?.onChanged(inputLength, minLength, maxLength)
            }
        }
        when (computerType) {
            ComputerType.WORD -> {
                filters.add(WordLengthBySpaceInputFilter().apply {
                    this.maxLength = this@LimitInputView.maxLength
                    this.minLength = this@LimitInputView.minLength
                    this.watcher = watcher
                })
            }
            ComputerType.WORD_NEW -> {
                val wordCounterWatcher = object : WordCounterWatcher(context.applicationContext) {
                    override fun changeTextLimit(wordCounter: WordCounter, text: String, count: Int) {
                        limitView.visibility = View.VISIBLE
                        if (count == 0) {
                            limitView.visibility = View.GONE
                        } else if (count > maxLength) {
                            val content = wordCounter.getWordLimitContent(text, maxLength)
                            editText.setText(content)
                            editText.setSelection(content.length)
                        } else {
                            limitView.text = getLengthString(count)
                            limitView.setTextColor(getShowTipColor(maxLength - count))
                        }
                        watcher.onChanged(count,minLength,maxLength)
                    }

                }
                editText.addTextChangedListener(wordCounterWatcher)
            }
            else -> {
                filters.add(CharLengthInputFilter().apply {
                    this.maxLength = this@LimitInputView.maxLength
                    this.minLength = this@LimitInputView.minLength
                    this.watcher = watcher
                })
            }
        }

        editText.filters = filters.toTypedArray()
    }

    fun getEditText() = editText

    //返回当前的tip color
    private fun getShowTipColor(remainLength: Int): Int {
        return if (remainLength > 0) {
            ContextCompat.getColor(context, android.R.color.black)
        } else {
            ContextCompat.getColor(context, android.R.color.holo_red_dark)
        }
    }

    //返回当前提醒文案
    private fun getLengthString(inputLength: Int): String {
        val remainLength = maxLength - inputLength
        return if (remainLength <= 0) {
            "$inputLength/$maxLength"
        } else {
            "$inputLength/$maxLength"
        }
    }

    fun getText(): String = editText.text.toString()

    fun setText(text: String?) {
        text ?: return
        editText.setText(text)
    }

    fun setHint(text: String?) {
        text ?: return
        hintString = text
        editText.hint = text
    }

    fun setLengthWatcher(watcher: LengthWatcher) {
        this.watcher = watcher
    }

    fun addTextChangedListener(watcher: TextWatcher) {
        editText.addTextChangedListener(watcher)
    }

    fun onDestroy() {
    }

}