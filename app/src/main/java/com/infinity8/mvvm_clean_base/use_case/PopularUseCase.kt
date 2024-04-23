/*
 *
 *   Created by Ved Prakash on 4/1/24, 4:55 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 4/1/24, 4:55 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.use_case

import com.infinity8.mvvm_clean_base.model.CuratedImageModel
import com.infinity8.mvvm_clean_base.repository.PopularImgRepo
import com.infinity8.mvvm_clean_base.utils.BAD_GATEWAY
import com.infinity8.mvvm_clean_base.utils.ERROR_MESSAGE
import com.infinity8.mvvm_clean_base.utils.FAILURE_MESSAGE
import com.infinity8.mvvm_clean_base.utils.Outcome
import com.infinity8.mvvm_clean_base.utils.TOO_MANY_REQUEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class PopularUseCase @Inject constructor(private val popularImgRepo: PopularImgRepo) {
    suspend operator fun invoke(): Flow<Outcome<CuratedImageModel?>> {
        return channelFlow {
            send(Outcome.Progress(true))
            popularImgRepo.getPopularImage().collectLatest { response ->
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        send(Outcome.Success(response.body()))
                    } else {
                        if (response.code() == 429) {
                            send(Outcome.Failure(Throwable(TOO_MANY_REQUEST)))
                        } else {
                            send(Outcome.Failure(Throwable("Failed body")))
                        }
                    }
                } else {
                    send(
                        Outcome.Failure(
                            Throwable(
                                when (response.code()) {
                                    503 -> FAILURE_MESSAGE
                                    502 -> BAD_GATEWAY
                                    429 -> TOO_MANY_REQUEST
                                    else -> ERROR_MESSAGE
                                }
                            )
                        )
                    )
                }

            }
        }
    }
}