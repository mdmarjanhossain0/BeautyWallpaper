package com.appbytes.beautywallpaper.util.download

import android.net.Uri
import android.os.Environment
import com.appbytes.beautywallpaper.BaseApplication
import com.facebook.binaryresource.FileBinaryResource
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.imagepipeline.request.ImageRequest
import java.io.File

object FileUtils {
    val downloadOutputDir: String?
        get() {
            val filesDir = BaseApplication.instance.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val dir = File(filesDir, "MyerSplash")

            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    return null
                }
            }

            return dir.absolutePath
        }

    val cachedPath: String?
        get() = BaseApplication.instance.cacheDir.absolutePath

    fun getCachedFile(url: String): File? {
        val cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(
                ImageRequest.fromUri(Uri.parse(url)), null)

        var localFile: File? = null

        if (cacheKey != null) {
            if (ImagePipelineFactory.getInstance().mainFileCache.hasKey(cacheKey)) {
                val resource = ImagePipelineFactory.getInstance().mainFileCache.getResource(cacheKey)
                localFile = (resource as? FileBinaryResource)?.file
            }
        }

        return localFile
    }
}