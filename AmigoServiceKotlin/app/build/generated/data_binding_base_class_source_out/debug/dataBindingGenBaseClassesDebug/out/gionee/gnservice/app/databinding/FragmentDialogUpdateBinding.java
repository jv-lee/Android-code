package gionee.gnservice.app.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class FragmentDialogUpdateBinding extends ViewDataBinding {
  @NonNull
  public final TextView btnBack;

  @NonNull
  public final TextView tvContent;

  @NonNull
  public final TextView tvTitle;

  protected FragmentDialogUpdateBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, TextView btnBack, TextView tvContent, TextView tvTitle) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnBack = btnBack;
    this.tvContent = tvContent;
    this.tvTitle = tvTitle;
  }

  @NonNull
  public static FragmentDialogUpdateBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentDialogUpdateBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentDialogUpdateBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_dialog_update, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentDialogUpdateBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentDialogUpdateBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentDialogUpdateBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_dialog_update, null, false, component);
  }

  public static FragmentDialogUpdateBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static FragmentDialogUpdateBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (FragmentDialogUpdateBinding)bind(component, view, gionee.gnservice.app.R.layout.fragment_dialog_update);
  }
}
