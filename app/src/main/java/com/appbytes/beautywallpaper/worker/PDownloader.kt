package com.appbytes.beautywallpaper.worker

import com.appbytes.beautywallpaper.BaseApplication
import com.appbytes.beautywallpaper.models.DownloadItem
import com.appbytes.beautywallpaper.persistance.WallpaperDatabase
import com.appbytes.beautywallpaper.util.download.DownloadUtils


class PDownloader {



    companion object {

        fun checkExisistance(){

            val allDownloadTasks = WallpaperDatabase.instance.downloadDao().getAll()
            if(allDownloadTasks.size == 0 ){
                return
            }
            val reTriggerTask = ArrayList<DownloadItem>()
            allDownloadTasks.forEach{  item ->
                if (item.status != DownloadItem.DOWNLOAD_STATUS_OK ) {
                    reTriggerTask.add(item)
                }
            }

            if(reTriggerTask.size == 0 ){
                return
            }

            reTriggerTask.forEach{ item ->
                val image = WallpaperDatabase.instance.iamgeDao().getImageById(item.id)
                DownloadUtils.download(BaseApplication.instance, image)
            }


        }
    }



}