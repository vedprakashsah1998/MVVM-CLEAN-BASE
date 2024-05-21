/*
 *
 *   Created by Ved Prakash on 3/29/24, 3:08 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 3/29/24, 2:38 PM
 *   Organization: NeoSoft
 *
 */

import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hiltApp)
    alias(libs.plugins.navigationSafeArgs)
    id("kotlin-parcelize")
//    kotlin("kapt")
    alias(libs.plugins.devToolKsp)
}

android {
    namespace = "com.infinity8.mvvm_clean_base"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.infinity8.mvvm_clean_base"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val properties = Properties().apply {
                load(rootProject.file("local.properties").inputStream())
            }
            buildConfigField("String", "BASE_URL", properties.getProperty("baseUrl"))
            buildConfigField("String", "Authorization", properties.getProperty("authorization"))
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val properties = Properties().apply {
                load(rootProject.file("local.properties").inputStream())
            }
            buildConfigField("String", "BASE_URL", properties.getProperty("baseUrl"))
            buildConfigField("String", "Authorization", properties.getProperty("authorization"))

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures.buildConfig = true
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Coroutine
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.common)
    implementation(libs.androidx.viewmodel)
    implementation(libs.kotlin.coroutines)

    //hilt
    implementation(libs.hilt.dagger)
    ksp(libs.hilt.compiler)
    ksp(libs.lifecycle.compiler)

    //Retrofit
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.glide.android)
    ksp(libs.glide.compiler)

    implementation(libs.androidx.paging.runtime.ktx)

}