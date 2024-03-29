package com.badap.adapters

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.badap.Album
import com.badap.MainActivity.Companion.generalUtil
import com.badap.R
import com.badap.adapters.viewHolders.gridItems.GenericGridItemViewHolder
import com.badap.adapters.viewHolders.rows.LargeAlbumRow
import com.badap.adapters.viewHolders.rows.MediumAlbumRow
import com.badap.adapters.viewHolders.rows.SmallAlbumRow

class AlbumRecyclerAdapter(private val albumList: ArrayList<Album>,
                           private val activity: FragmentActivity,
                           private val screenWidth: Int,
                           private var currentViewType: Int)
    : ListAdapter<Album, RecyclerView.ViewHolder>(ListItemCallback()) {

    class ListItemCallback : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.albumId == newItem.albumId
        }
    }

    enum class ViewType {
        LARGE_GRID,
        LARGE_ROW,
        MEDIUM_ROW,
        MEDIUM_GRID,
        SMALL_GRID,
        SMALL_ROW
    }

    fun setViewType(newViewType: Int) {
        this.currentViewType = newViewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            // Inflate layout for Large Grid
            ViewType.LARGE_GRID.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.generic_grid_item)
                view.layoutParams.height = (screenWidth / 2) - 16
                return GenericGridItemViewHolder(view, activity, (screenWidth / 2) - 16)
            }
            // Inflate layout for Medium Grid
            ViewType.MEDIUM_GRID.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.generic_grid_item)
                view.layoutParams.height = (screenWidth / 3) - 16
                return GenericGridItemViewHolder(view, activity, (screenWidth / 3) - 16)
            }
            // Inflate layout for Small Grid
            ViewType.SMALL_GRID.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.generic_grid_item)
                view.layoutParams.height = (screenWidth / 4) - 16
                return GenericGridItemViewHolder(view, activity, (screenWidth / 4) - 16)
            }
            // Inflate layout for Large Row
            ViewType.LARGE_ROW.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.large_album_item_row)
                view.layoutParams.height = (screenWidth / 2) - 120
                return LargeAlbumRow(view, activity)
            }
            // Inflate layout for Medium Row
            ViewType.MEDIUM_ROW.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.medium_album_row)
                view.layoutParams.height = (screenWidth / 3) - 80
                return MediumAlbumRow(view, activity)
            }
            // Inflate layout for Small Row
            else -> {
                val view = generalUtil.getInflatedView(parent, R.layout.small_album_row)
                view.layoutParams.height = (screenWidth / 4) - 80
                return SmallAlbumRow(view, activity)
            }
        }
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentViewType) {
            1 -> ViewType.LARGE_GRID.ordinal
            2 -> ViewType.LARGE_ROW.ordinal
            3 -> ViewType.MEDIUM_ROW.ordinal
            4 -> ViewType.MEDIUM_GRID.ordinal
            5 -> ViewType.SMALL_GRID.ordinal
            else -> ViewType.SMALL_ROW.ordinal
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val album = albumList[position]

        when(holder) {
            is LargeAlbumRow -> {
                holder.bindViewHolder(album, screenWidth / 2 - 166)
            }
            is MediumAlbumRow -> {
                holder.bindViewHolder(album, screenWidth / 3 - 100)
            }
            is SmallAlbumRow -> {
                holder.bindViewHolder(album, screenWidth / 4 - 80)
            }
            is GenericGridItemViewHolder -> {
                holder.bindViewHolder(album)
            }
        }
    }
}