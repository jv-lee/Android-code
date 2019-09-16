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

public abstract class FragmentNovelBinding extends ViewDataBinding {
  @NonNull
  public final FrameLayout frameContainer;

  protected FragmentNovelBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, FrameLayout frameContainer) {
    super(_bindingComponent, _root, _localFieldCount);
    this.frameContainer = frameContainer;
  }

  @NonNull
  public static FragmentNovelBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentNovelBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentNovelBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_novel, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentNovelBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentNovelBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentNovelBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_novel, null, false, component);
  }

  public static FragmentNovelBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static FragmentNovelBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (FragmentNovelBinding)bind(component, view, gionee.gnservice.app.R.layout.fragment_novel);
  }
}
