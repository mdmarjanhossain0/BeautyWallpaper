package com.appbytes.beautywallpaper.ui.main.home.viewmodel

import android.util.Log
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.ui.main.home.state.HomeStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview




@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
private fun HomeViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    Log.d(TAG, "current page number " + getCurrentViewStateOrNew().imageFields.page_number)
    val page = update.copy().imageFields.page_number ?: 1
    update.imageFields.page_number = page.plus(1)
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun HomeViewModel.nextPage(){
    if(!isJobAlreadyActive(HomeStateEvent.GetNewPhotos())) {
        Log.d(TAG, "HomeViewModel: Attempting to load next page...")
//        incrementPageNumber()
        Log.d(TAG, "next page set page number " + getCurrentViewStateOrNew().imageFields.page_number)
        /*setStateEvent(HomeStateEvent.GetNewPhotos(
                page_number = getCurrentViewStateOrNew().imageFields.page_number ?: 1
        ))*/
        setStateEvent(HomeStateEvent.GetNewPhotos())
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun HomeViewModel.refreshFromCache(){
    if(!isJobAlreadyActive(HomeStateEvent.GetNewPhotos())){
        setStateEvent(HomeStateEvent.GetNewPhotos(clearLayoutManagerState = false))
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun HomeViewModel.cacheData(){
    if(!isJobAlreadyActive(HomeStateEvent.GetNewPhotos())){
        setStateEvent(HomeStateEvent.CacheImageData())
    }
}


fun HomeViewModel.setLike(clickImage : CacheImage ) {
    if(!isJobAlreadyActive(HomeStateEvent.SetLikeEvent())){
        setStateEvent(HomeStateEvent.SetLikeEvent(clickImage = clickImage))
    }
}