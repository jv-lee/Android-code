package gionee.gnservice.app.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gionee.gnservice.app.view.widget.RedRainLayout;

public abstract class ActivityRedPackageBinding extends ViewDataBinding {
  @NonNull
  public final RedRainLayout redRain;

  protected ActivityRedPackageBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, RedRainLayout redRain) {
    super(_bindingComponent, _root, _localFieldCount);
    this.redRain = redRain;
  }

  @NonNull
  public static ActivityRedPackageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityRedPackageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityRedPackageBinding>inflate(inflater, gionee.gnservice.app.R.layout.activity_red_package, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityRedPackageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityRedPackageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityRedPackageBinding>inflate(inflater, gionee.gnservice.app.R.layout.activity_red_package, null, false, component);
  }

  public static ActivityRedPackageBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityRedPackageBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityRedPackageBinding)bind(component, view, gionee.gnservice.app.R.layout.activity_red_package);
  }
}
