package com.appbytes.beautywallpaper.repository.search

import com.appbytes.beautywallpaper.models.CacheKey
import com.appbytes.beautywallpaper.ui.main.search.state.SearchViewState
import com.appbytes.beautywallpaper.util.Constants
import com.appbytes.beautywallpaper.util.DataState
import com.appbytes.beautywallpaper.util.StateEvent
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getSearchPhotos(
            query : String,
            pageNumber: Int =1,
            per_page: Int = 11,
            client_id: String = Constants.unsplash_access_key,
            stateEvent: StateEvent
    ) : Flow<DataState<SearchViewState>>


    fun getSearchKeys(
            stateEvent: StateEvent
    ) : Flow<DataState<SearchViewState>>


    fun deleteKey(stateEvent: StateEvent, key: CacheKey) : Flow<DataState<SearchViewState>>

}