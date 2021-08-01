package com.appbytes.beautywallpaper.api.response

import com.google.gson.annotations.SerializedName


data class SearchResponse(
        @SerializedName("total") var total : Int,
        @SerializedName("total_pages") var totalPages : Int,
        @SerializedName("results") var results : List<SearchImage>
)