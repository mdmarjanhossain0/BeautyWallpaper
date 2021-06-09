package com.appbytes.beautywallpaper.persistance.main

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appbytes.beautywallpaper.api.main.response.Image
import com.appbytes.beautywallpaper.models.CacheImage
import kotlinx.coroutines.flow.Flow


@Dao
interface ImageDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: CacheImage) : Long

    @Query("SELECT * FROM image")
    fun getImages() : List<CacheImage>
}