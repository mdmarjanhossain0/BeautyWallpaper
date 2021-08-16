package com.appbytes.beautywallpaper.util.download

import android.content.Context
import android.content.Intent
import com.appbytes.beautywallpaper.BaseApplication
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.models.DownloadItem
import com.appbytes.beautywallpaper.persistance.DownloadItemDao
import com.appbytes.beautywallpaper.persistance.ImageDao
import com.appbytes.beautywallpaper.persistance.WallpaperDatabase
import com.appbytes.beautywallpaper.worker.DownloadService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@Suppress("unused_parameter")
class DownloadUtils {
    private val TAG = "DownloadUtils"


    @Inject
    lateinit var downloadDao: DownloadItemDao

    companion object {

        fun getFileToSave(expectedName: String): File? {
            val galleryPath = FileUtils.downloadOutputDir ?: return null
            val folder = File(galleryPath)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            return File(folder.toString() + File.separator + expectedName)
        }

        fun cancelDownload(context: Context, image: CacheImage) {
            val intent = Intent(BaseApplication.instance, DownloadService::class.java)
            intent.putExtra(Params.CANCELED_KEY, true)
            intent.putExtra(Params.URL_KEY, image.regularImageUrl)
            context.startService(intent)
        }

        fun download(context: Context, image: CacheImage) {
            var previewFile: File? = null
            previewFile = image.regularImageUrl?.let {
                FileUtils.getCachedFile(it)
            }
//        AppComponent.instance.reporter.report(image.downloadLocationLink)
            startDownloadService(context, image.id, image.regularImageUrl!!, previewFile?.path)
            persistDownloadItem(context, image)
            Toaster.sendShortToast(context.getString(R.string.downloading_in_background))
        }

        private fun persistDownloadItem(context: Context, image: CacheImage) {
            GlobalScope.launch(Dispatchers.IO) {
                val item = DownloadItem(
                        image.id!!,
                        image.thumb!!,
                        image.regularImageUrl!!,
                        image.id
                ).apply {
//                color = image.themeColor
                    width = image.width!!
                    height = image.height!!
                }
//            item.color = image.themeColor
//            AppDatabase.instance.downloadItemDao().insertAll(item)
//                downloadDao.insertAll(item)

                WallpaperDatabase.instance.downloadDao().insertAll((item))
            }
        }

        private fun startDownloadService(
            context: Context,
            name: String,
            url: String,
            previewUrl: String? = null
        ) {
            val intent = Intent(context, DownloadService::class.java)
            intent.putExtra(Params.NAME_KEY, name)
            intent.putExtra(Params.URL_KEY, url)
            previewUrl?.let {
                intent.putExtra(Params.PREVIEW_URI, previewUrl)
            }
            context.startService(intent)
        }


    }

}

