/*
 *
 *   Created by Ved Prakash on 3/29/24, 5:11 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 3/29/24, 5:11 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.network

import com.infinity8.mvvm_clean_base.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = BuildConfig.Authorization
        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization",  token)
            .build()
        return chain.proceed(modifiedRequest)
    }

}