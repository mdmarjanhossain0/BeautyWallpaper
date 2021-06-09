package com.appbytes.beautywallpaper.repository

import android.util.Log
import com.appbytes.beautywallpaper.util.*
import com.appbytes.beautywallpaper.util.ErrorHandling.Companion.NETWORK_ERROR
import com.appbytes.beautywallpaper.util.ErrorHandling.Companion.UNKNOWN_ERROR
import com.codingwithmitch.openapi.util.CacheResponseHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@FlowPreview
abstract class NetworkBoundResource<NetworkObj, CacheObj, ViewState>
constructor(
        private val dispatcher: CoroutineDispatcher,
        private val stateEvent: StateEvent,
        private val apiCall: suspend () -> NetworkObj?,
        private val cacheCall: suspend () -> CacheObj?
)
{

    private val TAG: String = "AppDebug"

    val result: Flow<DataState<ViewState>> = flow {
        Log.d("enter", "enter")
        Log.e("enter", "enter")
        print("enter")

        // ****** STEP 1: VIEW CACHE ******
        emit(returnCache(markJobComplete = false))

        // ****** STEP 2: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
        val apiResult = safeApiCall(dispatcher){apiCall.invoke()}

        Log.d(TAG, "Api Result " + apiResult.toString())

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
                    updateCache(apiResult.value as NetworkObj)
                }
            }
        }

        // ****** STEP 3: VIEW CACHE and MARK JOB COMPLETED ******
        emit(returnCache(markJobComplete = true))
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

    private suspend fun returnCache(markJobComplete: Boolean): DataState<ViewState> {
        Log.d(TAG,"sdfdsfdsfdsfdsfdfdfsdfdsfdsf")

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
                return handleCacheSuccess(resultObj)
            }
        }.getResult()

    }

    abstract suspend fun updateCache(networkObject: NetworkObj)

    abstract fun handleCacheSuccess(resultObj: CacheObj): DataState<ViewState> // make sure to return null for stateEvent


}













