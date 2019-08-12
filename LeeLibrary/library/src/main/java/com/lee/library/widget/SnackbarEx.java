package com.lee.library.widget;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

/**
 * @author jv.lee
 * @date 2019/8/7.
 * @description
 */
public class SnackbarEx {
    private Snackbar snackbar;

    private SnackbarEx(Builder builder) {
        snackbar = Snackbar.make(builder.view, builder.message == null ? "" : builder.message, builder.duration);

        if (builder.messageColor != 0) {
            setSnackbarMessageColor(ContextCompat.getColor(builder.view.getContext(), builder.messageColor));
        }

        if (builder.actionText != null) {
            setSnackbarAction(builder.actionText, builder.onClickListener);
        }

        if (builder.actionTextColor != 0) {
            setSnackbarActionTextColor(ContextCompat.getColor(builder.view.getContext(), builder.actionTextColor));
        }

        if (builder.backgroundColor != 0) {
            setSnackbarBackgroundColor(ContextCompat.getColor(builder.view.getContext(), builder.backgroundColor));
        }
    }

    public void show() {
        if (snackbar != null) {
            snackbar.show();
        }
    }

    private void setSnackbarBackgroundColor(int backgroundColor) {
        snackbar.getView().setBackgroundColor(backgroundColor);
    }

    private void setSnackbarActionTextColor(int actionTextColor) {
        snackbar.setActionTextColor(actionTextColor);
    }

    private void setSnackbarAction(String actionText, View.OnClickListener onClickListener) {
        snackbar.setAction(actionText, onClickListener);
    }

    private void setSnackbarMessageColor(int messageColor) {
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(messageColor);
    }

    public static class Builder {
        private View view;
        private String message;
        private String actionText;
        private int messageColor;
        private int actionTextColor;
        private int backgroundColor;
        private int duration;
        private View.OnClickListener onClickListener;

        public Builder(View view) {
            this.view = view;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setActionText(String actionText) {
            this.actionText = actionText;
            return this;
        }

        public Builder setMessageColor(int messageColor) {
            this.messageColor = messageColor;
            return this;
        }

        public Builder setActionTextColor(int actionTextColor) {
            this.actionTextColor = actionTextColor;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public SnackbarEx build() {
            return new SnackbarEx(this);
        }
    }
}
