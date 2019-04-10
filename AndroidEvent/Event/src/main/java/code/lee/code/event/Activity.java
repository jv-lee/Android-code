package code.lee.code.event;

/**
 * @author jv.lee
 * @date 2019/4/8
 */
public class Activity {

    public static void main(String[] args) {
        MotionEvent motionEvent = new MotionEvent(100, 100);
        motionEvent.setActionMasked(MotionEvent.ACTION_DOWN);

    }

}
