package com.appbytes.beautywallpaper.ui.main.collections.viewmodel

import android.os.Parcelable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.setLayoutManagerState(layoutManagerState: Parcelable){
    val update = getCurrentViewStateOrNew()
    update.collectionsFields.layoutManagerState = layoutManagerState
    setViewState(update)
}





@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.clearLayoutManagerState(){
    val update = getCurrentViewStateOrNew()
    update.collectionsFields.layoutManagerState = null
    setViewState(update)
}