package com.appbytes.beautywallpaper.ui.main.collections.detailslist.viewmodel

import android.os.Parcelable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsDetailsViewModel.setLayoutManagerState(layoutManagerState: Parcelable){
    val update = getCurrentViewStateOrNew()
    update.collectionsDetailsFields.layoutManagerState = layoutManagerState
    setViewState(update)
}





@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsDetailsViewModel.clearLayoutManagerState(){
    val update = getCurrentViewStateOrNew()
    update.collectionsDetailsFields.layoutManagerState = null
    setViewState(update)
}