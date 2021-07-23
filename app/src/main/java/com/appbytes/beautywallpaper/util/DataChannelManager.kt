package com.appbytes.beautywallpaper.util


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
@ExperimentalCoroutinesApi
abstract class DataChannelManager<ViewState> {

    private val TAG: String = "AppDebug"

    private val _activeStateEvents: HashSet<String> = HashSet()
    private val _numActiveJobs: MutableLiveData<Int> = MutableLiveData()
    private var channelScope: CoroutineScope? = null

    val messageStack = MessageStack()

    val numActiveJobs: LiveData<Int>
        get() = _numActiveJobs

    fun setupChannel(){
        cancelJobs()
        setupNewChannelScope(CoroutineScope(IO))
    }

    abstract fun handleNewData(data: ViewState)

    fun launchJob(
        stateEvent: StateEvent,
        jobFunction: Flow<DataState<ViewState>?>
    ){
        Log.d(TAG, "isStateEventActive " +isStateEventActive(stateEvent).toString())
        Log.d(TAG, "messageStack Size " +messageStack.size)
        if(!isStateEventActive(stateEvent) && messageStack.size == 0){
            addStateEvent(stateEvent)
            jobFunction
                .onEach{ dataState ->
                    withContext(Main){
                        Log.d(TAG, "datastate " +dataState.toString())
                        dataState?.data?.let { data ->
                            Log.d(TAG, "datastate handleNewData ")
                            handleNewData(data)
                        }
                        dataState?.stateMessage?.let { stateMessage ->
                            Log.d(TAG, "datastate handleNewMessage______ " )
                            handleNewStateMessage(stateMessage)
                        }
                        dataState?.stateEvent?.let { stateEvent ->
                            Log.d(TAG, "datastate handleNewEvent ")
                            removeStateEvent(stateEvent)
                        }
                    }
                }
                .launchIn(getChannelScope())
        }
    }

    private fun handleNewStateMessage(stateMessage: StateMessage){
        appendStateMessage(stateMessage)
    }

    private fun appendStateMessage(stateMessage: StateMessage) {
        messageStack.add(stateMessage)
    }

    fun clearStateMessage(index: Int = 0){
        messageStack.removeAt(index)
    }

    private fun clearActiveStateEventCounter(){
        _activeStateEvents.clear()
        syncNumActiveStateEvents()
    }

    private fun addStateEvent(stateEvent: StateEvent){
        _activeStateEvents.add(stateEvent.toString())
        syncNumActiveStateEvents()
    }

    private fun removeStateEvent(stateEvent: StateEvent?){
        _activeStateEvents.remove(stateEvent.toString())
        syncNumActiveStateEvents()
    }

    fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
        return isStateEventActive(stateEvent)
    }

    private fun isStateEventActive(stateEvent: StateEvent): Boolean{
        return _activeStateEvents.contains(stateEvent.toString())
    }

    fun getChannelScope(): CoroutineScope {
        return channelScope?: setupNewChannelScope(CoroutineScope(IO))
    }

    private fun setupNewChannelScope(coroutineScope: CoroutineScope): CoroutineScope{
        channelScope = coroutineScope
        return channelScope as CoroutineScope
    }

    fun cancelJobs(){
        if(channelScope != null){
            if(channelScope?.isActive == true){
                channelScope?.cancel()
            }
            channelScope = null
        }
        clearActiveStateEventCounter()
    }

    private fun syncNumActiveStateEvents(){
        _numActiveJobs.value = _activeStateEvents.size
    }
}
