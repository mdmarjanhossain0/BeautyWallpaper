package com.appbytes.beautywallpaper.ui.main.home

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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.ui.UICommunicationListener
import com.appbytes.beautywallpaper.ui.main.MainActivity
import com.appbytes.beautywallpaper.ui.main.home.state.HOME_VIEW_STATE_BUNDLE_KEY
import com.appbytes.beautywallpaper.ui.main.home.state.HomeViewState
import com.appbytes.beautywallpaper.ui.main.home.viewmodel.*
import com.appbytes.beautywallpaper.util.ErrorHandling.Companion.isPaginationDone
import com.appbytes.beautywallpaper.util.Response
import com.appbytes.beautywallpaper.util.StateMessageCallback
import com.appbytes.beautywallpaper.util.TopSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*


@AndroidEntryPoint
class HomeFragment : BaseHomeFragment(R.layout.fragment_home), ImageAdapter.Interaction {

    private val TAG = "HomeFragment"

    /*@Inject
    lateinit var mainApiService: MainApiService*/

    private lateinit var recyclerAdapter: ImageAdapter


    private lateinit var searchView: SearchView


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
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        Log.d(TAG, "ViewModel " + viewModel.toString())
        initRecyclerView()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            Log.d(TAG, "Data " + viewState.imageFields?.images?.size)
            if(viewState.imageFields.images?.size == 0 ) {
                home_progress.visibility = View.VISIBLE
            }
            else {
                home_progress.visibility = View.GONE
                viewState.imageFields?.let {
                    it1 -> recyclerAdapter.submitList(it1.images ?: null) }
            }
        })


        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
//            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
            Log.d(TAG, "Active Job " + jobCounter)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            Log.d(TAG, "HomeFragment State Message " + stateMessage.toString())
            stateMessage?.let {
                if(isPaginationDone(stateMessage.response.message)){
                    Log.d("AppDebug", "paginationDone")
                }

                else{
                    uiCommunicationListener.onResponseReceived(
                            response = it.response,
                            stateMessageCallback = object: StateMessageCallback {
                                override fun removeMessageFromStack() {
                                    viewModel.clearStateMessage()
                                }
                            }
                    )
                }
            }
        })
    }

    /*private fun callApi() {
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

    }*/

    override fun onResume() {
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


    private fun initRecyclerView(){

        test_recycler_view.apply {


            /*val linearLayoutManager = this@HomeFragment.context?.let { ZoomRecyclerLayout(it) }
            linearLayoutManager?.orientation = LinearLayoutManager.VERTICAL
            linearLayoutManager?.reverseLayout = false
            linearLayoutManager?.stackFromEnd = true
            layoutManager = linearLayoutManager // Add your recycler view to this ZoomRecycler layout*/



            val orientation = getResources().getConfiguration().orientation
            if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                layoutManager = GridLayoutManager(this@HomeFragment.context, 2)
            }
            else {
                layoutManager = LinearLayoutManager(this@HomeFragment.context)
            }
            val topSpacingDecorator = TopSpacingItemDecoration(5)
//            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
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
            (activity as MainActivity).navigateSearchHistoryFragment()
        }
    }


    override fun onResponseReceived(response: Response, stateMessageCallback: StateMessageCallback) {
        home_retry.visibility = View.VISIBLE
    }

    override fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            home_progress.visibility = View.VISIBLE
        }
        else {
            home_progress.visibility = View.GONE
        }
    }

    override fun expandAppBar() {
    }

    override fun hideSoftKeyboard() {
    }

    override fun isStoragePermissionGranted(): Boolean {
        return true
    }

}