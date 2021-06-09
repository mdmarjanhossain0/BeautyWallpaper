package com.appbytes.beautywallpaper.repository.main

import com.appbytes.beautywallpaper.ui.main.home.state.HomeViewState
import com.appbytes.beautywallpaper.util.Constants
import com.appbytes.beautywallpaper.util.DataState
import com.appbytes.beautywallpaper.util.StateEvent
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun getPhotos(
            pageNumber: Int =1,
            per_page: Int = 11,
            client_id: String = Constants.unsplash_access_key,
            stateEvent: StateEvent
    ) : Flow<DataState<HomeViewState>>
}