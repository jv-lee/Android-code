package com.lee.library.extensions

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author jv.lee
 * @date 2021/6/15
 * @description ViewBinding 扩展函数（属性委托解绑模式）
 */
@Suppress("UNCHECKED_CAST")
@JvmName("viewBindingActivity")
inline fun <A : ComponentActivity, V : ViewBinding> ComponentActivity.binding(
    crossinline inflate: (LayoutInflater) -> V
): ViewBindingProperty<A, V> {
    var binding: V? = null

    val createBinding = { activity: ComponentActivity ->
        binding ?: inflate(activity.layoutInflater).also { binding = it }
            .apply { setContentView(root) }
    }

    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun initLazyProperty() {
            lifecycle.removeObserver(this)
            createBinding(this@binding)
        }
    })

    return ActivityViewBindingProperty { activity: A ->
        createBinding(activity)
    }
}

@Suppress("UNCHECKED_CAST")
@JvmName("viewInflateActivity")
inline fun <A : ComponentActivity, V : ViewBinding> ComponentActivity.inflate(
    crossinline inflate: (LayoutInflater) -> V
): ViewBindingProperty<A, V> = ActivityViewBindingProperty { activity: A ->
    inflate(activity.layoutInflater)
}

@Suppress("UNCHECKED_CAST")
@JvmName("viewBindingFragment")
inline fun <F : Fragment, V : ViewBinding> Fragment.binding(
    crossinline viewBinder: (View) -> V,
    crossinline viewProvider: (F) -> View = Fragment::requireView
): ViewBindingProperty<F, V> = FragmentViewBindingProperty { fragment: F ->
    viewBinder(viewProvider(fragment))
}

@Suppress("UNCHECKED_CAST")
@JvmName("viewInflateFragment")
inline fun <F : Fragment, V : ViewBinding> Fragment.inflate(
    crossinline viewInflate: (LayoutInflater) -> V
): ViewBindingProperty<F, V> = FragmentViewBindingProperty { fragment: F ->
    viewInflate(fragment.layoutInflater)
}

@Suppress("UNCHECKED_CAST")
@JvmName("viewInflateViewGroup")
inline fun <G : ViewGroup, V : ViewBinding> ViewGroup.inflate(
    crossinline viewInflate: (LayoutInflater) -> V
): ViewGroupViewBindingProperty<G, V> = ViewGroupViewBindingProperty { view: G ->
    viewInflate(LayoutInflater.from(view.context))
}

interface ViewBindingProperty<in R : Any, out V : ViewBinding> : ReadOnlyProperty<R, V> {
    @MainThread
    fun clear()
}

abstract class LifecycleViewBindingProperty<in R : Any, out V : ViewBinding>(
    private val viewBinder: (R) -> V
) : ViewBindingProperty<R, V> {

    private var viewBinding: V? = null
    private var thisRef: R? = null

    protected abstract fun getLifecycleOwner(thisRef: R): LifecycleOwner

    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): V {
        this.thisRef = thisRef
        // Already bound
        viewBinding?.let { return it }

        val lifecycle = getLifecycleOwner(thisRef).lifecycle
        val viewBinding = viewBinder(thisRef)
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            Log.w(
                "viewBinding",
                "Access to viewBinding after Lifecycle is destroyed or hasn'V created yet. " +
                        "The instance of viewBinding will be not cached."
            )
            // We can access to ViewBinding after Fragment.onDestroyView(), but don'V save it to prevent memory leak
        } else {
            lifecycle.addObserver(ClearOnDestroyLifecycleObserver(this))
            this.viewBinding = viewBinding
        }
        return viewBinding
    }

    @MainThread
    override fun clear() {
        viewBinding = null
    }

    private class ClearOnDestroyLifecycleObserver(
        private val property: LifecycleViewBindingProperty<*, *>
    ) : LifecycleObserver {

        private companion object {
            private val mainHandler = Handler(Looper.getMainLooper())
        }

        @MainThread
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy(owner: LifecycleOwner) {
            mainHandler.post {
                property.clear()
            }
        }
    }
}

class ActivityViewBindingProperty<in A : ComponentActivity, out V : ViewBinding>(
    viewBinder: (A) -> V
) : LifecycleViewBindingProperty<A, V>(viewBinder) {

    override fun getLifecycleOwner(thisRef: A): LifecycleOwner {
        return thisRef
    }
}


class FragmentViewBindingProperty<in F : Fragment, out V : ViewBinding>(
    viewBinder: (F) -> V
) : LifecycleViewBindingProperty<F, V>(viewBinder) {

    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        try {
            return thisRef.viewLifecycleOwner
        } catch (ignored: IllegalStateException) {
            error("Fragment doesn't have view associated with it or the view has been destroyed")
        }
    }
}

class ViewGroupViewBindingProperty<in G : ViewGroup, out V : ViewBinding>(
    viewBinder: (G) -> V
) : LifecycleViewBindingProperty<G, V>(viewBinder) {

    override fun getLifecycleOwner(thisRef: G): LifecycleOwner {
        return checkNotNull(ViewTreeLifecycleOwner.get(thisRef)) {
            "Fragment doesn't have view associated with it or the view has been destroyed"
        }
    }
}

//该方式无法控制binding解除绑定
//fun <VB : ViewBinding> Activity.binding(inflate: (LayoutInflater) -> VB) = lazy {
//    inflate(layoutInflater).apply { setContentView(root) }
//}