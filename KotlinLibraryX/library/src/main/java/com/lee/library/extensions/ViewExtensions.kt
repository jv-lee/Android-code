package com.lee.library.extensions

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.text.*
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlin.math.abs

/**
 * @author jv.lee
 * @date 2020/4/1
 * @description  View.扩展函数
 */

/**
 * 监听RecyclerView滑动状态 Glide加载模式
 */
fun RecyclerView.glideEnable() {
    if (true) {
        return
    }
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        var isDown = false
        var lastPosition = 0
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!isDown) return
            when (newState) {
                //正在拖动
                RecyclerView.SCROLL_STATE_DRAGGING ->
                    context?.let { Glide.with(it).resumeRequests() }
                //滑动停止
                RecyclerView.SCROLL_STATE_IDLE ->
                    context?.let { Glide.with(it).resumeRequests() }
                //惯性滑动中
                RecyclerView.SCROLL_STATE_SETTLING ->
                    context?.let { Glide.with(it).pauseRequests() }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val currentPosition =
                (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            if (currentPosition == lastPosition) {
                isDown = true
                return
            }
            isDown = false
            if (dy >= 0) {
                if (currentPosition > lastPosition) {
                    isDown = true
                    lastPosition = currentPosition
                }
            }
        }
    })
}

/**
 * 向下兼容设置View背景着色器选择器
 */
fun View.setBackgroundSelectorTintCompat(selectorId: Int) {
    ViewCompat.setBackgroundTintList(
        this,
        ColorStateList.createFromXml(
            this.resources,
            this.resources.getXml(selectorId)
        )
    )
}

/**
 * 设置view透明度兼容方法
 */
fun View.setBackgroundAlphaCompat(alpha: Int) {
    background ?: return
    val mutate: Drawable? = background.mutate()
    mutate?.let {
        it.alpha = alpha
    } ?: kotlin.run {
        background.alpha = alpha
    }
}

/**
 * ImageView扩展函数 向下兼容Tint着色器
 */
fun ImageView.setImageTintCompat(drawableId: Int, color: Int = 0) {
    if (color == 0) {
        setImageResource(drawableId)
        return
    }
    var drawable =
        ContextCompat.getDrawable(context, drawableId)
    val colors = intArrayOf(color, color)
    val states = arrayOfNulls<IntArray>(2)
    states[0] = intArrayOf(android.R.attr.state_pressed)
    states[1] = intArrayOf()
    val colorList = ColorStateList(states, colors)
    val stateListDrawable = StateListDrawable()
    stateListDrawable.addState(states[0], drawable) //注意顺序

    stateListDrawable.addState(states[1], drawable)
    val state = stateListDrawable.constantState
    drawable =
        DrawableCompat.wrap(state?.newDrawable() ?: stateListDrawable)
            .mutate()
    DrawableCompat.setTintList(drawable, colorList)
    setImageDrawable(drawable)
}

/**
 * RadioButton 扩展函数 向下兼容Button Tint着色器
 */
fun RadioButton.setButtonTint(drawableId: Int, selectorId: Int) {
    val drawable =
        DrawableCompat.wrap(ContextCompat.getDrawable(context, drawableId)!!)
    val colorStateList = ContextCompat.getColorStateList(context, selectorId)
    DrawableCompat.setTintList(drawable, colorStateList)
    buttonDrawable = drawable
}

/**
 * NestedScrollView 滑动改变view 透明度及view状态
 */
fun NestedScrollView.setScrollTransparent(limit: Int, transparentBar: (Boolean, Int) -> Unit) {
    setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
        if (scrollY < limit) {
            transparentBar(true, scrollY)
        } else {
            transparentBar(false, 255)
        }
        return@OnScrollChangeListener
    })
}

/**
 * RecyclerView 滑动改变view 透明度及view状态
 */
fun RecyclerView.setScrollTransparent(transparentBar: (Boolean, Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (recyclerView.layoutManager !is LinearLayoutManager) return
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val position = linearLayoutManager.findFirstVisibleItemPosition()
            if (position == 0) {
                linearLayoutManager.findViewByPosition(position)?.let {
                    val scale = (255 / it.height)
                    transparentBar(true, (abs(it.top) * scale).toInt())
                }
            } else {
                transparentBar(false, 255)
            }
        }
    })
}

/**
 * RecyclerView 滑动改变view 透明度及view状态
 */
fun RecyclerView.setScrollTransparent2(transparentBar: (Boolean, Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (recyclerView.layoutManager !is LinearLayoutManager) return
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val position = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
            if (position == 0) {
                linearLayoutManager.findViewByPosition(position)?.let {
                    val scale = (255 / it.height)
                    transparentBar(true, (abs(it.top) * scale).toInt())
                }
            } else {
                transparentBar(false, 255)
            }
        }
    })
}

/**
 * 回调RecyclerView滑动高度
 * @return 滑动距离 - px值
 */
fun RecyclerView.callScrollHeight(callScroll: (Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        private var scrollHeight = 0
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            scrollHeight += dy
            callScroll(scrollHeight)
        }

    })
}

/**
 * 设置输入框最大输入长度
 */
fun EditText.setMaxSize(maxSize: Int) {
    filters = arrayOf(InputFilter.LengthFilter(maxSize))
}

/**
 * 设置输入框最大size / 最大lines
 * @param maxLines 最大行数
 * @param maxSize 最大长度
 */
fun EditText.setMaxSizeOrLines(maxSize: Int = 0, maxLines: Int = 0) {
    if (maxSize != 0) filters = arrayOf(InputFilter.LengthFilter(maxSize))
    if (maxLines != 0) this.maxLines = maxLines
}

/**
 * 设置输入框 为银行卡号输入样式 每4个字符 空格一位
 * @param lengthLimit 最大卡号长度
 */
fun EditText.setBankCodeTextWatcher(lengthLimit: Int = 16) {
    inputType = InputType.TYPE_CLASS_NUMBER
    addTextChangedListener(object : TextWatcher {
        //上次输入框中的内容
        private var lastString: String? = null

        //光标的位置
        private var selectPosition = 0
        override fun afterTextChanged(s: Editable?) {
            //获取输入框中的内容,不可以去空格
            val etContent: String = text.toString()
            if (TextUtils.isEmpty(etContent)) {
                return
            }
            //重新拼接字符串
            val newContent: String = addSpaceByCredit(etContent, lengthLimit)
            //保存本次字符串数据
            lastString = newContent

            //如果有改变，则重新填充
            //防止EditText无限setText()产生死循环
            if (newContent != etContent) {
                setText(newContent)
                //保证光标的位置
                setSelection(if (selectPosition > newContent.length) newContent.length else selectPosition)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //因为重新排序之后setText的存在
            //会导致输入框的内容从0开始输入，这里是为了避免这种情况产生一系列问题
            if (start == 0 && count > 1 && selectionStart == 0) {
                return
            }

            val textTrim: String = text.toString().trim()
            if (TextUtils.isEmpty(textTrim)) {
                return
            }

            //如果 before >0 && count == 0,代表此次操作是删除操作
            if (before > 0 && count == 0) {
                selectPosition = start
                if (TextUtils.isEmpty(lastString)) {
                    return
                }
                //将上次的字符串去空格 和 改变之后的字符串去空格 进行比较
                //如果一致，代表本次操作删除的是空格
                if (textTrim == lastString!!.replace(" ".toRegex(), "")) {
                    //帮助用户删除该删除的字符，而不是空格
                    val stringBuilder = java.lang.StringBuilder(lastString!!)
                    stringBuilder.deleteCharAt(start - 1)
                    selectPosition = start - 1
                    setText(stringBuilder.toString())
                }
            } else {
                //此处代表是添加操作
                //当光标位于空格之前，添加字符时，需要让光标跳过空格，再按照之前的逻辑计算光标位置
                selectPosition = if ((start + count) % 5 == 0) {
                    start + count + 1
                } else {
                    start + count
                }
            }
        }

        private fun addSpaceByCredit(content: String, lengthLimit: Int): String {
            var content = content
            if (TextUtils.isEmpty(content)) {
                return ""
            }
            //去空格
            content = content.replace(" ".toRegex(), "")
            if (TextUtils.isEmpty(content)) {
                return ""
            }
            //卡号限制为lengthLimit位
            if (content.length > lengthLimit) {
                content = content.substring(0, lengthLimit)
            }
            val newString = StringBuilder()
            for (i in 1..content.length) {
                //当为第4位时，并且不是最后一个第4位时
                //拼接字符的同时，拼接一个空格
                //如果在最后一个第四位也拼接，会产生空格无法删除的问题
                //因为一删除，马上触发输入框改变监听，又重新生成了空格
                if (i % 4 == 0 && i != content.length) {
                    newString.append(content[i - 1].toString() + " ")
                } else {
                    //如果不是4位的倍数，则直接拼接字符即可
                    newString.append(content[i - 1])
                }
            }
            return newString.toString()
        }

    })
}