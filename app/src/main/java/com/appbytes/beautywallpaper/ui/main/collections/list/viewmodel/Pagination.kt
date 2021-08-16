package com.appbytes.beautywallpaper.ui.main.collections.list.viewmodel

import android.util.Log
import com.appbytes.beautywallpaper.ui.main.collections.list.state.CollectionsEvent
import com.appbytes.beautywallpaper.ui.main.home.state.HomeStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview




@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
private fun CollectionsViewModel.incrementCollectionsPageNumber(){
    val update = getCurrentViewStateOrNew()
    Log.d(TAG, "current page number " + getCurrentViewStateOrNew().collectionsFields.page_number)
    val page = update.copy().collectionsFields.page_number ?: 1
    update.collectionsFields.page_number = page.plus(1)
    setViewState(update)
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.collectionsNextPage(){
    if(!isJobAlreadyActive(CollectionsEvent.GetNewCollections())) {
        Log.d(TAG, "CollectionsViewModel: Attempting to load next page...")
//        incrementCollectionsPageNumber()
        Log.d(TAG, "next page set page number " + getCurrentViewStateOrNew().collectionsFields.page_number)
        setStateEvent(CollectionsEvent.GetNewCollections())
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.refreshFromCacheCollections(){
    if(!isJobAlreadyActive(CollectionsEvent.GetNewCollections())){
        setStateEvent(CollectionsEvent.GetNewCollections(clearLayoutManagerState = false))
    }
}


/*
@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.cacheData(){
    if(!isJobAlreadyActive(HomeStateEvent.GetNewPhotos())){
        setStateEvent(CollectionsEvent.CacheCollections())
    }
}*/
