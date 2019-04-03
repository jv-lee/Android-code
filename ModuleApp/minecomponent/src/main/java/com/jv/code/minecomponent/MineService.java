package com.jv.code.minecomponent;

import android.content.Context;
import android.content.Intent;

import com.jv.code.componentlib.service.IMineService;

public class MineService implements IMineService {
    @Override
    public void launch(Context context, int userId) {
        Intent intent = new Intent(context, MineService.class);
        context.startActivity(intent);
    }
}
