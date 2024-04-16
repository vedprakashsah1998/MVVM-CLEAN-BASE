/*
 *
 *   Created by Ved Prakash on 4/16/24, 1:02 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 4/16/24, 1:02 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.controller

import com.infinity8.mvvm_clean_base.model.Photo

interface UICallback {
    fun recyclerviewItemClick(photo: Photo?) {}
}