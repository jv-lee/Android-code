package code.lee.code.event;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019/4/8
 */
public class ViewGroup extends View {

    List<View> childList = new ArrayList<>();
    private View[] mChildren = new View[0];
    private TouchTarget mFirstTouchTarget;

    public void addView(View view) {
        if (view == null) {
            return;
        }
        childList.add(view);
        mChildren = childList.toArray(new View[childList.size()]);
    }

    /**
     * 事件分发入口
     *
     * @param event
     * @return
     */
    public boolean dispatchTouchEvent(MotionEvent event) {

        boolean intercepted = onInterceptTouchEvent(event);

        //TouchTarget缓存模式
        TouchTarget newTouchTarget = null;
        int actionMasked = event.getActionMasked();
        if (actionMasked != MotionEvent.ACTION_CANCEL && !intercepted) {
            //Down事件
            if (actionMasked == MotionEvent.ACTION_DOWN) {
                final View[] children = mChildren;
                //耗时
                for (int i = children.length - 1; i >= 0; i--) {
                    View child = mChildren[i];

                    //View不能够接受事件
                    if (!child.isContainer(event.getX(), event.getY())) {
                        continue;
                    }

                    //能够接受事件的view 分发给他
                    if (dispatchTransformedTouchEvent(event, child)) {
                        //View[] 采取了 Message的方式进行 链表结构
                        newTouchTarget = addTouchTarget(child);
                        break;
                    }

                }
            }

            if (mFirstTouchTarget == null) {
                dispatchTransformedTouchEvent(event, null);
            }

        }
        return
    }

    private TouchTarget addTouchTarget(View child) {
        final TouchTarget target = TouchTarget.obtain(child);
        target.next = mFirstTouchTarget;
        mFirstTouchTarget = target;
        return target;
    }

    private static final class TouchTarget {
        /**
         * 当前缓存的View
         */
        public View child;
        /**
         * 回收池（一个对象）
         */
        private static TouchTarget sRecycleBin;
        private static final Object sRecycleLock = new Object[0];
        public TouchTarget next;
        private static int sRecycledCount;
        public static TouchTarget obtain(View child) {
            TouchTarget target;
            synchronized (sRecycleLock) {
                if (sRecycleBin == null) {
                    target = new TouchTarget();
                } else {
                    target = sRecycleBin;
                }
                sRecycleBin = target.next;
                sRecycledCount--;
                target.next = null;
            }
            target.child = child;
            return target;
        }

        public void recycle(){
            if (child == null) {

            }

            synchronized (sRecycleLock) {
                if (sRecycledCount < 32) {
                    next = sRecycleBin;
                    sRecycleBin =
                }
            }

        }

    }


    private boolean dispatchTransformedTouchEvent(MotionEvent event, View child) {
        boolean handle = false;
        //当前事件消费了
        if (child != null) {
            handle = child.dispatchTouchEvent(event);
        }
        return handle;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

}
