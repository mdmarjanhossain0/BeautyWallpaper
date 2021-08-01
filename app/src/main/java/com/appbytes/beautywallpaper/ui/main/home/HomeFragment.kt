package com.appbytes.beautywallpaper.ui.main.home

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.persistance.DownloadItemDao
import com.appbytes.beautywallpaper.persistance.ImageDao
import com.appbytes.beautywallpaper.ui.main.MainActivity
import com.appbytes.beautywallpaper.ui.main.home.state.HOME_VIEW_STATE_BUNDLE_KEY
import com.appbytes.beautywallpaper.ui.main.home.state.HomeViewState
import com.appbytes.beautywallpaper.ui.main.home.viewmodel.*
import com.appbytes.beautywallpaper.util.ErrorHandling.Companion.isPaginationDone
import com.appbytes.beautywallpaper.util.Response
import com.appbytes.beautywallpaper.util.StateMessageCallback
import com.appbytes.beautywallpaper.util.TopSpacingItemDecoration
import com.appbytes.beautywallpaper.util.download.DownloadUtils
import com.appbytes.beautywallpaper.util.download.PermissionUtils
import com.appbytes.beautywallpaper.util.download.Toaster
import com.appbytes.beautywallpaper.worker.DownloadService
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.SkeletonLayout
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.detail_no_item.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseHomeFragment(R.layout.fragment_home), ImageAdapter.Interaction {

    private val TAG = "HomeFragment"

    private lateinit var recyclerAdapter: ImageAdapter


    private lateinit var searchView: SearchView

    private lateinit var skeleton : Skeleton

    private var isSizeZero : Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore state after process death

        savedInstanceState?.let { inState ->
            Log.d(TAG, "HomeViewState: inState is NOT null")
            (inState[HOME_VIEW_STATE_BUNDLE_KEY] as HomeViewState?)?.let { viewState ->
                Log.d(TAG, "HomeViewState: restoring view state: ${viewState}")
                Log.d(TAG, "Restore Data " + viewState.imageFields?.images?.size)
                viewModel.setViewState(viewState)
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value
        outState.putParcelable(
            HOME_VIEW_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        Log.d(TAG, "ViewModel " + viewModel.toString())
        initRecyclerView()
        initSkeleton()
        subscribeObservers()
        no_details_retry.setOnClickListener{
            viewModel.cancelActiveJobs()
            viewModel.clearStateMessage()
            viewModel.refreshFromCache()
        }
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            Log.d(TAG, "Data " + viewState.imageFields?.images?.size)
            val images = viewState.imageFields.images
            if(images?.size != 0 ) {
                isSizeZero = false
                no_details.visibility = View.GONE
                recyclerAdapter.submitList(images!!)
            }
            else {
                isSizeZero = true
            }
        })


        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
            Log.d(TAG, "Active Job " + jobCounter)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            Log.d(TAG, "HomeFragment State Message " + stateMessage.toString())
            stateMessage?.let {
                viewModel.clearStateMessage()
                if(isPaginationDone(stateMessage.response.message)){
                    Log.d(TAG, "paginationDone")
                }
                else{

                    uiCommunicationListener.onResponseReceived(
                            response = it.response,
                            stateMessageCallback = object: StateMessageCallback {
                                override fun removeMessageFromStack() {
//                                    viewModel.clearStateMessage()
                                }
                            }
                    )
                }
            }
        })
    }

    override fun onResume() {
//        skeleton.showSkeleton()
        viewModel.refreshFromCache()
        super.onResume()
    }

    override fun onPause() {
        saveLayoutManagerState()
        super.onPause()
    }


    private fun saveLayoutManagerState(){
        test_recycler_view.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
            Log.d(TAG, "saveLayoutManager " + lmState.toString())
        }
    }

    override fun onItemSelected(position: Int, item: CacheImage) {
        findNavController().navigate(R.id.action_homeFragment_to_homeDetailsFragment)
    }


    override fun restoreListPosition() {
        viewModel.viewState.value?.imageFields?.layoutManagerState?.let { lmState ->
            test_recycler_view?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun onLikeClick(position: Int, item: CacheImage) {
        item.favorite = 0
        viewModel.setLike(item)
    }

    override fun onDownloadClick(position: Int, item: CacheImage) {
        download(item)
    }


    private fun initRecyclerView(){
        test_recycler_view.apply {
            val orientation = getResources().getConfiguration().orientation
            if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                layoutManager = GridLayoutManager(this@HomeFragment.context, 2)
            }
            else {
                layoutManager = LinearLayoutManager(this@HomeFragment.context)
            }
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
                    if (lastPosition == recyclerAdapter.itemCount.minus(2)) {
                        viewModel.nextPage()
                    }
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        viewModel.nextPage()
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if(test_recycler_view != null) {
            test_recycler_view.adapter = null
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        initSearchView(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_search -> {
                (activity as MainActivity).navigateSearchHistoryFragment()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }




    private fun initSearchView(menu: Menu){
        activity?.apply {
            val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.setIconifiedByDefault(true)
            searchView.isSubmitButtonEnabled = true
        }

        // ENTER ON COMPUTER KEYBOARD OR ARROW ON VIRTUAL KEYBOARD
        val searchPlate = searchView.findViewById(R.id.search_src_text) as EditText
        searchPlate.setOnEditorActionListener { v, actionId, event ->
            true
        }

        // SEARCH BUTTON CLICKED (in toolbar)
        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            (activity as MainActivity).navigateSearchHistoryFragment()
        }
    }


    override fun onResponseReceived(response: Response, stateMessageCallback: StateMessageCallback) {
        Log.d(TAG, "Message " + response.message.toString())
        Log.d(TAG, "Message Type " + response.messageType.toString())
        if(isSizeZero) {
            if(skeleton.isSkeleton()) {
                skeleton.showOriginal()
            }
            no_details.visibility = View.VISIBLE
        }
        else {
            no_details.visibility = View.GONE
            if(skeleton.isSkeleton()) {
                skeleton.showOriginal()
            }
        }
    }

    override fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
//            home_progress.visibility = View.VISIBLE
            no_details.visibility = View.GONE
            if(isSizeZero) {
                skeleton.showSkeleton()
            }
        }
        else {
//            home_progress.visibility = View.GONE
        }


    }

    override fun expandAppBar() {
    }

    override fun hideSoftKeyboard() {
    }

    override fun isStoragePermissionGranted(): Boolean {
        return true
    }


    private fun initSkeleton() {
        skeleton = test_recycler_view.applySkeleton(R.layout.skeleton_home_series, 2)
//        skeleton = view?.findViewById<SkeletonLayout>(R.id.skeletonLayout)!!
//        skeleton.showShimmer = true
        /*skeleton.maskColor = ContextCompat.getColor(requireContext(), R.color.maskColor)
        skeleton.shimmerColor = ContextCompat.getColor(requireContext(), R.color.shimmerColor)*/
    }

    private fun download(image: CacheImage) {
        val context = activity ?: return

        if (!PermissionUtils.check(context as MainActivity)) {
            Toaster.sendShortToast(context.getString(R.string.no_permission))
            return
        }
        DownloadUtils.download(context, image)
    }

}