package com.appbytes.beautywallpaper.ui.details.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appbytes.beautywallpaper.models.DownloadItem
import com.appbytes.beautywallpaper.repository.details.DetailsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DetailsViewModel
@ViewModelInject
constructor(
    private val detailsRepository: DetailsRepository,
    @Assisted
    stateHandle: SavedStateHandle ) : ViewModel() {

    var imageId : String = ""
        set

    fun getLoadImageUrl(imageId: String) : String {
            return detailsRepository.getLoadUrl(imageId)
    }

    fun downloadImage(imageId : String) : LiveData<DownloadItem> {
        return detailsRepository.downloadImage(imageId)
    }

    fun setUpWallpaper(imageId: String) : String? {
        return " "
    }

    fun shareImage(imageId: String) : String {
        return " "
    }

}