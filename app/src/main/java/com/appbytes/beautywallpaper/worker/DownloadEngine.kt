package com.appbytes.beautywallpaper.worker

import com.appbytes.beautywallpaper.models.DownloadItem

class DownloadEngine {

    fun downloadTriggerEngine(items : List<DownloadItem>){
        val downloadThread = Thread{
            if(items.size >= 2) {
                // Start 2 download

            }
        }
    }
}
