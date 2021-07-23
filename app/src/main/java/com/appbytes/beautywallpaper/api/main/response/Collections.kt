package com.appbytes.beautywallpaper.api.main.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Collections (

        @SerializedName("id") @Expose var id : String,
        @SerializedName("title") @Expose var title : String,
        @SerializedName("description") @Expose var description : String,
        @SerializedName("published_at") @Expose var publishedAt : String,
        @SerializedName("last_collected_at") @Expose var lastCollectedAt : String,
        @SerializedName("updated_at") @Expose var updatedAt : String,
        @SerializedName("curated") @Expose var curated : Boolean,
        @SerializedName("featured") @Expose var featured : Boolean,
        @SerializedName("total_photos") @Expose var totalPhotos : Int,
        @SerializedName("private") @Expose var private : Boolean,
        @SerializedName("share_key") @Expose var shareKey : String,
        @SerializedName("tags") @Expose var tags : List<Tags>,
        @SerializedName("links") @Expose var links : Links,
        @SerializedName("user") @Expose var user : User,
        @SerializedName("cover_photo") @Expose var coverPhoto : CoverPhoto,
        @SerializedName("preview_photos") @Expose var previewPhotos : List<PreviewPhotos>

)

data class CoverPhoto (

        @SerializedName("id") @Expose var id : String,
        @SerializedName("created_at") @Expose var createdAt : String,
        @SerializedName("updated_at") @Expose var updatedAt : String,
        @SerializedName("promoted_at") @Expose var promotedAt : String,
        @SerializedName("width") @Expose var width : Int,
        @SerializedName("height") @Expose var height : Int,
        @SerializedName("color") @Expose var color : String,
        @SerializedName("blur_hash") @Expose var blurHash : String,
        @SerializedName("description") @Expose var description : String,
        @SerializedName("alt_description") @Expose var altDescription : String,
        @SerializedName("urls") @Expose var urls : Urls,
        @SerializedName("links") @Expose var links : Links,
        @SerializedName("categories") @Expose var categories : List<String>,
        @SerializedName("likes") @Expose var likes : Int,
        @SerializedName("liked_by_user") @Expose var likedByUser : Boolean,
        @SerializedName("current_user_collections") @Expose var currentUserCollections : List<String>,
        @SerializedName("sponsorship") @Expose var sponsorship : String,
        @SerializedName("user") @Expose var user : User

)