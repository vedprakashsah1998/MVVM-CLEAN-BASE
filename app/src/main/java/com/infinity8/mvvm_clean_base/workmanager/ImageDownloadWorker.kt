/*
 *
 *   Created by Ved Prakash on 5/16/24, 7:02 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 5/16/24, 7:02 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.workmanager


import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageDownloadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // URL of the image to download
        val imageUrl = inputData.getString("IMAGE_URL") ?: return Result.failure()

        return try {
            downloadImage(imageUrl)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun downloadImage(url: String) {
        val uri = Uri.parse(url)
        val fileName = uri.lastPathSegment ?: generateFileName()
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading Image")
            .setDescription("Image is being downloaded")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager =
            applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    private fun generateFileName(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val timestamp = dateFormat.format(Date())
        return "downloadedImage_$timestamp.jpg"
    }
}


