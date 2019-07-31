package com.badap.adapters

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.badap.adapters.viewHolders.gridItems.GenericGridItemViewHolder
import com.badap.adapters.viewHolders.rows.LargeAlbumRow
import com.badap.adapters.viewHolders.rows.MediumAlbumRow
import com.badap.adapters.viewHolders.rows.SmallAlbumRow
import com.badap.Album
import com.badap.R
import com.badap.utilities.HelperMethods

class AlbumRecyclerAdapter(private val albumList: ArrayList<Album>,
                           private val activity: FragmentActivity,
                           private val screenWidth: Int)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val helper = HelperMethods()
    private var viewMode = 1

    enum class ViewType {
        SMALL_ROW,
        MEDIUM_ROW,
        LARGE_ROW,
        SMALL_GRID,
        MEDIUM_GRID,
        LARGE_GRID
    }

    fun setSpan(viewMode: Int) {
        this.viewMode = viewMode
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            // Inflate layout for Large Grid
            ViewType.LARGE_GRID.ordinal -> {
                val view = helper.getInflatedView(parent, R.layout.generic_grid_item)
                view.layoutParams.height = (screenWidth / 2) - 16
                return GenericGridItemViewHolder(view, activity, (screenWidth / 2) - 16, null)
            }
            // Inflate layout for Medium Grid
            ViewType.MEDIUM_GRID.ordinal -> {
                val view = helper.getInflatedView(parent, R.layout.generic_grid_item)
                view.layoutParams.height = (screenWidth / 3) - 16
                return GenericGridItemViewHolder(view, activity, (screenWidth / 3) - 16, null)
            }
            // Inflate layout for Small Grid
            ViewType.SMALL_GRID.ordinal -> {
                val view = helper.getInflatedView(parent, R.layout.generic_grid_item)
                view.layoutParams.height = (screenWidth / 4) - 16
                return GenericGridItemViewHolder(view, activity, (screenWidth / 4) - 16, null)
            }
            // Inflate layout for Large Row
            ViewType.LARGE_ROW.ordinal -> {
                val view = helper.getInflatedView(parent, R.layout.large_album_item_row)
                view.layoutParams.height = (screenWidth / 2) - 120
                return LargeAlbumRow(view, activity)
            }
            // Inflate layout for Medium Row
            ViewType.MEDIUM_ROW.ordinal -> {
                val view = helper.getInflatedView(parent, R.layout.medium_album_row)
                view.layoutParams.height = (screenWidth / 3) - 80
                return MediumAlbumRow(view, activity)
            }
            // Inflate layout for Small Row
            else -> {
                val view = helper.getInflatedView(parent, R.layout.small_album_row)
                view.layoutParams.height = (screenWidth / 4) - 80
                return SmallAlbumRow(view, activity)
            }
        }
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewMode) {
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