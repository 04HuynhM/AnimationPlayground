package com.badap.fragments.songs

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badap.MainActivity
import com.badap.R
import com.badap.adapters.SongRecyclerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


class AllSongsFragment : Fragment() {

    lateinit var adapter: SongRecyclerAdapter
    lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_songs, container, false)
        setHasOptionsMenu(true)
        requireActivity().title = "Songs"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = GridLayoutManager(requireContext(), 12)
        val recyclerView = view.findViewById<RecyclerView>(R.id.all_songs_recycler)

        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val initialViewType = prefs.getInt("view_mode", 1)
        val screenWidth = prefs.getInt("screen_width", -1)

        MainActivity.indexedSongs?.let {
            adapter = SongRecyclerAdapter(it, requireActivity(), screenWidth, initialViewType)
            recyclerView.adapter = adapter
        }

        recyclerView.layoutManager = layoutManager

        layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(adapter.getItemViewType(position)) {
                    0 -> 6 // large grid
                    1 -> 12 // large row
                    2 -> 12 // medium row
                    3 -> 4 // medium grid
                    4 -> 3 // small grid
                    5 -> 12 // small row
                    else -> -1
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.update_library_index -> {
                val activity = requireActivity() as MainActivity
                activity.initializeLibraryArrays()
            }
            R.id.large_grid_option -> {
                setViewType(1)
            }
            R.id.large_row_option -> {
                setViewType(2)
            }
            R.id.medium_grid_option -> {
                setViewType(4)
            }
            R.id.medium_row_option -> {
                setViewType(3)
            }
            R.id.small_grid_option -> {
                setViewType(5)
            }
            R.id.small_row_option -> {
                setViewType(6)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setViewType(viewType: Int) {
        adapter.setViewType(viewType)
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
        prefs.edit { putInt("view_mode", viewType)?.apply() }
    }
}
