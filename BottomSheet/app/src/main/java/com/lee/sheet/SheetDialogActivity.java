package com.lee.sheet;

import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * @author jv.lee
 */
public class SheetDialogActivity extends AppCompatActivity {

    private BottomSheetDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_dialog);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_sheet_layout);
    }

    public void sheetDialog(View view) {
        dialog.show();
    }
}
