package com.lee.api.flow

import androidx.lifecycle.viewModelScope
import com.lee.library.mvvm.ui.UiState
import com.lee.library.mvvm.ui.stateFlow
import com.lee.library.mvvm.viewmodel.CoroutineViewModel
import com.lee.library.utils.LogUtil
import kotlinx.coroutines.flow.*

/**
 * @author jv.lee
 * @data 2021/10/25
 * @description
 */
class FlowViewModel : CoroutineViewModel() {

    data class Action(val value: String, val version: Double = Math.random())

    private val _typeFlow = MutableStateFlow(Action("views"))
    private val typeFlow: StateFlow<Action> = _typeFlow

    val contentFlow: StateFlow<UiState> = typeFlow.flatMapLatest { type ->
        LogUtil.i("type -> $type")
        stateFlow { "this is callback data -> $type" }
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Default)

    fun updateType(type: String) {
        _typeFlow.value = Action("$type}")
    }

}