package com.appbytes.beautywallpaper.di

import android.content.Context
import androidx.room.Room
import com.appbytes.beautywallpaper.persistance.WallpaperDatabase
import com.appbytes.beautywallpaper.persistance.main.ImageDao
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
    fun provideBlogDb(@ApplicationContext context: Context): WallpaperDatabase {
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
    fun provideMovieDao(database: WallpaperDatabase): ImageDao {
        return database.iamgeDao()
    }
}