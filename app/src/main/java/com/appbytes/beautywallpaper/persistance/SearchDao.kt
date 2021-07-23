package com.appbytes.beautywallpaper.persistance

import androidx.room.*
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.models.CacheKey


@Dao
interface SearchDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: CacheImage) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKey(image: CacheKey) : Long

    @Query("SELECT * FROM image WHERE searchQuery = :query ORDER BY datetime DESC LIMIT (:pageNumber* 10)")
    fun getImages(pageNumber : Int, query : String = "") : List<CacheImage>

    @Query("SELECT * FROM searchkey")
    fun getKeys() : List<CacheKey>


    @Query("SELECT * FROM image")
    fun getImagesAll() : List<CacheImage>


    @Delete
    fun deleteKey(key: CacheKey)
}