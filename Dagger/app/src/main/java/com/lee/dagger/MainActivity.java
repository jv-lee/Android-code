package com.lee.dagger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dagger.library.annotation.Inject;
import com.lee.dagger.apt_create_code.DaggerStudentComponent;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Inject
    public Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerStudentComponent.create().inject(this);
        Log.i(TAG, "onCreate: " + student.hashCode());
    }
}
