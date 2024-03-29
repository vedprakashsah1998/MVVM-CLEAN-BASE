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
import javax.inject.Inject

class CuratedImageListSource @Inject constructor() : PagingSource<Int, Photo>() {
    override val jumpingSupported: Boolean = true

    override fun getRefreshKey(state: PagingState<Int, Photo>) =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        TODO("Not yet implemented")
    }
}