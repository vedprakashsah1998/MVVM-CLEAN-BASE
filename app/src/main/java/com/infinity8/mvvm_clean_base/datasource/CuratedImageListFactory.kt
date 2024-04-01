/*
 *
 *   Created by Ved Prakash on 3/31/24, 12:42 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 3/31/24, 12:42 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.datasource

import com.infinity8.mvvm_clean_base.network.RetroService
import javax.inject.Inject

class CuratedImageListFactory @Inject constructor(private val retroService: RetroService) {

    fun create() = CuratedImageListSource(retroService)
}