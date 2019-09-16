package gionee.gnservice.app.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lee.library.widget.WebViewEx;

public abstract class FragmentWalletBinding extends ViewDataBinding {
  @NonNull
  public final ContentLoadingProgressBar progress;

  @NonNull
  public final WebViewEx web;

  protected FragmentWalletBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, ContentLoadingProgressBar progress, WebViewEx web) {
    super(_bindingComponent, _root, _localFieldCount);
    this.progress = progress;
    this.web = web;
  }

  @NonNull
  public static FragmentWalletBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentWalletBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentWalletBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_wallet, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentWalletBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static FragmentWalletBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<FragmentWalletBinding>inflate(inflater, gionee.gnservice.app.R.layout.fragment_wallet, null, false, component);
  }

  public static FragmentWalletBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static FragmentWalletBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (FragmentWalletBinding)bind(component, view, gionee.gnservice.app.R.layout.fragment_wallet);
  }
}
