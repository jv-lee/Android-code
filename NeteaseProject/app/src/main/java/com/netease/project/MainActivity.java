package com.netease.project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.netease.core.network.rx.databus.RxBus;
import com.netease.project.adapter.GirlAdapter;
import com.netease.project.bean.Girl;
import com.netease.project.dagger.DaggerGirlPresenterComponent;
import com.netease.project.dagger.GirlPresenterModule;
import com.netease.project.presenter.GirlPresenter;
import com.netease.project.view.IGirlView;

import java.util.List;

import javax.inject.Inject;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity implements IGirlView {

    private ListView listView;

    @Inject
    GirlPresenter girlPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
//        DaggerGirlPresenterComponent.builder().build().inject(this);
        DaggerGirlPresenterComponent.builder().girlPresenterModule(new GirlPresenterModule(this)).build().inject(this);

        RxBus.getInstance().register(girlPresenter);
    }

    @Override
    public void showGirlData(List<Girl> girls) {
        listView.setAdapter(new GirlAdapter(getLayoutInflater(), girls));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(girlPresenter);
    }
}
