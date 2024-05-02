/*
 *
 *   Created by Ved Prakash on 4/1/24, 4:46 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 4/1/24, 4:46 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.repository

import com.infinity8.mvvm_clean_base.network.RetroService
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class PopularImgRepo @Inject constructor(private val retroService: RetroService) {
    suspend fun getPopularImage() =
        flowOf(retroService.getPopularImage("80", "1"))
}