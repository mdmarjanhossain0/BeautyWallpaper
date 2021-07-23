package com.appbytes.beautywallpaper.api.main.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class SearchImage  (

        @SerializedName("id")
        @Expose
        var id : String,
        @SerializedName("created_at")

        var query : String = "",

        @Expose
        var createdAt : String,
        @SerializedName("updated_at")
        @Expose
        var updatedAt : String,
        @SerializedName("promoted_at")
        @Expose
        var promotedAt : String,
        @SerializedName("width")
        @Expose
        var width : Int,
        @SerializedName("height")
        @Expose
        var height : Int,
        @SerializedName("color")
        @Expose
        var color : String,
        @SerializedName("blur_hash")
        @Expose
        var blurHash : String,
        @SerializedName("description")
        @Expose
        var description : String,
        @SerializedName("alt_description")
        @Expose
        var altDescription : String,
        @SerializedName("urls")
        @Expose
        var urls : Urls,
        @SerializedName("links")
        @Expose
        var links : Links,
        @SerializedName("categories")
        @Expose
        var categories : List<String>,
        @SerializedName("likes")
        @Expose
        var likes : Int,
        @SerializedName("liked_by_user")
        @Expose
        var likedByUser : Boolean,
        @SerializedName("current_user_collections")
        @Expose
        var currentUserCollections : List<String>,

        @SerializedName("sponsorship")
        var sponsorship : String,
        @SerializedName("user")
        @Expose
        var user : User,
        @SerializedName("exif")
        @Expose
        var exif : Exif,
        @SerializedName("location")
        @Expose
        var location : Location,
        @SerializedName("views")
        @Expose
        var views : Int,
        @SerializedName("downloads")
        @Expose
        var downloads : Int

)