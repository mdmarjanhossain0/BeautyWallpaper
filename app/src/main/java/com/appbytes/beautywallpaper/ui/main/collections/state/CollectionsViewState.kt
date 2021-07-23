package com.appbytes.beautywallpaper.ui.main.collections.state

import android.os.Parcelable
import com.appbytes.beautywallpaper.models.CacheCollections
import com.appbytes.beautywallpaper.models.CacheImage
import kotlinx.android.parcel.Parcelize

const val COLLECTIONS_VIEW_STATE_BUNDLE_KEY = "com.appbytes.beautywallpaper.ui.main.collections.state.CollectionsViewState"
const val COLLECTIONS_DETAILS_VIEW_STATE_BUNDLE_KEY = "com.appbytes.beautywallpaper.ui.main.collections.state.CollectionsViewState.details"

//var images: List<CacheImage>? = null,
//var page_number: Int = 1

@Parcelize
data class CollectionsViewState
(
        var collectionsFields : CollectionsFields = CollectionsFields(),

        var collectionsDetailsFields : CollectionsDetailsFields = CollectionsDetailsFields(),

        var detailsImage : Int = 1
) : Parcelable {

    @Parcelize
    data class CollectionsFields(
            var collections: List<CacheCollections>? = null,
            var searchQuery: String? = null,
            var page_number: Int? = null,
            var isQueryExhausted: Boolean? = null,
            var filter: String? = null,
            var order: String? = null,
            var layoutManagerState: Parcelable? = null
    ) : Parcelable


    @Parcelize
    data class CollectionsDetailsFields(
            var collectionsImages: List<CacheImage>? = null,
            var searchQuery: String? = null,
            var page_number: Int? = null,
            var isQueryExhausted: Boolean? = null,
            var filter: String? = null,
            var order: String? = null,
            var layoutManagerState: Parcelable? = null
    ) : Parcelable
}