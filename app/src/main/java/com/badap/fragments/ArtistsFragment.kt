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
import com.badap.adapters.ArtistRecyclerAdapter
import com.badap.R
import com.badap.utilities.HelperMethods
import com.badap.utilities.MediaStoreHelper

class ArtistsFragment : Fragment() {

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
        val helperMethods = HelperMethods()

        val layoutManager = GridLayoutManager(requireContext(), 1)
        val recyclerView = view.findViewById<RecyclerView>(R.id.artists_recycler)
        val scaleUpButton: Button = view.findViewById(R.id.scaleUpButton)
        val scaleDownButton: Button = view.findViewById(R.id.scaleDownButton)

        val preferences = requireActivity().getSharedPreferences("zoom_level", Context.MODE_PRIVATE)
        val initialViewMode = preferences.getInt("columnCount", 6)
        val adapter = ArtistRecyclerAdapter(mediaHelper.getAllArtists(requireContext()), requireActivity(), helperMethods.getScreenSize(requireActivity()).x, initialViewMode)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

//        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                return when(adapter.getItemViewType(position)) {
//                    5 -> 1 // large grid
//                    6 -> 2 // large row
////                    3 -> 1 // medium row
////                    4 -> 3 // medium grid
////                    5 -> 4 // small grid
////                    else -> 1 // small row
//                    else -> 2
//                }
//            }
//        }

        scaleDownButton.setOnClickListener {
            val currentViewMode = preferences.getInt("columnCount", 1)
            if (currentViewMode > 1) {
                val newViewMode = currentViewMode - 1
                adapter.setViewType(newViewMode)
                preferences.edit { putInt("columnCount", newViewMode) }
                adapter.notifyItemRangeChanged(0, adapter.itemCount)
            }
//            adapter.setViewType(6)
//            adapter.notifyItemRangeChanged(0, adapter.itemCount)
        }

        scaleUpButton.setOnClickListener {
            val currentViewMode = preferences.getInt("columnCount", 1)
            if (currentViewMode < 6) {
                val newViewMode = currentViewMode + 1
                adapter.setViewType(newViewMode)
                preferences.edit { putInt("columnCount", newViewMode) }
                adapter.notifyItemRangeChanged(0, adapter.itemCount)
            }
//            adapter.setViewType(5)
//            adapter.notifyItemRangeChanged(0, adapter.itemCount)
        }


    }
}
