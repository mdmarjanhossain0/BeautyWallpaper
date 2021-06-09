package com.appbytes.beautywallpaper.ui.main.home.state

import com.appbytes.beautywallpaper.api.main.response.Image
import com.appbytes.beautywallpaper.models.CacheImage


data class HomeViewState (
        var images: List<CacheImage>? = null
        )