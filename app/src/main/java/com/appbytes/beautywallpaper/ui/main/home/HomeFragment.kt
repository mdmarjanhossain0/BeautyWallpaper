package com.appbytes.beautywallpaper.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.api.main.MainApiService
import com.appbytes.beautywallpaper.api.main.response.Image
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.ui.main.home.state.HOME_VIEW_STATE_BUNDLE_KEY
import com.appbytes.beautywallpaper.ui.main.home.state.HomeStateEvent
import com.appbytes.beautywallpaper.ui.main.home.state.HomeViewState
import com.appbytes.beautywallpaper.ui.main.home.viewmodel.cacheData
import com.appbytes.beautywallpaper.ui.main.home.viewmodel.nextPage
import com.appbytes.beautywallpaper.ui.main.home.viewmodel.refreshFromCache
import com.appbytes.beautywallpaper.ui.main.home.viewmodel.setLayoutManagerState
import com.appbytes.beautywallpaper.util.Constants
import com.appbytes.beautywallpaper.util.TopSpacingItemDecoration
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject



@AndroidEntryPoint
class HomeFragment : BaseHomeFragment(R.layout.fragment_home), ImageAdapter.Interaction {

    private val TAG = "HomeFragment"

    @Inject
    lateinit var mainApiService: MainApiService

    private lateinit var recyclerAdapter: ImageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore state after process death

        savedInstanceState?.let { inState ->
            Log.d(TAG, "HomeViewState: inState is NOT null")
            (inState[HOME_VIEW_STATE_BUNDLE_KEY] as HomeViewState?)?.let { viewState ->
                Log.d(TAG, "HomeViewState: restoring view state: ${viewState}")
                Log.d(TAG, "Restore Data " + viewState.imageFields?.images?.size)
                viewModel.setViewState(viewState)
//                viewModel.refreshFromCache()
            }
        }
    }

    /**
     * !IMPORTANT!
     * Must save ViewState b/c in event of process death the LiveData in ViewModel will be lost
     */
    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value
        //clear the list. Don't want to save a large list to bundle.
//        viewState?.imageFields?.images = ArrayList()
        outState.putParcelable(
            HOME_VIEW_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*viewModel.setStateEvent(
                HomeStateEvent.GetNewPhotos()
        )*/
        Log.d(TAG, "ViewModel " + viewModel.toString())
        initRecyclerView()
        subscribeObservers()
//        callApi()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            Log.d(TAG, "Data " + viewState.imageFields?.images?.size)
            viewState.imageFields?.let { it1 -> recyclerAdapter.submitList(it1.images ?: null) }
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

    override fun onResume() {
        super.onResume()
        viewModel.refreshFromCache()
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun saveLayoutManagerState(){
        test_recycler_view.layoutManager?.onSaveInstanceState()?.let { lmState ->
            Log.d(TAG, "saveLayoutManager " + lmState.toString())
            viewModel.setLayoutManagerState(lmState)
        }
    }

    override fun onItemSelected(position: Int, item: CacheImage) {
        findNavController().navigate(R.id.action_homeFragment_to_detailsFragment)
    }


    override fun restoreListPosition() {
        viewModel.viewState.value?.imageFields?.layoutManagerState?.let { lmState ->
            Log.d(TAG, "restoreListPositon " + lmState.toString())
            test_recycler_view?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }


    private fun initRecyclerView(){

        test_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(5)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = ImageAdapter(
                interaction = this@HomeFragment
            )
            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "BlogFragment: attempting to load next page...")
                        viewModel.nextPage()
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }


    override fun onDestroy() {
        super.onDestroy()

//        test_recycler_view.adapter = null
    }

}