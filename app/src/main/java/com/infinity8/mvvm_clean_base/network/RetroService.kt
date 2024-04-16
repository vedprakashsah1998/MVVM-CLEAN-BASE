/*
 *
 *   Created by Ved Prakash on 3/29/24, 3:08 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 3/29/24, 12:35 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.network

import com.infinity8.mvvm_clean_base.model.CuratedImageModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroService {

    @GET("/v1/curated")
    suspend fun getCuratedImage(
        @Query("per_page") per_page: String,
        @Query("page") page: String
    ): Response<CuratedImageModel>

    @GET("/v1/popular")
    suspend fun getPopularImage(
        @Query("per_page") per_page: String,
        @Query("page") page: String
    ): Response<CuratedImageModel>
}