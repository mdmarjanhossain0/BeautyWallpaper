package com.appbytes.beautywallpaper.repository.favorite

import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.persistance.ImageDao
import com.appbytes.beautywallpaper.ui.main.favorite.state.FavoriteViewState
import com.appbytes.beautywallpaper.util.DataState
import com.appbytes.beautywallpaper.util.StateEvent
import kotlinx.coroutines.flow.flow


class FavoriteRepositoryImpl(
        private val cacheDao : ImageDao
) : FavoriteRepository {
    override fun getFavoriteList(pageNumber: Int, stateEvent: StateEvent) = flow {
        val updateResult = cacheDao.getFavoriteImages()
        emit(
                DataState.data(
                        response = null,
                        data = FavoriteViewState(
                                cacheImageList = updateResult
                        ),
                        stateEvent = stateEvent
                )
        )
    }

    override fun removeFromFavoriteList(
            pageNumber: Int,
            cacheImage: CacheImage,
            stateEvent: StateEvent) = flow{
        cacheDao.insert(cacheImage)
        val updateResult = cacheDao.getFavoriteImages()
        emit(
                DataState.data(
                        response = null,
                        data = FavoriteViewState(
                                cacheImageList = updateResult
                        ),
                        stateEvent = stateEvent
                )
        )
    }

}