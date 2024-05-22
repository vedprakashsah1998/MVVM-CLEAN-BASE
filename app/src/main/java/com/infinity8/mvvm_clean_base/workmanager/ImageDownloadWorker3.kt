/*
 *
 *   Created by Ved Prakash on 5/22/24, 5:57 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 5/22/24, 5:57 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinity8.mvvm_clean_base.R
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder

class ImageDownloadWorker3(val context: Context, val workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    private val notificationId = 1
    private val channelId = "download_channel"
    private val notificationManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(
            context
        )
    }

    override suspend fun doWork(): Result {
        val imageUrl = inputData.getString("IMAGE_URL")
        val downloadedFile = downloadImage(imageUrl)

        if (downloadedFile != null) {
            notificationManager.cancel(notificationId)
            showDownloadCompleteNotification(downloadedFile.absolutePath)
            return Result.success()
        } else {
            // Handle download failure
            return Result.failure()
        }
    }

    private fun downloadImage(imageUrl: String?): File? {
        if (imageUrl == null) {
            return null
        }

        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()

        if (connection.responseCode != HttpURLConnection.HTTP_OK) {
            // Handle failed download (e.g., not found)
            return null
        }

        val downloadedFileName = getFileNameFromUrl(imageUrl)
        val downloadedFile = createDownloadFile(downloadedFileName)
        Log.d("lskjdfksd", "downloadImage: $downloadedFile")
        val inputStream = connection.inputStream
        val outputStream = downloadedFile.outputStream()
        val buffer = ByteArray(1024)
        var bytesRead: Int

        do {
            bytesRead = inputStream.read(buffer)
            if (bytesRead > 0) {
                outputStream.write(buffer, 0, bytesRead)
                updateDownloadProgress(bytesRead.toFloat(), downloadedFile.length())
            }
        } while (bytesRead != -1)

        outputStream.close()
        inputStream.close()

        return downloadedFile
    }

    private fun createDownloadFile(fileName: String): File {
        val externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        return File(externalFilesDir, fileName)
    }

    private fun getFileNameFromUrl(urlString: String): String {
        val url = URL(urlString)
        val fileName = url.path.substringAfterLast("/")
        if (fileName.isEmpty()) {
            return URLDecoder.decode(url.toString(), "UTF-8") // Decode the entire URL as a fallback
        }
        return fileName
    }

    private fun updateDownloadProgress(downloadedBytes: Float, totalBytes: Long) {
        val progress = (downloadedBytes / totalBytes) * 100
        val notificationBuilder = createNotificationBuilder("Downloading...", progress.toInt())
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        notificationBuilder.setProgress(100, progress.toInt(), false) // Update progress
            .setOngoing(true)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun showDownloadCompleteNotification(filePath: String) {
        val notificationBuilder = createNotificationBuilder("Download Complete")
        // Create an Intent to view the downloaded image
        val intent = Intent(Intent.ACTION_VIEW)
        val file = File(filePath)
        Log.d("fowiueroiuwe: ", "foiwueriou: ${file.path}")
        Log.d("fowiueroiuwe: ", "filePath: ${filePath}")
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notificationBuilder.setContentIntent(pendingIntent)
            .setContentText("Download complete")
            .setStyle(NotificationCompat.BigTextStyle().bigText("File saved to: $filePath"))
            .setContentIntent(pendingIntent)
            .setOngoing(false)
        val notification = notificationBuilder.build()
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        notificationManager.notify(notificationId + 1, notification)
    }

    private fun createNotificationBuilder(
        progressText: String = "Downloading...",
        progress: Int? = null
    ): NotificationCompat.Builder {
        // Create the Notification channel (needed for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Download Channel"
            val channelDescription = "Channel for download notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = channelDescription
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(progressText)
            .setSmallIcon(R.drawable.outline_cloud_download_24)
            .setPriority(PRIORITY_HIGH)


        if (progress != null) {
            notificationBuilder.setProgress(100, progress, false)
                .setOngoing(true)
        }

        return notificationBuilder
    }
}