/*
 *
 *   Created by Ved Prakash on 3/31/24, 12:44 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 3/31/24, 12:44 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.infinity8.mvvm_clean_base.datasource.CuratedImageListFactory
import com.infinity8.mvvm_clean_base.model.Photo
import com.infinity8.mvvm_clean_base.network.RetroService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CuratedImageRepo @Inject constructor(private val retroService: RetroService,    private val curatedImageListFactory: CuratedImageListFactory,
) {
    suspend fun getPhotoList(per_page:String, page:String) =
        flowOf(  retroService.getCuratedImage(per_page,page))

    fun getCuratedImage(): Flow<PagingData<Photo>> = Pager(
        config = PagingConfig(
            jumpThreshold = 20,
            prefetchDistance = 20,
            pageSize = 20,
            initialLoadSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            curatedImageListFactory.create()
        }
    ).flow
}