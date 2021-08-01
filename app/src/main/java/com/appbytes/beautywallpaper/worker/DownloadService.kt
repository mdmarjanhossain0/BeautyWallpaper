package com.appbytes.beautywallpaper.worker

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.appbytes.beautywallpaper.BaseApplication
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.api.IOService
import com.appbytes.beautywallpaper.persistance.DownloadItemDao
import com.appbytes.beautywallpaper.persistance.ImageDao
import com.appbytes.beautywallpaper.util.Constants.Companion.DOWNLOAD_TIMEOUT_MS
import com.appbytes.beautywallpaper.util.download.DownloadUtils
import com.appbytes.beautywallpaper.util.download.NotificationUtils
import com.appbytes.beautywallpaper.util.download.Params
import com.appbytes.beautywallpaper.util.download.Toaster
import com.appbytes.beautywallpaper.util.extentions.notifyFileUpdated
import com.appbytes.beautywallpaper.util.extentions.writeToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject



@AndroidEntryPoint
class DownloadService : Service(), CoroutineScope by MainScope() {
    companion object {
        private const val TAG = "DownloadService"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // A map storing download url to downloading disposable object
    private val downloadUrlToJobMap = mutableMapOf<String, Job>()

    @Inject
    lateinit var downloadItemDao: DownloadItemDao

    @Inject
    lateinit var imgeDao: ImageDao

    @Inject
    lateinit var service: IOService

    override fun onCreate() {
        super.onCreate()
    }

    @SuppressLint("CheckResult")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "on start command $intent")
        CoroutineScope(Dispatchers.IO).launch{
            Log.d(TAG, "Hilt test " + imgeDao.getImagesAll())
        }
        intent?.let {
            // launch in main thread
            launch {
                onHandleIntent(it)
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "on destroy, cancel all, size: ${downloadUrlToJobMap.size}")
        cancel()
        super.onDestroy()
    }

    private suspend fun checkStatus() {
        Log.d(TAG, "checking status, thread: ${Thread.currentThread()}")
        downloadItemDao.markAllFailed()
    }

    private suspend fun onHandleIntent(intent: Intent) {
        if (intent.getBooleanExtra(Params.CHECK_STATUS, false)) {
            withContext(Dispatchers.IO) {
                checkStatus()
            }
            if (downloadUrlToJobMap.isEmpty()) {
                Log.d(TAG, "about to stop self")
                stopSelf()
            }
            return
        }

        Log.d(TAG, "onHandleIntent")

        val downloadUrl = intent.getStringExtra(Params.URL_KEY) ?: return
        val fileName = intent.getStringExtra(Params.NAME_KEY) ?: return

        val cancelJob = intent.getBooleanExtra(Params.CANCELED_KEY, false)
        val previewUrl = intent.getStringExtra(Params.PREVIEW_URI)
        val isUnsplash = intent.getBooleanExtra(Params.IS_UNSPLASH_WALLPAPER, true)
        val fromRetry = intent.getIntExtra(Params.RETRY_KEY, 0)

        if (fromRetry != 0) {
            NotificationUtils.cancelNotification(fromRetry)
        }

        if (!isUnsplash) {
            Toast.makeText(applicationContext.applicationContext, R.string.downloading, Toast.LENGTH_SHORT)
        }

        val previewUri: Uri? = if (previewUrl.isNullOrEmpty()) null else {
            Uri.parse(previewUrl)
        }

        if (cancelJob) {
            cancelJob(downloadUrl)
        } else {
            Log.d(TAG, "on handle intent progress: $downloadUrl")
            downloadImage(downloadUrl, fileName, previewUri, isUnsplash)
        }
    }

    private suspend fun cancelJob(url: String) {
        Log.d(TAG, "on handle intent cancelled, thread: ${Thread.currentThread()}")
        downloadUrlToJobMap[url]?.let { job ->
            job.cancelAndJoin()
            downloadUrlToJobMap.remove(url)
            Log.d(TAG, "job cancelled")
            NotificationUtils.cancelNotification(Uri.parse(url))
            Toaster.sendShortToast(getString(R.string.cancelled_download))
        }
    }

    private fun downloadImage(
        url: String,
        fileName: String,
        previewUri: Uri?,
        isUnsplash: Boolean) {
        val job = launch(context = CoroutineExceptionHandler { _, e ->
            Log.d(TAG, "CoroutineExceptionHandler error $e, url $url")
        }) {
            Log.d(TAG, "on start downloading")
            try {
                withContext(Dispatchers.IO) {
                    downloadItemDao.setProgress(url, 0)
                }

                val file = DownloadUtils.getFileToSave(fileName)

                val responseBody = withTimeout(DOWNLOAD_TIMEOUT_MS) {
                    service.downloadFile(url)
                }

                Log.d(TAG, "outputFile download onNext, " +
                        "size=${responseBody.contentLength()}")

                withContext(Dispatchers.IO) {
                    downloadItemDao.setProgress(url, 1)
                }

                responseBody.writeToFile(file!!.path) { p ->
                    Log.d(TAG, "dao setting progress: $p")
                    downloadItemDao.setProgress(url, p)
                    val item = downloadItemDao.getByUrl(url)
                    NotificationUtils.showProgressNotification(
                            item.fileName!!,
                            "Downloading...",
                            item.progress,
                            Uri.parse(url),
                            null
                    )
                }
                onSuccess(url, file, previewUri, isUnsplash)
                Log.d(TAG, getString(R.string.completed))
            } catch (e: CancellationException) {
                // CancellationException will be ignored by CoroutineExceptionHandler,
                // thus we handle it here.
                Log.d(TAG, "CancellationException error $e, url $url")
                e.printStackTrace()
                onError(url, fileName, null, false)
            } catch (e: Exception) {
                Log.d(TAG, "other error $e, url $url")
                e.printStackTrace()
                onError(url, fileName, null, true)
            }
        }
        downloadUrlToJobMap[url] = job
    }

    private suspend fun onSuccess(url: String, file: File, previewUri: Uri?, isUnsplash: Boolean) {
        Log.d(TAG, "output file:" + file.absolutePath)

        val newFile = File("${file.path.replace(" ", "")}.jpg")
        file.renameTo(newFile)

        Log.d(TAG, "renamed file:" + newFile.absolutePath)
        newFile.notifyFileUpdated(BaseApplication.instance)

        withContext(Dispatchers.IO) {
            downloadItemDao.setSuccess(url, newFile.path)
        }

        NotificationUtils.showCompleteNotification(Uri.parse(url), previewUri,
                if (isUnsplash) null else newFile.absolutePath)
    }

    private suspend fun onError(url: String, fileName: String, previewUri: Uri?, showNotification: Boolean) {
        if (showNotification) {
            NotificationUtils.showErrorNotification(Uri.parse(url), fileName, url, previewUri)
        }

        withContext(Dispatchers.IO) {
            downloadItemDao.setFailed(url)
        }
    }
}
