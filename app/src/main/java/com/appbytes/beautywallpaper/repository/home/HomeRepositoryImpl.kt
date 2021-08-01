package com.appbytes.beautywallpaper.repository.home

import android.util.Log
import com.appbytes.beautywallpaper.api.MainApiService
import com.appbytes.beautywallpaper.api.response.Image
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
            override suspend fun updateCache(networkObject: List<Image>) {
                Log.d(TAG, "update Cache " + networkObject.size)
                val imageList = Converter.makeImage(networkObject)
                withContext(IO) {
                    for(image in imageList){
                        try{
                            // Launch each insert as a separate job to be executed in parallel
                            launch {
                                cacheDao.insert(image)
                            }
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<CacheImage>, page : Int): DataState<HomeViewState> {

                var calculate_page = page
                if(resultObj.size == 0) {
                    calculate_page = page -1
                }

                if (calculate_page < 1) {
                    calculate_page = 1
                }

                val imagesAll = cacheDao.getImagesAll()
                Log.d(TAG, "All images size check " + imagesAll.size)
                return DataState.data(
                        response = null,
                        data = HomeViewState(
                                imageFields = HomeViewState.ImageFields(
                                        images = resultObj,
                                        page_number = calculate_page
                                )
                        ),
                        stateEvent = stateEvent
                )
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