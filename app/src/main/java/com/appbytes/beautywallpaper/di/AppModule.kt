package com.appbytes.beautywallpaper.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.appbytes.beautywallpaper.api.IOService
import com.appbytes.beautywallpaper.api.MainApiService
import com.appbytes.beautywallpaper.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.BASE_URL)
    }

    @Singleton
    @Provides
    fun provideApiService(builder: Retrofit.Builder): MainApiService {
        return builder
                .build()
                .create(MainApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideIOService(builder: Retrofit.Builder): IOService {
        return builder
                .build()
                .create(IOService::class.java)
    }


    @Singleton
    @Provides
    fun provideSharePreference(@ApplicationContext context: Context) : SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}