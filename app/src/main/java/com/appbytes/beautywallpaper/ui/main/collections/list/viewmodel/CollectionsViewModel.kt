package com.appbytes.beautywallpaper.ui.main.collections.list.viewmodel

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.appbytes.beautywallpaper.repository.collections.CollectionsRepository
import com.appbytes.beautywallpaper.ui.BaseViewModel
import com.appbytes.beautywallpaper.ui.main.collections.list.state.CollectionsEvent
import com.appbytes.beautywallpaper.ui.main.collections.list.state.CollectionsState
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
) : BaseViewModel<CollectionsState>() {


    init {
        setupChannel()
        setStateEvent(CollectionsEvent.GetNewCollections(false))
    }

    override fun handleNewData(data: CollectionsState) {
        setViewState(data)
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        if(!isJobAlreadyActive(stateEvent)){
            val job = when(stateEvent) {
                is CollectionsEvent.GetNewCollections -> {
                    if(stateEvent.clearLayoutManagerState){
                        clearLayoutManagerState()
                    }
                    Log.d(TAG, "inner event page number " +getCurrentViewStateOrNew().collectionsFields.page_number)
                    collectionsRepository.getCollections(
                            pageNumber = getCollectionsPage(),
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

    override fun initNewViewState(): CollectionsState {
        return CollectionsState()
    }
}