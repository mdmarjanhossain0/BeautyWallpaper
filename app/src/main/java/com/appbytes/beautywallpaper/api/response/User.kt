package com.appbytes.beautywallpaper.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(

        @SerializedName("id")
        @Expose
        var id : String,
        @SerializedName("updated_at")
        @Expose
        var updatedAt : String,
        @SerializedName("username")
        @Expose
        var username : String,
        @SerializedName("name")
        @Expose
        var name : String,
        @SerializedName("first_name")
        @Expose
        var firstName : String,
        @SerializedName("last_name")
        @Expose
        var lastName : String,
        @SerializedName("twitter_username")
        @Expose
        var twitterUsername : String,
        @SerializedName("portfolio_url")
        @Expose
        var portfolioUrl : String,
        @SerializedName("bio")
        @Expose
        var bio : String,
        @SerializedName("location")
        @Expose
        var location : String,
        @SerializedName("profile_image")
        @Expose
        var profileImage : ProfileImage,
        @SerializedName("instagram_username")
        @Expose
        var instagramUsername : String,
        @SerializedName("total_collections")
        @Expose
        var totalCollections : Int,
        @SerializedName("total_likes")
        @Expose
        var totalLikes : Int,
        @SerializedName("total_photos")
        @Expose
        var totalPhotos : Int,
        @SerializedName("accepted_tos")
        @Expose
        var acceptedTos : Boolean,
        @SerializedName("for_hire")
        @Expose
        var forHire : Boolean

)