package gionee.gnservice.app.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class FragmentVideoBinding extends ViewDataBinding {
  @NonNull
  public final TabLayout tab;

  @NonNull
  public final ViewPager vpContainer;

  protected FragmentVideoBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, TabLayout tab, ViewPager vpContainer) {
    super(_bindingComponent, _root, _localFieldCount);
    this.tab = tab;
    this.vpContainer = vpContainer;
  }

  @NonNull
  public static FragmentVideoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentVideoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentVideoBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_video, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentVideoBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentVideoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentVideoBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_video, null, false, component);
  }

  public static FragmentVideoBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static FragmentVideoBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (FragmentVideoBinding)bind(component, view, gionee.gnservice.app.R.layout.fragment_video);
  }
}
