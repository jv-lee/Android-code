package gionee.gnservice.app.tool;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author jv.lee
 * @date 2019/8/20.
 * @description
 */
public class DialogTool {
    private Context mContext;
    private ViewInterface viewInterface;

    public DialogTool(Context context, ViewInterface viewInterface) {
        mContext = context;
        this.viewInterface = viewInterface;
    }

    public AlertDialog build() {
        return createView();
    }

    private AlertDialog createView() {
        View view = viewInterface.createView();
        AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        Window window = dialog.getWindow();
        /**
         * Window 是分层的，每个 Window 都有对应的 z-ordered，
         * 层级大的会覆盖在层级小的 Window 上面，这和 HTML 中的 z-index 概念是完全一致的。
         * 在三种 Window 中，
         * 应用 Window 层级范围是 1~99，子 Window 层级范围是 1000~1999，系统 Window 层级范围是 2000~2999：
         *
         */
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        window.setContentView(view);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.alpha = 1f;
        lp.dimAmount = 0.8f;
        window.setAttributes(lp);
        return dialog;
    }

    public interface ViewInterface {
        View createView();
    }

}
