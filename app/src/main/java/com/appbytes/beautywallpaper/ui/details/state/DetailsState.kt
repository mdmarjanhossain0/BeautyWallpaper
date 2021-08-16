package com.appbytes.beautywallpaper.ui.details.state

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


const val DETAILS_STATE_BUNDLE_KEY = "com.appbytes.beautywallpaper.ui.details.state.DetailsState"


@Parcelize
data class DetailsState (
    var image : DetailsImageField = DetailsImageField()
        ) : Parcelable {

    @Parcelize
    data class DetailsImageField (
        var imageId : String = "",

    ) : Parcelable


        }