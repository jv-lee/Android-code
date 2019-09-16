package gionee.gnservice.app.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public abstract class FragmentDialogBackBinding extends ViewDataBinding {
  @NonNull
  public final Button btnCancel;

  @NonNull
  public final Button btnDefine;

  @NonNull
  public final FrameLayout frameAdContainer;

  @NonNull
  public final TextView tvHeading;

  @NonNull
  public final TextView tvTodayCount;

  protected FragmentDialogBackBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, Button btnCancel, Button btnDefine, FrameLayout frameAdContainer,
      TextView tvHeading, TextView tvTodayCount) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnCancel = btnCancel;
    this.btnDefine = btnDefine;
    this.frameAdContainer = frameAdContainer;
    this.tvHeading = tvHeading;
    this.tvTodayCount = tvTodayCount;
  }

  @NonNull
  public static FragmentDialogBackBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentDialogBackBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentDialogBackBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_dialog_back, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentDialogBackBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentDialogBackBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentDialogBackBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_dialog_back, null, false, component);
  }

  public static FragmentDialogBackBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static FragmentDialogBackBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (FragmentDialogBackBinding)bind(component, view, gionee.gnservice.app.R.layout.fragment_dialog_back);
  }
}
