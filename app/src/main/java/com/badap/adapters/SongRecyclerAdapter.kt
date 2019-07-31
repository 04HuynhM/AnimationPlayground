package com.badap.adapters

import android.net.Uri
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badap.adapters.viewHolders.gridItems.GenericGridItemViewHolder
import com.badap.adapters.viewHolders.rows.LargeSongRow
import com.badap.adapters.viewHolders.rows.MediumSongRow
import com.badap.adapters.viewHolders.rows.SmallSongRow
import com.badap.R
import com.badap.Song
import com.badap.utilities.HelperMethods

class SongRecyclerAdapter(private val songList: ArrayList<Song>,
                          private val activity: FragmentActivity,
                          private val albumArtUri : Uri,
                          private val layoutManager: GridLayoutManager? = null)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val helper = HelperMethods()

    override fun getItemCount(): Int {
        return songList.size
    }

    enum class ViewType {
        SMALL_GRID,
        SMALL_ROW,
        MEDIUM_GRID,
        MEDIUM_ROW,
        LARGE_GRID,
        LARGE_ROW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            ViewType.SMALL_GRID.ordinal -> {
                val view = helper.getInflatedView(parent, R.layout.generic_grid_item)
                return GenericGridItemViewHolder(view, activity, (parent.measuredWidth / 4) - 16, albumArtUri)
            }
            ViewType.SMALL_ROW.ordinal -> {
                return SmallSongRow(helper.getInflatedView(parent, R.layout.small_song_row), activity, albumArtUri)
            }
            ViewType.MEDIUM_GRID.ordinal -> {
                val view = helper.getInflatedView(parent, R.layout.generic_grid_item)
                return GenericGridItemViewHolder(view, activity, (parent.measuredWidth / 3) - 16, albumArtUri)
            }
            ViewType.MEDIUM_ROW.ordinal -> {
                return MediumSongRow(helper.getInflatedView(parent, R.layout.medium_song_row), activity, albumArtUri)
            }
            ViewType.LARGE_GRID.ordinal -> {
                val view = helper.getInflatedView(parent, R.layout.generic_grid_item)
                return GenericGridItemViewHolder(view, activity, (parent.measuredWidth / 2) - 16, albumArtUri)
            }
            else -> {
                return LargeSongRow(helper.getInflatedView(parent, R.layout.large_song_item_row), activity, albumArtUri)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(layoutManager?.spanCount) {
            1 -> ViewType.LARGE_ROW.ordinal
            2 -> ViewType.LARGE_GRID.ordinal
            3 -> ViewType.MEDIUM_GRID.ordinal
            else -> ViewType.SMALL_GRID.ordinal
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val song = songList[position]

        when(holder) {
            is LargeSongRow -> {
                holder.bindViewHolder(song)
            }
            is MediumSongRow -> {
                holder.bindViewHolder(song)
            }
            is SmallSongRow -> {
                holder.bindViewHolder(song)
            }
            is GenericGridItemViewHolder -> {
                holder.bindViewHolder(song)
            }
        }
    }
}