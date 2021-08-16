package com.appbytes.beautywallpaper.api

import com.appbytes.beautywallpaper.api.response.*
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MainApiService {


    @GET("/photos")
   suspend fun getNewPhotos (
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("client_id") client_id: String?
    ): List<Image>


  @GET("/search/photos")
  suspend fun getSearch(
          @Query("query") query: String?,
          @Query("page") page: Int?,
          @Query("per_page") perPage: Int?,
          @Query("client_id") client_id: String?
  ) : SearchResponse

    @GET("photos/random")
    suspend fun getRandom(
            @Query("count") count: String?,
            @Query("client_id") client_id: String?
    ): List<Image>


    @GET("/collections")
    suspend fun getCollections (
            @Query("page") page: Int?,
            @Query("per_page") per_page: Int?,
            @Query("client_id") client_id: String? /*, @Query("order_by") String orderBy*/
    ): List<Collections>

    @GET("collections/{id}/photos")
    suspend fun getCollectionsById (
            @Path("id") id: String?,
            @Query("page") page: Int?,
            @Query("per_page") per_page: Int?,
            @Query("client_id") client_id: String? /*, @Query("order_by") String orderBy*/
    ): List<Image>

    @GET("photos/{id}")
    suspend fun getPhotosById(
            @Path("id") id: String?,
            @Query("client_id") client_id: String? /*, @Query("order_by") String orderBy*/
    ): Image


    @GET("photos/{id}/download")
    suspend fun getDownloadPhoto(
            @Path("id") id: String?,
            @Query("client_id") client_id: String?
    ): Download


    @GET("collections/{id}/photos")
    suspend fun getNewPhotos(
        @Path("id") id: String?,
        @Query("client_id") client_id: String? /*, @Query("order_by") String orderBy*/
    ): List<Image>

    @GET("collections")
    suspend fun getExxplore(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("client_id") client_id: String? /*, @Query("order_by") String orderBy*/
    ): Call<JsonElement?>?

    @GET("collections")
    suspend fun getTrending(@Query("client_id") client_id: String?): Call<JsonElement?>?



    @GET("collections/{id}")
    suspend fun getExploreCat(
        @Path("id") id: String?,
        @Query("client_id") client_id: String?
    ): Call<JsonElement?>?





    @GET("users/{username}")
    suspend fun getPortfolio(
        @Path("username") users: String?,
        @Query("client_id") client_id: String?
    ): Call<JsonElement?>?
}



// https://api.unsplash.com/photos/random?count=5&client_id=b723d80217a350112f8754ec0e380fe9f22437db368c41573f695ace10ce031a

// https://api.unsplash.com/photos/random?count=5&client_id=b723d80217a350112f8754ec0e380fe9f22437db368c41573f695ace10ce031a