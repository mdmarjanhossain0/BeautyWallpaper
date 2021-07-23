package com.appbytes.beautywallpaper.repository.favorite

import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.ui.main.favorite.state.FavoriteViewState
import com.appbytes.beautywallpaper.util.DataState
import com.appbytes.beautywallpaper.util.StateEvent
import kotlinx.coroutines.flow.Flow


interface FavoriteRepository {

    fun getFavoriteList(pageNumber: Int, stateEvent: StateEvent) : Flow<DataState<FavoriteViewState>>

    fun removeFromFavoriteList(pageNumber: Int, cacheImage: CacheImage, stateEvent: StateEvent) : Flow<DataState<FavoriteViewState>>
}