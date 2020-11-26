package com.lee.library.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jv.lee
 * @date 2020/3/16
 * @description textView 文本标记span 帮助类
 */
public class TextSpanHelper {

    /**
     * 设置span的view
     */
    private TextView textView;
    /**
     * 具体span的样式
     */
    private SpannableStringBuilder style;
    /**
     * span样式生成正则规则
     */
    private Pattern pattern;
    /**
     * 监听span样式点击回调
     */
    private Callback callback;
    /**
     * 正则规则是否为分组模式
     */
    private boolean isGroup;
    /**
     * 设置span样式字体颜色
     */
    private int color;

    private boolean isBold;

    private void setBoldSpan(int star, int end) {
        style.setSpan(new StyleSpan(Typeface.BOLD), star, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 设置span样式 颜色
     *
     * @param star 开始位置
     * @param end  结束位置
     */
    private void setColorSpan(int star, int end) {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        style.setSpan(colorSpan, star, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(style);
    }

    /**
     * 设置span样式 点击
     *
     * @param star 点击文字开始下标
     * @param end  点击文字结束下标
     * @param text 被点击的文字
     */
    private void setClickSpan(int star, int end, final String text) {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (callback != null) {
                    callback.onClickString(text);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ds.linkColor);
                ds.setUnderlineText(false);
            }

        };
        style.setSpan(clickableSpan, star, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setHighlightColor(Color.TRANSPARENT);
        textView.setText(style);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 根据规则构建span样式及点击位置
     */
    public void buildSpan() {
        if (pattern == null) {
            return;
        }
        Matcher matcher = pattern.matcher(style);
        while (matcher.find()) {
            int group = isGroup ? 1 : 0;
            String text = matcher.group(group);
            setClickSpan(matcher.start(group), matcher.end(group), text);
            setColorSpan(matcher.start(group), matcher.end(group));
            if (isBold) {
                setBoldSpan(matcher.start(group), matcher.end(group));
            }
        }
    }

    public interface Callback {
        /**
         * 文本点击字符
         *
         * @param text
         */
        void onClickString(String text);
    }

    private TextSpanHelper() {

    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public SpannableStringBuilder getStyle() {
        return style;
    }

    public void setStyle(SpannableStringBuilder style) {
        this.style = style;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public static class Builder {
        private SpannableStringBuilder style;
        private String text;
        private TextView textView;
        private String pattern;
        private Callback callback;
        private boolean isGroup;
        private int color;
        private boolean isBold;

        private Builder() {
        }

        public Builder(TextView textView) {
            this.textView = textView;
        }

        public Builder setStyle(SpannableStringBuilder style) {
            this.style = style;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setPattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder setCallback(Callback callback) {
            this.callback = callback;
            return this;
        }

        public Builder isGroup(boolean group) {
            isGroup = group;
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setBold(boolean isBold) {
            this.isBold = isBold;
            return this;
        }

        public TextSpanHelper create() {
            TextSpanHelper textSpanHelper = new TextSpanHelper();

            if (textView != null) {
                textSpanHelper.setTextView(textView);
            }

            /**
             * 构建时穿text 则为基础设置span ， 传入style 则为在style原来基础之上继续添加span标记
             */
            if (text != null) {
                textSpanHelper.setStyle(new SpannableStringBuilder(text));
            } else if (style != null) {
                textSpanHelper.setStyle(style);
            } else {
                throw new IllegalArgumentException("请在构建" + TextSpanHelper.class.getSimpleName() + "时 调用setText()方法 或者 setStyle()方法 ");
            }

            if (pattern != null) {
                textSpanHelper.setPattern(Pattern.compile(pattern));
            } else {
                throw new IllegalArgumentException("请在构建" + TextSpanHelper.class.getSimpleName() + "时 setPattern()方法");
            }

            textSpanHelper.setGroup(isGroup);

            if (color != 0) {
                textSpanHelper.setColor(color);
            } else {
                textSpanHelper.setColor(Color.parseColor("#FF0000"));
            }

            if (callback != null) {
                textSpanHelper.setCallback(callback);
            }

            textSpanHelper.setBold(isBold);

            return textSpanHelper;
        }
    }

}
