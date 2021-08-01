package com.appbytes.beautywallpaper.ui.main.home.viewmodel

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.appbytes.beautywallpaper.repository.home.HomeRepository
import com.appbytes.beautywallpaper.ui.BaseViewModel
import com.appbytes.beautywallpaper.ui.main.home.state.HomeStateEvent
import com.appbytes.beautywallpaper.ui.main.home.state.HomeViewState
import com.appbytes.beautywallpaper.util.StateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class HomeViewModel
@ViewModelInject
    constructor(
        private val mainRepository: HomeRepository,
        @Assisted
        stateHandle: SavedStateHandle
    )
    : BaseViewModel<HomeViewState>() {

    override val TAG = "HomeViewModel"


    override fun handleNewData(data: HomeViewState) {
        setViewState(data)
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        if(!isJobAlreadyActive(stateEvent)){
            val job = when(stateEvent) {
                is HomeStateEvent.GetNewPhotos -> {
                    if(stateEvent.clearLayoutManagerState){
//                        clearLayoutManagerState()
                    }
                    Log.d(TAG, "inner event page number " + getPage())
                    Log.d(TAG, "finish inner event page " + getPage())
                    mainRepository.getPhotos(
                            pageNumber = getPage(),
                            stateEvent = stateEvent
                    )
                }

                is HomeStateEvent.SetLikeEvent -> {
                    mainRepository.setLike(stateEvent.clickImage, getPage(),stateEvent = stateEvent)
                }

                /*is HomeStateEvent.CacheData -> {
                    mainRepository.getCacheData(stateEvent)
                }*/
                else ->
                    mainRepository.getPhotos(
                            pageNumber = 1,
                            stateEvent = stateEvent
                    )
            }
            launchJob(stateEvent, job)

        }
    }

    override fun initNewViewState(): HomeViewState {
        return HomeViewState()
    }
}