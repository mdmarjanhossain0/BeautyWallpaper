package com.appbytes.beautywallpaper.persistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appbytes.beautywallpaper.models.CacheImage


@Dao
interface ImageDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: CacheImage) : Long

//    ORDER BY datetime DESC

    @Query("SELECT * FROM image WHERE searchQuery = :query ORDER BY datetime ASC LIMIT (:pageNumber* 10)")
    fun getImages(pageNumber : Int, query : String = "") : List<CacheImage>

    @Query("SELECT * FROM image")
    fun getImagesAll() : List<CacheImage>

    @Query("SELECT * FROM image WHERE id = :id")
    fun getImageById(id : String) : CacheImage

    @Query("SELECT * FROM image WHERE favorite = 0 ORDER BY datetime DESC")
    fun getFavoriteImages() : List<CacheImage>
}