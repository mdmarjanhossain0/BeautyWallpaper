package com.appbytes.beautywallpaper.ui.main.home.viewmodel



import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun HomeViewModel.getPage(): Int{
    return if(calculatePageNumber() < 1) {
        1
    }
    else {
        calculatePageNumber() + 1
    }
}

@ExperimentalCoroutinesApi
fun HomeViewModel.calculatePageNumber() : Int {
    val imageSize = getCurrentViewStateOrNew().imageFields.images?.size ?: return 0
    var pageNumber = imageSize?.div(10)
    return pageNumber?.toInt()!!
}







