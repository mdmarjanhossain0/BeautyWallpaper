package com.appbytes.beautywallpaper.ui.main.favorite.state

import android.os.Parcelable
import com.appbytes.beautywallpaper.models.CacheImage
import kotlinx.android.parcel.Parcelize

const val FAVORITE_VIEW_STATE_BUNDLE_KEY = "com.appbytes.beautywallpaper.ui.main.favorite.state.FavoriteViewstate"


@Parcelize
data class FavoriteViewState(
        var cacheImageList : List<CacheImage>? = null,
        var layoutManagerState: Parcelable? = null,
) : Parcelable