package code.lee.code.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private ImageView ivIcon;
    private Button btn;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivIcon = findViewById(R.id.iv_icon);
        btn = findViewById(R.id.btn);
        tvContent = findViewById(R.id.tv_content);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });
    }

    private void startAnimation() {
//        commonAnimator();
//        propertyAnimator();
        setAnimator();
        btn.setText("倒计时：5");
        startTime();
    }

    /**
     * 设置倒计时操作
     */
    private void startTime() {
        ValueAnimator animator = ValueAnimator.ofInt(5, 0);
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                btn.setText("倒计时："+(value));
            }
        });
        animator.start();
    }

    /**
     * 属性动画 直接设置动画操作
     * rotation旋转
     * translation平移
     * scale缩放
     * alpha透明度
     */
    public void commonAnimator(){
        ObjectAnimator.ofFloat(ivIcon, "rotation",0,360F).setDuration(1000).start();
        ObjectAnimator.ofFloat(ivIcon,"translationX",0,200F).setDuration(1000).start();
        ObjectAnimator.ofFloat(ivIcon,"translationY",0,200).setDuration(1000).start();
        ObjectAnimator.ofFloat(ivIcon,"scaleX",1,2F).setDuration(1000).start();
        ObjectAnimator.ofFloat(ivIcon,"scaleY",1,2F).setDuration(1000).start();
        ObjectAnimator.ofFloat(ivIcon,"alpha",1f,0.5f,1f).setDuration(1000).start();
    }

    /**
     * 使用多重动画时 使用Holder类优化性能
     */
    public void propertyAnimator(){
        PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation", 0, 360F);
        PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", 0, 200F);
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", 0, 200F);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1F, 2F);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1F, 2F);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1F, 0.5F, 1F);
        ObjectAnimator.ofPropertyValuesHolder(ivIcon,rotation,translationX,translationY,scaleX,scaleY,alpha).setDuration(1000).start();
    }

    /**
     * 多种动画组合在set中播放
     */
    public void setAnimator(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivIcon, "rotation", 0, 360F);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(ivIcon, "translationX", 0, 200F);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(ivIcon, "translationY", 0, 200);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(ivIcon, "scaleX", 1, 2F);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(ivIcon, "scaleY", 1, 2F);
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(ivIcon, "alpha", 1f, 0.5f, 1f);
        AnimatorSet set = new AnimatorSet();
        //同时播放方法
//        set.playTogether(animator,animator1,animator2,animator3,animator4,animator5);
        //按顺序执行动画 执行完第一个再执行下一个
//        set.playSequentially(animator,animator1,animator2,animator3,animator4,animator5);

        //单独控制每个动画顺序  with 同时执行的动画绑定 ，  after在谁之后执行  befor 在谁之前执行
        set.play(animator1).with(animator2);
        set.play(animator).with(animator3).with(animator4).with(animator5).after(animator1);
        set.setDuration(1000);
        set.start();
        //设置监听  AnimatorListenerAdapter是系统实现的一个监听适配接口 可以选择具体实现的方法 比如 end 和 start 其他就可以不用选择
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
    }
}
