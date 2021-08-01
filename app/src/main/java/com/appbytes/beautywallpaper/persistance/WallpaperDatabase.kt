package com.appbytes.beautywallpaper.persistance

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appbytes.beautywallpaper.BaseApplication
import com.appbytes.beautywallpaper.models.CacheCollections
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.models.CacheKey
import com.appbytes.beautywallpaper.models.DownloadItem

@Database(entities = [CacheImage::class, CacheCollections::class, CacheKey::class, DownloadItem::class], version = 13)
abstract class WallpaperDatabase : RoomDatabase (){


    abstract fun iamgeDao() : ImageDao

    abstract fun collectionsDao() : CollectionsDao

    abstract fun collectionsImagesDao() : CollectionsImagesDao

    abstract fun searchDao() : SearchDao

    abstract fun downloadDao() : DownloadItemDao

    companion object {
        const val DATABASE_NAME = "wallpaper_db"

        val instance: WallpaperDatabase by lazy {
            Room.databaseBuilder(
                BaseApplication.instance,
                WallpaperDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }
}