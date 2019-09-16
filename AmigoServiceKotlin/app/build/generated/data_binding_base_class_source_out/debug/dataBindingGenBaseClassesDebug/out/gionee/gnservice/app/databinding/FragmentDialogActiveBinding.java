package gionee.gnservice.app.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.lee.library.widget.WebViewEx;

public abstract class FragmentDialogActiveBinding extends ViewDataBinding {
  @NonNull
  public final ImageView ivClose;

  @NonNull
  public final ProgressBar progress;

  @NonNull
  public final WebViewEx web;

  protected FragmentDialogActiveBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, ImageView ivClose, ProgressBar progress, WebViewEx web) {
    super(_bindingComponent, _root, _localFieldCount);
    this.ivClose = ivClose;
    this.progress = progress;
    this.web = web;
  }

  @NonNull
  public static FragmentDialogActiveBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentDialogActiveBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentDialogActiveBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_dialog_active, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentDialogActiveBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentDialogActiveBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentDialogActiveBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_dialog_active, null, false, component);
  }

  public static FragmentDialogActiveBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static FragmentDialogActiveBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (FragmentDialogActiveBinding)bind(component, view, gionee.gnservice.app.R.layout.fragment_dialog_active);
  }
}
