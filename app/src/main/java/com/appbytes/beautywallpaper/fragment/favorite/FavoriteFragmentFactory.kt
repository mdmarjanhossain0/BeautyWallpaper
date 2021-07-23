package com.appbytes.beautywallpaper.fragment.favorite

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.appbytes.beautywallpaper.ui.main.favorite.FavoriteDetailsFragment
import com.appbytes.beautywallpaper.ui.main.favorite.FavoriteFragment
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FavoriteFragmentFactory
@Inject
constructor(
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            FavoriteFragment::class.java.name -> {
                FavoriteFragment()
            }

            FavoriteDetailsFragment::class.java.name -> {
                FavoriteDetailsFragment()
            }

            else -> {
                FavoriteFragment()
            }
        }


}