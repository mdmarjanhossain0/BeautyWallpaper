package com.appbytes.beautywallpaper.repository.main

import android.util.Log
import com.appbytes.beautywallpaper.api.main.MainApiService
import com.appbytes.beautywallpaper.api.main.response.Image
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.persistance.main.ImageDao
import com.appbytes.beautywallpaper.repository.NetworkBoundResource
import com.appbytes.beautywallpaper.ui.main.home.state.HomeViewState
import com.appbytes.beautywallpaper.util.Converter
import com.appbytes.beautywallpaper.util.DataState
import com.appbytes.beautywallpaper.util.StateEvent
import com.codingwithmitch.openapi.util.CacheResponseHandler
import com.codingwithmitch.openapi.util.CacheResult
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepositoryImpl
@Inject
    constructor(
            private val mainApiService: MainApiService,
            private val cacheDao: ImageDao
    )
    : HomeRepository {


    private val TAG = "HomeRepositoryImpl"
    override fun getPhotos(
            pageNumber: Int,
            per_page: Int,
            client_id: String,
            stateEvent: StateEvent)
    : Flow<DataState<HomeViewState>> {
        return object : NetworkBoundResource<List<Image>, List<CacheImage>, HomeViewState>(
                dispatcher = IO,
                stateEvent = stateEvent,
                apiCall = {
                    mainApiService.getNewPhotos(
                            page = pageNumber,
                            per_page = per_page,
                            client_id = client_id
                    )
                },
                cacheCall = {
                    cacheDao.getImages()
                }

        ){
            override suspend fun updateCache(networkObject: List<Image>) {
                val imageList = Converter.makeImage(networkObject)
                withContext(IO) {
                    for(image in imageList){
                        try{
                            // Launch each insert as a separate job to be executed in parallel
                            launch {
                                val insert = cacheDao.insert(image)
//                                Log.d(TAG, insert.toString())
                            }
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<CacheImage>): DataState<HomeViewState> {
                return DataState(
                        stateEvent = stateEvent,
                        stateMessage = null,
                        data = HomeViewState(
                                imageFields = HomeViewState.ImageFields(
                                    images = resultObj
                                )
                        )
                )
            }

        }.result
    }

    /*override fun getCacheData(stateEvent: StateEvent): Flow<DataState<HomeViewState>> {
        return object : NetworkBoundResource<List<Image>, List<CacheImage>, HomeViewState>(
                dispatcher = IO,
                stateEvent = stateEvent,
                apiCall = {
            mainApiService.getNewPhotos(
                    page = pageNumber,
                    per_page = per_page,
                    client_id = client_id
            )
        },
        cacheCall = {
            cacheDao.getImages()
        }

        ){
            override suspend fun updateCache(networkObject: List<Image>) {
                val imageList = Converter.makeImage(networkObject)
                withContext(IO) {
                    for(image in imageList){
                        try{
                            // Launch each insert as a separate job to be executed in parallel
                            launch {
                                val insert = cacheDao.insert(image)
//                                Log.d(TAG, insert.toString())
                            }
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<CacheImage>): DataState<HomeViewState> {
                return DataState(
                        stateEvent = stateEvent,
                        stateMessage = null,
                        data = HomeViewState(
                                imageFields = HomeViewState.ImageFields(
                                        images = resultObj
                                )
                        )
                )
            }

        }.result

    }*/

    /*override fun getCacheData(stateEvent: StateEvent): Flow<DataState<HomeViewState>> = flow {

        emit(
                withContext(IO) {
                    val result = cacheDao.getImages()
                    DataState<HomeViewState>(
                            data = HomeViewState(
                                    imageFields = HomeViewState.ImageFields(
                                            images = result
                                    )
                            )
                    )
                }
        )
    }*/


}