/*
 *
 *   Created by Ved Prakash on 3/29/24, 5:40 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 3/29/24, 5:40 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.infinity8.mvvm_clean_base.model.Photo
import com.infinity8.mvvm_clean_base.network.RetroService
import com.infinity8.mvvm_clean_base.utils.BAD_GATEWAY
import com.infinity8.mvvm_clean_base.utils.ERROR_MESSAGE
import com.infinity8.mvvm_clean_base.utils.FAILURE_MESSAGE
import com.infinity8.mvvm_clean_base.utils.TOO_MANY_REQUEST
import javax.inject.Inject

class CuratedImageListSource @Inject constructor(private val retroService: RetroService) :
    PagingSource<Int, Photo>() {
    override val jumpingSupported: Boolean = true

    override fun getRefreshKey(state: PagingState<Int, Photo>) =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: 1
        return try {

            val pageSize = params.loadSize
            val response = retroService.getCuratedImage(pageSize.toString(), page.toString())
            if (response.isSuccessful) {
                if (response.code() == 429) {
                    LoadResult.Error(Exception(TOO_MANY_REQUEST))
                } else {
                    val photoList = response.body()?.photos ?: emptyList()
                    val prevKey = if (page > 1) page - 1 else null
                    val nextKey = if (photoList.isNotEmpty()) page + 1 else null
                    LoadResult.Page(photoList, prevKey, nextKey)
                }
            } else {
                LoadResult.Error(
                    Exception(
                        when (response.code()) {
                            503 -> FAILURE_MESSAGE
                            502 -> BAD_GATEWAY
                            429 -> TOO_MANY_REQUEST
                            else -> ERROR_MESSAGE
                        }
                    )
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}