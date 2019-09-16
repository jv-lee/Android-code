package gionee.gnservice.app.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public abstract class FragmentDialogNoviceBinding extends ViewDataBinding {
  @NonNull
  public final Button btnNewsStart;

  @NonNull
  public final Button btnVideoStart;

  @NonNull
  public final ConstraintLayout constContainer;

  @NonNull
  public final ImageView ivClose;

  protected FragmentDialogNoviceBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, Button btnNewsStart, Button btnVideoStart,
      ConstraintLayout constContainer, ImageView ivClose) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnNewsStart = btnNewsStart;
    this.btnVideoStart = btnVideoStart;
    this.constContainer = constContainer;
    this.ivClose = ivClose;
  }

  @NonNull
  public static FragmentDialogNoviceBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentDialogNoviceBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentDialogNoviceBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_dialog_novice, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentDialogNoviceBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentDialogNoviceBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentDialogNoviceBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_dialog_novice, null, false, component);
  }

  public static FragmentDialogNoviceBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static FragmentDialogNoviceBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (FragmentDialogNoviceBinding)bind(component, view, gionee.gnservice.app.R.layout.fragment_dialog_novice);
  }
}
