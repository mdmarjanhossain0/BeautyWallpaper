package com.appbytes.beautywallpaper.ui.main.home.viewmodel

import android.os.Parcelable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun HomeViewModel.setLayoutManagerState(layoutManagerState: Parcelable){
    val update = getCurrentViewStateOrNew()
    update.imageFields.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun HomeViewModel.clearLayoutManagerState(){
    val update = getCurrentViewStateOrNew()
    update.imageFields.layoutManagerState = null
    setViewState(update)
}