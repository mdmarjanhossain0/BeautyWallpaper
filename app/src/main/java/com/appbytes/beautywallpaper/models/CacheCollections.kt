package com.appbytes.beautywallpaper.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "collections")
data class CacheCollections (

        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "id")
        var id : String,

        var title : String? = null,

        var description : String? = null,

        var publishedAt : String? = null,

        var updatedAt : String? = null,

        var totalPhotos : Int = 0,

        var links : String? = null,

        var coverPhoto : String? = null
) : Parcelable