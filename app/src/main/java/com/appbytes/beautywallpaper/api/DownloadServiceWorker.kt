package com.appbytes.beautywallpaper.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url


interface DownloadServiceWorker {

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl: String): ResponseBody

    @GET("photos/{id}/download")
    suspend fun trackDownload(@Path("id") id: String): ResponseBody
}