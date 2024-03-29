/*
 *
 *   Created by Ved Prakash on 3/29/24, 5:22 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 3/29/24, 5:22 PM
 *   Organization: NeoSoft
 *  
 */

package com.infinity8.mvvm_clean_base.model

data class CuratedImageModel(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val prev_page: String,
    val total_results: Int
)

data class Photo(
    val alt: String,
    val avg_color: String,
    val height: Int,
    val id: Int,
    val liked: Boolean,
    val photographer: String,
    val photographer_id: Int,
    val photographer_url: String,
    val src: Src,
    val url: String,
    val width: Int
)

data class Src(
    val landscape: String,
    val large: String,
    val large2x: String,
    val medium: String,
    val original: String,
    val portrait: String,
    val small: String,
    val tiny: String
)