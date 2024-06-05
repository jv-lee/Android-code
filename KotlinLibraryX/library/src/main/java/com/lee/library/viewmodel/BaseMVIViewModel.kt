@file:Suppress("LeakingThis", "PropertyName")

package com.lee.library.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

interface IViewState // Ui状态数据 订阅后可多次消费
interface IViewEvent // Ui执行事件 订阅后只在激活状态下消费一次，非激活状态发送不消费
interface IViewIntent // 页面主动执行意图

/**
 * ViewModel基类 - MVI模式
 *
 * [ViewState] 页面UI数据状态，订阅后在生命周期激活下可多次接收更新UI状态
 * [ViewEvent] 页面单次事件消费，订阅后处理如Toast、Dialog、导航等单次执行事件
 * [ViewIntent] 页面主动调用意图，意图驱动Ui更新逻辑
 *
 * @author jv.lee
 * @date 2024/5/28
 */
abstract class BaseMVIViewModel<ViewState : IViewState,
        ViewEvent : IViewEvent, ViewIntent : IViewIntent> : ViewModel() {

    /**
     * 页面UI 数据状态 - 动态更新根据生命周期触发collect 多次消费
     */
    protected val _viewStates = MutableStateFlow(initViewState())
    val viewStates: StateFlow<ViewState> = _viewStates

    /**
     * 页面动作 单次事件 - 页面事件单次消费
     */
    protected  val _viewEvents: Channel<ViewEvent> = Channel(Channel.BUFFERED)
    val viewEvents: Flow<ViewEvent> = _viewEvents.receiveAsFlow()

    /**
     * 初始化构建View状态数据默认值
     */
    protected abstract fun initViewState(): ViewState

    /**
     * 主动触发事件，ui界面通过事件流触发请求更新逻辑
     * 该方法提供给view层调用
     * @param intent 具体执行意图
     */
    abstract fun dispatch(intent: ViewIntent)
}