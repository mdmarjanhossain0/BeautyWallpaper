package com.appbytes.beautywallpaper.ui.main.collections.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.models.CacheImage
import com.appbytes.beautywallpaper.util.GenericViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_test.view.*
import java.util.ArrayList


class CollectionsDetailsAdapter
constructor(
        private val interaction: Interaction? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "ColDetailsAdapter"

    companion object {

        const val IMAGE_ITEM = 1
        const val LOADING_ITEM = 2
    }

    private var imageList : List<CacheImage> = ArrayList()
//    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        this.context = parent.context

        when(viewType) {

            IMAGE_ITEM -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_test,parent,false)
                return ImageViewHolder(
                    itemView,
                    interaction
                )
            }

            LOADING_ITEM -> {
                Log.d(TAG, "onCreateViewHolder: No more results...")
                return GenericViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                                R.layout.item_loading,
                                parent,
                                false
                        )
                )
            }
        }
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_test,parent,false)
        return ImageViewHolder(
            itemView,
            interaction
        )
    }

    override fun getItemViewType(position: Int): Int {
        if(differ.currentList.size == (position + 1)){
            interaction?.nextPage()
            return LOADING_ITEM
        }
        return IMAGE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {

            is ImageViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CacheImage>() {

        override fun areItemsTheSame(oldItem: CacheImage, newItem: CacheImage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CacheImage, newItem: CacheImage): Boolean {
            return oldItem == newItem
        }

    }

    private val differ =
            AsyncListDiffer(
                    ImageRecyclerChangeCallback(this),
                    AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
            )

    internal inner class ImageRecyclerChangeCallback(
            private val adapter: CollectionsDetailsAdapter
    ) : ListUpdateCallback
    {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    fun submitList(imageList: List<CacheImage>?) {
        /*if (imageList != null) {
            this.imageList = imageList
            notifyDataSetChanged()
        }*/
        val newList = imageList?.toMutableList()
        val commitCallback = Runnable {
            // if process died must restore list position
            // very annoying
            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)

    }

    class ImageViewHolder
    constructor(
            itemView: View,
            private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.ivTest


        fun bind(item: CacheImage) = with(itemView) {
            imageView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            val url = item.smallImageUrl
            Glide.with(context)
                    .load(url)
                    .error(R.drawable.ic_user)
                    .into(imageView)
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: CacheImage)

        fun restoreListPosition()

        fun nextPage()
    }
}