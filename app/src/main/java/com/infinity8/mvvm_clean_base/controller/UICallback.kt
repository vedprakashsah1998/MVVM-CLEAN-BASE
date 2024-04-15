/*
 *
 *   Created by Ved Prakash on 4/15/24, 12:56 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 4/15/24, 12:56 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.controller

import com.infinity8.mvvm_clean_base.model.Photo

interface UICallback {
     fun hideBottomNavigation(){}
     fun showBottomNavigation(){}
     fun recyclerviewItemClick(photo: Photo?) {}
}