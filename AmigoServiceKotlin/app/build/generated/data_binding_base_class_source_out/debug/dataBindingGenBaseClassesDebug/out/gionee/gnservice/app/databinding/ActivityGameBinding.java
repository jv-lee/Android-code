package gionee.gnservice.app.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.lee.library.widget.WebViewEx;

public abstract class ActivityGameBinding extends ViewDataBinding {
  @NonNull
  public final ProgressBar progress;

  @NonNull
  public final WebViewEx web;

  protected ActivityGameBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, ProgressBar progress, WebViewEx web) {
    super(_bindingComponent, _root, _localFieldCount);
    this.progress = progress;
    this.web = web;
  }

  @NonNull
  public static ActivityGameBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityGameBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityGameBinding>inflate(inflater, gionee.gnservice.app.R.layout.activity_game, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityGameBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityGameBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityGameBinding>inflate(inflater, gionee.gnservice.app.R.layout.activity_game, null, false, component);
  }

  public static ActivityGameBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityGameBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityGameBinding)bind(component, view, gionee.gnservice.app.R.layout.activity_game);
  }
}
