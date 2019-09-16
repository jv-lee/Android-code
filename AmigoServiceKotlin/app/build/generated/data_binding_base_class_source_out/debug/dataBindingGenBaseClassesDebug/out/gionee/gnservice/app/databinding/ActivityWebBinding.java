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

public abstract class ActivityWebBinding extends ViewDataBinding {
  @NonNull
  public final View include;

  @NonNull
  public final WebViewEx web;

  protected ActivityWebBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, View include, WebViewEx web) {
    super(_bindingComponent, _root, _localFieldCount);
    this.include = include;
    this.web = web;
  }

  @NonNull
  public static ActivityWebBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityWebBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityWebBinding>inflate(inflater, gionee.gnservice.app.R.layout.activity_web, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityWebBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityWebBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityWebBinding>inflate(inflater, gionee.gnservice.app.R.layout.activity_web, null, false, component);
  }

  public static ActivityWebBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityWebBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityWebBinding)bind(component, view, gionee.gnservice.app.R.layout.activity_web);
  }
}
