package com.appbytes.beautywallpaper.ui.main.search.state

import android.os.Parcelable
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.models.CacheKey
import kotlinx.android.parcel.Parcelize


const val SEARCH_HISTORY_VIEW_STATE_BUNDLE_KEY = "com.appbytes.beautywallpaper.ui.main.search.state.SearchViewState.Key"

const val SEARCH_RESULT_VIEW_STATE_BUNDLE_KEY = "com.appbytes.beautywallpaper.ui.main.search.state.SearchViewState.Result"

@Parcelize
data class SearchViewState
(
        var searchFields : SearchFields = SearchFields(),

        var searchKeys: SearchKeys = SearchKeys(),

        var detailsImage : Int = 1
) : Parcelable {

    @Parcelize
    data class SearchFields (
            var images: List<CacheImage>? = null,
            var searchQuery: String? = null,
            var page_number: Int? = null,
            var isQueryExhausted: Boolean? = null,
            var filter: String? = null,
            var order: String? = null,
            var layoutManagerState: Parcelable? = null
    ) : Parcelable


    @Parcelize
    data class SearchKeys (
        var keys: List<CacheKey>? = null,
        var searchQuery: String? = null,
        var page_number: Int? = null,
        var isQueryExhausted: Boolean? = null,
        var filter: String? = null,
        var order: String? = null,
        var layoutManagerState: Parcelable? = null
    ) : Parcelable
}