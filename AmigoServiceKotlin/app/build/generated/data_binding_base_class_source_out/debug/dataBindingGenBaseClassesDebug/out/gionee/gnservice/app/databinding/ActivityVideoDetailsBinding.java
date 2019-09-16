package gionee.gnservice.app.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lee.library.widget.WebViewEx;

public abstract class ActivityVideoDetailsBinding extends ViewDataBinding {
  @NonNull
  public final View include;

  @NonNull
  public final WebViewEx web;

  protected ActivityVideoDetailsBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, View include, WebViewEx web) {
    super(_bindingComponent, _root, _localFieldCount);
    this.include = include;
    this.web = web;
  }

  @NonNull
  public static ActivityVideoDetailsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityVideoDetailsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityVideoDetailsBinding>inflate(inflater, gionee.gnservice.app.R.layout.activity_video_details, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityVideoDetailsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityVideoDetailsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityVideoDetailsBinding>inflate(inflater, gionee.gnservice.app.R.layout.activity_video_details, null, false, component);
  }

  public static ActivityVideoDetailsBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityVideoDetailsBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityVideoDetailsBinding)bind(component, view, gionee.gnservice.app.R.layout.activity_video_details);
  }
}
