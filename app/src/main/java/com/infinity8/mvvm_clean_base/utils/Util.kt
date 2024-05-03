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
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.infinity8.mvvm_clean_base.model.Photo

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

fun Context.checkNetwork(
    recyclerView: RecyclerView,
    noInternetLabel: TextView,
    getPhotoList: () -> Unit
) {
    val isConnected = this.isNetworkAvailable()
    recyclerView.visibility = if (isConnected) View.VISIBLE else View.GONE
    noInternetLabel.visibility = if (isConnected) View.GONE else View.VISIBLE
    if (isConnected) {
        getPhotoList()
    }
}

fun Fragment.navigateFragment(@IdRes resId: Int, photo: Photo? = null) =
    findNavController().navigate(
        resId,
        bundleOf("photo" to photo)
    )

inline fun <reified T : RecyclerView.Adapter<*>> RecyclerView.setUpAdapter(
    adapter: T? = null
) {
    setItemViewCacheSize(20)
    setHasFixedSize(true)
    this.adapter = adapter
}