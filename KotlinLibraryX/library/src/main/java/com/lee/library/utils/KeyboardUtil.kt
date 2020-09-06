package com.lee.library.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout

/**
 * @author jv.lee
 * @date 2020/9/4
 * @description
 */
object KeyboardUtil {

    /**
     * 避免输入法面板遮挡
     * <p>在manifest.xml中activity中设置</p>
     * <p>android:windowSoftInputMode="adjustPan"</p>
     */

    /**
     * 避免输入法面板遮挡
     *
     * 在manifest.xml中activity中设置
     *
     * android:windowSoftInputMode="adjustPan"
     */
    /**
     * 动态隐藏软键盘
     *
     * @param activity activity
     */
    fun hideSoftInput(activity: Activity) {
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        var imm: InputMethodManager? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            imm =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun parentTouchHideSoftInput(activity: Activity?, view: View) {
        view.setOnTouchListener { view, motionEvent ->
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.requestFocus()
            hideSoftInput(activity!!)
            false
        }
    }

    /**
     * 点击屏幕空白区域隐藏软键盘（方法2）
     *
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
     *
     * 需重写dispatchTouchEvent
     *
     * 参照以下注释代码
     */
    fun clickBlankArea2HideSoftInput() {
        Log.d("tips", "U should copy the following code.")
        /*
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
        private boolean isShouldHideKeyboard(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + v.getHeight(),
                        right = left + v.getWidth();
                return !(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom);
            }
            return false;
        }
        */
    }

    /**
     * 动态显示软键盘
     *
     * @param context 上下文
     * @param edit    输入框
     */
    fun showSoftInput(context: Context, edit: EditText) {
        edit.isFocusable = true
        edit.isFocusableInTouchMode = true
        edit.requestFocus()
        var imm: InputMethodManager? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            imm =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            imm!!.showSoftInput(edit, 0)
        }
    }

    fun showSoftInput2(context: Context, editText: EditText) {
        editText.requestFocus()
        var imm: InputMethodManager? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            imm =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            imm!!.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
    }

    /**
     * 切换键盘显示与否状态
     *
     * @param context 上下文
     */
    fun toggleSoftInput(context: Context) {
        var imm: InputMethodManager? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            imm =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }

    /**
     * 打开键盘后移动rootView 该方式可使toolbar不动 ，只设置根View的marginBottom值来偏移
     * 只需给activity设置 android:windowSoftInputMode="stateHidden|adjustResize"
     */
    fun keyboardOpenMoveView(window: Window, rootView: ViewGroup) {
        val decorView = window.decorView
        //这个可以获取键盘的高度
        decorView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            decorView.getWindowVisibleDisplayFrame(rect);
            //计算出可见屏幕的高度
            val displayHeight = rect.bottom - rect.top
            //获取屏幕整体高度
            val height = decorView.height
            //判断键盘是否显示和消失
            val visible = displayHeight / height < 0.8
            val statusBarHeight = StatusUtil.getStatusBarHeight(rootView.context)
            val keyboardHeight = height - displayHeight - statusBarHeight
            onSoftKeyBoardVisible(visible, keyboardHeight, rootView)
        }
    }

    private fun onSoftKeyBoardVisible(visible: Boolean, keyboardHeight: Int, rootView: ViewGroup) {
        //如果键盘显示那么获取到他的高度设置rootView的marginBottom 这里rootView就是包裹EditText的那个view
        if (visible) {
            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            lp.setMargins(0, 0, 0, keyboardHeight)
            rootView.layoutParams = lp
        } else {
            //消失直接设置lyoContent的marginBottom 为0让其恢复到原来的布局
            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            lp.setMargins(0, 0, 0, 0)
            rootView.layoutParams = lp
        }
    }

}