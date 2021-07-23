package com.appbytes.beautywallpaper.ui.main.home.viewmodel



import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun HomeViewModel.getPage(): Int{
    return getCurrentViewStateOrNew().imageFields.page_number
        ?: return 1
}







