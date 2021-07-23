package com.appbytes.beautywallpaper.fragment.collections

import androidx.fragment.app.FragmentFactory
import com.appbytes.beautywallpaper.ui.main.collections.CollectionsDetailsFragment
import com.appbytes.beautywallpaper.ui.main.collections.CollectionsFragment
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CollectionsFragmentFactory
@Inject
constructor(
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            CollectionsFragment::class.java.name -> {
                CollectionsFragment()
            }
            CollectionsDetailsFragment::class.java.name -> {
                CollectionsDetailsFragment()
            }

            else -> {
                CollectionsFragment()
            }
        }


}