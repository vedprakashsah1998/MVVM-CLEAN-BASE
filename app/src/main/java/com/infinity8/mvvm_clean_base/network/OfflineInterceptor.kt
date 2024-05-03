/*
 *
 *   Created by Ved Prakash on 5/3/24, 10:57 AM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 5/3/24, 10:57 AM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.network

import android.content.Context
import com.infinity8.mvvm_clean_base.utils.isNetworkAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class OfflineInterceptor @Inject constructor(@ApplicationContext val context: Context) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()
        if (!context.isNetworkAvailable()) {
            originalRequest = originalRequest.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }
        return chain.proceed(originalRequest)
    }

}