package code.lee.code.parallax;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import code.lee.code.parallax.widget.ParallaxContainer;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParallaxContainer container = findViewById(R.id.parallax_container);
        container.setUp(this,
                R.layout.view_intro_1,
                R.layout.view_intro_2,
                R.layout.view_intro_3,
                R.layout.view_intro_4,
                R.layout.view_intro_5,
                R.layout.view_login);
        container.setPeople((ImageView) findViewById(R.id.iv_people));
    }
}
