package com.appbytes.beautywallpaper.ui.main.home.state

import android.os.Parcelable
import com.appbytes.beautywallpaper.models.CacheImage
import kotlinx.android.parcel.Parcelize


const val HOME_VIEW_STATE_BUNDLE_KEY = "com.appbytes.beautywallpaper.ui.main.home.state.HomeViewState"

//var images: List<CacheImage>? = null,
//var page_number: Int = 1

@Parcelize
data class HomeViewState
        (
        var imageFields : ImageFields = ImageFields(),

        var detailsImage : Int = 1
        ) : Parcelable {

        @Parcelize
        data class ImageFields(
                var images: List<CacheImage>? = null,
                var searchQuery: String? = null,
                var page_number: Int? = null,
                var isQueryExhausted: Boolean? = null,
                var filter: String? = null,
                var order: String? = null,
                var layoutManagerState: Parcelable? = null
        ) : Parcelable
}