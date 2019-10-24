package com.netease.project.task;

import com.netease.core.network.rx.databus.RxBus;
import com.netease.project.R;
import com.netease.project.bean.Girl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * @author jv.lee
 * @date 2019/10/22.
 * @description
 */
public class GirlTaskImpl implements IGirlTask {
    @Override
    public void loadGirlData() {
        RxBus.getInstance().doProcessInvoke(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                List<Girl> list = new ArrayList<>();
                list.add(new Girl(R.drawable.ic_launcher_background, "数据一号", "1111111111111"));
                list.add(new Girl(R.drawable.ic_launcher_background, "数据二号", "2222222222222"));
                list.add(new Girl(R.drawable.ic_launcher_background, "数据三号", "3333333333333"));
                list.add(new Girl(R.drawable.ic_launcher_background, "数据四号", "4444444444444"));
                list.add(new Girl(R.drawable.ic_launcher_background, "数据五号", "5555555555555"));
                list.add(new Girl(R.drawable.ic_launcher_background, "数据六号", "6666666666666"));
                list.add(new Girl(R.drawable.ic_launcher_background, "数据七号", "7777777777777"));
                return list;
            }
        });
    }
}
