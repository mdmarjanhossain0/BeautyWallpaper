package com.appbytes.beautywallpaper.ui.main.collections.list.state

import com.appbytes.beautywallpaper.util.Constants
import com.appbytes.beautywallpaper.util.StateEvent

sealed class CollectionsEvent : StateEvent {

    data class GetNewCollections(
            val clearLayoutManagerState: Boolean = true,
            val per_page: Int = Constants.PER_PAGE,
            val page_number: Int = Constants.PAGE,
            val client_id: String = Constants.unsplash_access_key
    ): CollectionsEvent (){
        override fun errorInfo(): String {
            return "Not Found Collections"
        }
    }
}