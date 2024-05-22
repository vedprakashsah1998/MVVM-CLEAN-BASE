/*
 *
 *   Created by Ved Prakash on 5/22/24, 2:47 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 5/22/24, 2:47 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.workmanager

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.infinity8.mvvm_clean_base.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadForegroundService : Service() {

    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private var notificationId = 1

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)
    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading Image")
            .setSmallIcon(R.drawable.outline_cloud_download_24)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val imageUrl = intent?.getStringExtra("IMAGE_URL") ?: return START_NOT_STICKY

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Check if the context allows starting foreground services
            if (!packageManager.canRequestPackageInstalls()) {
                // Show a notification or handle the condition where foreground service is not allowed
                return START_NOT_STICKY
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the notification channel if necessary
            createNotificationChannel()
        }

        this@DownloadForegroundService.startForeground(notificationId, notificationBuilder.build())
        downloadImageAsync(imageUrl)
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Download Channel"
            val descriptionText = "Channel for downloading images"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun downloadImageAsync(url: String) {
        coroutineScope.launch {
            try {
                downloadImage(url)
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle download failure
                withContext(Dispatchers.Main) {
                    notificationBuilder.setContentTitle("Download Failed")
                        .setContentText("Failed to download image")
                        .setProgress(0, 0, false)
                    if (ActivityCompat.checkSelfPermission(
                            this@DownloadForegroundService,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                    }
                    notificationManager.notify(notificationId, notificationBuilder.build())
                }
            }
        }
    }

    private suspend fun downloadImage(url: String) {
        val uri = Uri.parse(url)
        val connection = withContext(Dispatchers.IO) {
            URL(uri.toString()).openConnection()
        } as HttpURLConnection

        try {
            withContext(Dispatchers.IO) {
                connection.connect()
            }
            val responseCode = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val outputStream =
                    withContext(Dispatchers.IO) {
                        FileOutputStream(getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + "/downloadedImage.jpg")
                    }

                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (withContext(Dispatchers.IO) {
                        inputStream.read(buffer)
                    }.also { bytesRead = it } != -1) {
                    withContext(Dispatchers.IO) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }

                withContext(Dispatchers.IO) {
                    outputStream.close()
                }
                withContext(Dispatchers.IO) {
                    inputStream.close()
                }

                // Download complete
                withContext(Dispatchers.Main) {
                    notificationBuilder
                        .setProgress(0, 0, false)
                        .setContentTitle("Download Complete")
                        .setContentText("Image downloaded successfully")
                        .setContentIntent(createOpenImageIntent())
                    if (ActivityCompat.checkSelfPermission(
                            this@DownloadForegroundService,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                    }
                    notificationManager.notify(notificationId, notificationBuilder.build())
                }
            } else {
                // Handle HTTP response other than OK
                withContext(Dispatchers.Main) {
                    notificationBuilder.setContentTitle("Download Failed")
                        .setContentText("Failed to download image: HTTP $responseCode")
                        .setProgress(0, 0, false)
                    notificationManager.notify(notificationId, notificationBuilder.build())
                }
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun createOpenImageIntent(): PendingIntent? {
        // Create intent to open the downloaded image
        val intent = Intent(Intent.ACTION_VIEW)
        val uri =
            Uri.parse("file://" + applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/downloadedImage.jpg")
        intent.setDataAndType(uri, "image/*")
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val CHANNEL_ID = "DownloadChannel"
    }
}