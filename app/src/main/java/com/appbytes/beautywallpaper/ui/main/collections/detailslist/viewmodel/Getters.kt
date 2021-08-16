package com.appbytes.beautywallpaper.ui.main.collections.detailslist.viewmodel



import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsDetailsViewModel.getCollectionsDetailsPage(): Int{
    return getCurrentViewStateOrNew().collectionsDetailsFields.page_number
            ?: return 1
}







