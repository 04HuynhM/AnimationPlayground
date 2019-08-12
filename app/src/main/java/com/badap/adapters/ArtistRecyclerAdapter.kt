package com.badap.adapters

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.badap.Artist
import com.badap.MainActivity.Companion.generalUtil
import com.badap.R
import com.badap.adapters.viewHolders.ArtistGridItem
import com.badap.adapters.viewHolders.LargeArtistRow
import com.badap.adapters.viewHolders.MediumArtistRow
import com.badap.adapters.viewHolders.SmallArtistRow
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ArtistRecyclerAdapter(private val artistList: ArrayList<Artist>,
                            private val activity: FragmentActivity,
                            private val screenWidth: Int,
                            private var currentViewType: Int
) : ListAdapter<Artist, RecyclerView.ViewHolder>(ListItemCallback()) {

    class ListItemCallback : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.artistId == newItem.artistId
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
        when (viewType) {
            ViewType.LARGE_GRID.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.artist_grid_item)
                view.layoutParams.height = (screenWidth / 2) - 16
                return ArtistGridItem(view, activity, (screenWidth / 2) - 16)
            }
            // Inflate layout for Medium Grid
            ViewType.MEDIUM_GRID.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.artist_grid_item)
                view.layoutParams.height = (screenWidth / 3) - 16
                return ArtistGridItem(view, activity, (screenWidth / 3) - 16)
            }
            // Inflate layout for Small Grid
            ViewType.SMALL_GRID.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.artist_grid_item)
                view.layoutParams.height = (screenWidth / 4) - 16
                return ArtistGridItem(view, activity, (screenWidth / 4) - 16)
            }
            // Inflate layout for Large Row
            ViewType.LARGE_ROW.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.large_artist_item_row)
                view.layoutParams.height = (screenWidth / 2) - 120
                return LargeArtistRow(view, activity)
            }
            // Inflate layout for Medium Row
            ViewType.MEDIUM_ROW.ordinal -> {
                val view = generalUtil.getInflatedView(parent, R.layout.medium_artist_row)
                view.layoutParams.height = (screenWidth / 3) - 80
                return MediumArtistRow(view, activity)
            }
            // Inflate layout for Small Row
            else -> {
                val view = generalUtil.getInflatedView(parent, R.layout.small_artist_row)
                view.layoutParams.height = (screenWidth / 4) - 80
                return SmallArtistRow(view, activity)
            }
        }
    }

    override fun getItemCount(): Int {
        return artistList.size
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
        val artist = artistList[position]
        when (holder) {
            is LargeArtistRow -> {
                holder.bindViewHolder(artist, screenWidth / 2 - 166)
            }
            is MediumArtistRow -> {
                holder.bindViewHolder(artist, screenWidth / 3 - 100)
            }
            is SmallArtistRow -> {
                holder.bindViewHolder(artist, screenWidth / 4 - 80)
            }
            is ArtistGridItem -> {
                holder.bindViewHolder(artist)
            }
        }
    }
}