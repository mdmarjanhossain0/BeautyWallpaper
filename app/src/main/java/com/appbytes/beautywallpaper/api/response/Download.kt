package com.appbytes.beautywallpaper.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Download (
        @SerializedName("url") @Expose var url: String
        )