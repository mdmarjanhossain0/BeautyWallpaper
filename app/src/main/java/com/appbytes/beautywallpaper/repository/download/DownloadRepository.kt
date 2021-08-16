package com.appbytes.beautywallpaper.repository.download

import androidx.lifecycle.LiveData
import com.appbytes.beautywallpaper.api.MainApiService
import com.appbytes.beautywallpaper.models.DownloadItem
import com.appbytes.beautywallpaper.persistance.DownloadItemDao
import com.appbytes.beautywallpaper.persistance.ImageDao
import javax.inject.Inject


class DownloadRepository
@Inject
constructor(
    private val imageDao : ImageDao,
    private val downloadItemDao: DownloadItemDao
) {

    val downloadItems : LiveData<List<DownloadItem>>
        get() = downloadItemDao.getAllTask()
}