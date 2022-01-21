package com.lee.library.widget.toolbar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.lee.library.R
import com.lee.library.extensions.px2sp
import com.lee.library.extensions.setImageTintCompat
import com.lee.library.extensions.setSelectableItemForeground
import com.lee.library.extensions.setTextColorCompat
import com.lee.library.widget.toolbar.menu.CustomPopupMenuHelper

/**
 * @author jv.lee
 * @date 2020/4/1
 * @description 自定义项目 带标题的toolbar
 */
open class TitleToolbar : CustomToolbarLayout {

    private lateinit var ivBack: ImageView
    private lateinit var ivMore: ImageView
    private lateinit var tvTitle: TextView
    private var menuPopupHelper: CustomPopupMenuHelper? = null

    private var titleText: String
    private var titleColor: Int
    private var backIcon: Int
    private var backIconTint: Int
    private var moreIcon: Int
    private var moreIconTint: Int
    private var titleEnable: Int
    private var backEnable: Int
    private var moreEnable: Int
    private var menuRes: Int

    private var clickListener: ClickListener? = null

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributes: AttributeSet?) : this(context, attributes, 0)
    constructor(context: Context, attributes: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributes,
        defStyleAttr
    ) {
        val typeArray = context.obtainStyledAttributes(attributes, R.styleable.TitleToolbar)

        titleText = typeArray.getString(R.styleable.TitleToolbar_titleText) ?: ""
        titleColor = typeArray.getColor(
            R.styleable.TitleToolbar_titleColor,
            ContextCompat.getColor(context, R.color.baseDarkColor)
        )
        backIcon =
            typeArray.getResourceId(R.styleable.TitleToolbar_backIcon, R.drawable.vector_back)
        backIconTint =
            typeArray.getColor(
                R.styleable.TitleToolbar_backIconTint,
                ContextCompat.getColor(context, R.color.baseDarkColor)
            )
        moreIcon =
            typeArray.getResourceId(R.styleable.TitleToolbar_moreIcon, R.drawable.vector_more)
        moreIconTint =
            typeArray.getColor(
                R.styleable.TitleToolbar_moreIconTint,
                ContextCompat.getColor(context, R.color.baseDarkColor)
            )
        menuRes = typeArray.getResourceId(R.styleable.TitleToolbar_menuRes, 0)
        titleEnable = typeArray.getInt(R.styleable.TitleToolbar_titleEnable, View.VISIBLE)
        backEnable = typeArray.getInt(R.styleable.TitleToolbar_backEnable, View.VISIBLE)
        moreEnable = typeArray.getInt(R.styleable.TitleToolbar_moreEnable, View.GONE)
        typeArray.recycle()
        initView()
    }

    override fun initStatusBarPadding() {
        setPadding(0, getStatusBarHeight(), 0, 0)
    }

    private fun initView() {
        buildTitleText()
        buildBackImage()
        buildMoreImage()
        buildMenuWindow()
    }

    private fun buildBackImage() {
        ivBack = ImageView(context)
        ivBack.run {
            id = R.id.toolbar_back
            layoutParams =
                LayoutParams(
                    resources.getDimension(R.dimen.toolbar_height).toInt(),
                    resources.getDimension(R.dimen.toolbar_height).toInt()
                ).apply {
                    startToStart = 0
                }
            scaleType = ImageView.ScaleType.CENTER
            setSelectableItemForeground()
            setImageTintCompat(backIcon, backIconTint)
            visibility = backEnable
            setOnClickListener {
                try {
                    findNavController().navigateUp()
                } catch (e: Exception) {
                    (context as Activity).finish()
                }
                clickListener?.backClick()
            }
            addView(this)
        }
    }

    private fun buildTitleText() {
        tvTitle = TextView(context)
        tvTitle.run {
            id = R.id.toolbar_title
            layoutParams =
                LayoutParams(
                    WRAP_CONTENT,
                    resources.getDimension(R.dimen.toolbar_height).toInt()
                ).apply {
                    startToStart = 0
                    endToEnd = 0
                    topToTop = 0
                    bottomToBottom = 0
                    gravity = Gravity.CENTER
                }
            text = titleText
            setTextColor(titleColor)
            maxLines = 1
            maxEms = 10
            ellipsize = TextUtils.TruncateAt.END
            typeface = Typeface.DEFAULT_BOLD
            visibility = titleEnable
            textSize = context.px2sp(resources.getDimension(R.dimen.font_size_medium).toInt())
            addView(this)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun buildMoreImage() {
        ivMore = ImageView(context)
        ivMore.run {
            id = R.id.toolbar_more
            layoutParams =
                LayoutParams(
                    resources.getDimension(R.dimen.toolbar_height).toInt(),
                    resources.getDimension(R.dimen.toolbar_height).toInt()
                ).apply { endToEnd = 0 }
            scaleType = ImageView.ScaleType.CENTER
            setSelectableItemForeground()
            setImageTintCompat(moreIcon, moreIconTint)
            visibility = moreEnable
            setOnClickListener {
                clickListener?.moreClick()
            }
            addView(this)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun buildMenuWindow() {
        if (menuRes == 0) return
        menuPopupHelper = CustomPopupMenuHelper(context, menuRes)
    }

    fun showMenu() {
        menuPopupHelper?.menuPW?.showAsDropDown(ivMore)
    }

    fun showMenu(offsetX: Int, offsetY: Int) {
        menuPopupHelper?.menuPW?.showAsDropDown(ivMore, offsetX, offsetY)
    }

    /**
     * 设置toolbar title
     */
    fun setTitleText(title: String) {
        tvTitle.text = title
    }

    /**
     * 设置Back按键资源文件
     */
    fun setBackDrawable(drawable: Drawable) {
        ivBack.setImageDrawable(drawable)
    }

    /**
     * 设置Back按键资源
     */
    fun setBackDrawableRes(drawable: Int, tint: Int = R.color.baseDarkColor) {
        ivBack.setImageTintCompat(drawable, ContextCompat.getColor(context, tint))
    }

    fun setTitleColor(color: Int) {
        tvTitle.setTextColorCompat(color)
    }

    /**
     * 设置Menu按键资源文件
     */
    fun setMoreDrawable(drawable: Drawable) {
        ivMore.setImageDrawable(drawable)
    }

    /**
     * 设置More按键资源
     */
    fun setMoreDrawableRes(drawable: Int, tint: Int = R.color.baseDarkColor) {
        ivMore.setImageTintCompat(drawable, ContextCompat.getColor(context, tint))
    }

    /**
     * 设置标题显示状态
     */
    fun setTitleEnable(enable: Boolean) {
        tvTitle.visibility = if (enable) View.VISIBLE else View.GONE
    }

    /**
     * 设置back显示状态
     */
    fun setBackEnable(enable: Boolean) {
        ivBack.visibility = if (enable) View.VISIBLE else View.GONE
    }

    /**
     * 设置menu显示状态
     */
    fun setMoreEnable(enable: Boolean) {
        ivMore.visibility = if (enable) View.VISIBLE else View.GONE
    }

    open class ClickListener {
        open fun backClick() {}
        open fun moreClick() {}
        open fun menuItemClick(view: View) {}
    }

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
        menuPopupHelper?.toolbarClickListener = clickListener
    }

}