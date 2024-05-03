/*
 *
 *   Created by Ved Prakash on 3/29/24, 3:08 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 3/29/24, 12:18 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.di

import android.content.Context
import com.infinity8.mvvm_clean_base.BuildConfig
import com.infinity8.mvvm_clean_base.network.AuthInterceptor
import com.infinity8.mvvm_clean_base.network.OfflineInterceptor
import com.infinity8.mvvm_clean_base.network.RetroService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun providesCache(@ApplicationContext appContext: Context): Cache {
        val cacheSize = 10 * 1024 * 1024
        val cacheDir = File(appContext.cacheDir, "http-cache")
        return Cache(cacheDir, cacheSize.toLong())
    }

    @Singleton
    @Provides
    fun providesAuthInterceptor() = AuthInterceptor()

    @Singleton
    @Provides
    fun providesOfflineInterceptor(@ApplicationContext appContext: Context) =
        OfflineInterceptor(appContext)

    @Singleton
    @Provides
    fun providesOkHttpClient(
        interceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        offlineInterceptor: OfflineInterceptor,
        cache: Cache
    ) =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(offlineInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .cache(cache)
            .build()


    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

    @Singleton
    @Provides
    fun providesRetroApi(retrofit: Retrofit): RetroService =
        retrofit.create(RetroService::class.java)

}