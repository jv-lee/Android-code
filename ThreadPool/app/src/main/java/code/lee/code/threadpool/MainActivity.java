package code.lee.code.threadpool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startTask(View view) {
        for (int i = 0; i < 100; i++) {
            ThreadPoolManager.getInstance().addTask(new Runnable() {
                @Override
                public void run() {
                    Log.i("lee >>>", "this is "+Thread.currentThread().getName());
                }
            });
        }

    }
}
