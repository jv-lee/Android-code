package gionee.gnservice.app.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.lee.library.widget.refresh.RefreshLayout;

public abstract class FragmentVideoChildBinding extends ViewDataBinding {
  @NonNull
  public final FrameLayout frameVideoChildContainer;

  @NonNull
  public final RefreshLayout refresh;

  @NonNull
  public final RecyclerView rvContainer;

  protected FragmentVideoChildBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, FrameLayout frameVideoChildContainer, RefreshLayout refresh,
      RecyclerView rvContainer) {
    super(_bindingComponent, _root, _localFieldCount);
    this.frameVideoChildContainer = frameVideoChildContainer;
    this.refresh = refresh;
    this.rvContainer = rvContainer;
  }

  @NonNull
  public static FragmentVideoChildBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentVideoChildBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentVideoChildBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_video_child, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentVideoChildBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentVideoChildBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentVideoChildBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_video_child, null, false, component);
  }

  public static FragmentVideoChildBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static FragmentVideoChildBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (FragmentVideoChildBinding)bind(component, view, gionee.gnservice.app.R.layout.fragment_video_child);
  }
}
