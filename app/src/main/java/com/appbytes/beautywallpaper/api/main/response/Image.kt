package com.appbytes.beautywallpaper.api.main.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Image  (

    @SerializedName("id")
    @Expose
    var id : String,
    @SerializedName("created_at")
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



data class Exif (

    @SerializedName("make")
    @Expose var make : String,
    @SerializedName("model")
    @Expose var model : String,
    @SerializedName("exposure_time")
    @Expose var exposureTime : String,
    @SerializedName("aperture")
    @Expose var aperture : String,
    @SerializedName("focal_length")
    @Expose var focalLength : String,
    @SerializedName("iso")
    @Expose var iso : Int

)

data class Location (

    @SerializedName("title")
    @Expose var title : String,
    @SerializedName("name")
    @Expose var name : String,
    @SerializedName("city")
    @Expose var city : String,
    @SerializedName("country")
    @Expose var country : String,

)
