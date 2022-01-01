package com.lee.library.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.text.*
import android.view.*
import android.webkit.WebView
import android.widget.*
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.view.size
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.lee.library.R
import com.lee.library.tools.ReflexTools
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
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        var isDown = false
        var lastPosition = 0
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            context ?: return
            if (!isDown) {
                Glide.with(context).resumeRequests()
                return
            }
            when (newState) {
                //正在拖动
                RecyclerView.SCROLL_STATE_DRAGGING ->
                    Glide.with(context).resumeRequests()
                //滑动停止
                RecyclerView.SCROLL_STATE_IDLE ->
                    Glide.with(context).resumeRequests()
                //惯性滑动中
                RecyclerView.SCROLL_STATE_SETTLING ->
                    Glide.with(context).pauseRequests()
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
 * 获取view背景透明度兼容方法
 */
fun View.getBackgroundAlphaCompat(): Int {
    background ?: return 0
    val mutate = background.mutate()
    return mutate?.alpha ?: background.alpha
}

/**
 * 设置view背景颜色兼容方法
 */
fun View.setBackgroundColorCompat(color: Int) {
    setBackgroundColor(ContextCompat.getColor(context, color))
}

/**
 * 设置view背景资源兼容方法
 */
fun View.setBackgroundDrawableCompat(drawableId: Int) {
    background = ContextCompat.getDrawable(context, drawableId)
}

/**
 * 设置文本颜色兼容方法
 */
fun TextView.setTextColorCompat(color: Int) {
    setTextColor(ContextCompat.getColor(context, color))
}

/**
 * TextView设置 四个位置drawable
 */
fun TextView.setDrawableCompat(
    left: Int = 0,
    top: Int = 0,
    right: Int = 0,
    bottom: Int = 0,
    tint: Int = 0
) {
    val leftDrawable = if (left == 0) null else getTintDrawableCompat(context, left, tint)
    leftDrawable?.setBounds(0, 0, leftDrawable.minimumWidth, leftDrawable.minimumHeight)

    val topDrawable = if (top == 0) null else getTintDrawableCompat(context, top, tint)
    topDrawable?.setBounds(0, 0, topDrawable.minimumWidth, topDrawable.minimumHeight)

    val rightDrawable = if (right == 0) null else getTintDrawableCompat(context, right, tint)
    rightDrawable?.setBounds(0, 0, rightDrawable.minimumWidth, rightDrawable.minimumHeight)

    val bottomDrawable = if (bottom == 0) null else getTintDrawableCompat(context, bottom, tint)
    bottomDrawable?.setBounds(0, 0, bottomDrawable.minimumWidth, bottomDrawable.minimumHeight)

    setCompoundDrawables(leftDrawable, topDrawable, rightDrawable, bottomDrawable)
}

/**
 * ImageView扩展函数 向下兼容Tint着色器
 */
fun ImageView.setImageTintCompat(drawableId: Int, color: Int = 0) {
    val drawable = getTintDrawableCompat(context, drawableId, color)
    setImageDrawable(drawable)
}

private fun getTintDrawableCompat(context: Context, drawableId: Int, color: Int = 0): Drawable? {
    if (drawableId == 0) {
        return null
    }
    var drawable = ContextCompat.getDrawable(context, drawableId)
    if (color == 0) {
        return drawable
    }
    val colors = intArrayOf(color, color)
    val states = arrayOfNulls<IntArray>(2)
    states[0] = intArrayOf(android.R.attr.state_pressed)
    states[1] = intArrayOf()
    val colorList = ColorStateList(states, colors)
    val stateListDrawable = StateListDrawable()
    stateListDrawable.addState(states[0], drawable) //注意顺序

    stateListDrawable.addState(states[1], drawable)
    val state = stateListDrawable.constantState
    drawable = DrawableCompat.wrap(state?.newDrawable() ?: stateListDrawable).mutate()
    DrawableCompat.setTintList(drawable, colorList)
    return drawable
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
 * 设置view 前景色点击效果
 */
fun View.setSelectableItemForeground() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val attrs = intArrayOf(R.attr.selectableItemBackground)
        val typedArray = context.obtainStyledAttributes(attrs)
        val backgroundResource: Int = typedArray.getResourceId(0, 0)
        setBackgroundResource(backgroundResource)
        typedArray.recycle()
    }
}

/**
 * NestedScrollView 滑动改变view 透明度及view状态
 */
inline fun NestedScrollView.setScrollTransparent(
    limit: Int,
    crossinline transparentBar: (Boolean, Int) -> Unit
) {
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
inline fun RecyclerView.setScrollTransparent(crossinline transparentBar: (Boolean, Int) -> Unit) {
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
inline fun RecyclerView.setScrollTransparent2(crossinline transparentBar: (Boolean, Int) -> Unit) {
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
inline fun RecyclerView.callScrollHeight(crossinline callScroll: (Int) -> Unit) {
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

/**
 * 扩展容器类设置Marin方法
 */
fun ViewGroup.setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        (layoutParams as ViewGroup.MarginLayoutParams).run {
            setMargins(
                if (left == 0) leftMargin else left,
                if (top == 0) topMargin else top,
                if (right == 0) rightMargin else right,
                if (bottom == 0) bottomMargin else bottom
            )
        }
    }
}

/**
 * 沉浸式状态栏 设置adjustResize 后 解决软键盘无法正常顶起解决方式
 */
fun ViewGroup.adjustResizeStatusBar(
    window: Window,
    marginValue: Int = context.statusBarHeight
) {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    fitsSystemWindows = true
    setMargin(top = -marginValue)
}

/**
 * RecyclerView 反转布局方向
 */
fun RecyclerView.reverseLayout() {
    layoutManager?.takeIf { it is LinearLayoutManager }.let {
        (it as LinearLayoutManager).reverseLayout = true
    }
}

/**
 * RecyclerView 多数据列表快速滑动到顶部
 */
fun RecyclerView.smoothScrollToTop() {
    val itemCount = adapter?.itemCount ?: return
    if (itemCount == 0) return

    if (layoutManager is LinearLayoutManager &&
        (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() == 0
    ) {
        scrollToPosition(0)
        return
    }

    scrollToPosition(5)
    postDelayed({ smoothScrollToPosition(0) }, 50)
}

/**
 * 监听键盘弹起
 */
inline fun View.keyboardObserver(
    crossinline openObserver: () -> Unit = {},
    crossinline closeObserver: () -> Unit = {}
) {
    var isOpen = false
    val keyboardHeight = 200
    viewTreeObserver.addOnGlobalLayoutListener {
        val rect = android.graphics.Rect()
        getWindowVisibleDisplayFrame(rect)

        val height: Int = context.resources.displayMetrics.heightPixels
        // 获取键盘抬高的高度
        val diff: Int = height - rect.height()
        if (diff > keyboardHeight && !isOpen) {
            isOpen = true
            openObserver()
        } else if (diff < keyboardHeight && isOpen) {
            isOpen = false
            closeObserver()
        }
    }
}

/**
 * 监听键盘弹起
 */
inline fun View.keyboardObserver(
    crossinline keyboardObserver: (Int) -> Unit = {}
) {
    viewTreeObserver.addOnGlobalLayoutListener {
        val rect = android.graphics.Rect()
        getWindowVisibleDisplayFrame(rect)

        val height: Int = context.resources.displayMetrics.heightPixels
        // 获取键盘抬高的高度
        val diff: Int = height - rect.height()
        keyboardObserver(diff)
    }
}

/**
 * webView设置back事件拦截
 */
fun WebView.setWebBackEvent() {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
    setOnKeyListener(object : View.OnKeyListener {
        override fun onKey(view: View?, i: Int, keyEvent: KeyEvent?): Boolean {
            if (canGoBack()) {
                goBack()
                return true
            }
            return false
        }

    })
}

/**
 * 选择radioButton 不通知监听器
 * @param id radioButtonViewId
 */
fun RadioGroup.checkUnNotification(@IdRes id: Int) {
    findViewById<RadioButton>(id).isChecked = true
}

/**
 * 更具滑动page，慢慢递增page缓存
 * 解决一次性缓存所有page页面初始化卡顿问题
 */
fun ViewPager2.increaseOffscreenPageLimit() {
    registerOnPageChangeCallback(object :
        ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            if (position == 0) return
            if (offscreenPageLimit < position) {
                offscreenPageLimit = position
            }
        }
    })
}

/**
 * ViewPager2 联动RadioGroup
 * @param radioGroup 联动的View
 */
fun ViewPager2.bindRadioGroup(radioGroup: RadioGroup) {
    registerOnPageChangeCallback(object :
        ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (radioGroup.size <= position) return

            val button = (radioGroup.getChildAt(position) as RadioButton)

            if (!button.isChecked) button.isChecked = true
        }
    })
    radioGroup.setOnCheckedChangeListener { group, checkedId ->
        val button = group.findViewById<RadioButton>(checkedId)

        group.children.forEachIndexed { index, view ->
            if (button == view && index != currentItem) {
                setCurrentItem(index, false)
            }
        }
    }
}

/**
 * 通过反射设置viewPager2阴影
 */
fun ViewPager2.overScrollNever(){
    val recyclerView = ReflexTools.reflexField<RecyclerView>(this,"mRecyclerView")
    recyclerView?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
}

