package com.appbytes.beautywallpaper.ui.main.collections.state

import com.appbytes.beautywallpaper.util.Constants
import com.appbytes.beautywallpaper.util.StateEvent

sealed class CollectionsStateEvent : StateEvent {

    data class GetNewCollections(
            val clearLayoutManagerState: Boolean = true,
            val per_page: Int = Constants.PER_PAGE,
            val page_number: Int = Constants.PAGE,
            val client_id: String = Constants.unsplash_access_key
    ): CollectionsStateEvent (){
        override fun errorInfo(): String {
            return "Not Found Collections"
        }
    }


    data class GetNewCollectionsDetails(
            val clearLayoutManagerState: Boolean = true,
            val collectionsId : String = "1",
            val per_page: Int = Constants.PER_PAGE,
            val page_number: Int = Constants.PAGE,
            val client_id: String = Constants.unsplash_access_key
    ): CollectionsStateEvent (){
        override fun errorInfo(): String {
            return "Not Found CollectionsPhoto"
        }
    }

    class CacheCollections : CollectionsStateEvent() {
        override fun errorInfo(): String {
            return "Collections Cache Not Found"
        }

    }

    class None : CollectionsStateEvent() {
        override fun errorInfo(): String {
            return "None"
        }

    }
}