package com.appbytes.beautywallpaper.persistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appbytes.beautywallpaper.models.CacheCollections


@Dao
interface CollectionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: CacheCollections) : Long

    @Query("SELECT * FROM collections LIMIT (:page * 10)")
    fun getCollections(page : Int) : List<CacheCollections>
}