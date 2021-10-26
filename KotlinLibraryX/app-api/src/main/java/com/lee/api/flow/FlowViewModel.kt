package com.lee.api.flow

import androidx.lifecycle.viewModelScope
import com.lee.library.mvvm.ui.*
import com.lee.library.mvvm.viewmodel.CoroutineViewModel
import com.lee.library.utils.LogUtil
import kotlinx.coroutines.flow.*

/**
 * @author jv.lee
 * @data 2021/10/25
 * @description
 */
class FlowViewModel : CoroutineViewModel() {

    private val _typeFlow = MutableStateActionFlow("views")
    private val typeFlow: StateActionFlow<String> = _typeFlow

    val contentFlow: StateFlow<UiState> = typeFlow.flatMapLatest { action ->
        LogUtil.i("type -> $action")
        stateFlow { "this is callback data -> $action" }
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Default)

    fun updateType(type: String) {
        _typeFlow.emitAction(type)
    }

}