package code.lee.code.event;

/**
 * @author jv.lee
 * @date 2019/4/8
 */
public class MotionEvent {
    public static final int ACTION_DOWN = 0;
    public static final int ACTION_UP = 1;
    public static final int ACTION_MOVE = 2;
    public static final int ACTION_CANCEL = 3;

    private int x;
    private int y;
    private int actionMasked;

    public MotionEvent() {
    }

    public MotionEvent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getActionMasked() {
        return actionMasked;
    }

    public void setActionMasked(int actionMasked) {
        this.actionMasked = actionMasked;
    }
}
