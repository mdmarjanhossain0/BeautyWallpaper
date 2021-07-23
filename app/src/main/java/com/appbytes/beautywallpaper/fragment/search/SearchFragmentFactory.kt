package com.appbytes.beautywallpaper.fragment.search

import androidx.fragment.app.FragmentFactory
import com.appbytes.beautywallpaper.ui.main.search.SearchDetailsFragment
import com.appbytes.beautywallpaper.ui.main.search.SearchHistoryFragment
import com.appbytes.beautywallpaper.ui.main.search.SearchResultFragment


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchFragmentFactory
@Inject
constructor(
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            SearchHistoryFragment::class.java.name -> {
                SearchHistoryFragment()
            }

            SearchResultFragment::class.java.name -> {
                SearchResultFragment()
            }

            SearchDetailsFragment::class.java.name -> {
                SearchDetailsFragment()
            }

            else -> {
                SearchHistoryFragment()
            }
        }


}