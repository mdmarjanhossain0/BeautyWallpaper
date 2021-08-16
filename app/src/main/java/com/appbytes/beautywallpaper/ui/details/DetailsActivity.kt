package com.appbytes.beautywallpaper.ui.details

import android.content.Intent
import android.graphics.RectF
import android.graphics.drawable.Animatable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.models.DownloadItem
import com.appbytes.beautywallpaper.ui.details.viewmodel.DetailsViewModel
import com.appbytes.beautywallpaper.view.loadPhotoUrlWithThumbnail
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeController
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.item_download.*
import kotlin.math.max


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val TAG = "DetailsActivity"
    private lateinit var imageId : String
    private lateinit var url : String
    private var fileUri: Uri? = null

    private val viewModel : DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        getIntentData()
        loadImage()
        bottom_app_bar.setOnClickListener{
            when(it.id) {
                R.id.menu_download -> {
                    downloadClick()
                }
            }
        }
        setWallpaperClick()
    }

    private fun setWallpaperClick() {
    }

    private fun triggerSetWallpaper() {
        Log.d(TAG, "triggerSetWallpaper")
    }

    private fun downloadClick() {
        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.downloadImage(imageId).observe(this, Observer { data ->
            when(data.status) {
                DownloadItem.DOWNLOAD_STATUS_DOWNLOADING -> {
                    details_progress_bar.visibility = View.VISIBLE
                    details_progress_bar.progress = data.progress
                }

                DownloadItem.DOWNLOAD_STATUS_OK -> {
                    Toast.makeText(this@DetailsActivity,
                        "Download Completed", Toast.LENGTH_SHORT).show()
                    details_progress_bar.visibility = View.GONE
                    viewModel.downloadImage(imageId).removeObservers(this)
                }
                else ->{
                    Toast.makeText(this@DetailsActivity, "Error", Toast.LENGTH_SHORT).show()
                    details_progress_bar.visibility = View.GONE
                    viewModel.downloadImage(imageId).removeObservers(this)
                }
            }
        })
    }

    private fun loadImage() {
        Log.d(TAG, viewModel.toString())
//        val url = viewModel.getLoadImageUrl(imageId)
        details_image.loadPhotoUrlWithThumbnail(url, null)
    }

    private fun getIntentData() {
        imageId = intent.getStringExtra(IMAGE_ID)!!
        url = intent.getStringExtra(IMAGE_URL)!!
        viewModel.imageId = this.imageId
    }

    companion object {
        const val IMAGE_ID = "image_id"
        const val IMAGE_URL = "image_url"
        const val IMAGE_WIDTH = "image_width"
        const val IMAGE_HEIGHT = "image_height"
    }

    private fun updatePreviewImage(downloadItem: DownloadItem) {
        var width = details_image.width
        var height = details_image.height

        if (width == 0) {
            width = details_image.measuredWidth
        }

        if (height == 0) {
            height = details_image.measuredHeight
        }

        if (width == 0 || height == 0) {
            return
        }

//        fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri ?: intent.data
        fileUri = Uri.parse(downloadItem.filePath)


        val resize = max(width, height)

        val request = ImageRequestBuilder.newBuilderWithSource(fileUri)
            .setResizeOptions(ResizeOptions(resize, resize))
            .build()
        val controller = Fresco.newDraweeControllerBuilder()
            .setOldController(details_image.controller)
            .setImageRequest(request)
            .build() as PipelineDraweeController

        details_image.controller = controller
    }
}