package com.appbytes.beautywallpaper.ui.main.search.state

import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.util.Constants
import com.appbytes.beautywallpaper.util.StateEvent


sealed class SearchStateEvent : StateEvent {

    data class GetSearchPhotos(
            val clearLayoutManagerState: Boolean = true,
            val query: String,
            val per_page: Int = Constants.PER_PAGE,
            val page_number: Int = Constants.PAGE,
            val client_id: String = Constants.unsplash_access_key
    ): SearchStateEvent (){
        override fun errorInfo(): String {
            return "No Image Found Photo"
        }
    }

   class GetSearchKeys : SearchStateEvent() {
       override fun errorInfo(): String {
           return "Search Key Not Found"
       }

   }

    class SetLikeEvent (
            val clickImage : CacheImage = CacheImage(
                    id = "none"
            )
    ) : SearchStateEvent() {
        override fun errorInfo(): String {
            return "Cache Not Found"
        }

    }

    class CacheImageData : SearchStateEvent() {
        override fun errorInfo(): String {
            return "Cache Not Found"
        }

    }

    class None : SearchStateEvent() {
        override fun errorInfo(): String {
            return "None"
        }

    }
}