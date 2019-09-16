package gionee.gnservice.app.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.lee.library.widget.NoScrollViewPager;
import com.lee.library.widget.nav.BottomNavView;
import gionee.gnservice.app.view.widget.TouchMoveView;

public abstract class ActivityMainBinding extends ViewDataBinding {
  @NonNull
  public final FrameLayout frameUsercenterContainer;

  @NonNull
  public final TouchMoveView ivActive;

  @NonNull
  public final BottomNavView nav;

  @NonNull
  public final View statusBar;

  @NonNull
  public final NoScrollViewPager vpContainer;

  protected ActivityMainBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, FrameLayout frameUsercenterContainer, TouchMoveView ivActive,
      BottomNavView nav, View statusBar, NoScrollViewPager vpContainer) {
    super(_bindingComponent, _root, _localFieldCount);
    this.frameUsercenterContainer = frameUsercenterContainer;
    this.ivActive = ivActive;
    this.nav = nav;
    this.statusBar = statusBar;
    this.vpContainer = vpContainer;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityMainBinding>inflate(inflater, gionee.gnservice.app.R.layout.activity_main, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityMainBinding>inflate(inflater, gionee.gnservice.app.R.layout.activity_main, null, false, component);
  }

  public static ActivityMainBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityMainBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityMainBinding)bind(component, view, gionee.gnservice.app.R.layout.activity_main);
  }
}
