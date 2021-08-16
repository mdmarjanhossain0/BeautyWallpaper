package com.appbytes.beautywallpaper.util

import android.content.Context
import android.content.SharedPreferences
import com.appbytes.beautywallpaper.R


fun getLayout(
    sharedPreferences: SharedPreferences,
    context: Context) : String {
    return sharedPreferences.getString(
        context.getString(R.string.key_layout),
        context.getString(R.string.default_layout)
    )!!
}