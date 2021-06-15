package com.lee.library.extensions

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding

/**
 * @author jv.lee
 * @date 2021/6/15
 * @description
 */
fun <VB : ViewBinding> Activity.binding(inflate: (LayoutInflater) -> VB) = lazy {
    inflate(layoutInflater).apply { setContentView(root) }
}

fun <VB : ViewBinding> Fragment.binding(bind: (View) -> VB) = lazy {
    var binding: VB? = bind(requireView())
    viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
        private val mainHandler = Handler(Looper.getMainLooper())

        @MainThread
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy(owner: LifecycleOwner) {
            owner.lifecycle.removeObserver(this)
            // Fragment.viewLifecycleOwner call LifecycleObserver.onDestroy() before Fragment.onDestroyView().
            // That's why we need to postpone reset of the viewBinding
            mainHandler.post {
                binding = null
            }
        }
    })
    binding!!
}

///**
// * Wrapper of ViewBinding.bind(View)
// */
//class BindViewBinding<out VB : ViewBinding>(viewBindingClass: Class<VB>) {
//
//    private val bindViewBinding = viewBindingClass.getMethod("bind", View::class.java)
//
//    @Suppress("UNCHECKED_CAST")
//    fun bind(view: View): VB {
//        return bindViewBinding(null, view) as VB
//    }
//}
//
//class FragmentViewBindingProperty<T : ViewBinding>(private val viewBinder: BindViewBinding<T>) :
//    ReadOnlyProperty<Fragment, T> {
//
//    private var viewBinding: T? = null
//    private val lifecycleObserver = BindingLifecycleObserver()
//
//    @MainThread
//    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
//        this.viewBinding?.let { return it }
//
//        val view = thisRef.requireView()
//        thisRef.viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
//        return viewBinder.bind(view).also { vb -> this.viewBinding = vb }
//    }
//
//    private inner class BindingLifecycleObserver : LifecycleObserver {
//
//        private val mainHandler = Handler(Looper.getMainLooper())
//
//        @MainThread
//        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//        fun onDestroy(owner: LifecycleOwner) {
//            owner.lifecycle.removeObserver(this)
//            // Fragment.viewLifecycleOwner call LifecycleObserver.onDestroy() before Fragment.onDestroyView().
//            // That's why we need to postpone reset of the viewBinding
//            mainHandler.post {
//                viewBinding = null
//            }
//        }
//    }
//}