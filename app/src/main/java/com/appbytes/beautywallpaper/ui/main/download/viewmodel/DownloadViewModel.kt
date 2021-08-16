package com.appbytes.beautywallpaper.ui.main.download.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.appbytes.beautywallpaper.models.DownloadItem
import com.appbytes.beautywallpaper.repository.download.DownloadRepository


class DownloadViewModel
@ViewModelInject
constructor(
    private val downloadRepository: DownloadRepository,
    @Assisted
    stateHandle: SavedStateHandle
) : ViewModel() {

    val downloadItems : LiveData<List<DownloadItem>>
         get() = downloadRepository.downloadItems
}