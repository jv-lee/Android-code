package com.lee.sheet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private SheetDialogFragment sheetDialogFragment = new SheetDialogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void sheetView(View view) {
        startActivity(new Intent(this, SheetViewActivity.class));
    }

    public void sheetAlert(View view) {
        startActivity(new Intent(this, SheetDialogActivity.class));
    }

    public void sheetFragment(View view) {
        sheetDialogFragment.show(getSupportFragmentManager(), "dialog");
    }

}
