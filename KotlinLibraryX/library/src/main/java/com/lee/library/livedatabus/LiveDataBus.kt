package com.lee.library.livedatabus

import android.app.Activity
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.lee.library.livedatabus.LiveDataBus.ObserverWrapper

/**
 * @author jv.lee
 * @date 2019/3/30
 * @description 事件总线
 */
class LiveDataBus private constructor() {

    companion object {
        val instance by lazy { LiveDataBus() }
    }

    /**
     * 消息通道
     */
    private val bus: HashMap<String, BusMutableLiveData<Any>> = HashMap()

    /**
     * 存储非激活事件的临时容器
     */
    private val tempMap: HashMap<String, Observer<*>> = HashMap()

    fun <T> getChannel(target: String, type: Class<T>): MutableLiveData<T> {
        if (!bus.containsKey(target)) {
            bus[target] = BusMutableLiveData()
        }
        return bus[target] as MutableLiveData<T>
    }

    fun getChannel(target: String): MutableLiveData<Any> = getChannel(target, Any::class.java)

    private class ObserverWrapper<T>(private val observer: Observer<T>) : Observer<T> {

        override fun onChanged(t: T) {
            if (isCallOnObserve()) {
                return
            }
            observer.onChanged(t)
        }

        private fun isCallOnObserve(): Boolean {
            val stackTrace = Thread.currentThread().stackTrace
            stackTrace.takeIf { it.isNotEmpty() }?.forEach { element ->
                if ("android.arch.lifecycle.LiveData" == element.className && "observeForever" == element.methodName) {
                    return true
                }
            }
            return false
        }
    }

    private class BusMutableLiveData<T> : MutableLiveData<T>() {
        private val observerMap = hashMapOf<Observer<*>, Observer<*>>()

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            try {
                hook(observer)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun observeForever(observer: Observer<in T>) {
            if (!observerMap.containsKey(observer)) {
                observerMap[observer] = ObserverWrapper(observer)
            }
            super.observeForever(observerMap[observer] as Observer<in T>)
        }

        override fun removeObserver(observer: Observer<in T>) {
            val realObserver: Observer<T> = if (observerMap.containsKey(observer)) {
                (observerMap.remove(observer) as Observer<T>)
            } else {
                observer as Observer<T>
            }
            super.removeObserver(realObserver)
        }

        private fun hook(@NonNull observer: Observer<in T>) {
            // get wrapper`s version
            val classLiveData = LiveData::class.java
            val fieldObservers = classLiveData.getDeclaredField("mObservers")
            fieldObservers.isAccessible = true
            val objectObservers = fieldObservers.get(this)
            val classObservers = objectObservers.javaClass
            val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
            methodGet.isAccessible = true
            val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
            var objectWrapper: Any? = null
            if (objectWrapperEntry is Map.Entry<*, *>) {
                objectWrapper = objectWrapperEntry.value
            }
            checkNotNull(objectWrapper) { "Wrapper can not be bull!" }
            val classObserverWrapper = objectWrapper.javaClass.superclass
            val filedLastVersion = classObserverWrapper.getDeclaredField("mLastVersion")
            filedLastVersion.isAccessible = true

            // get livedata`s version
            val fieldVersion = classLiveData.getDeclaredField("mVersion")
            fieldVersion.isAccessible = true
            val objectVersion = fieldVersion.get(this)

            // set wrapper`s version
            filedLastVersion.set(objectWrapper, objectVersion)
        }
    }

    /**
     * 订阅通知
     * @param lifecycleOwner
     */
    fun injectBus(lifecycleOwner: LifecycleOwner) {
        val aClass = lifecycleOwner.javaClass
        var lifecycle: LifecycleOwner? = null

        if (lifecycleOwner is Activity) lifecycle = lifecycleOwner
        if (lifecycleOwner is Fragment) lifecycle = lifecycleOwner.viewLifecycleOwner

        // 获取当前类所有方法
        val declaredMethods = aClass.declaredMethods
        declaredMethods.forEach { method ->
            val injectBus = method.getAnnotation(InjectBus::class.java)
            injectBus?.run {
                val observer = Observer<Any> {
                    try {
                        method.invoke(lifecycleOwner, it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                // 是否是激活状态
                if (isActive) {
                    lifecycle?.run { instance.getChannel(value).observe(this, observer) }
                } else {
                    tempMap[value] = observer
                    instance.getChannel(value).observeForever(observer)
                }

                // 处理粘性事件
                if (isViscosity) {
                    lifecycle?.lifecycle?.addObserver(object : LifecycleEventObserver {
                        override fun onStateChanged(
                            source: LifecycleOwner,
                            event: Lifecycle.Event
                        ) {
                            if (event == Lifecycle.Event.ON_RESUME) {
                                val channel = instance.getChannel(value)
                                // 获取最新的消息补发
                                channel.value?.let { channel.postValue(it) }
                            }

                            if (event == Lifecycle.Event.ON_DESTROY) {
                                lifecycle.lifecycle.removeObserver(this)
                            }
                        }
                    })
                }

            }
        }
    }

    /**
     * 取消订阅通知 (仅在使用非激活状态可通知模式 需要取消订阅)
     * @param lifecycleOwner
     */
    fun unInjectBus(lifecycleOwner: LifecycleOwner) {
        val aClass = lifecycleOwner.javaClass
        val declaredMethods = aClass.declaredMethods
        declaredMethods.forEach { method ->
            val injectBus = method.getAnnotation(InjectBus::class.java)
            injectBus?.takeIf { !it.isActive }?.run {
                val remove = tempMap.remove(value)
                remove?.let {
                    instance.getChannel(value).removeObserver(it as Observer<in Any>)
                }
            }
        }
    }

}