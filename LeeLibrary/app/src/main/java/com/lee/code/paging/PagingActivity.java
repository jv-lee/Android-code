package com.lee.code.paging;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lee.code.R;
import com.lee.library.base.BaseActivity;
import com.lee.library.ioc.annotation.ContentView;
import com.lee.library.ioc.annotation.InjectView;


@ContentView(R.layout.activity_paging)
public class PagingActivity extends BaseActivity {

    @InjectView(R.id.rv_container)
    RecyclerView rvContainer;
    PersonAdapter personAdapter;

    @Override
    protected void bindData(Bundle savedInstanceState) {
        personAdapter = new PersonAdapter();
    }

    @Override
    protected void bindView() {
        rvContainer.setLayoutManager(new LinearLayoutManager(this));
        rvContainer.setAdapter(personAdapter);
        initData();
    }

    public void initData(){
        PersonDao dao = PersonDatabase.getInstance(PagingActivity.this).personDao();

        LiveData<PagedList<Person>> persons = new LivePagedListBuilder(dao.getAllPersons(),
                new PagedList.Config.Builder()
                        .setPageSize(10)
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .build()).build();
//        new LivePagedListBuilder(dao.getAllPersons2(),
//                new PagedList.Config.Builder()
//                .setPageSize(PAGE_SIZE)                         //配置分页加载的数量
//                .setEnablePlaceholders(false)     //配置是否启动PlaceHolders
//                .setInitialLoadSizeHint(PAGE_SIZE)              //初始化加载的数量
//                .build()).build();
        personAdapter.submitList(persons.getValue());
    }

}
