package com.appbytes.beautywallpaper.ui.main.collections.list.viewmodel



import com.appbytes.beautywallpaper.ui.main.home.viewmodel.HomeViewModel
import com.appbytes.beautywallpaper.ui.main.home.viewmodel.calculatePageNumber
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.getCollectionsPage() : Int{
    return if(calculatePageNumber() < 1) {
        1
    }
    else {
        calculatePageNumber() + 1
    }
}

@ExperimentalCoroutinesApi
fun CollectionsViewModel.calculatePageNumber() : Int {
    val imageSize = getCurrentViewStateOrNew().collectionsFields.collections?.size ?: return 0
    var pageNumber = imageSize?.div(10)
    return pageNumber?.toInt()!!
}








