package com.badap.adapters

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.badap.MainActivity.Companion.generalUtil
import com.badap.R
import com.badap.SongEntity
import com.badap.adapters.adapterViewHolders.GenericGridItemViewHolder
import com.badap.adapters.adapterViewHolders.LargeSongRow
import com.badap.adapters.adapterViewHolders.MediumSongRow
import com.badap.adapters.adapterViewHolders.SmallSongRow

class SongRecyclerAdapter(private val songList: ArrayList<SongEntity>,
                          private val activity: FragmentActivity,
                          private val screenWidth: Int,
                          private var currentViewType: Int)
    : ListAdapter<SongEntity, RecyclerView.ViewHolder>(ListItemCallback()) {

    class ListItemCallback : DiffUtil.ItemCallback<SongEntity>() {
        override fun areItemsTheSame(oldItem: SongEntity, newItem: SongEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SongEntity, newItem: SongEntity): Boolean {
            return oldItem.songId == newItem.songId
        }
    }

    fun setViewType(newViewType: Int) {
        this.currentViewType = newViewType
    }

    enum class ViewType {
        LARGE_GRID,
        LARGE_ROW,
        MEDIUM_ROW,
        MEDIUM_GRID,
        SMALL_GRID,
        SMALL_ROW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            // Inflate layout for Large Grid
            ViewType.LARGE_GRID.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.generic_grid_item)
                view.layoutParams.height = (screenWidth / 2) - 16
                return GenericGridItemViewHolder(
                    view,
                    activity,
                    (screenWidth / 2) - 16
                )
            }
            // Inflate layout for Medium Grid
            ViewType.MEDIUM_GRID.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.generic_grid_item)
                view.layoutParams.height = (screenWidth / 3) - 16
                return GenericGridItemViewHolder(
                    view,
                    activity,
                    (screenWidth / 3) - 16
                )
            }
            // Inflate layout for Small Grid
            ViewType.SMALL_GRID.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.generic_grid_item)
                view.layoutParams.height = (screenWidth / 4) - 16
                return GenericGridItemViewHolder(
                    view,
                    activity,
                    (screenWidth / 4) - 16
                )
            }
            // Inflate layout for Large Row
            ViewType.LARGE_ROW.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.large_song_item_row)
                view.layoutParams.height = (screenWidth / 2) - 120
                return LargeSongRow(view, activity)
            }
            // Inflate layout for Medium Row
            ViewType.MEDIUM_ROW.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.medium_song_row)
                view.layoutParams.height = (screenWidth / 3) - 80
                return MediumSongRow(view, activity)
            }
            // Inflate layout for Small Row
            else -> {
                val view = generalUtil.getInflatedView(parent, R.layout.small_song_row)
                view.layoutParams.height = (screenWidth / 4) - 80
                return SmallSongRow(view, activity)
            }
        }
    }

    override fun getItemCount(): Int {
        return songList.size
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
        val song = songList[position]
        when(holder) {
            is LargeSongRow -> {
                holder.bindViewHolder(song, screenWidth / 2 - 166)
            }
            is MediumSongRow -> {
                holder.bindViewHolder(song, screenWidth / 3 - 100)
            }
            is SmallSongRow -> {
                holder.bindViewHolder(song, screenWidth / 4 - 80)
            }
            is GenericGridItemViewHolder -> {
                holder.bindViewHolder(song)
            }
        }
    }
}