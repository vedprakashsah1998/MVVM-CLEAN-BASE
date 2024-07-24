/*
 *
 *   Created by Ved Prakash on 7/24/24, 5:47 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 7/24/24, 5:47 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.network

import com.infinity8.mvvm_clean_base.datasource.AnalyticsService
import javax.inject.Inject


class FirebaseAnalyticsService @Inject constructor() : AnalyticsService {
    override fun logEvent(eventName: String) {
        println("Logging event: $eventName")
    }
}
