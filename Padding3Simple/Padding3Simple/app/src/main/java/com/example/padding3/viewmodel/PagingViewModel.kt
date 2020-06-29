package com.example.padding3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.padding3.model.repository.ContentDataSource

class PagingViewModel : ViewModel() {
    fun getContent() = ContentDataSource.load().asLiveData()
}