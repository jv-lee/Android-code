package com.gionee.gnservice.widget;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Created by caocong on 11/10/16.
 */
public class SpaceAmigoTextView extends TextView {
    public final static float NORMAL = 0;
    private float spacing = NORMAL;
    private CharSequence mOriginalText;

    public SpaceAmigoTextView(Context context) {
        this(context, null);
    }

    public SpaceAmigoTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpaceAmigoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public float getSpacing() {
        return this.spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
        applySpacing();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mOriginalText = text;
        applySpacing();
    }

    @Override
    public CharSequence getText() {
        return mOriginalText;
    }

    private void applySpacing() {
        CharSequence finalText = "";
        if (!TextUtils.isEmpty(mOriginalText)) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mOriginalText.length(); i++) {
                builder.append(mOriginalText.charAt(i));
                if (i + 1 < mOriginalText.length()) {
                    builder.append("\u00A0");
                }
            }
            SpannableString string = new SpannableString(builder.toString());
            if (builder.toString().length() > 1) {
                for (int i = 1; i < builder.toString().length(); i += 2) {
                    string.setSpan(new ScaleXSpan((spacing + 1) / 10), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            finalText = string;
        }
        super.setText(finalText, BufferType.SPANNABLE);
    }
}
