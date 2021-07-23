package com.appbytes.beautywallpaper.ui.main.search

import android.app.SearchManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.ui.main.home.state.HOME_VIEW_STATE_BUNDLE_KEY
import com.appbytes.beautywallpaper.ui.main.search.adapter.SearchResultAdapter
import com.appbytes.beautywallpaper.ui.main.search.state.SEARCH_RESULT_VIEW_STATE_BUNDLE_KEY
import com.appbytes.beautywallpaper.ui.main.search.state.SearchViewState
import com.appbytes.beautywallpaper.ui.main.search.viewmodel.getSearchKeys
import com.appbytes.beautywallpaper.ui.main.search.viewmodel.nextPage
import com.appbytes.beautywallpaper.ui.main.search.viewmodel.refreshFromCache
import com.appbytes.beautywallpaper.ui.main.search.viewmodel.setLayoutManagerState
import com.appbytes.beautywallpaper.util.ErrorHandling
import com.appbytes.beautywallpaper.util.TopSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_result.*

@AndroidEntryPoint
class SearchResultFragment : BaseSearchFragment(R.layout.fragment_search_result), SearchResultAdapter.Interaction {

    private val TAG = "SRFragment"

    private lateinit var recyclerAdapter: SearchResultAdapter


    private lateinit var searchView: SearchView


    private val args : SearchResultFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore state after process death

        Log.d(TAG, "Search Query " + args.searchKey)

        savedInstanceState?.let { inState ->
            Log.d(TAG, "SearchViewState: inState is NOT null")
            (inState[SEARCH_RESULT_VIEW_STATE_BUNDLE_KEY] as SearchViewState?)?.let { viewState ->
                Log.d(TAG, "SearchViewState: restoring view state: ${viewState}")
                Log.d(TAG, "Restore Data " + viewState.searchFields?.images?.size)
                viewModel.setViewState(viewState)
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
                SEARCH_RESULT_VIEW_STATE_BUNDLE_KEY,
                viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        Log.d(TAG, "ViewModel " + viewModel.toString())
        initSearchResultRecyclerView()
        subscribeObservers()
//        callApi()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            Log.d(TAG, "Data " + viewState.searchFields?.images?.size)
            viewState.searchFields?.let {
                it1 -> recyclerAdapter.submitList(it1.images ?: null) }
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {
                if(ErrorHandling.isPaginationDone(stateMessage.response.message)){
                    Log.d("AppDebug", "paginationDone")
                }
                viewModel.clearStateMessage()
            }
        })
    }


    override fun onResume() {
        viewModel.refreshFromCache(args.searchKey)
        super.onResume()
    }

    override fun onPause() {
        saveLayoutManagerState()
        super.onPause()
    }


    private fun saveLayoutManagerState(){
        search_result_recycler_view.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
            Log.d(TAG, "saveLayoutManager " + lmState.toString())
        }
    }

    override fun onItemSelected(position: Int, item: CacheImage) {
        findNavController().navigate(R.id.action_homeFragment_to_homeDetailsFragment)
    }


    override fun restoreListPosition() {
        viewModel.viewState.value?.searchFields?.layoutManagerState?.let { lmState ->
            search_result_recycler_view?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun onLikeClick(position: Int, item: CacheImage) {
        item.favorite = 0
//        viewModel.setLike(item)
    }





    private fun initSearchResultRecyclerView(){

        search_result_recycler_view.apply {



            val orientation = getResources().getConfiguration().orientation
            if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                layoutManager = GridLayoutManager(this@SearchResultFragment.context, 2)
            }
            else {
                layoutManager = LinearLayoutManager(this@SearchResultFragment.context)
            }
            val topSpacingDecorator = TopSpacingItemDecoration(5)
//            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter =
                SearchResultAdapter(
                    interaction = this@SearchResultFragment
                )
            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "SearchViewState: attempting to load next page...")
                        viewModel.nextPage()
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if(search_result_recycler_view != null) {
            search_result_recycler_view.adapter = null
        }
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
            /*val searchQuery = searchPlate.text.toString()
            Log.e(TAG, "SearchView: (button) executing search...: ${searchQuery}")
            *//*viewModel.setQuery(searchQuery).let {
                onBlogSearchOrFilter()
            }*/
            /* val fragment = SearchNavHostFragment.create(R.navigation.nav_search)
             activity?.supportFragmentManager
                 ?.beginTransaction()
                 ?.replace(R.id.main_fragment_container, fragment,fragment.tag)
                 ?.commit()*/
//            (activity as MainActivity).callApi()
        }
    }


}