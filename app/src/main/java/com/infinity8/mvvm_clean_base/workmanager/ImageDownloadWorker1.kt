/*
 *
 *   Created by Ved Prakash on 5/22/24, 2:50 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 5/22/24, 2:50 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.workmanager

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class ImageDownloadWorker1(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val imageUrl = inputData.getString("IMAGE_URL") ?: return Result.failure()

        val foregroundIntent = Intent(applicationContext, DownloadForegroundService::class.java)
        foregroundIntent.putExtra("IMAGE_URL", imageUrl)
        ContextCompat.startForegroundService(applicationContext, foregroundIntent)

        return Result.success()
    }
}