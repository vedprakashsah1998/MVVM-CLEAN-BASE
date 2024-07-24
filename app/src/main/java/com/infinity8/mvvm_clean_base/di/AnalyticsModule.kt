/*
 *
 *   Created by Ved Prakash on 7/24/24, 5:48 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 7/24/24, 5:48 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.di

import com.infinity8.mvvm_clean_base.datasource.AnalyticsService
import com.infinity8.mvvm_clean_base.network.FirebaseAnalyticsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {
    @Provides
    @Singleton
    fun provideFirebaseAnalyticsService(): AnalyticsService {
        return FirebaseAnalyticsService()
    }
}