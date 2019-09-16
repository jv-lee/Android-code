package gionee.gnservice.app;

import android.databinding.DataBinderMapper;
import android.databinding.DataBindingComponent;
import android.databinding.ViewDataBinding;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import gionee.gnservice.app.databinding.ActivityGameBindingImpl;
import gionee.gnservice.app.databinding.ActivityMainBindingImpl;
import gionee.gnservice.app.databinding.ActivityRedPackageBindingImpl;
import gionee.gnservice.app.databinding.ActivitySplashBindingImpl;
import gionee.gnservice.app.databinding.ActivityVideoDetailsBindingImpl;
import gionee.gnservice.app.databinding.ActivityWebBindingImpl;
import gionee.gnservice.app.databinding.FragmentDialogActiveBindingImpl;
import gionee.gnservice.app.databinding.FragmentDialogBackBindingImpl;
import gionee.gnservice.app.databinding.FragmentDialogNoviceBindingImpl;
import gionee.gnservice.app.databinding.FragmentDialogUpdateBindingImpl;
import gionee.gnservice.app.databinding.FragmentGameBindingImpl;
import gionee.gnservice.app.databinding.FragmentNewsBindingImpl;
import gionee.gnservice.app.databinding.FragmentNovelBindingImpl;
import gionee.gnservice.app.databinding.FragmentVideoBindingImpl;
import gionee.gnservice.app.databinding.FragmentVideoChildBindingImpl;
import gionee.gnservice.app.databinding.FragmentWalletBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Generated;

@Generated("Android Data Binding")
public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYGAME = 1;

  private static final int LAYOUT_ACTIVITYMAIN = 2;

  private static final int LAYOUT_ACTIVITYREDPACKAGE = 3;

  private static final int LAYOUT_ACTIVITYSPLASH = 4;

  private static final int LAYOUT_ACTIVITYVIDEODETAILS = 5;

  private static final int LAYOUT_ACTIVITYWEB = 6;

  private static final int LAYOUT_FRAGMENTDIALOGACTIVE = 7;

  private static final int LAYOUT_FRAGMENTDIALOGBACK = 8;

  private static final int LAYOUT_FRAGMENTDIALOGNOVICE = 9;

  private static final int LAYOUT_FRAGMENTDIALOGUPDATE = 10;

  private static final int LAYOUT_FRAGMENTGAME = 11;

  private static final int LAYOUT_FRAGMENTNEWS = 12;

  private static final int LAYOUT_FRAGMENTNOVEL = 13;

  private static final int LAYOUT_FRAGMENTVIDEO = 14;

  private static final int LAYOUT_FRAGMENTVIDEOCHILD = 15;

  private static final int LAYOUT_FRAGMENTWALLET = 16;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(16);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.activity_game, LAYOUT_ACTIVITYGAME);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.activity_main, LAYOUT_ACTIVITYMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.activity_red_package, LAYOUT_ACTIVITYREDPACKAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.activity_splash, LAYOUT_ACTIVITYSPLASH);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.activity_video_details, LAYOUT_ACTIVITYVIDEODETAILS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.activity_web, LAYOUT_ACTIVITYWEB);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.fragment_dialog_active, LAYOUT_FRAGMENTDIALOGACTIVE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.fragment_dialog_back, LAYOUT_FRAGMENTDIALOGBACK);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.fragment_dialog_novice, LAYOUT_FRAGMENTDIALOGNOVICE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.fragment_dialog_update, LAYOUT_FRAGMENTDIALOGUPDATE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.fragment_game, LAYOUT_FRAGMENTGAME);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.fragment_news, LAYOUT_FRAGMENTNEWS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.fragment_novel, LAYOUT_FRAGMENTNOVEL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.fragment_video, LAYOUT_FRAGMENTVIDEO);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.fragment_video_child, LAYOUT_FRAGMENTVIDEOCHILD);
    INTERNAL_LAYOUT_ID_LOOKUP.put(gionee.gnservice.app.R.layout.fragment_wallet, LAYOUT_FRAGMENTWALLET);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYGAME: {
          if ("layout/activity_game_0".equals(tag)) {
            return new ActivityGameBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_game is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMAIN: {
          if ("layout/activity_main_0".equals(tag)) {
            return new ActivityMainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_main is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYREDPACKAGE: {
          if ("layout/activity_red_package_0".equals(tag)) {
            return new ActivityRedPackageBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_red_package is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYSPLASH: {
          if ("layout/activity_splash_0".equals(tag)) {
            return new ActivitySplashBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_splash is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYVIDEODETAILS: {
          if ("layout/activity_video_details_0".equals(tag)) {
            return new ActivityVideoDetailsBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_video_details is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYWEB: {
          if ("layout/activity_web_0".equals(tag)) {
            return new ActivityWebBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_web is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTDIALOGACTIVE: {
          if ("layout/fragment_dialog_active_0".equals(tag)) {
            return new FragmentDialogActiveBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_dialog_active is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTDIALOGBACK: {
          if ("layout/fragment_dialog_back_0".equals(tag)) {
            return new FragmentDialogBackBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_dialog_back is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTDIALOGNOVICE: {
          if ("layout/fragment_dialog_novice_0".equals(tag)) {
            return new FragmentDialogNoviceBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_dialog_novice is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTDIALOGUPDATE: {
          if ("layout/fragment_dialog_update_0".equals(tag)) {
            return new FragmentDialogUpdateBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_dialog_update is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTGAME: {
          if ("layout/fragment_game_0".equals(tag)) {
            return new FragmentGameBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_game is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTNEWS: {
          if ("layout/fragment_news_0".equals(tag)) {
            return new FragmentNewsBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_news is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTNOVEL: {
          if ("layout/fragment_novel_0".equals(tag)) {
            return new FragmentNovelBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_novel is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTVIDEO: {
          if ("layout/fragment_video_0".equals(tag)) {
            return new FragmentVideoBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_video is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTVIDEOCHILD: {
          if ("layout/fragment_video_child_0".equals(tag)) {
            return new FragmentVideoChildBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_video_child is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTWALLET: {
          if ("layout/fragment_wallet_0".equals(tag)) {
            return new FragmentWalletBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_wallet is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(2);
    result.add(new com.android.databinding.library.baseAdapters.DataBinderMapperImpl());
    result.add(new com.lee.library.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(2);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(16);

    static {
      sKeys.put("layout/activity_game_0", gionee.gnservice.app.R.layout.activity_game);
      sKeys.put("layout/activity_main_0", gionee.gnservice.app.R.layout.activity_main);
      sKeys.put("layout/activity_red_package_0", gionee.gnservice.app.R.layout.activity_red_package);
      sKeys.put("layout/activity_splash_0", gionee.gnservice.app.R.layout.activity_splash);
      sKeys.put("layout/activity_video_details_0", gionee.gnservice.app.R.layout.activity_video_details);
      sKeys.put("layout/activity_web_0", gionee.gnservice.app.R.layout.activity_web);
      sKeys.put("layout/fragment_dialog_active_0", gionee.gnservice.app.R.layout.fragment_dialog_active);
      sKeys.put("layout/fragment_dialog_back_0", gionee.gnservice.app.R.layout.fragment_dialog_back);
      sKeys.put("layout/fragment_dialog_novice_0", gionee.gnservice.app.R.layout.fragment_dialog_novice);
      sKeys.put("layout/fragment_dialog_update_0", gionee.gnservice.app.R.layout.fragment_dialog_update);
      sKeys.put("layout/fragment_game_0", gionee.gnservice.app.R.layout.fragment_game);
      sKeys.put("layout/fragment_news_0", gionee.gnservice.app.R.layout.fragment_news);
      sKeys.put("layout/fragment_novel_0", gionee.gnservice.app.R.layout.fragment_novel);
      sKeys.put("layout/fragment_video_0", gionee.gnservice.app.R.layout.fragment_video);
      sKeys.put("layout/fragment_video_child_0", gionee.gnservice.app.R.layout.fragment_video_child);
      sKeys.put("layout/fragment_wallet_0", gionee.gnservice.app.R.layout.fragment_wallet);
    }
  }
}
