package com.appbytes.beautywallpaper.ui.main.collections.list

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.api.MainApiService
import com.appbytes.beautywallpaper.api.response.Image
import com.appbytes.beautywallpaper.models.CacheCollections
import com.appbytes.beautywallpaper.ui.main.MainActivity
import com.appbytes.beautywallpaper.ui.main.collections.BaseCollectionsFragment
import com.appbytes.beautywallpaper.ui.main.collections.adapter.CollectionsAdapter
import com.appbytes.beautywallpaper.ui.main.collections.list.state.COLLECTIONS_STATE_BUNDLE_KEY
import com.appbytes.beautywallpaper.ui.main.collections.list.state.CollectionsState
import com.appbytes.beautywallpaper.ui.main.collections.list.viewmodel.CollectionsViewModel
import com.appbytes.beautywallpaper.ui.main.collections.list.viewmodel.collectionsNextPage
import com.appbytes.beautywallpaper.ui.main.collections.list.viewmodel.setLayoutManagerState
import com.appbytes.beautywallpaper.util.Constants
import com.appbytes.beautywallpaper.util.ErrorHandling.Companion.isPaginationDone
import com.appbytes.beautywallpaper.util.TopSpacingItemDecoration
import com.appbytes.beautywallpaper.util.customLayoutManager
import com.appbytes.beautywallpaper.util.getLayout
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_collections.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CollectionsFragment : BaseCollectionsFragment(R.layout.fragment_collections), CollectionsAdapter.Interaction {

    private val TAG = "CollectionFra"

    @Inject
    lateinit var mainApiService: MainApiService

    private lateinit var recyclerAdapter: CollectionsAdapter


    private lateinit var searchView: SearchView

    val viewModel: CollectionsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore state after process death

        savedInstanceState?.let { inState ->
            Log.d(TAG, "HomeViewState: inState is NOT null")
            (inState[COLLECTIONS_STATE_BUNDLE_KEY] as CollectionsState?)?.let { viewState ->
                Log.d(TAG, "HomeViewState: restoring view state: ${viewState}")
                Log.d(TAG, "Restore Data " + viewState.collectionsFields?.collections?.size)
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value
        outState.putParcelable(
                COLLECTIONS_STATE_BUNDLE_KEY,
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
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            Log.d(TAG, "Data " + viewState.collectionsFields?.collections?.size)
            viewState.collectionsFields?.let { it1 -> recyclerAdapter.submitList(it1.collections ?: null) }
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
            photo = mainApiService.getNewPhotos(1,11, Constants.unsplash_access_key)
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
//        (activity as MainActivity).drawerToggle.syncState()
        (activity as MainActivity).hamburgerArrow(true)
//        viewModel.refreshFromCacheCollections()
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun saveLayoutManagerState(){
        collections_recycler_view.layoutManager?.onSaveInstanceState()?.let { lmState ->
            Log.d(TAG, "saveLayoutManager " + lmState.toString())
            viewModel.setLayoutManagerState(lmState)
        }
    }

    override fun onItemSelected(position: Int, item: CacheCollections) {
        val action = CollectionsFragmentDirections.actionCollectionsFragmentToCollectionsDetailsFragment(item.id)
        findNavController().navigate(action)
    }


    override fun restoreListPosition() {
        viewModel.viewState.value?.collectionsFields?.layoutManagerState?.let { lmState ->
            Log.d(TAG, "restoreListPositon " + lmState.toString())
            collections_recycler_view?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun nextPage() {
        Log.d(TAG, "CollectionsFragment next page callback call")
        viewModel.collectionsNextPage()
    }


    private fun initRecyclerView(){

        collections_recycler_view.apply {
            val orientation = resources.configuration.orientation
            layoutManager = customLayoutManager(
                land_scape = orientation,
                layout = getLayout((activity as MainActivity).sharedPreferences, context),
                context
            )
            val topSpacingDecorator = TopSpacingItemDecoration(5)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter =
                CollectionsAdapter(
                    interaction = this@CollectionsFragment
                )
            /*addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "BlogFragment: attempting to load next page...")
                        viewModel.collectionsNextPage()
                    }
                }
            })*/
            adapter = recyclerAdapter
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if(collections_recycler_view != null) {
            collections_recycler_view.adapter = null
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