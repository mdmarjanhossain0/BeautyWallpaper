package com.appbytes.beautywallpaper.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.appbytes.beautywallpaper.R



private const val LIGHT = "light"
private const val DARK = "dark"


object ThemeHelper {
    @Suppress("MemberVisibilityCanBePrivate")
    fun getCurrentTheme(context: Context): Int {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        return when (sp.getString(
            context.getString(R.string.key_theme),
            context.getString(R.string.default_theme)
        )) {
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }

    fun switchTheme(context: Context) {
        AppCompatDelegate.setDefaultNightMode(getCurrentTheme(context))
    }

    fun isDark(): Boolean {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }
}