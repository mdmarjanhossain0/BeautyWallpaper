package com.appbytes.beautywallpaper.ui.main.home.state

import com.appbytes.beautywallpaper.util.Constants
import com.appbytes.beautywallpaper.util.StateEvent

sealed class HomeStateEvent : StateEvent {

    data class GetNewPhotos(
        val clearLayoutManagerState: Boolean = true,
        val per_page: Int = Constants.PER_PAGE,
        val page_number: Int = Constants.PAGE,
        val client_id: String = Constants.unsplash_access_key
    ): HomeStateEvent (){
        override fun errorInfo(): String {
            return "Not Found Photo"
        }
    }

    class CacheData : HomeStateEvent() {
        override fun errorInfo(): String {
            return "Cache Not Found"
        }

    }

    class None : HomeStateEvent() {
        override fun errorInfo(): String {
            return "None"
        }

    }
}