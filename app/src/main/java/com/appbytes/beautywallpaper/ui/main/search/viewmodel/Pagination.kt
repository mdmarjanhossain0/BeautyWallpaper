package com.appbytes.beautywallpaper.ui.main.search.viewmodel

import android.util.Log
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.ui.main.search.state.SearchStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
private fun SearchViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    Log.d(TAG, "current page number " + getCurrentViewStateOrNew().searchFields.page_number)
    val page = update.copy().searchFields.page_number ?: 1
    update.searchFields.page_number = page.plus(1)
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun SearchViewModel.nextPage(query : String){
    if(!isJobAlreadyActive(SearchStateEvent.GetSearchPhotos(query = ""))) {
        Log.d(TAG, "SearchViewModel: Attempting to load next page...")
//        incrementPageNumber()
        Log.d(TAG, "next page set page number " + getCurrentViewStateOrNew().searchFields.page_number)
        /*setStateEvent(HomeStateEvent.GetNewPhotos(
                page_number = getCurrentViewStateOrNew().imageFields.page_number ?: 1
        ))*/
        setStateEvent(SearchStateEvent.GetSearchPhotos(
                query = query,
                clearLayoutManagerState = false))
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun SearchViewModel.refreshFromCache(key : String){
    if(!isJobAlreadyActive(SearchStateEvent.GetSearchPhotos(query = ""))){
        setStateEvent(SearchStateEvent.GetSearchPhotos(
                query = key,
                clearLayoutManagerState = false))
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun SearchViewModel.getSearchKeys(){
    if(!isJobAlreadyActive(SearchStateEvent.GetSearchKeys())){
        setStateEvent(SearchStateEvent.GetSearchKeys())
    }
}

/*@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun SearchViewModel.cacheData(){
    if(!isJobAlreadyActive(SearchStateEvent.GetSearchPhotos())){
        setStateEvent(HomeStateEvent.CacheImageData())
    }
}*/


/*
fun SearchViewModel.setLike(clickImage : CacheImage) {
    if(!isJobAlreadyActive(HomeStateEvent.SetLikeEvent())){
        setStateEvent(HomeStateEvent.SetLikeEvent(clickImage = clickImage))
    }
}*/
