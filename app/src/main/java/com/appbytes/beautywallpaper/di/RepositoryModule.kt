package com.appbytes.beautywallpaper.di

import com.appbytes.beautywallpaper.api.MainApiService
import com.appbytes.beautywallpaper.persistance.CollectionsDao
import com.appbytes.beautywallpaper.persistance.CollectionsImagesDao
import com.appbytes.beautywallpaper.persistance.ImageDao
import com.appbytes.beautywallpaper.persistance.SearchDao
import com.appbytes.beautywallpaper.repository.collections.CollectionsRepository
import com.appbytes.beautywallpaper.repository.collections.CollectionsRepositoryImpl
import com.appbytes.beautywallpaper.repository.favorite.FavoriteRepository
import com.appbytes.beautywallpaper.repository.favorite.FavoriteRepositoryImpl
import com.appbytes.beautywallpaper.repository.home.HomeRepository
import com.appbytes.beautywallpaper.repository.home.HomeRepositoryImpl
import com.appbytes.beautywallpaper.repository.search.SearchRepository
import com.appbytes.beautywallpaper.repository.search.SearchRepositoryImpl
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


    @Singleton
    @Provides
    fun provideCollectionsRepository(
            mainApiService : MainApiService,
            collectionsDao: CollectionsDao,
            collectionsImagesDao: CollectionsImagesDao
    ): CollectionsRepository {
        return CollectionsRepositoryImpl(
                mainApiService = mainApiService,
                collectionsDao = collectionsDao,
                cacheCollectionsImages = collectionsImagesDao
        )
    }


    @Singleton
    @Provides
    fun provideFavoriteRepository(
            cacheDao: ImageDao
    ): FavoriteRepository {
        return FavoriteRepositoryImpl(
                cacheDao = cacheDao
        )
    }

    @Singleton
    @Provides
    fun provideSearchRepository(
            mainApiService: MainApiService,
            searchDao: SearchDao
    ): SearchRepository {
        return SearchRepositoryImpl(
                mainApiService = mainApiService,
                searchDao = searchDao
        )
    }
}