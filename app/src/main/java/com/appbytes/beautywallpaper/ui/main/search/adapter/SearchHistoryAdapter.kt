package com.appbytes.beautywallpaper.ui.main.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.models.CacheKey
import kotlinx.android.synthetic.main.item_search_history.view.*


class SearchHistoryAdapter
constructor(
        private val interaction: Interaction? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "SearchHistoryAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_search_history,parent,false)
        return ImageViewHolder(
            itemView,
            interaction
        )
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

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CacheKey>() {

        override fun areItemsTheSame(oldItem: CacheKey, newItem: CacheKey): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: CacheKey, newItem: CacheKey): Boolean {
            return oldItem == newItem
        }

    }

    private val differ =
            AsyncListDiffer(
                    ImageRecyclerChangeCallback(this),
                    AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
            )

    internal inner class ImageRecyclerChangeCallback(
            private val historyAdapter: SearchHistoryAdapter
    ) : ListUpdateCallback
    {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            historyAdapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            historyAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            historyAdapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            historyAdapter.notifyDataSetChanged()
        }
    }

    fun submitList(imageList: List<CacheKey>?) {
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
        val history_text = itemView.id_history


        fun bind(item: CacheKey) = with(itemView) {
            history_text.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            history_text.setText(item.key)
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: CacheKey)

        fun restoreListPosition()

        fun onDelete(position: Int, item: CacheKey)
    }
}