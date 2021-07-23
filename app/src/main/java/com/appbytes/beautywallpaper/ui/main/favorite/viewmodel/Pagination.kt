package com.appbytes.beautywallpaper.ui.main.favorite.viewmodel

import android.util.Log
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.ui.main.favorite.state.FavoriteStateEvent
import com.appbytes.beautywallpaper.ui.main.home.state.HomeStateEvent
import com.appbytes.beautywallpaper.ui.main.home.viewmodel.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview



@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun FavoriteViewModel.getFromCache() {
    if(!isJobAlreadyActive(FavoriteStateEvent.GetFavoriteEvent())) {
        setStateEvent(FavoriteStateEvent.GetFavoriteEvent())
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun FavoriteViewModel.setUnLike(clickImage : CacheImage) {
    if(!isJobAlreadyActive(FavoriteStateEvent.RemoveFromFavoriteEvent())){
        setStateEvent(FavoriteStateEvent.RemoveFromFavoriteEvent(cacheImage = clickImage))
    }
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun FavoriteViewModel.nextPage(){
    if(!isJobAlreadyActive(FavoriteStateEvent.GetFavoriteEvent())) {
        Log.d(TAG, "FavoiteStateEvent: Attempting to load next page...")
//        incrementPageNumber()
//        Log.d(TAG, "next page set page number " + getCurrentViewStateOrNew().page_number)
        /*setStateEvent(HomeStateEvent.GetNewPhotos(
                page_number = getCurrentViewStateOrNew().imageFields.page_number ?: 1
        ))*/
        setStateEvent(HomeStateEvent.GetNewPhotos())
    }
}
