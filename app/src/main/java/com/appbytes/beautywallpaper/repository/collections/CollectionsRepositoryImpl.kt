package com.appbytes.beautywallpaper.repository.collections

import android.util.Log
import com.appbytes.beautywallpaper.api.main.MainApiService
import com.appbytes.beautywallpaper.api.main.response.Collections
import com.appbytes.beautywallpaper.api.main.response.Image
import com.appbytes.beautywallpaper.models.CacheCollections
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.persistance.CollectionsDao
import com.appbytes.beautywallpaper.persistance.CollectionsImagesDao
import com.appbytes.beautywallpaper.repository.NetworkBoundResource
import com.appbytes.beautywallpaper.ui.main.collections.state.CollectionsViewState
import com.appbytes.beautywallpaper.util.Converter
import com.appbytes.beautywallpaper.util.DataState
import com.appbytes.beautywallpaper.util.StateEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CollectionsRepositoryImpl
@Inject
constructor(
    private val mainApiService : MainApiService,
    private val collectionsDao: CollectionsDao,
    private val cacheCollectionsImages: CollectionsImagesDao
) : CollectionsRepository {


    private val TAG = "CollectionsRepoImpl"
    override fun getCollections(
            pageNumber: Int,
            per_page: Int,
            client_id: String,
            stateEvent: StateEvent)
            : Flow<DataState<CollectionsViewState>> {
        return object : NetworkBoundResource<List<Collections>, List<CacheCollections>, CollectionsViewState>(
                dispatcher = Dispatchers.IO,
                stateEvent = stateEvent,
                apiCall = {
                    mainApiService.getCollections(
                            page = pageNumber,
                            per_page = per_page,
                            client_id = client_id
                    )
                },
                cacheCall = {
                    collectionsDao.getCollections(pageNumber)
                },
                page_number = pageNumber

        ){
            var current_page_number = -1
            override suspend fun updateCache(networkObject: List<Collections>) {
//                current_page_number = pageNumber
                val imageList = Converter.makeCollections(networkObject)
                withContext(Dispatchers.IO) {
                    for(image in imageList){
                        try{
                            // Launch each insert as a separate job to be executed in parallel
                            launch {
                                val insert = collectionsDao.insert(image)
//                                Log.d(TAG, insert.toString())
                            }
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<CacheCollections>, page : Int): DataState<CollectionsViewState> {
                return DataState(
                        stateEvent = stateEvent,
                        stateMessage = null,
                        data = CollectionsViewState(
                                collectionsFields = CollectionsViewState.CollectionsFields(
                                        collections = resultObj,
                                        page_number = page
                                )
                        )
                )
                /*return if (current_page_number <= pageNumber || current_page_number == -1 ) {
                    DataState(
                            stateEvent = stateEvent,
                            stateMessage = null,
                            data = CollectionsViewState(
                                    collectionsFields = CollectionsViewState.CollectionsFields(
                                            collections = resultObj
                                    )
                            )
                    )
                }
                else {
                    DataState(
                            stateEvent = stateEvent,
                            stateMessage = null,
                            data = CollectionsViewState(
                                    collectionsFields = CollectionsViewState.CollectionsFields(
                                            collections = resultObj,
                                            page_number = current_page_number
                                    )
                            )
                    )
                }*/
            }

        }.result
    }


    override fun getCollectionsImages (
            collectionsId : String?,
            pageNumber: Int,
            per_page: Int,
            client_id: String,
            stateEvent: StateEvent)
            : Flow<DataState<CollectionsViewState>> {
        return object : NetworkBoundResource<List<Image>, List<CacheImage>, CollectionsViewState>(
                dispatcher = Dispatchers.IO,
                stateEvent = stateEvent,
                apiCall = {
                    mainApiService.getCollectionsById(
                            id = collectionsId,
                            page = pageNumber,
                            per_page = per_page,
                            client_id = client_id
                    )
                },
                cacheCall = {
                    cacheCollectionsImages.getCollectionsById(collectionsId = collectionsId, page = pageNumber)
                },
                page_number = pageNumber

        ){
            var current_page_number = -1
            override suspend fun updateCache(networkObject: List<Image>) {
//                current_page_number = page
                val imageList = Converter.makeCollectionsImages(networkObject, collectionsId)
                withContext(Dispatchers.IO) {
                    for(image in imageList){
                        try{
                            // Launch each insert as a separate job to be executed in parallel
                            launch {
                                val insert = cacheCollectionsImages.insert(image)
                                Log.d(TAG, "CollectionsDetails Data " +insert.toString())
                            }
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<CacheImage>, page : Int): DataState<CollectionsViewState> {
                return DataState(
                        stateEvent = stateEvent,
                        stateMessage = null,
                        data = CollectionsViewState(
                                collectionsDetailsFields = CollectionsViewState.CollectionsDetailsFields(
                                        collectionsImages = resultObj,
                                        page_number = page
                                )
                        )
                )
                /*return if (current_page_number <= pageNumber || current_page_number == -1 ) {
                    DataState(
                            stateEvent = stateEvent,
                            stateMessage = null,
                            data = CollectionsViewState(
                                    collectionsDetailsFields = CollectionsViewState.CollectionsDetailsFields(
                                            collectionsImages = resultObj
                                    )
                            )
                    )
                }
                else {
                    DataState(
                            stateEvent = stateEvent,
                            stateMessage = null,
                            data = CollectionsViewState(
                                    collectionsDetailsFields = CollectionsViewState.CollectionsDetailsFields(
                                            collectionsImages = resultObj,
                                            page_number = current_page_number
                                    )
                            )
                    )
                }*/
            }

        }.result
    }



}