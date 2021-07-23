package com.appbytes.beautywallpaper.repository.search

import android.util.Log
import com.appbytes.beautywallpaper.api.main.MainApiService
import com.appbytes.beautywallpaper.api.main.response.Image
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.models.CacheKey
import com.appbytes.beautywallpaper.persistance.SearchDao
import com.appbytes.beautywallpaper.repository.NetworkBoundResource
import com.appbytes.beautywallpaper.ui.main.search.state.SearchViewState
import com.appbytes.beautywallpaper.util.Converter
import com.appbytes.beautywallpaper.util.DataState
import com.appbytes.beautywallpaper.util.StateEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchRepositoryImpl
//@Inject
constructor(
        private val mainApiService: MainApiService,
        private val searchDao: SearchDao
) : SearchRepository {

    private val TAG = "SearchRepositoryImpl"
    override fun getSearchPhotos(
            query : String,
            pageNumber: Int,
            per_page: Int,
            client_id: String,
            stateEvent: StateEvent): Flow<DataState<SearchViewState>> {
        return object : NetworkBoundResource<List<Image>, List<CacheImage>, SearchViewState>(
                dispatcher = IO,
                stateEvent = stateEvent,
                apiCall = {
                    mainApiService.getSearch(
                            query = query,
                            page = pageNumber,
                            perPage = per_page,
                            client_id = client_id
                    )
                },
                cacheCall = {
                    searchDao.getImages(pageNumber = pageNumber,
                            query = query)
                },
                page_number = pageNumber
        ) {
            override suspend fun updateCache(networkObject: List<Image>) {
                val imageList = Converter.makeImageFromSearch(networkObject, query)
                setSearchKey(query)
                withContext(IO) {
                    for(image in imageList){
                        try{
                            // Launch each insert as a separate job to be executed in parallel
                            launch {
                                val insert = searchDao.insert(image)
//                                Log.d(TAG, insert.toString())
                            }
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun handleCacheSuccess(resultObj: List<CacheImage>, page: Int): DataState<SearchViewState> {
                val imagesAll = searchDao.getImagesAll()
                Log.d(TAG, "All images size check " + imagesAll.size)
                setSearchKey(query)
                return DataState.data(
                        response = null,
                        data = SearchViewState(
                                searchFields = SearchViewState.SearchFields(
                                        images = resultObj,
                                        page_number = page
                                )
                        ),
                        stateEvent = stateEvent
                )
            }

        }.result
    }

    override fun getSearchKeys(stateEvent: StateEvent): Flow<DataState<SearchViewState>>
            = flow {
        val updateResult = searchDao.getKeys()
        emit(
                DataState.data(
                        response = null,
                        data = SearchViewState(
                                searchKeys = SearchViewState.SearchKeys(
                                        keys = updateResult
                                )
                        ),
                        stateEvent = stateEvent
                )
        )
    }

    override fun deleteKey(
        stateEvent: StateEvent,
        key: CacheKey
    ) = flow {
        searchDao.deleteKey(key)
        val updateResult = searchDao.getKeys()
        emit(
            DataState.data(
                response = null,
                data = SearchViewState(
                    searchKeys = SearchViewState.SearchKeys(
                        keys = updateResult
                    )
                ),
                stateEvent = stateEvent
            )
        )

    }

    fun setSearchKey(key : String) {
        searchDao.insertKey(CacheKey(key = key))
    }
}