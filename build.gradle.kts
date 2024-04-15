/*
 *
 *   Created by Ved Prakash on 3/29/24, 3:08 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 3/29/24, 10:44 AM
 *   Organization: NeoSoft
 *
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.hiltApp) apply false
    alias(libs.plugins.navigationSafeArgs) apply false
    alias(libs.plugins.devToolKsp) apply false
}