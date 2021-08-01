package com.appbytes.beautywallpaper.util

class Constants {

    companion object{

        const val BASE_URL = "https://api.unsplash.com/"
        const val unsplash_access_key = "5w-oIYi9TM5URHX-BARW3xaZGsZjggUngj6pBKkRVEI"
        const val NEW_ID = "4807737"
        const val collectionid = "collectionid"
        const val photoid = "photoid"
        const val DatabasePATH = "gs://wallsplash-880c5.appspot.com"
        const val username = "username"
        const val searchKey = "searchKey"


        const val NETWORK_TIMEOUT = 6000L
        const val CACHE_TIMEOUT = 2000L
        const val TESTING_NETWORK_DELAY = 0L // fake network delay for testing
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing


        const val PAGINATION_PAGE_SIZE = 10

        const val GALLERY_REQUEST_CODE = 201
        const val PERMISSIONS_REQUEST_READ_STORAGE: Int = 301
        const val CROP_IMAGE_INTENT_CODE: Int = 401

        const val PER_PAGE = 10
        const val PAGE = 1



        const val DOWNLOAD_TIMEOUT_MS = 30_000L
    }
}