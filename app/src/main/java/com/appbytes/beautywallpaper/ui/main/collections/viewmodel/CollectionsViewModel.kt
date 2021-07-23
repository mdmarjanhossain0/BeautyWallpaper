package com.appbytes.beautywallpaper.ui.main.collections.viewmodel

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.appbytes.beautywallpaper.repository.collections.CollectionsRepository
import com.appbytes.beautywallpaper.ui.BaseViewModel
import com.appbytes.beautywallpaper.ui.main.collections.state.CollectionsStateEvent
import com.appbytes.beautywallpaper.ui.main.collections.state.CollectionsViewState
import com.appbytes.beautywallpaper.util.StateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
class CollectionsViewModel
@ViewModelInject
constructor(
        private val collectionsRepository: CollectionsRepository,
        @Assisted
        stateHandle: SavedStateHandle
) : BaseViewModel<CollectionsViewState>() {




    override fun handleNewData(data: CollectionsViewState) {
        setViewState(data)
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        if(!isJobAlreadyActive(stateEvent)){
            val job = when(stateEvent) {
                is CollectionsStateEvent.GetNewCollections -> {
                    if(stateEvent.clearLayoutManagerState){
                        clearLayoutManagerState()
                    }
                    Log.d(TAG, "inner event page number " +getCurrentViewStateOrNew().collectionsFields.page_number)
                    collectionsRepository.getCollections(
                            pageNumber = getCollectionsPage(),
                            stateEvent = stateEvent
                    )
                }

                is CollectionsStateEvent.GetNewCollectionsDetails -> {
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
                    collectionsRepository.getCollections(
                            pageNumber = 1,
                            stateEvent = stateEvent
                    )
            }
            launchJob(stateEvent, job)

        }
    }

    override fun initNewViewState(): CollectionsViewState {
        return CollectionsViewState()
    }
}