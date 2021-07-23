package com.appbytes.beautywallpaper.ui.main.favorite.viewmodel

import android.os.Parcelable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun FavoriteViewModel.setLayoutManagerState(layoutManagerState: Parcelable){
    val update = getCurrentViewStateOrNew()
    update.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun FavoriteViewModel.clearLayoutManagerState(){
    val update = getCurrentViewStateOrNew()
    update.layoutManagerState = null
    setViewState(update)
}