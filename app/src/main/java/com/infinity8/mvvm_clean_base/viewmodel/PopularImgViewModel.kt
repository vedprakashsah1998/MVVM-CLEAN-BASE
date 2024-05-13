/*
 *
 *   Created by Ved Prakash on 4/1/24, 4:48 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 4/1/24, 4:48 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity8.mvvm_clean_base.model.CuratedImageModel
import com.infinity8.mvvm_clean_base.use_case.PopularUseCase
import com.infinity8.mvvm_clean_base.utils.Outcome
import com.infinity8.mvvm_clean_base.utils.mapOutcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularImgViewModel @Inject constructor(
    private val popularUseCase: PopularUseCase
) :
    ViewModel() {

    private val _postFlowSearchPaging: MutableStateFlow<Outcome<CuratedImageModel?>> =
        MutableStateFlow(Outcome.Progress(true))
    val postFlowSearchPaging: StateFlow<Outcome<CuratedImageModel?>> =
        _postFlowSearchPaging

    init {
        getPopularImg()
    }

    fun getPopularImg() {
        viewModelScope.launch {

            popularUseCase.invoke().onStart {
                _postFlowSearchPaging.value = Outcome.Progress(true)
            }.onCompletion {
                _postFlowSearchPaging.value = Outcome.Progress(false)

            }.catch { e ->
                _postFlowSearchPaging.value = Outcome.Failure(e)
            }.collectLatest {
                _postFlowSearchPaging.value = it.mapOutcome { data -> data }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}