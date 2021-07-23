package com.appbytes.beautywallpaper.ui.main.search.viewmodel

import android.os.Parcelable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun SearchViewModel.setLayoutManagerState(layoutManagerState: Parcelable){
    val update = getCurrentViewStateOrNew()
    update.searchFields.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun SearchViewModel.clearLayoutManagerState(){
    val update = getCurrentViewStateOrNew()
    update.searchFields.layoutManagerState = null
    setViewState(update)
}