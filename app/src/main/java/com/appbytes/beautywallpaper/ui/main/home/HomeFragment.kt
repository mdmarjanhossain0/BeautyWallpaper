package com.appbytes.beautywallpaper.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.api.main.MainApiService
import com.appbytes.beautywallpaper.api.main.response.Image
import com.appbytes.beautywallpaper.ui.main.home.state.HomeStateEvent
import com.appbytes.beautywallpaper.util.Constants
import com.appbytes.beautywallpaper.util.TopSpacingItemDecoration
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val TAG = "HomeFragment"

    @Inject
    lateinit var mainApiService: MainApiService

    private lateinit var recyclerAdapter: ImageAdapter

    private val viewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setStateEvent(
                HomeStateEvent.GetNewPhotos()
        )
        Log.d(TAG, "ViewModel " + viewModel.toString())
        initRecyclerView()
        subscribeObservers()
//        callApi()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "Data " + it.toString())
            it.images?.let { it1 -> recyclerAdapter.submitList(it1) }
        })
    }

    private fun callApi() {
        var photo: List<Image>?=null
        CoroutineScope(IO).launch {
             photo = mainApiService.getNewPhotos(1,11,Constants.unsplash_access_key)
            Log.d(TAG,photo.toString())

            MainScope().launch {
                activity?.let {
                    Glide.with(it)
                        .load(photo?.get(0)?.urls?.regular)
                        .error(R.drawable.ic_user)
//                        .into(ivRandom)
                }
            }
        }

    }


    private fun initRecyclerView(){

        test_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            val topSpacingDecorator = TopSpacingItemDecoration(5)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = ImageAdapter(
            )
            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "BlogFragment: attempting to load next page...")
//                        viewModel.nextPage()
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }
}