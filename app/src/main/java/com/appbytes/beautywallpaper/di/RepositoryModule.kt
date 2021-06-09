package com.appbytes.beautywallpaper.di

import com.appbytes.beautywallpaper.api.main.MainApiService
import com.appbytes.beautywallpaper.persistance.main.ImageDao
import com.appbytes.beautywallpaper.repository.main.HomeRepository
import com.appbytes.beautywallpaper.repository.main.HomeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
            mainApiService: MainApiService,
            cacheDao: ImageDao
    ): HomeRepository {
        return HomeRepositoryImpl(
                mainApiService = mainApiService,
                cacheDao = cacheDao
        )
    }
}