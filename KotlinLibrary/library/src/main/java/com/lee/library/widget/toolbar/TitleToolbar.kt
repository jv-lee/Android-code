package com.lee.library.widget.toolbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.navigation.findNavController
import com.lee.library.R
import com.lee.library.extensions.setImageTintCompat
import com.lee.library.utils.SizeUtil
import com.lee.library.utils.StatusUtil
import com.lee.library.widget.menu.CustomPopupMenuHelper

/**
 * @author jv.lee
 * @date 2020/4/1
 * @description 自定义项目 带标题的toolbar
 */
open class TitleToolbar : CustomToolbarLayout {

    var ivBack: ImageView? = null
    var ivMenu: ImageView? = null
    var tvTitle: TextView? = null
    var menuPopupHelper: CustomPopupMenuHelper? = null

    private var titleText: String? = null
    private var backIcon: Int? = null
    private var backIconTint: Int? = null
    private var menuIcon: Int? = null
    private var menuIconTint: Int? = null
    private var menuRes: Int? = null
    private var titleEnable: Int? = null
    private var backEnable: Int? = null
    private var menuEnable: Int? = null

    private var clickListener: ClickListener? = null

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributes: AttributeSet) : this(context, attributes, 0)
    constructor(context: Context, attributes: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributes,
        defStyleAttr
    ) {
        initAttr(attributes!!)
        initView()
    }

    /**
     * 设置状态栏填充padding
     */
    override fun initStatusBarPadding() {
        val statusHeight = StatusUtil.getStatusBarHeight(context)
        setPadding(0, statusHeight, 0, 0)
    }

    private fun initAttr(attrs: AttributeSet) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.TitleToolbar)

        titleText = typeArray.getString(R.styleable.TitleToolbar_titleText)
        backIcon =
            typeArray.getResourceId(R.styleable.TitleToolbar_backIcon, R.drawable.vector_back)
        backIconTint =
            typeArray.getColor(
                R.styleable.TitleToolbar_backIconTint,
                ContextCompat.getColor(context, R.color.colorThemeAccent)
            )
        menuIcon =
            typeArray.getResourceId(R.styleable.TitleToolbar_menuIcon, R.drawable.vector_menu)
        menuIconTint =
            typeArray.getColor(
                R.styleable.TitleToolbar_menuIconTint,
                ContextCompat.getColor(context, R.color.colorThemeAccent)
            )
        menuRes = typeArray.getResourceId(R.styleable.TitleToolbar_menuRes, 0)
        titleEnable = typeArray.getInt(R.styleable.TitleToolbar_titleEnable, View.VISIBLE)
        backEnable = typeArray.getInt(R.styleable.TitleToolbar_backEnable, View.VISIBLE)
        menuEnable = typeArray.getInt(R.styleable.TitleToolbar_menuEnable, View.VISIBLE)
        typeArray.recycle()
    }

    private fun initView() {
        buildTitleText()
        buildBackImage()
        buildMenuImage()
        buildMenuWindow()
    }

    private fun buildBackImage() {
        ivBack = ImageView(context)
        ivBack?.run {
            layoutParams =
                LayoutParams(
                    resources.getDimension(R.dimen.toolbar_button_width).toInt(),
                    MATCH_PARENT
                )
            updateLayoutParams<ConstraintLayout.LayoutParams> { startToStart = 0 }
            scaleType = ImageView.ScaleType.CENTER
            backIcon?.let { setImageTintCompat(it, backIconTint!!) }
            backEnable?.let { visibility = it }
            setOnClickListener {
                findNavController().popBackStack()
                clickListener?.backClick()
            }
            addView(this)
        }
    }

    private fun buildTitleText() {
        tvTitle = TextView(context)
        tvTitle?.run {
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            updateLayoutParams<ConstraintLayout.LayoutParams> {
                startToStart = 0
                endToEnd = 0
                topToTop = 0
                bottomToBottom = 0
            }
            setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
            titleText?.let { text = it }
            titleEnable?.let { visibility = it }
            textSize =
                SizeUtil.px2sp(context, resources.getDimension(R.dimen.font_size_medium)).toFloat()
            addView(this)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun buildMenuImage() {
        ivMenu = ImageView(context)
        ivMenu?.run {
            layoutParams =
                LayoutParams(
                    resources.getDimension(R.dimen.toolbar_button_width).toInt(),
                    MATCH_PARENT
                )
            updateLayoutParams<ConstraintLayout.LayoutParams> { endToEnd = 0 }
            scaleType = ImageView.ScaleType.CENTER
            menuIcon?.let { setImageTintCompat(it, menuIconTint!!) }
            menuEnable?.let { visibility = it }
            setOnClickListener {
                clickListener?.menuClick()
            }
            addView(this)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun buildMenuWindow() {
        if (menuRes == 0) return
        ivMenu?.let {
            menuRes?.let { it ->
                menuPopupHelper = CustomPopupMenuHelper(context, it)
            }
        }
    }

    fun showMenu() {
        menuPopupHelper?.menuPW?.showAsDropDown(ivMenu)
    }

    fun showMenu(offsetX: Int, offsetY: Int) {
        menuPopupHelper?.menuPW?.showAsDropDown(ivMenu, offsetX, offsetY)
    }

    /**
     * 设置toolbar title
     */
    fun setTitleText(title: String) {
        tvTitle?.text = title
    }

    /**
     * 设置Back按键资源文件
     */
    fun setBackDrawable(drawable: Drawable) {
        ivBack?.setImageDrawable(drawable)
    }

    /**
     * 设置Menu按键资源文件
     */
    fun setMenuDrawable(drawable: Drawable) {
        ivMenu?.setImageDrawable(drawable)
    }

    open class ClickListener {
        open fun backClick() {}
        open fun menuClick() {}
        open fun menuItemClick(view: View) {}
    }

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
        menuPopupHelper?.toolbarClickListener = clickListener
    }

}