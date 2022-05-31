package com.simple.compose.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simple.compose.source.Message
import com.simple.compose.source.SampleData

/**
 *
 * @author jv.lee
 * @date 2022/1/6
 */
    class ListDataViewModel : ViewModel() {

        private val _messagesLive = MutableLiveData<List<Message>>()
        val messagesLive: LiveData<List<Message>> = _messagesLive

        init {
            _messagesLive.postValue(SampleData.conversationSample)
        }
    }