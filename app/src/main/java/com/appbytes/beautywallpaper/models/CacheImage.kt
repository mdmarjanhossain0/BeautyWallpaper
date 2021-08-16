package com.appbytes.beautywallpaper.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "image")
data class CacheImage(

        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "id")
        var id : String,

        @ColumnInfo(name = "collectionsid")
        var collectionsid : String? = null,

        @ColumnInfo(name = "searchQuery")
        var query : String = "",

        @ColumnInfo(name = "favorite")
        var favorite : Int = 1,

        @ColumnInfo(name = "promotedAt")
        var promotedAt : String? = null,

        @ColumnInfo(name = "color")
        var color : String? = null,

        @ColumnInfo(name = "blurHash")
        var blurHash : String? = null,

        @ColumnInfo(name = "datetime")
        var dateTime : Long = 0,                        // System.currentTimeMillis()

        @ColumnInfo(name = "lastedit")
        var lastEdit : Long = System.currentTimeMillis(),

        @ColumnInfo(name = "width")
        var width : Int? = null,

        @ColumnInfo(name = "height")
        var height : Int? = null,

        @ColumnInfo(name = "description")
        var description : String? = null,

        @ColumnInfo(name = "thumb")
        var thumb : String? = null,

        @ColumnInfo(name = "small")
        var smallImageUrl : String? = null,

        @ColumnInfo(name = "regular")
        var regularImageUrl : String? = null,

        @ColumnInfo(name = "full")
        var fullImageUrl : String? = null,

        @ColumnInfo(name = "raw")
        var rawImageUrl : String? = null,

        @ColumnInfo(name = "links")
        var links : String? = null,

        @ColumnInfo(name = "downloads")
        var downloads : Int? = null
) : Parcelable
