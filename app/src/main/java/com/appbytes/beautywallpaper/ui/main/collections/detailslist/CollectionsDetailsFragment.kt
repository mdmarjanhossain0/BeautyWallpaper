package com.appbytes.beautywallpaper.ui.main.collections.detailslist

import android.app.SearchManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.api.MainApiService
import com.appbytes.beautywallpaper.api.response.Image
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.ui.main.MainActivity
import com.appbytes.beautywallpaper.ui.main.collections.BaseCollectionsFragment
import com.appbytes.beautywallpaper.ui.main.collections.adapter.CollectionsDetailsAdapter
import com.appbytes.beautywallpaper.ui.main.collections.detailslist.state.COLLECTIONS_DETAILS_STATE_BUNDLE_KEY
import com.appbytes.beautywallpaper.ui.main.collections.detailslist.state.CollectionsDetailsState
import com.appbytes.beautywallpaper.ui.main.collections.detailslist.viewmodel.CollectionsDetailsViewModel
import com.appbytes.beautywallpaper.ui.main.collections.detailslist.viewmodel.collectionsDetailsNextPage
import com.appbytes.beautywallpaper.ui.main.collections.detailslist.viewmodel.refreshFromCacheCollectionsDetails
import com.appbytes.beautywallpaper.ui.main.collections.detailslist.viewmodel.setLayoutManagerState
import com.appbytes.beautywallpaper.util.Constants
import com.appbytes.beautywallpaper.util.ErrorHandling.Companion.isPaginationDone
import com.appbytes.beautywallpaper.util.TopSpacingItemDecoration
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_collections_details.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CollectionsDetailsFragment : BaseCollectionsFragment(R.layout.fragment_collections_details), CollectionsDetailsAdapter.Interaction {


    private val TAG = "CollectionsDetailsFgmnt"

    @Inject
    lateinit var mainApiService: MainApiService

    private lateinit var recyclerAdapter: CollectionsDetailsAdapter


    private lateinit var searchView: SearchView


    val args: CollectionsDetailsFragmentArgs by navArgs()

    val viewModel : CollectionsDetailsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore state after process death
        savedInstanceState?.let { inState ->
            Log.d(TAG, "CollectionsDetailsViewState: inState is NOT null")
            (inState[COLLECTIONS_DETAILS_STATE_BUNDLE_KEY] as CollectionsDetailsState?)?.let { viewState ->
                Log.d(TAG, "HomeViewState: restoring view state: ${viewState}")
                Log.d(TAG, "Restore Data " + viewState.collectionsDetailsFields?.collectionsImages?.size)
                viewModel.setViewState(viewState)
//                viewModel.refreshFromCacheCollectionsDetails(args.collectionsId)
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
                COLLECTIONS_DETAILS_STATE_BUNDLE_KEY,
                viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
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
            Log.d(TAG, "Collections Data " + viewState.collectionsDetailsFields?.collectionsImages?.size)
            viewState.collectionsDetailsFields?.let { it1 -> recyclerAdapter.submitList(it1.collectionsImages ?: null) }
        })


        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {
                if(isPaginationDone(stateMessage.response.message)){
                }
                viewModel.clearStateMessage()
                /*else{
                    uiCommunicationListener.onResponseReceived(
                            response = it.response,
                            stateMessageCallback = object: StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            }
                    )
                }*/
            }
        })
    }

    private fun callApi() {
        var photo: List<Image>?=null
        CoroutineScope(Dispatchers.IO).launch {
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
        (activity as MainActivity).hamburgerArrow(false)
        viewModel.refreshFromCacheCollectionsDetails(args.collectionsId)
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun saveLayoutManagerState(){
        collections_details_recycler_view.layoutManager?.onSaveInstanceState()?.let { lmState ->
            Log.d(TAG, "saveLayoutManager " + lmState.toString())
            viewModel.setLayoutManagerState(lmState)
        }
    }

    override fun onItemSelected(position: Int, item: CacheImage) {
        findNavController().navigate(R.id.action_collectionsFragment_to_collectionsDetailsFragment)
    }


    override fun restoreListPosition() {
        viewModel.viewState.value?.collectionsDetailsFields?.layoutManagerState?.let { lmState ->
            Log.d(TAG, "restoreListPositon " + lmState.toString())
            test_recycler_view?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun nextPage() {
        Log.d(TAG, "CollectionsDetailsFragment next page callback call")
        viewModel.collectionsDetailsNextPage(args.collectionsId)
    }


    private fun initRecyclerView(){

        collections_details_recycler_view.apply {
            val orientation = getResources().getConfiguration().orientation
            if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                layoutManager = GridLayoutManager(this@CollectionsDetailsFragment.context, 2)
            }
            else {
                layoutManager = LinearLayoutManager(this@CollectionsDetailsFragment.context)
            }
            val topSpacingDecorator = TopSpacingItemDecoration(0)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter =
                CollectionsDetailsAdapter(
                    interaction = this@CollectionsDetailsFragment
                )
            /*addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "CollectionsDetails: attempting to load next page...")
                        viewModel.collectionsDetailsNextPage(args.collectionsId)
                    }
                }
            })*/
            adapter = recyclerAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if(collections_details_recycler_view != null) {
            collections_details_recycler_view.adapter = null
        }
//        test_recycler_view.adapter = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        initSearchView(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_filter_settings -> {
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

            /*if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                    || actionId == EditorInfo.IME_ACTION_SEARCH ) {
                val searchQuery = v.text.toString()
                Log.e(TAG, "SearchView: (keyboard or arrow) executing search...: ${searchQuery}")
                viewModel.setQuery(searchQuery).let{
                    onBlogSearchOrFilter()
                }
            }*/
            true
        }

        // SEARCH BUTTON CLICKED (in toolbar)
        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            Log.e(TAG, "SearchView: (button) executing search...: ${searchQuery}")
            /*viewModel.setQuery(searchQuery).let {
                onBlogSearchOrFilter()
            }*/


            (activity as MainActivity).navigateSearchHistoryFragment()
        }
    }

}