/*
 *
 *   Created by Ved Prakash on 7/24/24, 5:47 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 7/24/24, 5:47 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.datasource

interface AnalyticsService {
    fun logEvent(eventName: String)
}
