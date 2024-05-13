/*
 *
 *   Created by Ved Prakash on 3/31/24, 12:48 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 3/31/24, 12:48 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.infinity8.mvvm_clean_base.model.Photo
import com.infinity8.mvvm_clean_base.repository.CuratedImageRepo
import com.infinity8.mvvm_clean_base.utils.Outcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CuratedImageViewModel @Inject constructor(
    private val curatedImageRepo: CuratedImageRepo,
) :
    ViewModel() {
    private val _postFlowSearchPaging: MutableStateFlow<Outcome<PagingData<Photo>>> =
        MutableStateFlow(Outcome.Progress(true))
    val postFlowSearchPaging: StateFlow<Outcome<PagingData<Photo>>> =
        _postFlowSearchPaging

    init {
        getCuratedImage()
    }

    private fun getCuratedImage() {
        viewModelScope.launch {
            try {
                curatedImageRepo.getCuratedImage()
                    .cachedIn(viewModelScope)
                    .distinctUntilChanged()
                    .onStart { _postFlowSearchPaging.value = Outcome.Progress(true) }
                    .onCompletion { _postFlowSearchPaging.value = Outcome.Progress(false) }
                    .catch { e ->
                        _postFlowSearchPaging.value = Outcome.Failure(e)
                    }
                    .collectLatest { pagingData ->
                        _postFlowSearchPaging.value = Outcome.Success(pagingData)
                    }
            } catch (e: Exception) {
                _postFlowSearchPaging.value = Outcome.Failure(e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}