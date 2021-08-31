package com.appbytes.beautywallpaper.ui.main.collections.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.models.CacheCollections
import com.appbytes.beautywallpaper.util.GenericViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collections.view.*
import kotlinx.android.synthetic.main.item_test.view.ivTest
import java.util.ArrayList


class CollectionsAdapter
constructor(
        private val interaction: Interaction? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "CollectionsAdapter"

    companion object {

        const val COLLECTIONS_ITEM = 1
        const val LOADING_ITEM = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType) {

            COLLECTIONS_ITEM -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_collections,parent,false)
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
        return COLLECTIONS_ITEM
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

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CacheCollections>() {

        override fun areItemsTheSame(oldItem: CacheCollections, newItem: CacheCollections): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CacheCollections, newItem: CacheCollections): Boolean {
            return oldItem == newItem
        }

    }

    private val differ =
            AsyncListDiffer(
                    CollectionsRecyclerCallback(this),
                    AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
            )

    internal inner class CollectionsRecyclerCallback(
            private val adapter: CollectionsAdapter
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

    fun submitList(collectionsList: List<CacheCollections>?) {
        val newList = collectionsList?.toMutableList()
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
        val imageView = itemView.ivTestCol


        fun bind(item: CacheCollections) = with(itemView) {
            imageView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            val url = item.coverPhoto
            Glide.with(context)
                    .load(url)
                    .error(R.drawable.ic_user)
                    .into(imageView)


            title.setText(item.title)
            total_count.setText(item.totalPhotos.toString())
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: CacheCollections)

        fun restoreListPosition()

        fun nextPage()
    }
}