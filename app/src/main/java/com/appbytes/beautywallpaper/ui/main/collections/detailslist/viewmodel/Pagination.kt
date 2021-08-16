package com.appbytes.beautywallpaper.ui.main.collections.detailslist.viewmodel

import android.util.Log
import com.appbytes.beautywallpaper.ui.main.collections.detailslist.state.CollectionsDetailsEvent
import com.appbytes.beautywallpaper.ui.main.collections.detailslist.viewmodel.CollectionsDetailsViewModel
import com.appbytes.beautywallpaper.ui.main.home.state.HomeStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview



@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
private fun CollectionsDetailsViewModel.incrementCollectionsDetailsPageNumber(){
    val update = getCurrentViewStateOrNew()
    Log.d(TAG, "current page number " + getCurrentViewStateOrNew().collectionsDetailsFields.page_number)
    val page = update.copy().collectionsDetailsFields.page_number ?: 1
    update.collectionsDetailsFields.page_number = page.plus(1)
    setViewState(update)
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsDetailsViewModel.collectionsDetailsNextPage(collectionsId: String){
    if(!isJobAlreadyActive(CollectionsDetailsEvent.GetNewCollectionsDetails())) {
        Log.d(TAG, "CollectionsViewModel: Attempting to load next page...")
//        incrementCollectionsDetailsPageNumber()
        Log.d(TAG, "next page set page number " + getCurrentViewStateOrNew().collectionsDetailsFields.page_number)
        setStateEvent(CollectionsDetailsEvent.GetNewCollectionsDetails(collectionsId = collectionsId))
    }
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsDetailsViewModel.refreshFromCacheCollectionsDetails(collectionsId : String){
    if(!isJobAlreadyActive(CollectionsDetailsEvent.GetNewCollectionsDetails())){
        setStateEvent(CollectionsDetailsEvent.GetNewCollectionsDetails(
                clearLayoutManagerState = false,
                collectionsId = collectionsId
        )
        )
    }
}
