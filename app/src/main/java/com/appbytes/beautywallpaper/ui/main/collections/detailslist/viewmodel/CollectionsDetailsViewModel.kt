package com.appbytes.beautywallpaper.ui.main.collections.detailslist.viewmodel

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.appbytes.beautywallpaper.repository.collections.CollectionsRepository
import com.appbytes.beautywallpaper.ui.BaseViewModel
import com.appbytes.beautywallpaper.ui.main.collections.detailslist.state.CollectionsDetailsEvent
import com.appbytes.beautywallpaper.ui.main.collections.detailslist.state.CollectionsDetailsState
import com.appbytes.beautywallpaper.util.StateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
class CollectionsDetailsViewModel
@ViewModelInject
constructor(
        private val collectionsRepository: CollectionsRepository,
        @Assisted
        stateHandle: SavedStateHandle
) : BaseViewModel<CollectionsDetailsState>() {

    init {
        setupChannel()
    }

    override fun handleNewData(data: CollectionsDetailsState) {
        setViewState(data)
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        if(!isJobAlreadyActive(stateEvent)){
            val job = when(stateEvent) {
                is CollectionsDetailsEvent.GetNewCollectionsDetails -> {
                    if(stateEvent.clearLayoutManagerState){
//                        clearLayoutManagerState()
                    }
                    Log.d(TAG, "inner event page number " +stateEvent.page_number)
                    collectionsRepository.getCollectionsImages(
                            collectionsId = stateEvent.collectionsId,
                            pageNumber = getCollectionsDetailsPage(),
                            stateEvent = stateEvent
                    )
                }
                else ->
                    collectionsRepository.getCollectionsImages(
                            pageNumber = 1,
                            stateEvent = stateEvent
                    )
            }
            launchJob(stateEvent, job)

        }
    }

    override fun initNewViewState(): CollectionsDetailsState {
        return CollectionsDetailsState()
    }
}