package com.appbytes.beautywallpaper.ui.main.favorite.state

import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.util.StateEvent


sealed class FavoriteStateEvent : StateEvent {

    data class GetFavoriteEvent (
            val pageNumber : Int = 1,
            val clearStateEvent : Boolean = true
    ) : FavoriteStateEvent() {
        override fun errorInfo(): String {
            return "Favorite gating fail"
        }
    }


    data class RemoveFromFavoriteEvent (
            val pageNumber : Int = 1,
            val cacheImage: CacheImage = CacheImage(id = "-1"),
            val clearStateEvent : Boolean = true
    ) : FavoriteStateEvent() {
        override fun errorInfo(): String {
            return "Remove From Favorite fail"
        }
    }
}

