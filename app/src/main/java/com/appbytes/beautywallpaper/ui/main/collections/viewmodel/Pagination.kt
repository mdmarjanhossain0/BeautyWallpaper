package com.appbytes.beautywallpaper.ui.main.collections.viewmodel

import android.util.Log
import com.appbytes.beautywallpaper.ui.main.collections.state.CollectionsStateEvent
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
private fun CollectionsViewModel.incrementCollectionsDetailsPageNumber(){
    val update = getCurrentViewStateOrNew()
    Log.d(TAG, "current page number " + getCurrentViewStateOrNew().collectionsDetailsFields.page_number)
    val page = update.copy().collectionsFields.page_number ?: 1
    update.collectionsFields.page_number = page.plus(1)
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.collectionsNextPage(){
    if(!isJobAlreadyActive(CollectionsStateEvent.GetNewCollections())) {
        Log.d(TAG, "CollectionsViewModel: Attempting to load next page...")
//        incrementCollectionsPageNumber()
        Log.d(TAG, "next page set page number " + getCurrentViewStateOrNew().collectionsFields.page_number)
        setStateEvent(CollectionsStateEvent.GetNewCollections())
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.collectionsDetailsNextPage(collectionsId: String){
    if(!isJobAlreadyActive(CollectionsStateEvent.GetNewCollectionsDetails())) {
        Log.d(TAG, "CollectionsViewModel: Attempting to load next page...")
//        incrementCollectionsDetailsPageNumber()
        Log.d(TAG, "next page set page number " + getCurrentViewStateOrNew().collectionsDetailsFields.page_number)
        setStateEvent(CollectionsStateEvent.GetNewCollectionsDetails(collectionsId = collectionsId))
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.refreshFromCacheCollections(){
    if(!isJobAlreadyActive(CollectionsStateEvent.GetNewCollections())){
        setStateEvent(CollectionsStateEvent.GetNewCollections(clearLayoutManagerState = false))
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.refreshFromCacheCollectionsDetails(collectionsId : String){
    if(!isJobAlreadyActive(CollectionsStateEvent.GetNewCollectionsDetails())){
        setStateEvent(CollectionsStateEvent.GetNewCollectionsDetails(
                clearLayoutManagerState = false,
                collectionsId = collectionsId
        )
        )
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.cacheData(){
    if(!isJobAlreadyActive(HomeStateEvent.GetNewPhotos())){
        setStateEvent(CollectionsStateEvent.CacheCollections())
    }
}