package com.appbytes.beautywallpaper.ui.main.home.state

import android.os.Parcelable
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.util.Queue
import com.appbytes.beautywallpaper.util.StateMessage


data class HomeState(
        val isLoading: Boolean = false,
        val blogList: List<CacheImage> = listOf(),
        val query: String = "",
        val page: Int = 1,
        val isQueryExhausted: Boolean = false, // no more results available, prevent next page
        var filter: String? = null,
        var order: String? = null,
        val queue: Queue<StateMessage> = Queue(mutableListOf())
)


/*
var images: List<CacheImage>? = null,
var searchQuery: String? = null,
var page_number: Int? = null,
var isQueryExhausted: Boolean? = null,
var filter: String? = null,
var order: String? = null,
var layoutManagerState: Parcelable? = null*/
