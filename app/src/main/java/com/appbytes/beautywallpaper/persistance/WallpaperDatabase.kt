package com.appbytes.beautywallpaper.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.persistance.main.ImageDao

@Database(entities = [CacheImage::class], version = 2)
abstract class WallpaperDatabase : RoomDatabase (){


    abstract fun iamgeDao() : ImageDao

    companion object {
        const val DATABASE_NAME = "wallpaper_db"
    }
}