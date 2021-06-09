package com.appbytes.beautywallpaper.ui.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.models.CacheImage
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_test.view.*
import java.util.ArrayList

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var imageList : List<CacheImage> = ArrayList()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        this.context = parent.context
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_test,parent,false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url = imageList.get(position).thumb
        Glide.with(context)
                .load(url)
                .error(R.drawable.ic_user)
                .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun submitList(imageList: List<CacheImage>) {
        this.imageList = imageList
        notifyDataSetChanged()
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.ivTest
    }
}