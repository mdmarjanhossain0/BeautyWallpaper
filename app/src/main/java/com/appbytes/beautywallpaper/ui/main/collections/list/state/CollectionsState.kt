package com.appbytes.beautywallpaper.ui.main.collections.list.state

import android.os.Parcelable
import com.appbytes.beautywallpaper.models.CacheCollections
import com.appbytes.beautywallpaper.models.CacheImage
import kotlinx.android.parcel.Parcelize

const val COLLECTIONS_STATE_BUNDLE_KEY = "com.appbytes.beautywallpaper.ui.main.collections.state.CollectionsViewState"

@Parcelize
data class CollectionsState
(
        var collectionsFields : CollectionsFields = CollectionsFields(),
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
}