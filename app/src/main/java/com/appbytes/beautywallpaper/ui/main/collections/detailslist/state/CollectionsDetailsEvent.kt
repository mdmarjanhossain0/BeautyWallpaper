package com.appbytes.beautywallpaper.ui.main.collections.detailslist.state

import com.appbytes.beautywallpaper.util.Constants
import com.appbytes.beautywallpaper.util.StateEvent

sealed class CollectionsDetailsEvent : StateEvent {

    data class GetNewCollectionsDetails(
            val clearLayoutManagerState: Boolean = true,
            val collectionsId : String = "1",
            val per_page: Int = Constants.PER_PAGE,
            val page_number: Int = Constants.PAGE,
            val client_id: String = Constants.unsplash_access_key
    ): CollectionsDetailsEvent (){
        override fun errorInfo(): String {
            return "Not Found CollectionsPhoto"
        }
    }
}