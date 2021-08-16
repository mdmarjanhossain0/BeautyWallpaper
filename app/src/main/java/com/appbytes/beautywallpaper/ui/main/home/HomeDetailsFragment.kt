package com.appbytes.beautywallpaper.ui.main.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.ui.main.MainActivity
import com.appbytes.beautywallpaper.util.Response
import com.appbytes.beautywallpaper.util.StateMessageCallback
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_home_details.*
import kotlinx.android.synthetic.main.fragment_home_details.view.*


class HomeDetailsFragment : BaseHomeFragment(R.layout.fragment_home_details) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiCommunicationListener.expandAppBar()
    }
    override fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {

    }

    override fun displayProgressBar(isLoading: Boolean) {
    }

    override fun expandAppBar() {
        (activity as MainActivity).findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }

    override fun hideSoftKeyboard() {
    }

    override fun isStoragePermissionGranted(): Boolean {
        return true
    }
}