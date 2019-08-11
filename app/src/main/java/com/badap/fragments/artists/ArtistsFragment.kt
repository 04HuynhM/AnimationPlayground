package com.badap.fragments.artists


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
import com.badap.adapters.ArtistRecyclerAdapter
import com.badap.fragments.BottomSheetViewModeDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ArtistsFragment(val viewFab: FloatingActionButton) : Fragment() {

    lateinit var adapter: ArtistRecyclerAdapter
    lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = GridLayoutManager(requireContext(), 12)
        val recyclerView = view.findViewById<RecyclerView>(R.id.artists_recycler)

        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val initialViewMode = prefs.getInt("view_mode", 2)
        val screenWidth = prefs.getInt("screen_width", -1)

        recyclerView.layoutManager = layoutManager

        MainActivity.indexedArtists?.let {
            adapter = ArtistRecyclerAdapter(it, requireActivity(), screenWidth, initialViewMode, viewFab)
            recyclerView.adapter = adapter
        }

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
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

        viewFab.setOnClickListener {
            val viewDialog = BottomSheetViewModeDialog(this, viewFab)
            viewDialog.show(requireActivity().supportFragmentManager, "viewmode_dialog_artists")
        }
    }

    fun setViewType(viewType: Int) {
        adapter.setViewType(viewType)
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
        prefs.edit { putInt("view_mode", viewType)?.apply() }
    }
}
