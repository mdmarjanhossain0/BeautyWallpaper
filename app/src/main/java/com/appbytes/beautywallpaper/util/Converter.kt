package com.appbytes.beautywallpaper.util

import com.appbytes.beautywallpaper.api.response.Collections
import com.appbytes.beautywallpaper.api.response.Image
import com.appbytes.beautywallpaper.models.CacheCollections
import com.appbytes.beautywallpaper.models.CacheImage

class Converter {

    companion object {

        fun makeImage(networkObjects: List<Image>) : List<CacheImage> {
            var images : ArrayList<CacheImage> = ArrayList()
            for ( obj in networkObjects) {
                images.add(
                        CacheImage(
                                id = obj.id,
                                color = obj.color,
                                blurHash = obj.blurHash,
                                dateTime = System.currentTimeMillis(),
                                promotedAt = obj.promotedAt,
                                width = obj.width,
                                height = obj.height,
                                description = obj.description,
                                smallImageUrl = obj.urls.small,
                                regularImageUrl = obj.urls.regular,
                                thumb = obj.urls.thumb,
                                links = obj.links.html,
                                downloads = obj.downloads
                        )
                )
            }
            return images
        }

        fun makeCollections(networkObjects : List<Collections>): List<CacheCollections> {
            var images : ArrayList<CacheCollections> = ArrayList()
            for ( obj in networkObjects) {
                images.add(
                        CacheCollections(
                                id = obj.id,
                                title = obj.title,
                                description = obj.description,
                                publishedAt = obj.publishedAt,
                                updatedAt = obj.updatedAt,
                                totalPhotos = obj.totalPhotos,
                                links = obj.links.html,
                                coverPhoto = obj.coverPhoto.urls.small
                        )
                )
            }
            return images
        }

        fun makeCollectionsImages(networkObject: List<Image>, collectionsId : String?): List<CacheImage> {
            var images : ArrayList<CacheImage> = ArrayList()
            for ( obj in networkObject) {
                images.add(
                        CacheImage(
                                id = obj.id,
                                collectionsid = collectionsId,
                                color = obj.color,
                                blurHash = obj.blurHash,
                                dateTime = System.currentTimeMillis(),
                                promotedAt = obj.promotedAt,
                                width = obj.width,
                                height = obj.height,
                                description = obj.description,
                                smallImageUrl = obj.urls.small,
                                regularImageUrl = obj.urls.regular,
                                thumb = obj.urls.thumb,
                                links = obj.links.html,
                                downloads = obj.downloads
                        )
                )
            }
            return images
        }

        fun makeImageFromSearch(networkObjects: List<Image>, query : String ): List<CacheImage> {
            var images : ArrayList<CacheImage> = ArrayList()
            for ( obj in networkObjects) {
                images.add(
                        CacheImage(
                                id = obj.id,
                                query = query,
                                promotedAt = obj.promotedAt,
                                blurHash = obj.blurHash,
                                dateTime = System.currentTimeMillis(),
                                width = obj.width,
                                height = obj.height,
                                description = obj.description,
                                smallImageUrl = obj.urls.small,
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