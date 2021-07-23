package com.appbytes.beautywallpaper.persistance

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

    @Query("SELECT * FROM image WHERE searchQuery = :query ORDER BY datetime DESC LIMIT (:pageNumber* 10)")
    fun getImages(pageNumber : Int, query : String = "") : List<CacheImage>

    @Query("SELECT * FROM image")
    fun getImagesAll() : List<CacheImage>

    @Query("SELECT * FROM image WHERE favorite = 0 ORDER BY datetime DESC")
    fun getFavoriteImages() : List<CacheImage>
}