package com.appbytes.beautywallpaper.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.api.main.MainApiService
import com.appbytes.beautywallpaper.util.Constants
import com.google.gson.JsonElement
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    @Inject
    lateinit var mainApiService: MainApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    private fun callApi() {
        CoroutineScope(IO).launch {
            val photos = mainApiService.getPhotosById("YFYVI47TgYo", Constants.unsplash_access_key)
            Log.d(TAG,photos.toString())
        }

    }
}