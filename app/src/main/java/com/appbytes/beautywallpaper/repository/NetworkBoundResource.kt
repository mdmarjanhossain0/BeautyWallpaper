package com.appbytes.beautywallpaper.repository

import com.appbytes.beautywallpaper.util.*
import com.appbytes.beautywallpaper.util.ErrorHandling.Companion.NETWORK_ERROR
import com.appbytes.beautywallpaper.util.ErrorHandling.Companion.UNKNOWN_ERROR
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.appbytes.beautywallpaper.repository.buildError
import com.appbytes.beautywallpaper.util.ApiResult.*
import kotlinx.coroutines.flow.flow




@FlowPreview
abstract class NetworkBoundResource<NetworkObj, CacheObj, ViewState>
constructor(
        private val dispatcher: CoroutineDispatcher,
        private val stateEvent: StateEvent,
        private val apiCall: suspend () -> NetworkObj?,
        private val cacheCall: suspend () -> CacheObj?,
        private val page_number : Int = 0
)
{

    private val TAG: String = "AppDebug"

    val result: Flow<DataState<ViewState>> = flow {

        // ****** STEP 1: VIEW CACHE ******
//        emit(returnCache(markJobComplete = false, page = page_number + 1))

        // ****** STEP 2: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
        val apiResult = safeApiCall(dispatcher){apiCall.invoke()}

        when(apiResult){
            is GenericError -> {
                emit(
                        buildError(
                                apiResult.errorMessage?.let { it }?: UNKNOWN_ERROR,
                                UIComponentType.Dialog(),
                                stateEvent
                        )
                )
            }

            is NetworkError -> {
                emit(
                        buildError(
                                NETWORK_ERROR,
                                UIComponentType.Dialog(),
                                stateEvent
                        )
                )
            }

            is Success -> {
                if(apiResult.value == null){
                    emit(
                            buildError(
                                    UNKNOWN_ERROR,
                                    UIComponentType.Dialog(),
                                    stateEvent
                            )
                    )
                }
                else{
                    updateCache(apiResult.value as NetworkObj)
                }
            }
        }

        // ****** STEP 3: VIEW CACHE and MARK JOB COMPLETED ******
        emit(returnCache(markJobComplete = true, page = page_number + 1))
    }

    private suspend fun returnCache(markJobComplete: Boolean, page : Int): DataState<ViewState> {

        val cacheResult = safeCacheCall(dispatcher){cacheCall.invoke()}

        var jobCompleteMarker: StateEvent? = null
        if(markJobComplete){
            jobCompleteMarker = stateEvent
        }

        return object: CacheResponseHandler<ViewState, CacheObj>(
                response = cacheResult,
                stateEvent = jobCompleteMarker
        ) {
            override suspend fun handleSuccess(resultObj: CacheObj): DataState<ViewState> {
                return handleCacheSuccess(resultObj, page)
            }
        }.getResult()

    }

    abstract suspend fun updateCache(networkObject: NetworkObj)

    abstract fun handleCacheSuccess(resultObj: CacheObj, page: Int): DataState<ViewState> // make sure to return null for stateEvent



    fun buildError(
            message: String,
            uiComponentType: UIComponentType,
            stateEvent: StateEvent?
    ): DataState<ViewState> {
        return DataState.error(
                response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${message}",
                        uiComponentType = uiComponentType,
                        messageType = MessageType.Error()
                ),
                stateEvent = stateEvent
        )

    }

}






/*@FlowPreview
abstract class NetworkBoundResource<NetworkObj, CacheObj, ViewState>
constructor(
        private val dispatcher: CoroutineDispatcher,
        private val stateEvent: StateEvent,
        private val apiCall: suspend () -> NetworkObj?,
        private val cacheCall: suspend () -> CacheObj?,
        val page_number : Int = -1
)
{

    private val TAG: String = "AppDebug"

    val result: Flow<DataState<ViewState>> = flow {
        print("enter")

        // ****** STEP 1: VIEW CACHE ******
        emit(returnCache(markJobComplete = false, page = page_number))

        // ****** STEP 2: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
        val apiResult = safeApiCall(dispatcher){apiCall.invoke()}

        when(apiResult){
            is ApiResult.GenericError -> {
                emit(
                        buildError(
                                apiResult.errorMessage?.let { it }?: UNKNOWN_ERROR,
                                UIComponentType.Dialog(),
                                stateEvent
                        )
                )
            }

            is ApiResult.NetworkError -> {
                emit(
                        buildError(
                                NETWORK_ERROR,
                                UIComponentType.Dialog(),
                                stateEvent
                        )
                )
            }

            is ApiResult.Success -> {
                if(apiResult.value == null){
                    emit(
                            buildError(
                                    UNKNOWN_ERROR,
                                    UIComponentType.Dialog(),
                                    stateEvent
                            )
                    )
                }
                else{
                    updateCache(apiResult.value as NetworkObj, page_number + 1 )
                }
            }
        }

        // ****** STEP 3: VIEW CACHE and MARK JOB COMPLETED ******
        emit(returnCache(markJobComplete = true, page= page_number + 1))
    }

    fun buildError(
            message: String,
            uiComponentType: UIComponentType,
            stateEvent: StateEvent?
    ): DataState<ViewState> {
        return DataState.error(
                response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${message}",
                        uiComponentType = uiComponentType,
                        messageType = MessageType.Error()
                ),
                stateEvent = stateEvent
        )

    }

    private suspend fun returnCache(markJobComplete: Boolean, page: Int): DataState<ViewState> {

        val cacheResult = safeCacheCall(dispatcher){cacheCall.invoke()}

        var jobCompleteMarker: StateEvent? = null
        if(markJobComplete){
            jobCompleteMarker = stateEvent
        }

        return object: CacheResponseHandler<ViewState, CacheObj>(
                response = cacheResult,
                stateEvent = jobCompleteMarker
        ) {
            override suspend fun handleSuccess(resultObj: CacheObj): DataState<ViewState> {
                return handleCacheSuccess(resultObj, page)
            }
        }.getResult()

    }

    abstract suspend fun updateCache(networkObject: NetworkObj, page_number: Int)

    abstract fun handleCacheSuccess(resultObj: CacheObj, page_number: Int): DataState<ViewState> // make sure to return null for stateEvent


}*/













