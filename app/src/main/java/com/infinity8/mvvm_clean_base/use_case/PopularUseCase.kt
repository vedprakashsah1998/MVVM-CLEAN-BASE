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
import com.infinity8.mvvm_clean_base.utils.Outcome
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
                        send(Outcome.Failure(Throwable("Failed body")))
                    }
                } else {
                    send(Outcome.Failure(Throwable("Error Found")))
                }

            }
        }
    }
}