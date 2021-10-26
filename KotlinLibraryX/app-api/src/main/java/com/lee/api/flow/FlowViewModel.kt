package com.lee.api.flow

import androidx.lifecycle.viewModelScope
import com.lee.library.extensions.getCache
import com.lee.library.extensions.putCache
import com.lee.library.mvvm.ui.*
import com.lee.library.mvvm.viewmodel.CoroutineViewModel
import com.lee.library.utils.LogUtil
import kotlinx.coroutines.Dispatchers
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

//    //flow数据执行顺序是从下往上执行
//    val bannerFlow: StateFlow<UiState> = repository.api.getBannerFlow()
//        .map {
//            //转换数据并存储网络数据
//            it.data.also { banner ->
//                cacheManager.putCache(RECOMMEND_BANNER_KEY, banner)
//            }
//        }
//        .onStart {
//            //查询缓存
//            cacheManager.getCache<MutableList<Banner>>(RECOMMEND_BANNER_KEY)?.let {
//                emit(it)
//            }
//        }
//        .uiState()
//        .flowOn(Dispatchers.IO)
//        .stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Default)

}