package com.appbytes.beautywallpaper.ui.main.download

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.models.DownloadItem
import com.appbytes.beautywallpaper.ui.main.download.viewmodel.DownloadViewModel
import com.appbytes.beautywallpaper.util.TopSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_download.*


@AndroidEntryPoint
class DownloadFragment : Fragment(R.layout.fragment_download), DownloadAdapter.Interaction {

    private val TAG = "DownloadFragment"

    private val viewModel : DownloadViewModel by viewModels()

    private lateinit var adapter: DownloadAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerAdapter()
        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.downloadItems.observe(viewLifecycleOwner, Observer { data ->
            Log.d(TAG, "size of data " + data.size.toString())

            adapter.submitList(data)
        })
    }

    private fun initRecyclerAdapter() {
        adapter = DownloadAdapter(this@DownloadFragment)
        downloade_recycler_view.apply {
            val orientation = getResources().getConfiguration().orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = GridLayoutManager(this@DownloadFragment.context, 2)
            } else {
                layoutManager = GridLayoutManager(this@DownloadFragment.context, 2)
            }
            val topSpacingDecorator = TopSpacingItemDecoration(5)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)
            adapter = this@DownloadFragment.adapter
        }
    }

    override fun onItemSelected(position: Int, item: DownloadItem) {
    }

    override fun restoreListPosition() {
    }

    override fun onDownloadCancel(position: Int, item: DownloadItem) {
    }

    override fun onDeleteClick(position: Int, item: DownloadItem) {
    }

}