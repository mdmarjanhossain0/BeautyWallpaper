package com.appbytes.beautywallpaper.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import com.appbytes.beautywallpaper.models.CacheCollections
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.models.CacheKey

@Database(entities = [CacheImage::class, CacheCollections::class, CacheKey::class], version = 10)
abstract class WallpaperDatabase : RoomDatabase (){


    abstract fun iamgeDao() : ImageDao

    abstract fun collectionsDao() : CollectionsDao

    abstract fun collectionsImagesDao() : CollectionsImagesDao

    abstract fun searchDao() : SearchDao

    companion object {
        const val DATABASE_NAME = "wallpaper_db"
    }
}