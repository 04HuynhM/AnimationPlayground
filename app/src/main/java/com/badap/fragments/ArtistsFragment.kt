package com.badap.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.edit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badap.Artist
import com.badap.adapters.ArtistRecyclerAdapter
import com.badap.R
import com.badap.utilities.MediaStoreHelper

class ArtistsFragment : Fragment() {

    companion object ArtistList{
        var companionArtistList: ArrayList<Artist>? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mediaHelper = MediaStoreHelper()

        val layoutManager = GridLayoutManager(requireContext(), 12)
        val recyclerView = view.findViewById<RecyclerView>(R.id.artists_recycler)
        val scaleUpButton: Button = view.findViewById(R.id.scaleUpButton)
        val scaleDownButton: Button = view.findViewById(R.id.scaleDownButton)

        val preferences = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val initialViewMode = preferences.getInt("view_mode", 2)
        val screenWidth = preferences.getInt("screen_width", -1)

        val adapter: ArtistRecyclerAdapter?

        recyclerView.layoutManager = layoutManager
        if (companionArtistList.isNullOrEmpty()) {
            companionArtistList = mediaHelper.getAllArtists(requireContext())
            adapter = ArtistRecyclerAdapter(companionArtistList!!, requireActivity(), screenWidth, initialViewMode)
            recyclerView.adapter = adapter
        } else {
            adapter = ArtistRecyclerAdapter(companionArtistList!!, requireActivity(), screenWidth, initialViewMode)
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

        scaleUpButton.setOnClickListener {
            val currentViewMode = preferences.getInt("view_mode", 1)
            if (currentViewMode > 1) {
                val newViewMode = currentViewMode - 1
                adapter.setViewType(newViewMode)
                println("NEW MODE: $newViewMode")
                preferences.edit { putInt("view_mode", newViewMode) }
                adapter.notifyItemRangeChanged(0, adapter.itemCount)
            }
        }

        scaleDownButton.setOnClickListener {
            val currentViewMode = preferences.getInt("view_mode", 1)
            if (currentViewMode < 6) {
                val newViewMode = currentViewMode + 1
                adapter.setViewType(newViewMode)
                println("NEW MODE: $newViewMode")
                preferences.edit { putInt("view_mode", newViewMode) }
                adapter.notifyItemRangeChanged(0, adapter.itemCount)
            }
        }


    }
}
