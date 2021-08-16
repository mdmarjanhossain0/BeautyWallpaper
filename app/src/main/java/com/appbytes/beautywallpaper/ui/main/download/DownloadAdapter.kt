package com.appbytes.beautywallpaper.ui.main.download

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.appbytes.beautywallpaper.R
import com.appbytes.beautywallpaper.models.DownloadItem
import com.appbytes.beautywallpaper.models.DownloadItem.Companion.DOWNLOAD_STATUS_DOWNLOADING
import com.appbytes.beautywallpaper.models.DownloadItem.Companion.DOWNLOAD_STATUS_FAILED
import com.appbytes.beautywallpaper.models.DownloadItem.Companion.DOWNLOAD_STATUS_OK
import com.appbytes.beautywallpaper.view.loadPhotoUrlWithThumbnail
import com.appbytes.beautywallpaper.view.setAspectRatio
import kotlinx.android.synthetic.main.activity_details.view.*
import kotlinx.android.synthetic.main.item_download.view.*


class DownloadAdapter(
    private val interaction : Interaction
)
    : RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_download,parent, false)
        return DownloadViewHolder(view)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        holder.bind(differ.currentList.get(position))
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DownloadItem>() {

        override fun areItemsTheSame(oldItem: DownloadItem, newItem: DownloadItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DownloadItem, newItem: DownloadItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ =
        AsyncListDiffer(
            DownloadRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class DownloadRecyclerChangeCallback(
        private val adapter: DownloadAdapter
    ) : ListUpdateCallback {

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

    fun submitList(imageList: List<DownloadItem>) {
        val newList = imageList.toMutableList()
        val commitCallback = Runnable {
            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)

    }


    class DownloadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: DownloadItem?) {
            val url = item?.thumbUrl
            itemView.download_image_view.setAspectRatio(item?.width, item?.height)
            itemView.download_image_view.loadPhotoUrlWithThumbnail(
                url!!,
            null)
            val status = when(item.status) {
                DOWNLOAD_STATUS_DOWNLOADING -> "Downloading"
                DOWNLOAD_STATUS_OK -> "Completed"
                DOWNLOAD_STATUS_FAILED -> "Retry"
                else -> {
                    "..."
                }
            }
            itemView.download_status.setText(status)
            if(item.status == DOWNLOAD_STATUS_DOWNLOADING){
                itemView.download_progress_bar.progress = item.progress
            }
            else {
                itemView.download_progress_bar.visibility = View.GONE
            }
        }
    }


    interface Interaction {

        fun onItemSelected(position: Int, item: DownloadItem)

        fun restoreListPosition()

        fun onDownloadCancel(position: Int, item: DownloadItem)

        fun onDeleteClick(position: Int, item: DownloadItem)
    }
}