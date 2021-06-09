package com.appbytes.beautywallpaper.ui.main.home

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.appbytes.beautywallpaper.api.main.MainApiService
import com.appbytes.beautywallpaper.repository.main.HomeRepository
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




    override fun handleNewData(data: HomeViewState) {
        setViewState(data)
    }

    override fun setStateEvent(stateEvent: StateEvent) {



        if(!isJobAlreadyActive(stateEvent)){
            val job = mainRepository.getPhotos(
                            pageNumber = 1,
                            stateEvent = stateEvent
                    )
            launchJob(stateEvent, job)

        }
    }

    override fun initNewViewState(): HomeViewState {
        return HomeViewState(null)
    }
}