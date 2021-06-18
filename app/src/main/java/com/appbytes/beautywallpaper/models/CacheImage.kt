package com.appbytes.beautywallpaper.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "image")
data class CacheImage(

        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "id")
        var id : String,

        @ColumnInfo(name = "promotedAt")
        var promotedAt : String? = null,

        @ColumnInfo(name = "width")
        var width : Int? = null,

        @ColumnInfo(name = "height")
        var height : Int? = null,

        @ColumnInfo(name = "description")
        var description : String? = null,

        @ColumnInfo(name = "regular")
        var regularImageUrl : String? = null,

        @ColumnInfo(name = "thumb")
        var thumb : String? = null,

        @ColumnInfo(name = "links")
        var links : String? = null,

        @ColumnInfo(name = "downloads")
        var downloads : Int? = null
) : Parcelable
