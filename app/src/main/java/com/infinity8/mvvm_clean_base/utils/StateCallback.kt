/*
 *
 *   Created by Ved Prakash on 3/29/24, 3:08 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 3/29/24, 3:07 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.utils

import android.util.Log
import android.view.View
import com.infinity8.mvvm_clean_base.controller.Callbacks

/**
 * @param callbacks = this variable used for setting different
 * callbacks from different states of ui
 * @param view = this is not used now but later it will
 * be helpful for showing toast or any other things
 * this method is very helpful for handling the different
 * states of ui and network as well.
 */
fun <T> Outcome<T>.handleStateData(
    view: View?,
    callbacks: Callbacks,
) {
    when (this) {
        is Outcome.Progress -> {
            Log.d("PROGRESS STATUS: ", "Progress:${loading} ")
            callbacks.loadingNetwork(loading)
        }

        is Outcome.Success -> {
            Log.d("PROGRESS STATUS: ", "Progress:${data} ")
            callbacks.loadingNetwork(false)
            callbacks.successResponse(data)
        }

        is Outcome.Failure -> {
            Log.d("FAILURE RESPONSE: ", "Failure: $error")
            callbacks.failureResponse(error)
            callbacks.loadingNetwork(false)
        }

        is Outcome.Unknown -> {
            Log.d("UNKNOWN_BEHAVIOUR: ", "Unknown")
            callbacks.loadingNetwork(false)
            callbacks.unknownBehaviour(message)
        }
    }
}