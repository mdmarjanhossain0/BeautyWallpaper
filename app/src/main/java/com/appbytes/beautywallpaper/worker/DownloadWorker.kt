package com.appbytes.beautywallpaper.worker


import android.app.NotificationManager
import android.content.Context
import androidx.work.*
import com.appbytes.beautywallpaper.api.DownloadServiceWorker


class DownloadWorker(
        private val context: Context,
        params: WorkerParameters,
        private val downloadService: DownloadServiceWorker,
        private val notificationManager: NotificationManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val url = inputData.getString(KEY_INPUT_URL) ?: return Result.failure()
        val fileName = inputData.getString(KEY_OUTPUT_FILE_NAME) ?: return Result.failure()

        /*val downloadAction = when (inputData.getString(KEY_DOWNLOAD_ACTION)) {
            DownloadAction.DOWNLOAD.name -> DownloadAction.DOWNLOAD
            DownloadAction.WALLPAPER.name -> DownloadAction.WALLPAPER
            else -> null
        } ?: return Result.failure()*/

        val notificationId = id.hashCode()
        val cancelIntent = WorkManager.getInstance(context).createCancelPendingIntent(id)
        /*val notificationBuilder =
                notificationManager.getProgressNotificationBuilder(fileName, cancelIntent)

        setForeground(ForegroundInfo(notificationId, notificationBuilder.build()))

        download(url, fileName, downloadAction, notificationId, notificationBuilder)*/

        return Result.success()
    }

/*    private suspend fun download(
            url: String,
            fileName: String,
            downloadAction: DownloadAction,
            notificationId: Int,
            notificationBuilder: NotificationCompat.Builder
    ) = withContext(Dispatchers.IO) {
        *//*try {
            val responseBody = downloadService.downloadFile(url)

            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                responseBody.saveImage(context, fileName) {
                    launch { onProgress(notificationId, notificationBuilder, it) }
                }
            } else {
                responseBody.saveImageLegacy(context, fileName) {
                    launch { onProgress(notificationId, notificationBuilder, it) }
                }
            }

            if (uri != null) {
                onSuccess(downloadAction, fileName, uri)
                inputData.getString(KEY_PHOTO_ID)?.let {
                    safeApiCall(Dispatchers.IO) { downloadService.trackDownload(it) }
                }
            } else {
                onError(downloadAction, fileName, Exception("Failed writing to file"),
                        STATUS_FAILED, true)
            }
        } catch (e: CancellationException) {
            onError(downloadAction, fileName, e, STATUS_CANCELLED, false)
        } catch (e: Exception) {
            onError(downloadAction, fileName, e, STATUS_FAILED, true)
        }*//*
    }

    private suspend fun onProgress(
            notificationId: Int,
            builder: NotificationCompat.Builder,
            progress: Int
    ) {
        setForeground(ForegroundInfo(notificationId,
                notificationManager.updateProgressNotification(builder, progress).build()))
    }

    private fun onSuccess(
            downloadAction: DownloadAction,
            fileName: String,
            uri: Uri
    ) {
        info("onSuccess: $fileName - $uri")

        val localIntent = Intent(ACTION_DOWNLOAD_COMPLETE).apply {
            putExtra(DOWNLOAD_STATUS, STATUS_SUCCESSFUL)
            putExtra(DATA_ACTION, downloadAction)
            putExtra(DATA_URI, uri)
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent)

        if (downloadAction == DownloadAction.DOWNLOAD) {
            notificationManager.showDownloadCompleteNotification(fileName, uri)
        }
    }

    private fun onError(
            downloadAction: DownloadAction,
            fileName: String,
            exception: Exception,
            status: Int,
            showNotification: Boolean
    ) {
        error("onError: $fileName", exception)

        val localIntent = Intent(ACTION_DOWNLOAD_COMPLETE).apply {
            putExtra(DOWNLOAD_STATUS, status)
            putExtra(DATA_ACTION, downloadAction)
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent)

        if (showNotification) {
            notificationManager.showDownloadErrorNotification(fileName)
        }
    }*/



    companion object {

        const val KEY_DOWNLOAD_ACTION = "KEY_DOWNLOAD_ACTION"
        const val KEY_INPUT_URL = "KEY_INPUT_URL"
        const val KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME"
        const val KEY_PHOTO_ID = "KEY_PHOTO_ID"
    }
}