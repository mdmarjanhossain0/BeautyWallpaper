package com.appbytes.beautywallpaper.ui.main.search.viewmodel

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.appbytes.beautywallpaper.repository.home.HomeRepository
import com.appbytes.beautywallpaper.repository.search.SearchRepository
import com.appbytes.beautywallpaper.ui.BaseViewModel
import com.appbytes.beautywallpaper.ui.main.home.state.HomeStateEvent
import com.appbytes.beautywallpaper.ui.main.home.state.HomeViewState
import com.appbytes.beautywallpaper.ui.main.search.state.SearchStateEvent
import com.appbytes.beautywallpaper.ui.main.search.state.SearchViewState
import com.appbytes.beautywallpaper.util.StateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class SearchViewModel
@ViewModelInject
constructor(
        private val searchRepository: SearchRepository,
        @Assisted
        stateHandle: SavedStateHandle
)
    : BaseViewModel<SearchViewState>() {




    override fun handleNewData(data: SearchViewState) {
        setViewState(data)
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        if(!isJobAlreadyActive(stateEvent)){
            val job = when(stateEvent) {
                is SearchStateEvent.GetSearchPhotos -> {
                    if(stateEvent.clearLayoutManagerState){
                        clearLayoutManagerState()
                    }
                    Log.d(TAG, "inner event page number " + getPage())
                    Log.d(TAG, "finish inner event page " + getPage())
                    searchRepository.getSearchPhotos(
                            query = stateEvent.query,
                            pageNumber = getPage(),
                            stateEvent = stateEvent
                    )
                }
                is SearchStateEvent.GetSearchKeys -> {
                    searchRepository.getSearchKeys(stateEvent)
                }
                else ->
                    searchRepository.getSearchKeys(stateEvent)
            }
            launchJob(stateEvent, job)

        }
    }

    override fun initNewViewState(): SearchViewState {
        return SearchViewState()
    }
}