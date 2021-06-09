package com.appbytes.beautywallpaper.util

import com.appbytes.beautywallpaper.api.main.response.Image
import com.appbytes.beautywallpaper.models.CacheImage

class Converter {

    companion object {

        fun makeImage(networkObjs: List<Image>) : List<CacheImage> {
            var images : ArrayList<CacheImage> = ArrayList()
            for ( obj in networkObjs) {
                images.add(
                        CacheImage(
                                id = obj.id,
                                promotedAt = obj.promotedAt,
                                width = obj.width,
                                height = obj.height,
                                description = obj.description,
                                regularImageUrl = obj.urls.regular,
                                thumb = obj.urls.thumb,
                                links = obj.links.html,
                                downloads = obj.downloads
                        )
                )
            }
            return images
        }
    }
}