package com.appbytes.beautywallpaper.persistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appbytes.beautywallpaper.models.CacheImage


@Dao
interface CollectionsImagesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(image: CacheImage) : Long

    @Query("SELECT * FROM image")
    fun getCollections() : List<CacheImage>

    @Query("SELECT * FROM image WHERE collectionsid  =:collectionsId ORDER BY datetime DESC LIMIT (:page * 10)")
    fun getCollectionsById(collectionsId : String?, page : Int) : List<CacheImage>
}