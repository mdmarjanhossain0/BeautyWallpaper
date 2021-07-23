package com.appbytes.beautywallpaper.fragment.home

import androidx.fragment.app.FragmentFactory
import com.appbytes.beautywallpaper.ui.main.home.HomeDetailsFragment
import com.appbytes.beautywallpaper.ui.main.home.HomeFragment


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeFragmentFactory
@Inject
constructor(
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            HomeFragment::class.java.name -> {
                HomeFragment()
            }

            HomeDetailsFragment::class.java.name -> {
                HomeDetailsFragment()
            }

            else -> {
                HomeFragment()
            }
        }


}