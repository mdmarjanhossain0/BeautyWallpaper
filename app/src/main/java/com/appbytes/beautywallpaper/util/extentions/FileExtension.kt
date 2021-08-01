package com.appbytes.beautywallpaper.util.extentions

import android.content.Context
import com.appbytes.beautywallpaper.util.download.Pasteur
import java.io.File

fun File.getLengthInKB(): Long = length() / 1024 / 1024

fun File.notifyFileUpdated(ctx: Context) {
    insertToMedia(ctx, this)
}

private fun insertToMedia(context: Context, file: File) {
    val uri = MediaStoreExtensions.insertImage(context.contentResolver, file.path, file.name, "")
    Pasteur.info("insertToMedia", "insertToMedia success: $uri, original file: $file")
}