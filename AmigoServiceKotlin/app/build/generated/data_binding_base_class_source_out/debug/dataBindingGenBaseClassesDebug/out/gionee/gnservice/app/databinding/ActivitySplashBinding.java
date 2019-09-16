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

public abstract class ActivitySplashBinding extends ViewDataBinding {
  @NonNull
  public final FrameLayout frameSplashContainer;

  protected ActivitySplashBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, FrameLayout frameSplashContainer) {
    super(_bindingComponent, _root, _localFieldCount);
    this.frameSplashContainer = frameSplashContainer;
  }

  @NonNull
  public static ActivitySplashBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivitySplashBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivitySplashBinding>inflate(inflater, gionee.gnservice.app.R.layout.activity_splash, root, attachToRoot, component);
  }

  @NonNull
  public static ActivitySplashBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivitySplashBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivitySplashBinding>inflate(inflater, gionee.gnservice.app.R.layout.activity_splash, null, false, component);
  }

  public static ActivitySplashBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivitySplashBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivitySplashBinding)bind(component, view, gionee.gnservice.app.R.layout.activity_splash);
  }
}
