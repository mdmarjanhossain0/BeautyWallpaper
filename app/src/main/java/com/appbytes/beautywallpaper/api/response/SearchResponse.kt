package com.appbytes.beautywallpaper.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class SearchResponse(
        @SerializedName("total")
        @Expose
        var total : Int,

        @SerializedName("total_pages")
        @Expose
        var totalPages : Int,

        @SerializedName("results")
        @Expose
        var results : List<SearchImage>
)