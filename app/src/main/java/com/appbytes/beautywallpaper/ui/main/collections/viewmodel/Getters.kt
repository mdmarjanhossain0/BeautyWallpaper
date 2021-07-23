package com.appbytes.beautywallpaper.ui.main.collections.viewmodel



import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.getCollectionsPage(): Int{
    return getCurrentViewStateOrNew().collectionsFields.page_number
        ?: return 1
}


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.getCollectionsDetailsPage(): Int{
    return getCurrentViewStateOrNew().collectionsDetailsFields.page_number
            ?: return 1
}







