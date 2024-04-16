/*
 *
 *   Created by Ved Prakash on 4/16/24, 5:56 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 4/16/24, 5:56 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class BarModel(
    val monthName: String,
    val barValue: Int
) : Parcelable
