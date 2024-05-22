/*
 *
 *   Created by Ved Prakash on 5/22/24, 4:59 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 5/22/24, 4:59 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageDownloaderWorker2(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    companion object {
        private const val CHANNEL_ID = "image_download_channel"
        private const val NOTIFICATION_ID = 101
    }

    override suspend fun doWork(): Result {
        val imageUrl = inputData.getString("imageUrl") ?: return Result.failure()

        createNotificationChannel()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Downloading Image")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setProgress(100, 0, true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)

        val downloadedFile = downloadImage(imageUrl)

        if (downloadedFile != null) {
            notificationManager.cancel(NOTIFICATION_ID)

            val fileUri: Uri = FileProvider.getUriForFile(
                applicationContext,
                "${applicationContext.packageName}.fileprovider",
                downloadedFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(fileUri, "image/*")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
            }

            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val completedNotification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle("Image Downloaded")
                .setContentText("Tap to view")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setLargeIcon(BitmapFactory.decodeFile(downloadedFile.path))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(NOTIFICATION_ID, completedNotification)
        }

        return if (downloadedFile != null) Result.success() else Result.failure()
    }

    private suspend fun downloadImage(imageUrl: String): File? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection()
                connection.connect()
                val uri = Uri.parse(imageUrl)

                val fileName = uri.lastPathSegment ?: generateFileName()
                val publicFile = File(
                    applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    fileName
                )

                val input = connection.getInputStream()
                val output = FileOutputStream(publicFile)

                val buffer = ByteArray(1024)
                var totalBytesRead = 0
                var bytesRead: Int

                while (input.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                    totalBytesRead += bytesRead

                    // Update notification progress
                    val progress =
                        ((totalBytesRead.toDouble() / connection.contentLength) * 100).toInt()
                    val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                        .setContentTitle("Downloading Image")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setProgress(100, progress, false)
                        .build()

                    val notificationManager =
                        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(NOTIFICATION_ID, notification)
                }

                output.flush()
                output.close()
                input.close()


                File(applicationContext.cacheDir, fileName)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun generateFileName(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val timestamp = dateFormat.format(Date())
        return "downloadedImage_$timestamp.jpg"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Image Download",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager =
                applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}