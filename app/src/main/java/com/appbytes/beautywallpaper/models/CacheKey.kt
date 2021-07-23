package com.appbytes.beautywallpaper.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "searchkey")
data class CacheKey(
        @PrimaryKey(autoGenerate = false)
        var key : String = ""
) : Parcelable