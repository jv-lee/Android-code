package com.lee.code.uidraw.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.lee.code.uidraw.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义淘宝数字密码输入组合控件
 * @author jv.lee
 * @date 2019/5/1
 */
public class InputPasswordView extends LinearLayout implements TextWatcher,View.OnKeyListener {
    private Paint mPaint;
    private int mWidth, mHeight;
    private int lineWidth = 3;
    private int childViewCount = 6;
    private String sign = "*";
    private List<EditText> views;
    private int[] passwordArray = new int[6];
    private PasswordChange onPasswordChange;

    public InputPasswordView(Context context) {
        this(context,null);
    }

    public InputPasswordView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public InputPasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //打开onDraw调用
        setWillNotDraw(false);
        init();
        initView();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(">>>", widthMeasureSpec + "-" + heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }


    private void initView() {
        views = new ArrayList<>();
        LayoutParams layoutParams = new LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,1);
        for (int i = 0; i < childViewCount; i++) {
            EditText editText = new EditText(getContext());
            editText.setTag(i);
            editText.setLayoutParams(layoutParams);
            editText.setBackground(null);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.setGravity(Gravity.CENTER);
            editText.setTextSize(20);
            editText.setFocusable(false);
            editText.setCursorVisible(false);
            editText.addTextChangedListener(this);
            editText.setOnKeyListener(this);
            editText.setTextColor(Color.BLACK);
            views.add(editText);
            addView(editText);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(">>>", "onDraw");
//        super.onDraw(canvas);
        drawRect(canvas);
        for (int i = 1; i < childViewCount; i++) {
            drawLine(i,canvas);
        }
    }

    /**
     * 绘制密码输入框最外层矩形
     *
     * @param canvas
     */
    private void drawRect(Canvas canvas) {
        canvas.drawRoundRect(new RectF(lineWidth, lineWidth, mWidth-lineWidth, mHeight-lineWidth), 10, 10, mPaint);
    }

    /**
     * 绘制分割矩形的线
     *
     * @param index
     * @param canvas
     */
    private void drawLine(int index, Canvas canvas) {
        mPaint.setStrokeWidth((float) lineWidth);
        int x = ((mWidth / childViewCount) * index );
        canvas.drawLine(x, (float) (mHeight*0.3), x, (float) (mHeight*0.7), mPaint);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(sign)) {
            onHideNumber();
            onNextEdit();
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            onDeleteChange();
        }
        return false;
    }

    /**
     * 设置下一个editText获取焦点
     */
    private void onNextEdit(){
        for (EditText view : views) {
            //判断当前view是否输入
            if (view.getText().length() > 0) {
                //输入后取消焦点
                view.setFocusable(false);

                //最后一个输入完直接回调发起验证
                if ((Integer)view.getTag() == views.size() - 1) {
                    if (onPasswordChange != null) {
                        onPasswordChange.onChange(getPassword());
                    }
                    return;
                }
                //设置下一个edit获取焦点
                etFocusable(views.get((Integer) view.getTag()+1));
            }
        }
    }

    /**
     * 输入数字后隐藏 设置为  sign:*
     */
    private void onHideNumber(){
        for (int i = 0; i < views.size(); i++) {
            EditText editText = views.get(i);
            String text = editText.getText().toString();
            if (!text.equals(sign) && text.length() > 0) {
                passwordArray[i] = Integer.valueOf(text);
                editText.setText(sign);
            }
        }

    }

    /**
     * 设置获取焦点
     * @param editText
     */
    private void etFocusable(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    /**
     * 按键盘删除时清除当前text  焦点移动到上一个editText
     */
    private void onDeleteChange() {
        for (EditText view : views) {
            if (view.isFocusable()) {
                view.setFocusable(false);
                Integer index = getEtIndex(view)==0?0:getEtIndex(view)-1;
                EditText editText = views.get(index);
                editText.getText().clear();
                etFocusable(editText);
                return;
            }
        }
    }

    private Integer getEtIndex(EditText editText) {
        return (Integer) editText.getTag();
    }

    private Integer getPassword(){
        StringBuilder psd = new StringBuilder();
        for (int i : passwordArray) {
            psd.append(i);
        }
        return Integer.valueOf(psd.toString());
    }

    /**
     * 获取焦点打开键盘
     */
    public void openInput(){
        EditText editText = views.get(0);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    /**
     * 获取焦点打开键盘
     */
    public void openInput(Activity activity){
        EditText editText = views.get(0);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public interface PasswordChange {
        /**
         * 密码回调
         * @param password
         */
        void onChange(Integer password);
    }

    public void setPasswordChange(PasswordChange onPasswordChange) {
        this.onPasswordChange = onPasswordChange;
    }
}
