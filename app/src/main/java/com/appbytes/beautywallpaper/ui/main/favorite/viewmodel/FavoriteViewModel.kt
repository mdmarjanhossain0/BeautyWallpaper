package com.appbytes.beautywallpaper.ui.main.favorite.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.appbytes.beautywallpaper.repository.favorite.FavoriteRepository
import com.appbytes.beautywallpaper.ui.BaseViewModel
import com.appbytes.beautywallpaper.ui.main.favorite.state.FavoriteStateEvent
import com.appbytes.beautywallpaper.ui.main.favorite.state.FavoriteViewState
import com.appbytes.beautywallpaper.util.StateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class FavoriteViewModel
@ViewModelInject
constructor(
        private val favoriteRepository : FavoriteRepository,
        @Assisted
        stateHandle: SavedStateHandle
)
    : BaseViewModel<FavoriteViewState>() {




    override fun handleNewData(data: FavoriteViewState) {
        setViewState(data)
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        if(!isJobAlreadyActive(stateEvent)){
            val job = when(stateEvent) {
                is FavoriteStateEvent.GetFavoriteEvent -> {
                    if (stateEvent.clearStateEvent) {
                        clearLayoutManagerState()
                    }
                    favoriteRepository.getFavoriteList(
                            pageNumber = stateEvent.pageNumber,
                            stateEvent = stateEvent
                    )
                }

                is FavoriteStateEvent.RemoveFromFavoriteEvent -> {
                    if (stateEvent.clearStateEvent) {
                        clearLayoutManagerState()
                    }
                    favoriteRepository.removeFromFavoriteList(
                            pageNumber = stateEvent.pageNumber,
                            cacheImage = stateEvent.cacheImage,
                            stateEvent = stateEvent
                    )
                }
                else ->
                    favoriteRepository.getFavoriteList(
                            pageNumber = 1,
                            stateEvent = stateEvent
                    )


            }
            launchJob(stateEvent, job)

        }
    }

    override fun initNewViewState(): FavoriteViewState {
        return FavoriteViewState()
    }
}

