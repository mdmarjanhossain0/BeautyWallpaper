package com.appbytes.beautywallpaper.ui.main.collections.detailslist.viewmodel



import com.appbytes.beautywallpaper.ui.main.home.viewmodel.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsDetailsViewModel.getCollectionsDetailsPage() : Int{
    return if(calculatePageNumber() < 1) {
        1
    }
    else {
        calculatePageNumber() + 1
    }
}

@ExperimentalCoroutinesApi
fun CollectionsDetailsViewModel.calculatePageNumber() : Int {
    val imageSize = getCurrentViewStateOrNew().collectionsDetailsFields.collectionsImages?.size ?: return 0
    var pageNumber = imageSize?.div(10)
    return pageNumber?.toInt()!!
}







