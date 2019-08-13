package com.badap.fragments.albums

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badap.MainActivity.Companion.mediaStoreUtil
import com.badap.R
import com.badap.adapters.AlbumRecyclerAdapter


class AlbumsFragment : Fragment(){

    lateinit var adapter: AlbumRecyclerAdapter
    lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val artistId = bundle?.get("artistId") as Long

        val layoutManager = GridLayoutManager(requireContext(), 12)
        val recyclerView = view.findViewById<RecyclerView>(R.id.albums_recycler)

        val albumList = mediaStoreUtil.getAllAlbumsForArtist(requireContext(), artistId)
        prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val initialViewMode = prefs.getInt("view_mode", 1)
        val screenWidth = prefs.getInt("screen_width", -1)

        val viewTypeRadioGroup: RadioGroup = view.findViewById(R.id.albums_drawer_viewtype_radiogroup)
        viewTypeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.albums_drawer_row_large -> {
                    setViewType(2)
                }
                R.id.albums_drawer_row_medium -> {
                    setViewType(3)
                }
                R.id.albums_drawer_row_small -> {
                    setViewType(6)
                }
                R.id.albums_drawer_grid_large -> {
                    setViewType(1)
                }
                R.id.albums_drawer_grid_medium -> {
                    setViewType(4)
                }
                else -> {
                    setViewType(5)
                }
            }
        }

        adapter = AlbumRecyclerAdapter(albumList, requireActivity(), screenWidth, initialViewMode)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
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

    fun setViewType(viewType: Int) {
        adapter.setViewType(viewType)
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
        prefs.edit { putInt("view_mode", viewType)?.apply() }
    }
}