package com.lee.code;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lee.library.base.BaseFragment;
import com.lee.library.ioc.annotation.ContentView;
import com.lee.library.ioc.annotation.InjectView;
import com.lee.library.widget.MarqueeView;
import com.lee.library.widget.banner.MZBannerView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * @author jv.lee
 */
@ContentView(R.layout.fragment_banner_and_marquee)
public class BannerAndMarqueeFragment extends BaseFragment {

    @InjectView(R.id.banner)
    MZBannerView<String> banner;
    @InjectView(R.id.marquee)
    MarqueeView marquee;

    private List<String> data;
    private List<String> marqueeData;

    public BannerAndMarqueeFragment() {
        // Required empty public constructor
    }

    @Override
    protected void bindData(Bundle savedInstanceState) {
        data = new ArrayList<>();
        data.add("https://cdn.pixabay.com/photo/2019/05/01/09/54/pink-4170404__340.jpg");
        data.add("https://cdn.pixabay.com/photo/2015/12/08/00/28/elephants-1081749__340.jpg");
        data.add("https://cdn.pixabay.com/photo/2018/10/01/09/21/pets-3715733__340.jpg");
        data.add("https://cdn.pixabay.com/photo/2017/01/14/16/36/city-1979892__340.jpg");
        data.add("https://cdn.pixabay.com/photo/2019/05/17/04/35/lighthouse-4208843__340.jpg");

        marqueeData = new ArrayList<>();
        marqueeData.add("这是我第一次中奖~");
        marqueeData.add("我又中奖了！！！");
        marqueeData.add("3连发？？ 还是我啊~");
        marqueeData.add("什么鬼 怎么都是我中奖~");
        marqueeData.add("人气大爆发？？？~");
        marqueeData.add("我都不敢相信还是我了！！！~");
    }

    @Override
    protected void bindView() {
//        banner.setPages(data, ContentBannerViewHolder::new);
        marquee.bindData(marqueeData);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onFragmentResume() {
        super.onFragmentResume();
        if (banner != null) {
            banner.start();
        }
        if (marquee != null) {
            marquee.start();
        }
    }

    @Override
    protected void onFragmentPause() {
        super.onFragmentPause();
        if (banner != null) {
            banner.pause();
        }
        if (marquee != null) {
            marquee.pause();
        }
    }

}
