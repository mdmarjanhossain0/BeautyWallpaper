package com.appbytes.beautywallpaper.ui.details.state

import com.appbytes.beautywallpaper.util.StateEvent

sealed class DetailsEvent : StateEvent {

    data class GetDetailsImage(
        var imageId : String = "",
    ) : DetailsEvent() {
        override fun errorInfo(): String {
            return "Failed collect Details Image"
        }

    }
}