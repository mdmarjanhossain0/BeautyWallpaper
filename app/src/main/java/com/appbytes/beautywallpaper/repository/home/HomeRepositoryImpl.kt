package com.appbytes.beautywallpaper.repository.home

import android.util.Log
import com.appbytes.beautywallpaper.api.main.MainApiService
import com.appbytes.beautywallpaper.api.main.response.Image
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.persistance.ImageDao
import com.appbytes.beautywallpaper.repository.NetworkBoundResource
import com.appbytes.beautywallpaper.ui.main.home.state.HomeViewState
import com.appbytes.beautywallpaper.util.Converter
import com.appbytes.beautywallpaper.util.DataState
import com.appbytes.beautywallpaper.util.StateEvent
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
                    cacheDao.getImages(pageNumber)
                },
                page_number = pageNumber

        ){
            var current_page_number : Int = -1
            override suspend fun updateCache(networkObject: List<Image>) {
//                current_page_number = page
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

            override fun handleCacheSuccess(resultObj: List<CacheImage>, page : Int): DataState<HomeViewState> {

                val imagesAll = cacheDao.getImagesAll()
                Log.d(TAG, "All images size check " + imagesAll.size)
                return DataState.data(
                        response = null,
                        data = HomeViewState(
                                imageFields = HomeViewState.ImageFields(
                                        images = resultObj,
                                        page_number = page
                                )
                        ),
                        stateEvent = stateEvent
                )

                /*return DataState(
                        stateEvent = stateEvent,
                        stateMessage = null,
                        data = HomeViewState(
                                imageFields = HomeViewState.ImageFields(
                                        images = resultObj,
                                        page_number = page
                                )
                        )
                )*/
            }

        }.result
    }

    override fun setLike(clickImage: CacheImage, pageNumber: Int, stateEvent: StateEvent): Flow<DataState<HomeViewState>>
            = flow {
        cacheDao.insert(clickImage)
        val updateResult = cacheDao.getImages(pageNumber)
        emit(
                DataState.data(
                        response = null,
                        data = HomeViewState(
                                imageFields = HomeViewState.ImageFields(
                                        images = updateResult,
                                        page_number = pageNumber
                                )
                        ),
                        stateEvent = stateEvent
                )
        )
    }


}