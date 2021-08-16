package com.appbytes.beautywallpaper.repository.collections

import com.appbytes.beautywallpaper.ui.main.collections.detailslist.state.CollectionsDetailsState
import com.appbytes.beautywallpaper.ui.main.collections.list.state.CollectionsState
import com.appbytes.beautywallpaper.util.Constants
import com.appbytes.beautywallpaper.util.DataState
import com.appbytes.beautywallpaper.util.StateEvent
import kotlinx.coroutines.flow.Flow

interface CollectionsRepository {

    fun getCollections(
            pageNumber: Int =1,
            per_page: Int = 11,
            client_id: String = Constants.unsplash_access_key,
            stateEvent: StateEvent
    ) : Flow<DataState<CollectionsState>>


    fun getCollectionsImages (
            collectionsId : String? = null,
            pageNumber: Int =1,
            per_page: Int = 11,
            client_id: String = Constants.unsplash_access_key,
            stateEvent: StateEvent
    ) : Flow<DataState<CollectionsDetailsState>>
}