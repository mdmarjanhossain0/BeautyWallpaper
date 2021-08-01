package com.appbytes.beautywallpaper

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
    companion object {
        lateinit var instance: BaseApplication
            private set
    }

    override fun onCreate() {
//        ThemeHelper.switchTheme(this)

        super.onCreate()
        instance = this
//        Pasteur.init(BuildConfig.DEBUG)

        /*val config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, OkHttpClientAPI.createClient()).build()*/
        Fresco.initialize(this)
    }
}