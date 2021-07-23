package com.appbytes.beautywallpaper.di

import android.content.Context
import androidx.room.Room
import com.appbytes.beautywallpaper.persistance.WallpaperDatabase
import com.appbytes.beautywallpaper.persistance.CollectionsDao
import com.appbytes.beautywallpaper.persistance.CollectionsImagesDao
import com.appbytes.beautywallpaper.persistance.ImageDao
import com.appbytes.beautywallpaper.persistance.SearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideWallpaperDb(@ApplicationContext context: Context): WallpaperDatabase {
        return Room
                .databaseBuilder(
                        context,
                        WallpaperDatabase::class.java,
                        WallpaperDatabase.DATABASE_NAME
                        )
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideImageDao(database: WallpaperDatabase): ImageDao {
        return database.iamgeDao()
    }


    @Singleton
    @Provides
    fun provideCollectionsDao(database: WallpaperDatabase): CollectionsDao {
        return database.collectionsDao()
    }


    @Singleton
    @Provides
    fun provideCollectionsImagesDao(database: WallpaperDatabase): CollectionsImagesDao {
        return database.collectionsImagesDao()
    }

    @Singleton
    @Provides
    fun provideSearchDao(database: WallpaperDatabase): SearchDao {
        return database.searchDao()
    }
}