package com.appbytes.beautywallpaper.ui.main.favorite

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.ui.main.MainActivity
import com.appbytes.beautywallpaper.ui.main.favorite.state.FAVORITE_VIEW_STATE_BUNDLE_KEY
import com.appbytes.beautywallpaper.ui.main.favorite.state.FavoriteViewState
import com.appbytes.beautywallpaper.ui.main.favorite.viewmodel.getFromCache
import com.appbytes.beautywallpaper.ui.main.favorite.viewmodel.nextPage
import com.appbytes.beautywallpaper.ui.main.favorite.viewmodel.setLayoutManagerState
import com.appbytes.beautywallpaper.ui.main.favorite.viewmodel.setUnLike
import com.appbytes.beautywallpaper.util.ErrorHandling
import com.appbytes.beautywallpaper.util.TopSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class FavoriteFragment : BaseFavoriteFragment(R.layout.fragment_home), FavoriteImageAdapter.Interaction {

    private val TAG = "FavoriteFragment"

    private lateinit var recyclerAdapter: FavoriteImageAdapter


    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore state after process death

        savedInstanceState?.let { inState ->
            Log.d(TAG, "FavoriteViewState: inState is NOT null")
            (inState[FAVORITE_VIEW_STATE_BUNDLE_KEY] as FavoriteViewState?)?.let { viewState ->
                Log.d(TAG, "FavoriteViewState: restoring view state: ${viewState}")
                Log.d(TAG, "Restore Data " + viewState.cacheImageList?.size)
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value
        outState.putParcelable(
            FAVORITE_VIEW_STATE_BUNDLE_KEY,
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
            Log.d(TAG, "Data " + viewState.cacheImageList?.size)
            recyclerAdapter.submitList(viewState.cacheImageList)
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
        viewModel.getFromCache()
        (activity as MainActivity).hamburgerArrow(true)
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
        findNavController().navigate(R.id.action_favoriteFragment_to_favoriteDetailsFragment)
    }


    override fun restoreListPosition() {
        viewModel.viewState.value?.layoutManagerState?.let { lmState ->
            test_recycler_view?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun onLikeClick(position: Int, item: CacheImage) {
        item.favorite = 0
        viewModel.setUnLike(item)
    }


    private fun initRecyclerView(){

        test_recycler_view.apply {


            /*val linearLayoutManager = this@HomeFragment.context?.let { ZoomRecyclerLayout(it) }
            linearLayoutManager?.orientation = LinearLayoutManager.VERTICAL
            linearLayoutManager?.reverseLayout = false
            linearLayoutManager?.stackFromEnd = true
            layoutManager = linearLayoutManager // Add your recycler view to this ZoomRecycler layout*/



            val orientation = getResources().getConfiguration().orientation
            if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                layoutManager = GridLayoutManager(this@FavoriteFragment.context, 2)
            }
            else {
                layoutManager = LinearLayoutManager(this@FavoriteFragment.context)
            }
            val topSpacingDecorator = TopSpacingItemDecoration(5)
//            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = FavoriteImageAdapter(
                interaction = this@FavoriteFragment
            )
            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "FavoriteFragment: attempting to load next page...")
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