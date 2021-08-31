package com.appbytes.beautywallpaper.util

import android.content.Context
import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appbytes.beautywallpaper.util.Constants.Companion.GRID
import com.appbytes.beautywallpaper.util.Constants.Companion.MINIMAL


fun RecyclerView.customLayoutManager(
    land_scape : Int,
    layout : String,
    context: Context) : RecyclerView.LayoutManager{

    var layoutManager : RecyclerView.LayoutManager = GridLayoutManager(context, 1)
    if(land_scape == Configuration.ORIENTATION_LANDSCAPE) {
//        layoutManager = GridLayoutManager(context, 3)
        layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
    }
    else {
        when (layout) {
            GRID -> {
//                layoutManager = GridLayoutManager(context, 2)
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            }
            MINIMAL -> {
//                layoutManager = GridLayoutManager(context, 1)
                layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            }
        }
    }
    return layoutManager
}