package com.appbytes.beautywallpaper.ui.main.search.viewmodel

import com.appbytes.beautywallpaper.ui.main.home.viewmodel.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun SearchViewModel.getPage(): Int{
    return getCurrentViewStateOrNew().searchFields.page_number
            ?: return 1
}