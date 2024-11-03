package com.simple.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.compose.source.Message
import com.simple.compose.source.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 *
 * @author jv.lee
 * @date 2022/1/6
 */
class ListDataViewModel : ViewModel() {

    private val _messagesFlow = MutableStateFlow<List<Message>>(emptyList())
    val messagesFlow: StateFlow<List<Message>> = _messagesFlow

    init {
        viewModelScope.launch {
            _messagesFlow.emit(SampleData.conversationSample)
        }
    }
}