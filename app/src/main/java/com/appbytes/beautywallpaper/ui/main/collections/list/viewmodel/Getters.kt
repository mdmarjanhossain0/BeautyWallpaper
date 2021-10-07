package com.appbytes.beautywallpaper.ui.main.collections.list.viewmodel



import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun CollectionsViewModel.getCollectionsPage(): Int{
    return getCurrentViewStateOrNew().collectionsFields.page_number
        ?: return 1
}








