package com.appbytes.beautywallpaper.ui.main.collections.detailslist.state

import android.os.Parcelable
import com.appbytes.beautywallpaper.models.CacheCollections
import com.appbytes.beautywallpaper.models.CacheImage
import kotlinx.android.parcel.Parcelize

const val COLLECTIONS_DETAILS_STATE_BUNDLE_KEY = "com.appbytes.beautywallpaper.ui.main.collections.state.CollectionsViewState.details"

@Parcelize
data class CollectionsDetailsState(
        var collectionsDetailsFields : CollectionsDetailsFields = CollectionsDetailsFields(),
) : Parcelable {

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