package com.appbytes.beautywallpaper.api.main.response

import com.google.gson.annotations.SerializedName

data class Explore (

        @SerializedName("id") var id : String,
        @SerializedName("title") var title : String,
        @SerializedName("description") var description : String,
        @SerializedName("published_at") var publishedAt : String,
        @SerializedName("last_collected_at") var lastCollectedAt : String,
        @SerializedName("updated_at") var updatedAt : String,
        @SerializedName("curated") var curated : Boolean,
        @SerializedName("featured") var featured : Boolean,
        @SerializedName("total_photos") var totalPhotos : Int,
        @SerializedName("private") var private : Boolean,
        @SerializedName("share_key") var shareKey : String,
        @SerializedName("tags") var tags : List<Tags>,
        @SerializedName("links") var links : Links,
        @SerializedName("user") var user : User,
//        @SerializedName("cover_photo") var coverPhoto : CoverPhoto,
        @SerializedName("preview_photos") var previewPhotos : List<PreviewPhotos>

)

data class Tags (

        @SerializedName("type") var type : String,
        @SerializedName("title") var title : String

)








data class PreviewPhotos (

        @SerializedName("id") var id : String,
        @SerializedName("created_at") var createdAt : String,
        @SerializedName("updated_at") var updatedAt : String,
        @SerializedName("blur_hash") var blurHash : String,
        @SerializedName("urls") var urls : Urls

)