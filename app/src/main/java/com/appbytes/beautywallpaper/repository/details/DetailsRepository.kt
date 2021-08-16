package com.appbytes.beautywallpaper.repository.details

import androidx.lifecycle.LiveData
import com.appbytes.beautywallpaper.BaseApplication
import com.appbytes.beautywallpaper.models.DownloadItem
import com.appbytes.beautywallpaper.persistance.DownloadItemDao
import com.appbytes.beautywallpaper.persistance.ImageDao
import com.appbytes.beautywallpaper.ui.details.state.DetailsEvent
import com.appbytes.beautywallpaper.ui.details.state.DetailsState
import com.appbytes.beautywallpaper.util.DataState
import com.appbytes.beautywallpaper.util.download.DownloadUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DetailsRepository
@Inject
constructor(
        private val imageDao: ImageDao,
        private val downloadItemDao: DownloadItemDao) {

        fun getLoadUrl(imageId : String ) : String {
                val image = imageDao.getImageById(imageId)
                return image.regularImageUrl!!
        }


        fun downloadImage(imageId: String) : LiveData<DownloadItem> {
                val checkItem = downloadItemDao.getById(imageId)

                if(checkItem == null) {
                        startDownload(imageId)
                }

                return downloadImageLiveData(imageId)
        }

        private fun startDownload(imageId: String) {
                val downloadItem = imageDao.getImageById(imageId)
                DownloadUtils.download(BaseApplication.instance, downloadItem)
        }

        private fun downloadImageLiveData(imageId: String ) : LiveData<DownloadItem> {
                return downloadItemDao.getLiveById(imageId)
        }


}

