package com.appbytes.beautywallpaper.ui.main.home.viewmodel

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.appbytes.beautywallpaper.repository.home.HomeRepository
import com.appbytes.beautywallpaper.ui.BaseViewModel
import com.appbytes.beautywallpaper.ui.main.home.state.HomeStateEvent
import com.appbytes.beautywallpaper.ui.main.home.state.HomeViewState
import com.appbytes.beautywallpaper.util.StateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext


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

    init {
//        initial()
        setupChannel()
        setStateEvent(HomeStateEvent.GetNewPhotos(false))

    }


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

    fun initial() {
        mainRepository.getPhotos(
                pageNumber = getPage(),
                stateEvent = HomeStateEvent.GetNewPhotos()
        ).onEach{ dataState ->
            withContext(Dispatchers.Main){
                Log.d(TAG, "datastate " +dataState.toString())
                dataState?.data?.let { data ->
                    Log.d(TAG, "datastate handleNewData ")
                    handleNewData(data)
                }
                dataState?.stateMessage?.let { stateMessage ->
                    Log.d(TAG, "datastate handleNewMessage______ " )
//                    handleNewStateMessage(stateMessage)
                }
                dataState?.stateEvent?.let { stateEvent ->
                    Log.d(TAG, "datastate handleNewEvent ")
//                    removeStateEvent(stateEvent)
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

}