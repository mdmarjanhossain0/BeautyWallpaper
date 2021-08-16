package com.appbytes.beautywallpaper.fragment.download

import androidx.fragment.app.FragmentFactory
import com.appbytes.beautywallpaper.ui.main.download.DownloadDetailsFragment
import com.appbytes.beautywallpaper.ui.main.download.DownloadFragment
import com.appbytes.beautywallpaper.ui.main.home.HomeDetailsFragment
import com.appbytes.beautywallpaper.ui.main.home.HomeFragment


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadFragmentFactory
@Inject
constructor(
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            DownloadFragment::class.java.name -> {
                DownloadFragment()
            }

            DownloadDetailsFragment::class.java.name -> {
                DownloadDetailsFragment()
            }

            else -> {
                DownloadFragment()
            }
        }


}