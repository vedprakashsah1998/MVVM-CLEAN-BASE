/*
 *
 *   Created by Ved Prakash on 4/1/24, 10:02 AM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 4/1/24, 10:02 AM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build

object Util {

}
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val networkCapabilitiesInfo =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        networkCapabilitiesInfo.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                networkCapabilitiesInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    } else {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        networkInfo != null && networkInfo.isConnected
    }
}